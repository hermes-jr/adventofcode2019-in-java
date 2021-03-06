package level_03;

import common.Point2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class Level03Test {
    Level03 l;
    ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires0;
    ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires1;
    ImmutablePair<ArrayList<Point2D>, ArrayList<Point2D>> wires2;
    Set<Point2D> in0intersections;
    Set<Point2D> in1intersections;
    Set<Point2D> in2intersections;

    @BeforeEach
    void beforeEach() {
        l = new Level03();
        wires0 = l.processResource("in0");
        wires1 = l.processResource("in1");
        wires2 = l.processResource("in2");
        in0intersections = l.findIntersections(wires0);
        in1intersections = l.findIntersections(wires1);
        in2intersections = l.findIntersections(wires2);
    }

    @Test
    void distanceToTheClosestIntersectionToOriginShouldBeCalculatedProperly() {
        assertThat(l.getDistanceToClosestIntersection(in0intersections)).isEqualTo(6);
        assertThat(l.getDistanceToClosestIntersection(in1intersections)).isEqualTo(159);
        assertThat(l.getDistanceToClosestIntersection(in2intersections)).isEqualTo(135);
    }

    @Test
    void distanceToTheClosestIntersectionByWireShouldBeCalculatedProperly() {
        assertThat(l.getShortestWireLengthToIntersection(wires0, in0intersections)).isEqualTo(30);
        assertThat(l.getShortestWireLengthToIntersection(wires1, in1intersections)).isEqualTo(610);
        assertThat(l.getShortestWireLengthToIntersection(wires2, in2intersections)).isEqualTo(410);
    }

    @Test
    void lineCoordinatesShouldBeParsed() {
        ArrayList<Point2D> lineCoords = l.parseLine("R8,U5,L5,D3");
        assertThat(lineCoords)
                .hasSize(22)
                .contains(new Point2D(3, 2));
    }
}