package common;

import java.util.Comparator;
import java.util.Objects;

public class Point implements Comparable<Point> {
    int x;
    int y;
    public final static Point ZERO = new Point(0, 0);
    public final static Comparator<Point> POINT_COMPARATOR = Comparator.comparingInt(Point::getX).thenComparingInt(Point::getY);

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return this.x == point.x && this.y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return "[" + getX() + ":" + getY() + "]";
    }

    @Override
    public int compareTo(Point o) {
        return POINT_COMPARATOR.compare(this, o);
    }
}

