public class Vector2 {

    static final boolean CARTESIAN = false;
    static final boolean GEOMETRIC = true;
    
    private double x;
    private double y;

    /**
     * Constructs a 2D cartesian vector with the specified x and y components
     * @param x
     * @param y
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a 2D vector with the specified magnitude and heading (degrees)
     * @param val1 If this is a cartesian vector, indicates the x component of the vector. If this is a geometric vector, indicates the magnitude of the vector.
     * @param val2 If this is a cartesian vector, indicates the y component of the vector. If this is a geometric vector, indicates the heading of the vector.
     * @param vectorType a flag to indicate whether this is a cartesian or geometric vector, using either {@code Vector.CARTESIAN} or {@code Vector.GEOMETRIC}
     */
    public Vector2(double val1, double val2, boolean vectorType) {
        if (vectorType) {
            this.x = val1 * Math.cos(val2 * Math.PI / 180);
            this.y = val1 * Math.sin(val2 * Math.PI / 180);
        } else {
            this.x = val1;
            this.y = val2;
        }
    }
    
    /**
     * Calculates the magnitude of a vector
     * @return the current vector's magnitude
     */
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Calculates the dot product of a vector
     * @param v2 the vector to dot product with
     * @return the dot product
     */
    public double dot(Vector2 v2) {
        return this.x * v2.getX() + this.y * v2.getY();
    }

    /**
     * Calculates the dot product of two vectors
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the dot product
     */
    public static double dot(Vector2 v1, Vector2 v2) {
        return v1.getX() * v2.getX() + v1.getY() + v2.getY();
    }

    /**
     * Uses Java's {@code Math.atan2()} method to determine the <b>heading</b> of the vector (clockwise with 0° pointing towards +y).
     * @return the heading of a vector in degrees
     */
    public double heading() {
        double atanDeg = (Math.atan2(this.y, this.x) * (180.0/Math.PI));

        return atanDeg;
    }

    /**
     * Adds a given vector to the current vector
     * @param v2 the vector to add
     * @return the resultant vector
     */
    public Vector2 add(Vector2 v2) {
        return new Vector2(this.x + v2.getX(), this.y + v2.getY());
    }
    
    /**
     * Adds two vectors together
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the resultant vector
     */
    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    /**
     * Subtracts a given vector from the current one (equivalent to adding the negative vector)
     * @param v2 the vector to subtract
     * @return the resultant vector
     */
    public Vector2 sub(Vector2 v2) {
        return new Vector2(this.x - v2.getX(), this.y - v2.getY());
    }

    /**
     * Subtracts one vector from another
     * @param v1 the initial vector
     * @param v2 the vector to subtract
     * @return the resultant vector
     */
    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    /**
     * Multiplies the current vector by a scalar
     * @param scalar the scalar to multiply by
     * @return the scaled vector
     */
    public Vector2 multiply(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    /**
     * Multiplies the given vector by a scalar
     * @param vec the vector to multiply
     * @param scalar the scalar to multiply by
     * @return the scaled vector
     */
    public static Vector2 multiply(Vector2 vec, double scalar) {
        return new Vector2(vec.getX() * scalar, vec.getY() * scalar);
    }

    /**
     * Linearly interpolates between two vectors using a value {@code t}. 
     * @param v1 starting vector (t = 0)
     * @param v2 resultant vector (t = 1)
     * @param t interpolation value (clamped to 0 ≤ t ≤ 1)
     * @return interpolated vector
     */
    public static Vector2 lerp(Vector2 v1, Vector2 v2, double t) {
        // Value clamping
        if (t < 0) {
            t = 0;
        } else if (t > 1) {
            t = 1;
        }

        if (t == 0) {
            return v1;
        } else if (t == 1) {
            return v2;
        }

        return Vector2.add(v1, Vector2.sub(v1, v2).multiply(t));
    }

    public Vector2 unitVector() {
        Vector2 unitVector = new Vector2(this.x, this.y);
        double magnitude = this.magnitude();
        unitVector.x /= magnitude;
        unitVector.y /= magnitude;

        return unitVector;
    }

    public String toString() {
        return "[ " + x + " , " + y + " ]";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
