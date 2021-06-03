import java.awt.*;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.*;

public class TrackBlock {

    static final int BLOCK_WIDTH = 256;
    static final int BLOCK_HEIGHT = 256;

    private BlockType type; // Immutable
    private BlockDirection direction; // Immutable

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

    /**
     * Constructs a TrackBlock object, under the assumption that it follows the format "(x, y) <type> <direction>".
     * @param str the string to parse
     * @throws IllegalArgumentException if either the given coordinates are out of range
     */
    public TrackBlock(String str) throws IllegalArgumentException {
        int typeDirSeperator = str.lastIndexOf(" ");

        String direction = str.substring(typeDirSeperator + 1);
        switch (direction) {
            case "UP":
                this.direction = BlockDirection.UP;
                break;
            case "RIGHT":
                this.direction = BlockDirection.RIGHT;
                break;
            case "DOWN":
                this.direction = BlockDirection.DOWN;
                break;
            case "LEFT":
                this.direction = BlockDirection.LEFT;
                break;
        }

        String coordTypeData = str.substring(0, typeDirSeperator);
        int coordTypeSeparator = coordTypeData.lastIndexOf(" ");

        String type = coordTypeData.substring(coordTypeSeparator + 1);
        switch (type) {
            case "START":
                this.type = BlockType.START;
                break;
            case "CHECKPOINT":
                this.type = BlockType.CHECKPOINT;
                break;
            case "FINISH":
                this.type = BlockType.FINISH;
                break;
            case "BOOST":
                this.type = BlockType.BOOST;
                break;
            case "NOCONTROL":
                this.type = BlockType.NOCONTROL;
                break;
            case "RESET":
                this.type = BlockType.RESET;
                break;
            case "WALL":
                this.type = BlockType.WALL;
                break;
        }

        String coordData = coordTypeData.substring(0, coordTypeSeparator);
        StringTokenizer coordTokenizer = new StringTokenizer(coordData, " \t\n\r\f(),");
        
        int x = Integer.parseInt(coordTokenizer.nextToken());
        if (x < 0 || x >= Track.MAX_GRID_X) {
            throw new IllegalArgumentException();
        }
        this.gridX = x;

        int y = Integer.parseInt(coordTokenizer.nextToken());
        if (y < 0 || y >= Track.MAX_GRID_Y) {
            throw new IllegalArgumentException();
        }
        this.gridY = y;

        this.checkpointHit = false;
        this.hitbox = new Rectangle(gridX * BLOCK_WIDTH, gridY * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
    }

    /**
     * Given a string following the format "(x1, y1) (x2, y2) <type> <direction>", returns a list of track blocks corresponding to that range of blocks. 
     * Does not validate for proper format, this must be done prior to the method call.
     * @param str the block data to parse
     * @return a HashSet containing each individual track block
     * @throws IllegalArgumentException if the data is invalid (e.g. invalid coordinates, invalid types or directions)
     */
    public static HashSet<TrackBlock> parseBlockRange(String str) throws IllegalArgumentException {
        HashSet<TrackBlock> blockSet = new HashSet<>();

        int typeDirSeperator = str.lastIndexOf(" ");

        String directionStr = str.substring(typeDirSeperator + 1);
        BlockDirection direction = null;
        switch (directionStr) {
            case "UP":
                direction = BlockDirection.UP;
                break;
            case "RIGHT":
                direction = BlockDirection.RIGHT;
                break;
            case "DOWN":
                direction = BlockDirection.DOWN;
                break;
            case "LEFT":
                direction = BlockDirection.LEFT;
                break;
        }
        if (direction == null) {
            throw new IllegalArgumentException();
        }

        String otherData = str.substring(0, typeDirSeperator);

        String typeStr = otherData.substring(otherData.lastIndexOf(" ") + 1);
        BlockType type = null;
        switch (typeStr) {
            case "BOOST":
                type = BlockType.BOOST;
                break;
            case "NOCONTROL":
                type = BlockType.NOCONTROL;
                break;
            case "RESET":
                type = BlockType.RESET;
                break;
            case "WALL":
                type = BlockType.WALL;
                break;
        }
        if (type == null) {
            throw new IllegalArgumentException();
        }

        String coordinates = otherData.substring(0, otherData.lastIndexOf(" "));
        StringTokenizer coordTokenizer = new StringTokenizer(coordinates, " \t\n\r\f(),");
        
        int x1 = Integer.parseInt(coordTokenizer.nextToken());
        if (x1 < 0 || x1 >= Track.MAX_GRID_X) {
            throw new IllegalArgumentException();
        }

        int y1 = Integer.parseInt(coordTokenizer.nextToken());
        if (y1 < 0 || y1 >= Track.MAX_GRID_Y) {
            throw new IllegalArgumentException();
        }

        int x2 = Integer.parseInt(coordTokenizer.nextToken());
        if (x2 < 0 || x2 >= Track.MAX_GRID_X) {
            throw new IllegalArgumentException();
        }

        int y2 = Integer.parseInt(coordTokenizer.nextToken());
        if (y2 < 0 || y2 >= Track.MAX_GRID_Y) {
            throw new IllegalArgumentException();
        }

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                blockSet.add(new TrackBlock(type, i, j, direction));
            }
        }

        return blockSet;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TrackBlock other = (TrackBlock) obj;
        if (gridX != other.gridX)
            return false;
        if (gridY != other.gridY)
            return false;
        return true;
    }

    public BlockType getType() {
        return type;
    }

    public BlockDirection getDirection() {
        return direction;
    }
}
