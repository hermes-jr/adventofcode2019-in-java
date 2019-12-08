package level_08;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class Level08Test {
    /*
    Layer 1:   123
               456
    Layer 2:   789
               012
    Layer 3:   345
               678
     */
    @Test
    void correctPointShouldBeReturnedByGivenCoordinates() {
        Level08 l = new Level08("in2", 3, 2);
        assertThat(l.data)
                .containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(l.layers).isEqualTo(3);
        assertThat(l.getDigitAt(0, 0, 0)).isEqualTo(1);
        assertThat(l.getDigitAt(1, 1, 0)).isEqualTo(5);
        assertThat(l.getDigitAt(2, 1, 0)).isEqualTo(6);
        assertThat(l.getDigitAt(0, 0, 1)).isEqualTo(7);
        assertThat(l.getDigitAt(1, 1, 1)).isEqualTo(1);
        assertThat(l.getDigitAt(2, 0, 1)).isEqualTo(9);
        assertThat(l.getDigitAt(2, 1, 1)).isEqualTo(2);
        assertThat(l.getDigitAt(0, 0, 2)).isEqualTo(3);
        assertThat(l.getDigitAt(1, 0, 2)).isEqualTo(4);
        assertThat(l.getDigitAt(2, 0, 2)).isEqualTo(5);
        assertThat(l.getDigitAt(1, 1, 2)).isEqualTo(7);
        assertThat(l.getDigitAt(2, 1, 2)).isEqualTo(8);
    }

    @Test
    void badCoordinateShouldBeReported() {
        Level08 l = new Level08("in1", 3, 2);
        assertThat(l.layers).isEqualTo(2);
        assertThat(l.getDigitAt(0, 0, 0)).isEqualTo(1);

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> l.getDigitAt(0, 0, 9))
                .withMessageContaining("No such coordinate in image")
                .withNoCause();

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> l.getDigitAt(3, 3, 0));
    }

    @Test
    void layerWithFewestZerosShouldBeDetected() {
        Level08 l = new Level08("in3", 3, 2);
        assertThat(l.getLayerWithFewestZeros()).isEqualTo(2);

        Level08 l2 = new Level08("in1", 3, 2);
        assertThat(l2.getLayerWithFewestZeros()).isEqualTo(0);
    }

    @Test
    void coloredPixelsTest() {
        Level08 l = new Level08("in4", 2, 2);
        assertThat(l.p2()).isEqualTo("\n #\n# \n");
    }
}