public class Testing {
    public static void main(String[] args) {
        Vector2 vector = new Vector2(-1.0, 1.0);
        System.out.println(vector);
        System.out.println(vector.magnitude() + " " + vector.heading());

        Vector2 convertedVector = new Vector2(vector.magnitude(), (int) vector.heading());
        System.out.println(convertedVector);
    }
}
