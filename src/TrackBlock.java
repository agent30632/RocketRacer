public class TrackBlock {
    enum Type {
        START,
        CHECKPOINT,
        FINISH,
        BOOST,
        NOCONTROL,
        WALL
    }

    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    private Type type;
    private Direction direction;
    
    public int gridX;
    public int gridY;

    public TrackBlock(Type type, int gridX, int gridY) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public static TrackBlock parseBlockString(String str) {
        // TODO: track block parsing
        return new TrackBlock(TrackBlock.Type.WALL, 0, 0);
    }
}
