public class Camera {
    private double x;
    private double y;
    
    /**
     * Creates a new camera object with the specified x and y coordinates
     * @param x
     * @param y
     */
    public Camera(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the camera's position with the provided x and y coordinates
     * @param x
     * @param y
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the current camera's position, using the given player and screen dimensions as reference
     * @param player player object to base position on
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     */
    public void update(Player player, int screenWidth, int screenHeight) {
        // Don't know why these had to be negative but hey it works
        x = -player.posX + screenWidth / 2;
        y = -player.posY + screenHeight / 2;
    }

    public double getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
}
