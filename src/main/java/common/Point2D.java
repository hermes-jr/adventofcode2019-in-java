package common;

public class Point2D extends Point3D {
    public final static Point2D ZERO = new Point2D(0, 0);

    public Point2D(int x, int y) {
        super(x, y, 0);
    }

    @Override
    public void setZ(int z) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String toString() {
        return "[" + getX() + ":" + getY() + "]";
    }
}

