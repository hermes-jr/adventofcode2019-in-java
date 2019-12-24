package level_24;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level24Test {

    @Test
    void biodiversityCalculation() {
        Level24 l = new Level24("in1");
        assertThat(l.getBiodiversity()).isEqualTo(2129920L);
    }

    @Test
    void givenExampleShouldWork() {
        Level24 l = new Level24("in0");
        assertThat(l.p1()).isEqualTo(2129920L);
    }

}