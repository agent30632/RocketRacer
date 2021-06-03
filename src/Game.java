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

    // Game variables
    // Test elements
    Rectangle rectangle = new Rectangle(0, 0, 100, 100);
    // TODO: non-flat background because hoh dear
    Rectangle playableArea = new Rectangle(0, 0, Track.MAX_GRID_X  * TrackBlock.BLOCK_WIDTH, Track.MAX_GRID_Y * TrackBlock.BLOCK_HEIGHT);

    // Track elements
    Track track;
    String trackFilePath;

    boolean trackLoaded = false;
    HashSet<TrackBlock> trackBlockData = new HashSet<>();
    // TrackBlock boostBlock = new TrackBlock(BlockType.BOOST, 2, 1, BlockDirection.UP);
    // TrackBlock checkpointBlock = new TrackBlock(BlockType.CHECKPOINT, 2, 2, BlockDirection.UP);
    // TrackBlock wallBlock = new TrackBlock(BlockType.WALL, 4, 2, BlockDirection.UP);
    // TrackBlock wallBlock2 = new TrackBlock(BlockType.WALL, 5, 2, BlockDirection.UP);
    // TrackBlock resetBlock = new TrackBlock(BlockType.RESET, 3, 1, BlockDirection.UP);
    // TrackBlock noControlBlock = new TrackBlock(BlockType.NOCONTROL, 4, 1, BlockDirection.UP);
    // TrackBlock finishBlock = new TrackBlock(BlockType.FINISH, 1, 0, BlockDirection.UP);
    // TrackBlock startBlock = new TrackBlock(BlockType.START, 0, 0, BlockDirection.UP);

    // UI Elements

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
            
            player1 = new Player(startX, startY, rotation);
        }
        
        camera = new Camera(300, 300);

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
                player1.checkBlockIntersection(block);
            }
        }

        camera.update(player1, this.getWidth(), this.getHeight());
    }

    public void restart() {
        // TODO: allow restarting the game
    }

    // Constructor for the panel
    public Game(String trackFile) {
        setPreferredSize(new Dimension(1920, 1080));
        setVisible(true);
        setBackground(Color.BLACK);
        trackFilePath = trackFile;

        try {
            track = new Track(trackFilePath);
        } catch (FileNotFoundException e) {
            // TODO: handle exception
            System.out.println("file not found ruh roh");
            System.exit(1);
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("IOException whey oh");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("illegal argument what the hay");
            System.exit(1);
        }
        trackBlockData = track.getBlockSet();
        trackLoaded = true;

        gameThread = new Thread(this);
        gameThread.start();
    }
 
    @Override
    public void paintComponent(Graphics g) {
        // TODO: screen space works from top left, remember that when building things
        super.paintComponent(g);

        // Game draw calls
        Graphics2D g2D = (Graphics2D) g;
        g2D.translate(camera.getX(), camera.getY()); // god bless this method
        
        // Rendering settings
        RenderingHints antiAliasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHints(antiAliasing);
        RenderingHints imageInterp = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2D.setRenderingHints(imageInterp);

        g2D.setColor(Color.LIGHT_GRAY);
        g2D.fill(playableArea);

        // Walls
        // TODO: level loading + walls
        if (trackLoaded) {
            for (TrackBlock block : trackBlockData) {
                block.draw(g2D);
            }
            track.getStartBlock().draw(g2D);
            track.getFinishBlock().draw(g2D);
        }
        
        // Player drawing
        player1.draw(g2D);

        // UI elements
        g2D.translate(-camera.getX(), -camera.getY());

        // Debug
        g2D.setColor(Color.WHITE);
        g2D.drawString("Player direction = " + player1.direction, 1000, 50);
        g2D.drawString("Checkpoints = " + player1.checkpointCount, 1400, 50);
        g2D.drawString("Position: x = " + player1.posX + " y = " + player1.posY, 50, 50);
        g2D.drawString("Speed: x = " + player1.velX + " y = " + player1.velY, 50, 75);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO: use the Controls class to allow for remappable controls
        int key = e.getKeyCode();

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
            player1.setPos(track.getStartBlock().hitbox.getCenterX(), track.getStartBlock().hitbox.getCenterY());
            player1.zeroVelocity();
            player1.resetState();
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
