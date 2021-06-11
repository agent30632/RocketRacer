import java.awt.*;
import javax.swing.*;

public class Player {

    // Static final variables (i.e. things that should never change during the game)
    static final double ACCELERATION = 0.5;
    static final double BOOST_ACCELERATION = 0.5; // ADDED onto acceleration value
    static final double ROTATION_RATE = 5;
    static final double MIN_SPEED = 0.25;
    static final double MAX_SPEED = 60;

    static final double BRAKING = 0.95; // velocities are multiplied by this rate every frame (i.e. 0.8 = 20% velocity loss)
    static final double COLLISION_DAMPING = 0.25; // when collision occurs, the reflection velocity is multiplied by this value

    static final int PLAYER_WIDTH = 64;
    static final int PLAYER_HEIGHT = 64;
    static final int PLAYER_HITBOXTRIM = 8; // adjusts hitbox by trimming this amount from each dimension

    // instance variables
    // velocity
    public double velX;
    public double velY;
    
    // position
    public double posX;
    public double posY;

    public int gridX;
    public int gridY;

    // direction
    public double direction;

    // hitbox
    public Rectangle hitbox;
    
    // block logic stuff
    public int checkpointCount;
    public TrackBlock lastCheckpoint = null;
    public TrackBlock startBlock = null;
    public boolean isFinished = false;

    // movement booleans
    public boolean isAccelerating; // if player accelerating
    public boolean isBraking; // if player braking
    public boolean isTurningLeft; // if player turn left
    public boolean isTurningRight; // if player turn right

    // block booleans
    public boolean isBoosting;
    public boolean isNoControl;    

    ImageIcon playerBody = new ImageIcon("assets/img/player_main.png");
    Image playerBodyScaled;
    ImageIcon playerExhaust = new ImageIcon("assets/img/player_exhaust.png");
    Image playerExhaustScaled;
    ImageIcon playerBoost = new ImageIcon("assets/img/player_boost.png");
    Image playerBoostScaled;
    
    public Player(double posX, double posY, double direction, TrackBlock startBlock) {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;

        this.velX = 0;
        this.velY = 0;

        // block logic vars
        this.checkpointCount = 0;
        this.startBlock = startBlock;

        this.gridX = startBlock.gridX;
        this.gridY = startBlock.gridY;

        this.isFinished = false;

        hitbox = new Rectangle((int) posX, (int) posY, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerBodyScaled = playerBody.getImage().getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_DEFAULT);
        playerExhaustScaled = playerExhaust.getImage().getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_DEFAULT);
        playerBoostScaled = playerBoost.getImage().getScaledInstance(PLAYER_WIDTH, PLAYER_HEIGHT, Image.SCALE_DEFAULT);
    }


    public void update() {
        // changing the player state only works if you have control
        // duh
        if (!isNoControl) {
            if (isTurningRight) {
                direction -= ROTATION_RATE;
            }
            if (isTurningLeft) {
                direction += ROTATION_RATE;
            }
    
            if (direction < -180) {
                direction += 360;
            } else if (direction > 180) {
                direction -= 360;
            }
    
            if (isAccelerating) {
                // Oh geez trigonometry is pain
                // TODO: reduce the number of trig calculations
                double cos = Math.cos(direction * Math.PI / 180);
                double sin = Math.sin(direction * Math.PI / 180);

                velX += ACCELERATION * cos;
                velY += ACCELERATION * sin;
                if (isBoosting) {
                    // TODO: reduce the number of trig calculations
                    velX += BOOST_ACCELERATION * cos;
                    velY += BOOST_ACCELERATION * sin;
                }
            } else if (isBraking) {
                velX *= BRAKING;
                velY *= BRAKING;    
                // Zeroes out velocity if it goes below a certain threshold
                if (Math.abs(velY) < MIN_SPEED) {
                    velY = 0;
                }
                if (Math.abs(velX) < MIN_SPEED) {
                    velX = 0;
                }        
            }
        }

        if (velX > MAX_SPEED) {
            velX = MAX_SPEED;
        }
        if (velY > MAX_SPEED) {
            velY = MAX_SPEED;
        }

        posX += velX;
        posY -= velY;

        this.gridX = (int) posX / TrackBlock.BLOCK_WIDTH;
        this.gridY = (int) posY / TrackBlock.BLOCK_HEIGHT;

        hitbox.setRect(posX - PLAYER_WIDTH / 2 + PLAYER_HITBOXTRIM, posY - PLAYER_HEIGHT / 2 + PLAYER_HITBOXTRIM, PLAYER_WIDTH - PLAYER_HITBOXTRIM * 2, PLAYER_HEIGHT - PLAYER_HITBOXTRIM * 2);
    }

    public void draw(Graphics2D g) {
        // TODO: camera offset
        g.rotate(-direction * Math.PI / 180, posX, posY);
            
        if (isAccelerating) {
            if (isBoosting) {
                g.drawImage(playerBoostScaled, (int) posX - PLAYER_WIDTH/2 - (PLAYER_WIDTH - (PLAYER_WIDTH / 4)), (int) posY - PLAYER_WIDTH/2, null);
            } else {
                g.drawImage(playerExhaustScaled, (int) posX - PLAYER_WIDTH/2 - (PLAYER_WIDTH - (PLAYER_WIDTH / 4)), (int) posY - PLAYER_WIDTH/2, null);
            }
        }

        g.drawImage(playerBodyScaled, (int) posX - PLAYER_WIDTH/2, (int) posY - PLAYER_WIDTH/2, null);
        g.rotate(direction * Math.PI / 180, posX, posY);
    }

    public void checkCollision(Rectangle rect) {
        // NOTE: i yoinked this code from Stanley's game examples (the MovingAndCollisions one)
        // o7 to Stanley and his code

        if (hitbox.intersects(rect)) {
			//stop the rect from moving
			double left1 = hitbox.getX();
			double right1 = hitbox.getX() + hitbox.getWidth();
			double top1 = hitbox.getY();
			double bottom1 = hitbox.getY() + hitbox.getHeight();
			double left2 = rect.getX();
			double right2 = rect.getX() + rect.getWidth();
			double top2 = rect.getY();
			double bottom2 = rect.getY() + rect.getHeight();
			
			if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
	            //rect collides from left side of the wall
				hitbox.x = rect.x - hitbox.width;
                posX = hitbox.getCenterX();
                velX = -velX * COLLISION_DAMPING;
	        }

	        else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1) {
	            //rect collides from right side of the wall
	        	hitbox.x = rect.x + rect.width;
                posX = hitbox.getCenterX();
                velX = -velX * COLLISION_DAMPING;
	        }
            
	        else if(bottom1 > top2 && top1 < top2) {
	            //rect collides from top side of the wall
	        	hitbox.y = rect.y - hitbox.height;
                posY = hitbox.getCenterY();
                velY = -velY * COLLISION_DAMPING;
	        }

	        else if(top1 < bottom2 && bottom1 > bottom2) {
	            //rect collides from bottom side of the wall
	        	hitbox.y = rect.y + rect.height;
                posY = hitbox.getCenterY();
                velY = -velY * COLLISION_DAMPING;
	        }
		}
    }

    /**
     * Checks if the player is intersecting the given block, and applies block behaviour accordingly
     * @param block the block to check intersections with
     */
    public void checkBlockIntersection(TrackBlock block, Track track) {
        // If this block is adjacent to the player's current position
        // i.e. x within +- 1, y within +- 1
        // Should hopefully optimize collision calculations
        if (Math.abs(this.gridX - block.gridX) <= 1 && Math.abs(this.gridY - block.gridY) <= 1) {
            if (hitbox.intersects(block.hitbox)) {
                switch (block.getType()) {
                    case WALL:
                        checkCollision(block.hitbox);
                        break;
    
                    case START:
                        // does nothing upon intersection
                        break;
                    
                    case CHECKPOINT:
                        if (block.checkpointHit == false) {
                            this.checkpointCount++;
                            block.checkpointHit = true;
                            this.lastCheckpoint = block;
                        }
                        break;
        
                    case FINISH:
                        if (this.checkpointCount == track.getCheckpointCount()) {
                            this.isFinished = true;
                        }
                        break;
        
                    case BOOST:
                        this.isBoosting = true;
                        break;
        
                    case NOCONTROL:
                        this.isNoControl = true;
                        break;
                    
                    case RESET:
                        this.isNoControl = false;
                        this.isBoosting = false;
                        break;
                }
            }
        }        
    }

    public void keepInBounds() {
		if(this.posX < hitbox.width / 2) {
            this.setPos(hitbox.width / 2, this.posY);
            velX = -velX * COLLISION_DAMPING;
        } else if(this.posX > (Track.MAX_GRID_X * TrackBlock.BLOCK_WIDTH) - hitbox.width / 2) {
            this.setPos((Track.MAX_GRID_X * TrackBlock.BLOCK_WIDTH) - hitbox.width / 2, this.posY);
            velX = -velX * COLLISION_DAMPING;
        }
		
		if(this.posY < hitbox.height / 2) {
            this.setPos(this.posX, hitbox.height / 2);
            velY = -velY * COLLISION_DAMPING;
        } else if(this.posY > (Track.MAX_GRID_Y * TrackBlock.BLOCK_HEIGHT) - hitbox.width / 2) {
            this.setPos(this.posX, (Track.MAX_GRID_Y * TrackBlock.BLOCK_HEIGHT) - hitbox.width / 2);
            velY = -velY * COLLISION_DAMPING;
        }
			
	}

    public void setPos(double x, double y) {
        this.posX = x;
        this.posY = y;

        hitbox.setRect(posX - PLAYER_WIDTH / 2 + PLAYER_HITBOXTRIM, posY - PLAYER_HEIGHT / 2 + PLAYER_HITBOXTRIM, PLAYER_WIDTH - PLAYER_HITBOXTRIM * 2, PLAYER_HEIGHT - PLAYER_HITBOXTRIM * 2);
    }

    public void resetToCP() {
        velX = 0;
        velY = 0;
        
        this.isBoosting = false;
        this.isNoControl = false;

        this.isFinished = false;

        if (this.lastCheckpoint == null) {
            respawnToStart();
        } else {
            double x = this.lastCheckpoint.hitbox.getCenterX();
            double y = this.lastCheckpoint.hitbox.getCenterY();

            this.setPos(x, y);
            this.setDirection(this.lastCheckpoint.getDirection());
        }
    }

    public void respawnToStart() {
        velX = 0;
        velY = 0;

        this.isBoosting = false;
        this.isNoControl = false;

        this.isFinished = false;
        this.lastCheckpoint = null;

        double x = this.startBlock.hitbox.getCenterX();
        double y = this.startBlock.hitbox.getCenterY();

        this.setPos(x, y);
        this.setDirection(this.startBlock.getDirection());
        this.checkpointCount = 0;
    }

    /**
     * Sets the player's rotation to the same as a given block direction
     * @param direction the direction of the player
     */
    public void setDirection(BlockDirection direction) {
        switch(direction) {
            case UP:
                this.direction = 90;
                break;
            case DOWN:
                this.direction = -90;
                break;
            case LEFT:
                this.direction = -180;
                break;
            case RIGHT:
                this.direction = 0;
                break;
        }
    }
}
