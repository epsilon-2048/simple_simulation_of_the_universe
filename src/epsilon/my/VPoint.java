package epsilon.my;

public class VPoint {
    private double x;
    private double y;

    @Override
    public String toString() {
        return "VPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public VPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
