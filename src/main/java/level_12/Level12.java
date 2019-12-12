package level_12;

import common.Level;
import common.Point3D;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Level12 extends Level {
    final static AtomicInteger moonCounter = new AtomicInteger(0);
    final Pattern coordinatesPattern = Pattern.compile("^<x=(-?[0-9]+), y=(-?[0-9]+), z=(-?[0-9]+)>$");
    List<Moon> moons = new LinkedList<>();

    public Level12(String input) {
        for (String s : readResources(input)) {
            Matcher inMatcher = coordinatesPattern.matcher(s);
            if (!inMatcher.matches()) {
                throw new RuntimeException("Unable to parse input: " + s);
            }
            int x = Integer.parseInt(inMatcher.group(1));
            int y = Integer.parseInt(inMatcher.group(2));
            int z = Integer.parseInt(inMatcher.group(3));
            moons.add(new Moon(new Point3D(x, y, z), Point3D.ZERO));
        }
    }

    public long p1(int steps) {
        while (steps-- > 0) {
            doStep();
        }
        long totalEnergy = 0;
        for (Moon m : moons) {
            totalEnergy += m.getTotalEnergy();
        }
        return totalEnergy;
    }

    private void doStep() {
        List<Integer> xs = moons.stream().map(p -> p.getPosition().getX()).sorted(Integer::compareTo).collect(Collectors.toList());
        List<Integer> ys = moons.stream().map(p -> p.getPosition().getY()).sorted(Integer::compareTo).collect(Collectors.toList());
        List<Integer> zs = moons.stream().map(p -> p.getPosition().getZ()).sorted(Integer::compareTo).collect(Collectors.toList());
        for (Moon m : moons) {
            ImmutablePair<Integer, Integer> xba = getBeforeAfter(xs, m.getPosition().getX());
            ImmutablePair<Integer, Integer> yba = getBeforeAfter(ys, m.getPosition().getY());
            ImmutablePair<Integer, Integer> zba = getBeforeAfter(zs, m.getPosition().getZ());
            int oldXv = m.getVelocity().getX();
            int oldYv = m.getVelocity().getY();
            int oldZv = m.getVelocity().getZ();
            // subtract number of moons below, add number of moons above
            Point3D newVel = new Point3D(
                    oldXv - xba.getLeft() + xba.getRight(),
                    oldYv - yba.getLeft() + yba.getRight(),
                    oldZv - zba.getLeft() + zba.getRight()
            );

            m.velocity = newVel;
            m.getPosition().add(newVel);
        }
    }

    private ImmutablePair<Integer, Integer> getBeforeAfter(List<Integer> list, int x) {
        int before = 0;
        int after = 0;
        for (Integer c : list) {
            if (c.compareTo(x) < 0) {
                before++;
            } else if (c.compareTo(x) > 0) {
                after++;
            }
        }
        return ImmutablePair.of(before, after);
    }

    public static void main(String[] args) {
        Level12 l = new Level12("input");
        System.out.println("Part1: " + l.p1(1000));
    }

    static class Moon {
        Point3D position;
        Point3D velocity;
        int id;

        Moon(Point3D position, Point3D velocity) {
            this.position = position;
            this.velocity = velocity;
            this.id = moonCounter.getAndAdd(1);
        }

        public Point3D getPosition() {
            return position;
        }

        public Point3D getVelocity() {
            return velocity;
        }

        long getPotentialEnergy() {
            return Math.abs(position.getX()) + Math.abs(position.getY()) + Math.abs(position.getZ());
        }

        long getKineticEnergy() {
            return Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());
        }

        long getTotalEnergy() {
            return getKineticEnergy() * getPotentialEnergy();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Moon moon = (Moon) o;

            return id == moon.id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return String.format("id=%3d pos=<x=%3d, y=%3d, z=%3d>, vel=<x=%3d, y=%3d, z=%3d>",
                    id,
                    position.getX(), position.getY(), position.getZ(),
                    velocity.getX(), velocity.getY(), velocity.getZ());
        }
    }
}