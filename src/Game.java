/*
 * Name: Zhenyang Cai
 * Date: 2021-06-13
 * Description: panel where all gameplay occurs
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
    // Static final variables (i.e. things that should never change)
    // buttery smooth pls
    // TODO: FIX THE VERY CHUNKY BUTTER
    static final int FPS = 60;
    static final int MAX_GRID_X = 32;
    static final int MAX_GRID_Y = 32;

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
    Rectangle playableArea = new Rectangle(0, 0, MAX_GRID_X  * TrackBlock.BLOCK_WIDTH, MAX_GRID_Y * TrackBlock.BLOCK_HEIGHT);

    // Track elements
    ArrayList<TrackBlock> trackBlockList = new ArrayList<>();
    TrackBlock boostBlock = new TrackBlock(BlockType.BOOST, 2, 1, BlockDirection.UP);
    TrackBlock checkpointBlock = new TrackBlock(BlockType.CHECKPOINT, 2, 2, BlockDirection.UP);
    TrackBlock wallBlock = new TrackBlock(BlockType.WALL, 4, 2, BlockDirection.UP);
    TrackBlock wallBlock2 = new TrackBlock(BlockType.WALL, 5, 2, BlockDirection.UP);
    TrackBlock resetBlock = new TrackBlock(BlockType.RESET, 3, 1, BlockDirection.UP);
    TrackBlock noControlBlock = new TrackBlock(BlockType.NOCONTROL, 4, 1, BlockDirection.UP);
    TrackBlock finishBlock = new TrackBlock(BlockType.FINISH, 6, 4, BlockDirection.UP);
    TrackBlock startBlock = new TrackBlock(BlockType.START, 0, 0, BlockDirection.UP);

    // UI Elements

    // As it says on the tin
    // TODO: initialize with more than just the player
    public void initialize() {
        player1 = new Player(300, 300, 0);
        camera = new Camera(300, 300);
        
        trackBlockList.add(boostBlock);
        trackBlockList.add(checkpointBlock);
        trackBlockList.add(wallBlock);
        trackBlockList.add(wallBlock2);
        trackBlockList.add(resetBlock);
        trackBlockList.add(noControlBlock);
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
        for (TrackBlock block : trackBlockList) {
            player1.checkBlockIntersection(block);
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
        for (TrackBlock block : trackBlockList) {
            block.draw(g2D);
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
            player1.setPos(150, 150);
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
