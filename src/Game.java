/*
 * Name: Zhenyang Cai
 * Date: 2021-06-13
 * Description: panel where all gameplay occurs
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
    // Static final variables (i.e. things that should never change)
    // buttery smooth pls
    static final int FPS = 60;

    // moosic
    static File musicDir = new File("music/wav/");
    static File[] musicList = musicDir.listFiles();

    // Game objects
    Thread gameThread;
    Camera camera;
    // I added a 1 to differentiate the class and the object itself
    // "Player player" is cursed and likely punishable by life in prison
    Player player1;
    
    // Game state variables
    boolean gameLoaded;
    boolean timeIsRunning;
    boolean startingState;
    boolean haveParsedFinish;

    // Timing variables
    long timeStart;
    long timeElapsed;
    // boolean countdownTimer;
    // long countdownLastCount;
    // int countdownTimesCounted;

    // Misc. elements
    Dimension screenDimensions;
    Rectangle playableArea = new Rectangle(0, 0, Track.MAX_GRID_X  * TrackBlock.BLOCK_WIDTH, Track.MAX_GRID_Y * TrackBlock.BLOCK_HEIGHT);
    Time personalBest;

    // Audio elements
    Clip music;
    Clip startSound;
    Clip engineSound;
    boolean engineSoundPlaying;

    // Track elements
    Track track;
    String trackFilePath;

    boolean trackLoaded = false;
    HashSet<TrackBlock> trackBlockData = new HashSet<>();

    // UI Elements
    Time timeObj = null;
    // Medals
    ImageIcon bronzeMedal = new ImageIcon("assets/img/medal_bronze.png");
    Image bronzeMedalImage;
    ImageIcon silverMedal = new ImageIcon("assets/img/medal_silver.png");
    Image silverMedalImage;
    ImageIcon goldMedal = new ImageIcon("assets/img/medal_gold.png");
    Image goldMedalImage;
    ImageIcon authorMedal = new ImageIcon("assets/img/medal_author.png");
    Image authorMedalImage;
    ImageIcon noMedal = new ImageIcon("assets/img/medal_blank.png");
    Image noMedalImage;

    JLabel restartLabel;
    JLabel exitLabel;

    /**
     * Creates an empty game panel, with zero functionality. Exists only to avoid NullPointerExceptions.
     */
    public Game() {

    }

    /**
     * Loads an instance of Game, using the provided track file
     * @param trackFile
     */
    public Game(String trackFile) {
        // Setting preferences for the panel
        gameLoaded = false;
        setPreferredSize(new Dimension(1920, 1080));
        setVisible(true);
        setBackground(Color.BLACK);
        this.trackFilePath = trackFile;

        // Loading the track
        try {
            this.track = new Track(trackFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("wuh oh");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(" there was an");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(" exception that i didn't figure out");
            System.exit(1);
        }
        trackBlockData = track.getBlockSet();
        trackLoaded = true;

        // Personal best times
        personalBest = Main.getPersonalBest(track.getTrackID());

        // Creating UI elements
        screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        exitLabel = new JLabel("Exit", SwingConstants.LEFT);
        exitLabel.setBounds(
            (int) screenDimensions.getWidth() / 2 - 350, 
            (int) screenDimensions.getHeight() / 2 + 205, 
            350, 
            50
        );
        exitLabel.setFont(Main.uiTextMedium);
        exitLabel.setForeground(Color.WHITE);
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: EXIT THE GAME AND RETURN TO REALITY
                stopMusic();
                Main.saveDataToFile();
                Main.exitFromGame();
            }
        });

        restartLabel = new JLabel("Restart", SwingConstants.RIGHT);
        restartLabel.setBounds(
            (int) screenDimensions.getWidth() / 2, 
            (int) screenDimensions.getHeight() / 2 + 205, 
            350, 
            50
        );
        restartLabel.setFont(Main.uiTextMedium);
        restartLabel.setForeground(Color.WHITE);
        restartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player1.respawnToStart();
                reset();
            }
        });

        // Medal image loading
        bronzeMedalImage = bronzeMedal.getImage();
        silverMedalImage = silverMedal.getImage();
        goldMedalImage = goldMedal.getImage();
        authorMedalImage = authorMedal.getImage();
        noMedalImage = noMedal.getImage();

        // Idk probably a good idea to initialize this so I don't get NullPointerExceptions
        engineSoundPlaying = false;

        setDoubleBuffered(true);

        gameThread = new Thread(this);
        gameThread.start();
    }

    // As it says on the tin
    public void initialize() {
        if (trackLoaded) {
            // player initialization
            Rectangle startRectangle = track.getStartBlock().hitbox;
            double startX = startRectangle.getCenterX();
            double startY = startRectangle.getCenterY();
            double rotation = 0;
            switch(track.getStartBlock().getDirection()) {
                case UP:
                    rotation = 90;
                    break;
                case DOWN:
                    rotation = -90;
                    break;
                case LEFT:
                    rotation = -180;
                    break;
                case RIGHT:
                    rotation = 0;
                    break;
            }
            
            player1 = new Player(startX, startY, rotation, track.getStartBlock());
        }
        // Camera obj
        camera = new Camera(300, 300);

        // Notifies paintComponent that painting is a good idea
        // This was necessary to not have 10231976 NullPointerExceptions at the start
        gameLoaded = true;

        // Timer stuff
        timeStart = System.currentTimeMillis();
        timeIsRunning = true;

        playMusic();

        reset();
    }

    @Override
    public void run() {
        initialize();

        while (true) {
            // TODO: fix the bloody lag
            update();
            this.repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Player update functions
        player1.update();
        player1.keepInBounds();
        // Block collisions
        if (trackLoaded) {
            for (TrackBlock block : trackBlockData) {
                player1.checkBlockIntersection(block, track);
            }
            player1.checkBlockIntersection(track.getStartBlock(), track);
            player1.checkBlockIntersection(track.getFinishBlock(), track);
        }
        
        // Camera only updates position if the player hasn't finished
        // Idk I think it looks neat
        if (!player1.isFinished) {
            camera.update(player1, this.getWidth(), this.getHeight());
        }

        // Timer object only does its thing if the player has not finished
        if (timeIsRunning && !player1.isFinished) {
            timeElapsed = System.currentTimeMillis() - timeStart;
            timeObj = new Time(timeElapsed);
        }

        // Player finish is processed here
        if (player1.isFinished && !haveParsedFinish) {
            Time latestTime = timeObj;
            if (personalBest != null) {
                // If PB exists
                if (timeObj.compareTo(personalBest) < 0) {
                    // new PB only if it's better
                    Main.addPersonalData(track.getTrackID(), latestTime);
                    personalBest = latestTime;
                }
            } else {
                // If PB does not exist, add it
                Main.addPersonalData(track.getTrackID(), timeObj);
                personalBest = latestTime;
            }
            
            // NOTE: PBs are only saved upon exiting the level
            // This means if your game crashes before you exit to menu, say goodbye to your super poggers epic personal best time
            haveParsedFinish = true;
        }
    }

    public void reset() {
        // TODO: allow restarting the game
        timeStart = System.currentTimeMillis();
        timeObj = new Time(0, 0, 0);

        for (TrackBlock trackBlock : trackBlockData) {
            if (trackBlock.getType() == BlockType.CHECKPOINT) {
                trackBlock.checkpointHit = false;
            }
        }

        timeIsRunning = false;
        player1.isNoControl = true;

        startingState = true;
        haveParsedFinish = false;

        remove(exitLabel);
        remove(restartLabel);
    }

    public void start() {
        timeStart = System.currentTimeMillis();
        timeIsRunning = true;
        player1.isNoControl = false;
        startingState = false;
    }
 
    @Override
    public void paintComponent(Graphics g) {
        // TODO: screen space works from top left, remember that when building things
        Graphics2D g2D = (Graphics2D) g;

        // Rendering settings
        RenderingHints antiAliasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHints(antiAliasing);
        RenderingHints imageInterp = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2D.setRenderingHints(imageInterp);
        RenderingHints textAntiAliasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2D.setRenderingHints(textAntiAliasing);

        super.paintComponent(g);

        // Game draw calls
        if (gameLoaded) {
            g2D.translate(camera.getX(), camera.getY()); // god bless this method        

            // Background
            g2D.setColor(Color.LIGHT_GRAY);
            g2D.fill(playableArea);

            // Track blocks
            if (trackLoaded) {
                for (TrackBlock block : trackBlockData) {
                    block.draw(g2D);
                }
                track.getStartBlock().draw(g2D);
                track.getFinishBlock().draw(g2D);
            }

            // Debug: player hitbox
            // g2D.setColor(Color.WHITE);
            // g2D.fill(player1.hitbox);
            // Player drawing
            player1.draw(g2D);        

            // UI elements
            g2D.translate(-camera.getX(), -camera.getY());

            // Debug elements
            g2D.setColor(Color.WHITE);
            // g2D.drawString("countdown thing = " + countdownTimesCounted, 1000, 50);
            // g2D.drawString("Player direction = " + player1.direction, 1000, 50);
            // g2D.drawString("Checkpoints = " + player1.checkpointCount, 1400, 50);
            // g2D.drawString("Grid: x = " + player1.gridX + " y = " + player1.gridY, 50, 50);
            g2D.drawString("Speed: x = " + player1.velX + " y = " + player1.velY, 50, 75);

            // UI elements
            if (startingState) {
                drawCenteredText(g2D, "Press any key to start...", 72, Main.uiTextBig);
            }
            
            if (!player1.isFinished) {
                // Anything in here only displays if the player is still going
                if (player1.isBoosting) {
                    drawCenteredText(g2D, "BOOST", 72, Main.uiTextBig);
                } else if (player1.isNoControl && !startingState) {
                    drawCenteredText(g2D, "NO CONTROL", 72, Main.uiTextBig);
                }

                // Rectangle below time & cp count
                Color prevColor = g2D.getColor();
                g2D.setColor(new Color(0f, 0f, 0f, 0.5f));
                g2D.fillRoundRect(
                    ((int) screenDimensions.getWidth() - 288) / 2, 
                    ((int) screenDimensions.getHeight() - 112), 
                    288, 162, 50, 50
                );
                g2D.setColor(prevColor);

                    
                if (timeObj != null) {
                    drawCenteredText(
                        g2D, 
                        timeObj.toString(), 
                        (int) screenDimensions.getHeight() - 64, 
                        Main.uiTextMediumHighlight
                    );
                }
                drawCenteredText(
                    g2D, 
                    String.format("%d/%d", player1.checkpointCount, track.getCheckpointCount()), 
                    (int) screenDimensions.getHeight() - 112, 
                    Main.uiTextSmallItalics
                );
            } else {
                // Game finish UI
                // transparent-ish black background
                Color prevColor = g2D.getColor();
                g2D.setColor(new Color(0f, 0f, 0f, 0.75f));
                g2D.fillRoundRect(
                    ((int) screenDimensions.getWidth() - 800) / 2, 
                    ((int) screenDimensions.getHeight() - 550) / 2, 
                    800, 550, 50, 50
                );

                // text
                g2D.setColor(Color.WHITE);
                g2D.setFont(Main.uiTextMediumHighlight);
                drawCenteredText(g2D, "Time: " + timeObj, 290, Main.uiTextMediumHighlight);
                if (personalBest == null) {
                    drawCenteredText(g2D, "Personal Best: " + new Time(0, 0, 0), 365, Main.uiTextMediumHighlight);
                } else {
                    drawCenteredText(g2D, "Personal Best: " + personalBest, 365, Main.uiTextMediumHighlight);
                }

                int topTextOffset = 530;
                int bottomTextOffset = 640;
                // This entire block of text feels scuffed
                String nextMedal = null;
                Image medalImage = null;
                if (personalBest.compareTo(track.getAuthorTime()) <= 0) {
                    nextMedal = "author medal";
                    medalImage = authorMedalImage;
                } else if (personalBest.compareTo(track.getGoldTime()) <= 0) {
                    nextMedal = "gold medal";
                    medalImage = goldMedalImage;
                } else if (personalBest.compareTo(track.getSilverTime()) <= 0) {
                    nextMedal = track.getGoldTime().toString();
                    medalImage = silverMedalImage;
                } else if (personalBest.compareTo(track.getBronzeTime()) <= 0) {
                    nextMedal = track.getSilverTime().toString();
                    medalImage = bronzeMedalImage;
                } else {
                    nextMedal = track.getBronzeTime().toString();
                    medalImage = noMedalImage;
                }

                if (nextMedal.compareTo("author medal") == 0 && medalImage == authorMedalImage) {
                    g2D.drawImage(authorMedalImage, (int) (screenDimensions.getWidth() / 2 - 330), 460, null);
                    g2D.setFont(Main.uiTextMediumHighlight);
                    g2D.drawString("Congratulations!", (int) (screenDimensions.getWidth() / 2 - 30), 515);
                    g2D.drawString("You've beat the", (int) (screenDimensions.getWidth() / 2 - 30), 610);
                    g2D.drawString("author medal!", (int) (screenDimensions.getWidth() / 2 - 30), 665);
                } else if (nextMedal.compareTo("gold medal") == 0 && medalImage == goldMedalImage) {
                    g2D.drawImage(goldMedalImage, (int) (screenDimensions.getWidth() / 2 - 330), 460, null);
                    g2D.setFont(Main.uiTextMediumHighlight);
                    g2D.drawString("Congratulations!", (int) (screenDimensions.getWidth() / 2 - 30), 515);
                    g2D.drawString("Now go for the", (int) (screenDimensions.getWidth() / 2 - 30), 610);
                    g2D.drawString("author medal!", (int) (screenDimensions.getWidth() / 2 - 30), 665);
                } else {
                    g2D.drawImage(medalImage, (int) (screenDimensions.getWidth() / 2 - 330), 460, null);
                    g2D.setFont(Main.uiTextMediumHighlight);
                    g2D.drawString("Next medal:", (int) (screenDimensions.getWidth() / 2 - 30), topTextOffset);
                    g2D.drawString(nextMedal, (int) (screenDimensions.getWidth() / 2 - 30), bottomTextOffset);
                }

                // buttons
                add(exitLabel);
                add(restartLabel);

                g2D.setColor(prevColor);
            }
        } else {
            g2D.setColor(Color.WHITE);
            g2D.setFont(Main.uiTextBig);
            g2D.drawString("Loading...", (int) (screenDimensions.getWidth() - 400), (int) screenDimensions.getHeight() - 80);
        }
    }

    public void drawCenteredText(Graphics g, String text, int yPos, Font font) {
        // adapted from an answer by Daniel Kvist on StackOverflow for drawing centered text
        // Get the FontMetrics
        Font oldFont = g.getFont();
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = 0 + (screenDimensions.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = yPos + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
        g.setFont(oldFont);
    }

    public void playMusic() {
        try {
            int randInt = (int) (Math.random() * musicList.length);
            System.out.println(randInt);
            File file = musicList[randInt];
            music = AudioSystem.getClip();

            // getAudioInputStream() also accepts a File or InputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            music.open(ais);
            FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15);
            music.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        music.stop();
        music.close();
    }

    public void playEngineSound() {
        try {
            // Engine audio courtesy of Stingray Productions on YouTube
            // https://www.youtube.com/watch?v=MZwsO6H_FYo
            File file = new File("assets/audio/player_engine.wav");
            engineSound = AudioSystem.getClip();

            // getAudioInputStream() also accepts a  File or InputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            engineSound.open(ais);
            FloatControl gainControl = (FloatControl) engineSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10);
            engineSound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public void stopEngineSound() {
        engineSound.stop();
        engineSound.close();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (startingState && key != KeyEvent.VK_ESCAPE) {
            start();
        }
        // Player controls
        if (!player1.isNoControl) {
            if (key == KeyEvent.VK_W) {
                player1.isAccelerating = true;
                if (!engineSoundPlaying) {
                    playEngineSound();
                    engineSoundPlaying = true;
                }
            }
            if (key == KeyEvent.VK_SHIFT) {
                player1.isBraking = true;
            }
    
            if (key == KeyEvent.VK_D) {
                player1.isTurningRight = true;
            }
            if (key == KeyEvent.VK_A) {
                player1.isTurningLeft = true;
            }
        }

        if (key == KeyEvent.VK_BACK_SPACE) {
            player1.respawnToStart();
            reset();
        } else if (key == KeyEvent.VK_ENTER && !player1.isFinished) {
            if (player1.lastCheckpoint == null) {
                player1.respawnToStart();
                reset();
            } else {
                player1.resetToCP();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player1.isAccelerating = false;
            stopEngineSound();
            engineSoundPlaying = false;
        }
        if (key == KeyEvent.VK_SHIFT) {
            player1.isBraking = false;
        }

        if (key == KeyEvent.VK_D) {
            player1.isTurningRight = false;
        }
        if (key == KeyEvent.VK_A) {
            player1.isTurningLeft = false;
        }
    }
}
