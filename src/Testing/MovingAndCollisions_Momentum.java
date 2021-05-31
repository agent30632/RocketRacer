package Testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MovingAndCollisions_Momentum extends JPanel implements Runnable, KeyListener {
	
	Rectangle rect = new Rectangle(10, 10, 40, 40);
	Rectangle[] walls = new Rectangle[5];
	boolean up, down, left, right;
	int speed = 1;

    int velX = 0;
    int velY = 0;

    double elasticity = 0.3;
    double braking = 0.8;

    int posX = 0;
    int posY = 0;

	int screenWidth = 600;
	int screenHeight = 600;
	Thread thread;
	int FPS = 60;
	
	public MovingAndCollisions_Momentum() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setVisible(true);
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		initialize();
		while(true) {
			//main game loop
			update();
			this.repaint();
			try {
				Thread.sleep(1000/FPS);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initialize() {
		//setups before the game starts running
		walls[0] = new Rectangle(200, 200, 60, 60);
		walls[1] = new Rectangle(300, 40, 40, 100);
		walls[2] = new Rectangle(450, 100, 80, 35);
		walls[3] = new Rectangle(60, 60, 15, 15);
		walls[4] = new Rectangle(250, 350, 150, 200);
	}
	
	public void update() {
		move();
		keepInBound();
		for(int i = 0; i < walls.length; i++)
			checkCollision(walls[i]);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.MAGENTA);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		g2.setColor(Color.GRAY);
		for(int i = 0; i < walls.length; i++)
			g2.fill(walls[i]);
		g2.setColor(Color.BLUE);
		g2.fill(rect);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = true;
			right = false;
		}else if(key == KeyEvent.VK_D) {
			right = true;
			left = false;
		}else if(key == KeyEvent.VK_W) {
			up = true;
			down = false;
		}else if(key == KeyEvent.VK_S) {
			down = true;
			up = false;
		} else if (key == KeyEvent.VK_SHIFT) {
            velX *= braking;
            velY *= braking;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = false;
		}else if(key == KeyEvent.VK_D) {
			right = false;
		}else if(key == KeyEvent.VK_W) {
			up = false;
		}else if(key == KeyEvent.VK_S) {
			down = false;
		} else if (key == KeyEvent.VK_SHIFT) {
            velX *= braking;
            velY *= braking;
        }
	}
	
	void move() {
        rect.x += velX;
        rect.y += velY;

        if (left) {
            velX -= speed;
        } else if (right) {
            velX += speed;
        }
        
        if (up) {
            velY -= speed;
        } else if (down) {
            velY += speed;
        }
	}
	
	void keepInBound() {
		if(rect.x < 0) {
            rect.x = 0;
            velX = (int) (-velX * elasticity);
        } else if(rect.x > screenWidth - rect.width) {
            rect.x = screenWidth - rect.width;
            velX = (int) (-velX * elasticity);
        }
		
		if(rect.y < 0) {
            rect.y = 0;
            velY = (int) (-velY * elasticity);
        } else if(rect.y > screenHeight - rect.height) {
            rect.y = screenHeight - rect.height;
            velY = (int) (-velY * elasticity);
        }
			
	}
	
	void checkCollision(Rectangle wall) {
		//check if rect touches wall
		if(rect.intersects(wall)) {
			System.out.println("collision");
			//stop the rect from moving
			double left1 = rect.getX();
			double right1 = rect.getX() + rect.getWidth();
			double top1 = rect.getY();
			double bottom1 = rect.getY() + rect.getHeight();
			double left2 = wall.getX();
			double right2 = wall.getX() + wall.getWidth();
			double top2 = wall.getY();
			double bottom2 = wall.getY() + wall.getHeight();
			
			if(right1 > left2 && 
			   left1 < left2 && 
			   right1 - left2 < bottom1 - top2 && 
			   right1 - left2 < bottom2 - top1)
	        {
	            //rect collides from left side of the wall
				rect.x = wall.x - rect.width;
                velX = (int) (-velX * elasticity);
	        }
	        else if(left1 < right2 &&
	        		right1 > right2 && 
	        		right2 - left1 < bottom1 - top2 && 
	        		right2 - left1 < bottom2 - top1)
	        {
	            //rect collides from right side of the wall
	        	rect.x = wall.x + wall.width;
                velX = (int) (-velX * elasticity);
	        }
	        else if(bottom1 > top2 && top1 < top2)
	        {
	            //rect collides from top side of the wall
	        	rect.y = wall.y - rect.height;
                velY = (int) (-velY * elasticity);
	        }
	        else if(top1 < bottom2 && bottom1 > bottom2)
	        {
	            //rect collides from bottom side of the wall
	        	rect.y = wall.y + wall.height;
                velY = (int) (-velY * elasticity);
	        }
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("Example");
		MovingAndCollisions_Momentum myPanel = new MovingAndCollisions_Momentum();
		frame.add(myPanel);
		frame.addKeyListener(myPanel);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}
}
