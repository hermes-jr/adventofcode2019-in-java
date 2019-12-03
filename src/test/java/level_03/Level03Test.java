package level_03;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class Level03Test {
    Level03 l;

    @BeforeEach
    void beforeEach() {
        l = new Level03();
    }

    @Test
    void processResource() {
        assertThat(l.processResource("in0")).isEqualTo(6);
        assertThat(l.processResource("in1")).isEqualTo(159);
        assertThat(l.processResource("in2")).isEqualTo(135);
    }

    @Test
    void parseLine() {
        Set<ImmutablePair<Integer, Integer>> lineCoords = l.parseLine("R8,U5,L5,D3");
        assertThat(lineCoords).hasSize(22);
        assertThat(lineCoords).contains(new ImmutablePair<>(3, 2));
    }
}