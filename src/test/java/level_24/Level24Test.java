package level_24;

import org.junit.jupiter.api.Test;

import static level_24.Level24.DEFAULT_LAYER;
import static org.assertj.core.api.Assertions.assertThat;

class Level24Test {

    @Test
    void biodiversityCalculation() {
        Level24 l = new Level24("in1");
//        assertThat(l.getBiodiversity(l.recursiveMap[DEFAULT_LAYER])).isEqualTo(2129920L);
    }

    @Test
    void givenExampleShouldWork() {
        Level24 l = new Level24("in0");
//        assertThat(l.p1()).isEqualTo(2129920L);
    }

    @Test
    void givenExampleForPartTwoShouldWork() {
        Level24 l = new Level24("in0");
        assertThat(l.p2(10)).isEqualTo(99);
    }

}