/*
 * Name: Zhenyang Cai
 * Date: 2021-06-13
 * Description: panel where all gameplay occurs
 */

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
    // Static final variables (i.e. things that should never change)
    // buttery smooth pls
    // TODO: FIX THE VERY CHUNKY BUTTER
    static final int FPS = 60;

    // Game objects
    Thread gameThread;
    Camera camera;
    // I added a 1 to differentiate the class and the object itself
    // "Player player" is cursed and likely punishable by life in prison
    Player player1;
    
    // Game state variables
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

    // Track elements
    Track track;
    String trackFilePath;

    boolean trackLoaded = false;
    HashSet<TrackBlock> trackBlockData = new HashSet<>();

    // UI Elements
    Time timeObj = null;
    Font uiTextBig = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    Font uiTextSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 40);
    Font uiTextSmallItalics = new Font(Font.SANS_SERIF, Font.ITALIC, 40);
    Font uiTextMediumHighlight = new Font(Font.SANS_SERIF, Font.ITALIC|Font.BOLD, 48);
    Font uiTextMedium = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    JLabel restartLabel;
    JLabel exitLabel;

    // Constructor for the panel
    public Game(String trackFile) {
        // Setting preferences for the panel
        setPreferredSize(new Dimension(1920, 1080));
        setVisible(true);
        setBackground(Color.BLACK);
        trackFilePath = trackFile;

        // Loading the track
        try {
            track = new Track(trackFilePath);
        } catch (FileNotFoundException e) {
            // TODO: handle exception
            System.out.println("wuh oh");
            System.exit(1);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(" there was an");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
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
            (int) screenDimensions.getHeight() / 2 + 170, 
            350, 
            50
        );
        exitLabel.setFont(uiTextMedium);
        exitLabel.setForeground(Color.WHITE);
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: EXIT THE GAME AND RETURN TO REALITY
            }
        });

        restartLabel = new JLabel("Restart", SwingConstants.RIGHT);
        restartLabel.setBounds(
            (int) screenDimensions.getWidth() / 2, 
            (int) screenDimensions.getHeight() / 2 + 170, 
            350, 
            50
        );
        restartLabel.setFont(uiTextMedium);
        restartLabel.setForeground(Color.WHITE);
        restartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                player1.respawnToStart();
                reset();
            }
        });

        gameThread = new Thread(this);
        gameThread.start();
    }

    // As it says on the tin
    // TODO: initialize with more than just the player
    public void initialize() {
        if (trackLoaded) {
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
        
        camera = new Camera(300, 300);

        // Timer stuff
        timeStart = System.currentTimeMillis();
        timeIsRunning = true;

        reset();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
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
        if (trackLoaded) {
            for (TrackBlock block : trackBlockData) {
                player1.checkBlockIntersection(block, track);
            }
            player1.checkBlockIntersection(track.getStartBlock(), track);
            player1.checkBlockIntersection(track.getFinishBlock(), track);
        }

        if (!player1.isFinished) {
            camera.update(player1, this.getWidth(), this.getHeight());
        }

        if (timeIsRunning && !(player1.isFinished)) {
            timeElapsed = System.currentTimeMillis() - timeStart;
            timeObj = new Time(timeElapsed);
        }

        if (player1.isFinished && !haveParsedFinish) {
            // TODO: parse finish
            Time latestTime = timeObj;
            if (personalBest != null) {
                if (timeObj.compareTo(personalBest) < 0) {
                    Main.addPersonalData(track.getTrackID(), latestTime);
                    personalBest = latestTime;
                    Main.saveDataToFile();
                }
            } else {
                Main.addPersonalData(track.getTrackID(), timeObj);
                personalBest = latestTime;
                // TODO: don't always save to file when you finish
                // Maybe just when you exit or something
                Main.saveDataToFile();
            }
            
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
        RenderingHints imageInterp = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.setRenderingHints(imageInterp);
        RenderingHints textAntiAliasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2D.setRenderingHints(textAntiAliasing);

        super.paintComponent(g);

        // Game draw calls
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
        // g2D.drawString("Position: x = " + player1.posX + " y = " + player1.posY, 50, 50);
        // g2D.drawString("Speed: x = " + player1.velX + " y = " + player1.velY, 50, 75);

        // UI elements
        if (startingState) {
            drawCenteredText(g2D, "Press any key to start...", 72, uiTextBig);
        }
        
        if (!player1.isFinished) {
            // Anything in here only displays if the player is still going
            if (player1.isBoosting) {
                drawCenteredText(g2D, "BOOST", 72, uiTextBig);
            } else if (player1.isNoControl && !startingState) {
                drawCenteredText(g2D, "NO CONTROL", 72, uiTextBig);
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
                    uiTextMediumHighlight
                );
            }
            drawCenteredText(
                g2D, 
                String.format("%d/%d", player1.checkpointCount, track.getCheckpointCount()), 
                (int) screenDimensions.getHeight() - 112, 
                uiTextSmallItalics
            );
        } else {
            // Game finininininsh UI
            Color prevColor = g2D.getColor();
            g2D.setColor(new Color(0f, 0f, 0f, 0.75f));
            g2D.fillRoundRect(
                ((int) screenDimensions.getWidth() - 800) / 2, 
                ((int) screenDimensions.getHeight() - 500) / 2, 
                800, 500, 50, 50
            );

            g2D.setColor(Color.WHITE);
            g2D.setFont(uiTextMediumHighlight);
            drawCenteredText(g2D, "Time: " + timeObj, 300, uiTextMediumHighlight);
            if (personalBest == null) {
                drawCenteredText(g2D, "Personal Best: " + new Time(0, 0, 0), 375, uiTextMediumHighlight);
            } else {
                drawCenteredText(g2D, "Personal Best: " + personalBest, 375, uiTextMediumHighlight);
            }
            

            add(exitLabel);
            add(restartLabel);

            g2D.setColor(prevColor);
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
            player1.resetToCP();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player1.isAccelerating = false;
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
