package level_05;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest(name = "0th element of \"{0}\" should be set to {1}")
    @CsvSource(value = {
            "1,0,0,0,99:2",
            "2,3,0,3,99:2",
            "2,4,4,5,99,0:2",
            "1,1,1,4,99,5,6,0,99:30",
            "1,9,10,3,2,3,11,0,99,30,40,50:3500"
    }, delimiter = ':')
    void testFirstNumber(String s, int result) {
        int[] data = l.parseData(s);
        assertThat(l.runProg(data, 1)).isEqualTo(result);
    }

    @Test
    void wysiwygTest() {
        l.runProg(new int[]{3, 0, 4, 0, 99}, 1); // supposed to output "PROGOUT: 1"
//        l.runProg(new int[]{1002, 4, 3, 4, 33}, 1);
    }

}