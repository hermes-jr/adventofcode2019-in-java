package level_20;

import common.Level;
import common.Point3D;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Level20Test extends Level {
    private Level20 l;

    @BeforeEach
    void before() {
        l = new Level20();
    }

    @Test
    void shortestRouteInSingleDimensionShouldBeCalculatedSimpleGraph() {
        SimpleGraph<Point3D, DefaultEdge> g0 = l.parseMap(l.readResources("in0"));
        assertThat(l.p1(g0)).isEqualTo(23);
    }

    @Test
    void shortestRouteInSingleDimensionShouldBeCalculatedComplexGraph() {
        SimpleGraph<Point3D, DefaultEdge> g1 = l.parseMap(l.readResources("in1"));
        assertThat(l.p1(g1)).isEqualTo(58);
    }
}

