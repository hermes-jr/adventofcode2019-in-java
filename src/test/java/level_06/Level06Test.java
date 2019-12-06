package level_06;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level06Test {
    static Level06 l;

    @BeforeAll
    static void before() {
        l = new Level06();
    }

    @Test
    void partOneFormulaShouldWork() {
        SimpleGraph<String, DefaultEdge> g = l.readResources("example1");
        assertThat(l.p1(g)).isEqualTo(42);
    }

    @Test
    void partTwoFormulaShouldWork() {
        SimpleGraph<String, DefaultEdge> g = l.readResources("example2");
        assertThat(l.p2(g)).isEqualTo(4);
    }

}