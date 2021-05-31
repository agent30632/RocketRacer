import java.awt.*;
import javax.swing.*;

public class Player {

    // unchanging variables
    static final double acceleration = 0.5;
    static final double rotationRate = 5;
    static final double speedCap = 1000;
    static final double minSpeed = 0.1;

    static final double braking = 0.95; // velocities are multiplied by this rate every frame (i.e. 0.8 = 20% velocity loss)
    static final double elasticity = 0.3; // when collision occurs, the reflection velocity is multiplied by this value

    static final int playerWidth = 64;
    static final int playerHeight = 64;
    static final int hitboxTrim = 5; // adjusts hitbox by trimming this amount from each dimension

    // instance variables
    public double velX;
    public double velY;
    
    public double posX;
    public double posY;

    public double direction;

    public boolean isAccelerating;
    public boolean isBraking;
    public boolean isTurningLeft;
    public boolean isTurningRight;

    public Rectangle hitbox;

    ImageIcon playerBody = new ImageIcon("assets/img/player_main.png");
    Image playerBodyScaled = playerBody.getImage().getScaledInstance(playerWidth, playerHeight, Image.SCALE_DEFAULT);
    ImageIcon playerExhaust = new ImageIcon("assets/img/player_exhaust.png");
    Image playerExhaustScaled = playerExhaust.getImage().getScaledInstance(playerWidth, playerHeight, Image.SCALE_DEFAULT);
    
    public Player(int posX, int posY, double direction) {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;

        this.velX = 0;
        this.velY = 0;

        hitbox = new Rectangle((int) posX, (int) posY, playerWidth, playerHeight);
    }


    public void update() {

        if (isTurningRight) {
            direction -= rotationRate;
        }
        if (isTurningLeft) {
            direction += rotationRate;
        }

        if (direction < -180) {
            direction += 360;
        } else if (direction > 180) {
            direction -= 360;
        }

        if (isAccelerating) {
            Vector2 accelerationVector = new Vector2(acceleration, direction, Vector2.GEOMETRIC);
            velX += accelerationVector.getX();
            velY += accelerationVector.getY();
        }

        if (isBraking) {
            velX *= braking;
            velY *= braking;    
            // Zeroes out velocity if it goes below a certain threshold
            if (Math.abs(velY) < minSpeed) {
                velY = 0;
            }
            if (Math.abs(velX) < minSpeed) {
                velX = 0;
            }        
        }

        posX += velX;
        posY -= velY;

        // TODO: verify hitbox position
        hitbox.setRect(posX - playerWidth/2 + hitboxTrim, posY- playerHeight/2 + hitboxTrim, playerWidth - hitboxTrim * 2, playerHeight - hitboxTrim * 2);
    }

    public void draw(Graphics2D g) {
        // TODO: camera offset
        g.fill(hitbox);

        g.rotate(-direction * Math.PI / 180, posX, posY);

        if (isAccelerating) {
            g.drawImage(playerExhaustScaled, (int) posX - playerWidth/2 - (playerWidth - 16), (int) posY - playerWidth/2, null);
        }

        g.drawImage(playerBodyScaled, (int) posX - playerWidth/2, (int) posY - playerWidth/2, null);
        // g.fill(new Rectangle((int) posX - playerWidth/2, (int) posY - playerWidth/2, playerWidth, playerHeight));
        g.rotate(direction * Math.PI / 180, posX, posY);
    }

    public void checkCollision(Rectangle wall) {
        // NOTE: i yoinked this code from Stanley's game examples (the MovingAndCollisions one)
        // o7 to Stanley and his code
        if (hitbox.intersects(wall)) {
			//stop the rect from moving
			double left1 = hitbox.getX();
			double right1 = hitbox.getX() + hitbox.getWidth();
			double top1 = hitbox.getY();
			double bottom1 = hitbox.getY() + hitbox.getHeight();
			double left2 = wall.getX();
			double right2 = wall.getX() + wall.getWidth();
			double top2 = wall.getY();
			double bottom2 = wall.getY() + wall.getHeight();
			
			if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
	            //rect collides from left side of the wall
				hitbox.x = wall.x - hitbox.width;
                velX = (int) (-velX * elasticity);
	        }

	        else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1) {
	            //rect collides from right side of the wall
	        	hitbox.x = wall.x + wall.width;
                velX = (int) (-velX * elasticity);
	        }
            
	        else if(bottom1 > top2 && top1 < top2) {
	            //rect collides from top side of the wall
	        	hitbox.y = wall.y - hitbox.height;
                velY = (int) (-velY * elasticity);
	        }

	        else if(top1 < bottom2 && bottom1 > bottom2) {
	            //rect collides from bottom side of the wall
	        	hitbox.y = wall.y + wall.height;
                velY = (int) (-velY * elasticity);
	        }
		}
    }
}
