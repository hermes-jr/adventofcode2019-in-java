package level_05;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level05Test {
    static Level05 l;

    @BeforeAll
    static void before() {
        l = new Level05();
    }

    @Test
    void digitAtWorksProperly() {
        assertThat(Level05.getDigitAt(2, 1)).isEqualTo(2);
        assertThat(Level05.getDigitAt(10, 1)).isEqualTo(0);
        assertThat(Level05.getDigitAt(10, 2)).isEqualTo(1);
        assertThat(Level05.getDigitAt(10, 3)).isEqualTo(0);
        assertThat(Level05.getDigitAt(1503, 1)).isEqualTo(3);
        assertThat(Level05.getDigitAt(1503, 4)).isEqualTo(1);
        assertThat(Level05.getDigitAt(1503, 7)).isEqualTo(0);
        assertThat(Level05.getDigitAt(0, 1)).isEqualTo(0);
        assertThat(Level05.getDigitAt(-1, 1)).isEqualTo(1);
        assertThat(Level05.getDigitAt(-1, 2)).isEqualTo(0);
        assertThat(Level05.getDigitAt(-15, 1)).isEqualTo(5);
        assertThat(Level05.getDigitAt(-15, 2)).isEqualTo(1);
        assertThat(Level05.getDigitAt(1, 0)).isEqualTo(1);
        assertThat(Level05.getDigitAt(99, 0)).isEqualTo(99);
        assertThat(Level05.getDigitAt(183, 0)).isEqualTo(83);
        assertThat(Level05.getDigitAt(-15, 0)).isEqualTo(15);
    }


}