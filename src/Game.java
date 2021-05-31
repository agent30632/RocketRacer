import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {

    Thread gameThread;
    Camera camera;
    Player player;

    long frameStart;
    long frameEnd;
    long frameTime;

    Rectangle rectangle = new Rectangle(0, 0, 100, 100);

    ImageIcon testImage = new ImageIcon("assets/img/nord blue.png");

    // buttery smooth pls
    static final int FPS = 60;

    // As it says on the tin
    public void initialize() {
        player = new Player(150, 150, 0);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        initialize();
        frameStart = System.currentTimeMillis();

        while (true) {
            frameEnd = System.currentTimeMillis();
            frameTime = frameEnd - frameStart;
            update();
            frameStart = System.currentTimeMillis();
            this.repaint();
            try {
                Thread.sleep(1000/FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }

    // Constructor for the panel
    public Game() {
        setPreferredSize(new Dimension(1920, 1080));
        setVisible(true);
        setBackground(Color.BLACK);

        gameThread = new Thread(this);
        gameThread.start();
    }
 
    @Override
    public void paintComponent(Graphics g) {
        // TODO: screen space works from top right, remember that when building things
        super.paintComponent(g);
        
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.MAGENTA);
        g2D.fill(rectangle);

        g2D.setColor(Color.RED);
        player.draw(g2D);

        g2D.drawString("Player direction = " + player.direction, 1000, 50);
        g2D.drawString("Frame time = " + frameTime, 1300, 50);

        // g2D.drawImage(testImage.getImage(), 0, 0, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_R) {
            player.posX = 150;
            player.posY = 150;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.isAccelerating = true;
        }
        if (key == KeyEvent.VK_SHIFT) {
            player.isBraking = true;
        }

        if (key == KeyEvent.VK_D) {
            player.isTurningRight = true;
        }
        if (key == KeyEvent.VK_A) {
            player.isTurningLeft = true;
        }
            
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.isAccelerating = false;
        }
        if (key == KeyEvent.VK_SHIFT) {
            player.isBraking = false;
        }

        if (key == KeyEvent.VK_D) {
            player.isTurningRight = false;
        }
        if (key == KeyEvent.VK_A) {
            player.isTurningLeft = false;
        }
    }
}
