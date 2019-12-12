package common;

import java.util.Comparator;

public class Point3D implements Comparable<Point3D> {
    int x;
    int y;
    int z;
    public final static Point3D ZERO = new Point3D(0, 0, 0);
    public final static Comparator<Point3D> POINT_COMPARATOR = Comparator.comparingInt(Point3D::getZ).thenComparingInt(Point3D::getX).thenComparingInt(Point3D::getY);

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3D point3D = (Point3D) o;

        if (x != point3D.x) return false;
        if (y != point3D.y) return false;
        return z == point3D.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "[" + getX() + ":" + getY() + ":" + getZ() + "]";
    }

    @Override
    public int compareTo(Point3D o) {
        return POINT_COMPARATOR.compare(this, o);
    }

    public void add(Point3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }
}

