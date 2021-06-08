import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.*;

import javax.swing.ImageIcon;

public class TrackBlock {

    static final int BLOCK_WIDTH = 256;
    static final int BLOCK_HEIGHT = 256;

    private BlockType type; // Immutable
    private BlockDirection direction; // Immutable

    public int gridX;
    public int gridY;
    public Rectangle hitbox;

    public boolean checkpointHit;

    // Images
    public static ImageIcon startBlock = new ImageIcon("assets/img/block_start.png");
    public static ImageIcon checkpointBlock = new ImageIcon("assets/img/block_checkpoint.png");
    public static ImageIcon finishBlock = new ImageIcon("assets/img/block_finish.png");
    public static ImageIcon nocontrolBlock = new ImageIcon("assets/img/block_nocontrol.png");
    public static ImageIcon resetBlock = new ImageIcon("assets/img/block_reset.png");
    public static ImageIcon boostBlock = new ImageIcon("assets/img/block_boost.png");
    
    public static Image startBlockImage = startBlock.getImage();
    public static Image checkpointBlockImage = checkpointBlock.getImage();
    public static Image finishBlockImage = finishBlock.getImage();
    public static Image nocontrolBlockImage = nocontrolBlock.getImage();
    public static Image resetBlockImage = resetBlock.getImage();
    public static Image boostBlockImage = boostBlock.getImage();



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
    public static TrackBlock parseBlock(String str) throws IllegalArgumentException {
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

        String coordTypeData = str.substring(0, typeDirSeperator);
        int coordTypeSeparator = coordTypeData.lastIndexOf(" ");

        String typeStr = coordTypeData.substring(coordTypeSeparator + 1);
        BlockType type = null;
        switch (typeStr) {
            case "START":
                type = BlockType.START;
                break;
            case "CHECKPOINT":
                type = BlockType.CHECKPOINT;
                break;
            case "FINISH":
                type = BlockType.FINISH;
                break;
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

        String coordData = coordTypeData.substring(0, coordTypeSeparator);
        StringTokenizer coordTokenizer = new StringTokenizer(coordData, " \t\n\r\f(),");
        
        int x = Integer.parseInt(coordTokenizer.nextToken());
        if (x < 0 || x >= Track.MAX_GRID_X) {
            throw new IllegalArgumentException();
        }

        int y = Integer.parseInt(coordTokenizer.nextToken());
        if (y < 0 || y >= Track.MAX_GRID_Y) {
            throw new IllegalArgumentException();
        }

        return new TrackBlock(type, x, y, direction);
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
                // g.fill(hitbox);
                g.drawImage(startBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                break;
            
            case CHECKPOINT:
                g.setColor(Color.YELLOW);
                // g.fill(hitbox);
                g.drawImage(checkpointBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                break;

            case FINISH:
                g.setColor(Color.RED);
                // g.fill(hitbox);
                g.drawImage(finishBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                break;

            case BOOST:
                g.setColor(Color.ORANGE);
                // g.fill(hitbox);
                double rotation = 0;
                switch (this.direction) {
                    case UP:
                        break;
                    case RIGHT:
                        rotation = Math.PI / 2;
                        break;
                    case DOWN:
                        rotation = Math.PI;
                        break;
                    case LEFT:
                        rotation = -Math.PI / 2;
                        break;
                }

                g.rotate(rotation, hitbox.getCenterX(), hitbox.getCenterY());
                
                g.drawImage(boostBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                g.rotate(-rotation, hitbox.getCenterX(), hitbox.getCenterY());
                break;

            case NOCONTROL:
                g.setColor(Color.MAGENTA);
                // g.fill(hitbox);
                g.drawImage(nocontrolBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                break;
            
            case RESET:
                g.setColor(Color.CYAN);
                // g.fill(hitbox);
                g.drawImage(resetBlockImage, (int) hitbox.getX(), (int) hitbox.getY(), null);
                break;
        
            default:
                break;
        }

        g.setColor(previousColor);
    }

    @Override
    public int hashCode() {
        // TODO: should hashcode hash based on other factors too?
        final int prime = 31;
        int result = 1;
        result = prime * result + gridX;
        result = prime * result + gridY;
        return result;
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
