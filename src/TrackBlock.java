import java.awt.*;

public class TrackBlock {

    static final int BLOCK_WIDTH = 256;
    static final int BLOCK_HEIGHT = 256;

    private final BlockType type; // Immutable
    private final BlockDirection direction; // Immutable

    public int gridX;
    public int gridY;
    public Rectangle hitbox;

    public boolean checkpointHit;

    public TrackBlock(BlockType type, int gridX, int gridY, BlockDirection direction) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
        this.direction = direction;
        this.checkpointHit = false;

        this.hitbox = new Rectangle(gridX * BLOCK_WIDTH, gridY * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    }

    public static TrackBlock parseBlockString(String str) {
        // TODO: track block parsing
        return new TrackBlock(BlockType.WALL, 0, 0, BlockDirection.UP);
    }

    public void draw(Graphics2D g) {
        Color previousColor = g.getColor();

        // int xPos = gridX * blockWidth;
        // int yPos = gridY * blockHeight;
        // TODO: draw
        switch (type) {
            case WALL:
                g.setColor(Color.GRAY);
                g.fill(hitbox);
                break;

            case START:
                // TODO: actual graphics and images and whatnot
                g.setColor(Color.GREEN);
                g.fill(hitbox);
                break;
            
            case CHECKPOINT:
                g.setColor(Color.YELLOW);
                g.fill(hitbox);
                break;

            case FINISH:
                g.setColor(Color.RED);
                g.fill(hitbox);
                break;

            case BOOST:
                g.setColor(Color.ORANGE);
                g.fill(hitbox);
                break;

            case NOCONTROL:
                g.setColor(Color.MAGENTA);
                g.fill(hitbox);
                break;
            
            case RESET:
                g.setColor(Color.CYAN);
                g.fill(hitbox);
                break;
        
            default:
                break;
        }

        g.setColor(previousColor);
    }

    public BlockType getType() {
        return type;
    }

    public BlockDirection getDirection() {
        return direction;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
