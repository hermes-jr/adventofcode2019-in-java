package level_05;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(10)
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
        l.parseData(s);
        l.runProg(-1);
        assertThat(l.getData()[0]).isEqualTo(result);
    }

    @Test
    void wysiwygTest() {
        assertThat(l.runProg(new int[]{3, 0, 4, 0, 99}, 1)).containsExactly(1);
        assertThat(l.runProg(new int[]{3, 0, 4, 0, 99}, 123987)).containsExactly(123987);
    }

    // Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    @Test
    void conditionalProgramsEqEightPm() {
        l.parseData("3,9,8,9,10,9,4,9,99,-1,8");
        assertThat(l.runProg(8)).containsExactly(1);
        assertThat(l.runProg(9)).containsExactly(0);
        assertThat(l.runProg(-1)).containsExactly(0);
    }

    // Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsLessEightPm() {
        l.parseData("3,9,7,9,10,9,4,9,99,-1,8");
        assertThat(l.runProg(-1)).containsExactly(1);
        assertThat(l.runProg(1)).containsExactly(1);
        assertThat(l.runProg(8)).containsExactly(0);
        assertThat(l.runProg(100)).containsExactly(0);
    }

    // Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsEqEightIm() {
        l.parseData("3,3,1108,-1,8,3,4,3,99");
        assertThat(l.runProg(8)).containsExactly(1);
        assertThat(l.runProg(9)).containsExactly(0);
        assertThat(l.runProg(-1)).containsExactly(0);
    }

    // Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsLessEightIm() {
        l.parseData("3,3,1107,-1,8,3,4,3,99");
        assertThat(l.runProg(-1)).containsExactly(1);
        assertThat(l.runProg(1)).containsExactly(1);
        assertThat(l.runProg(8)).containsExactly(0);
        assertThat(l.runProg(100)).containsExactly(0);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroPm(int p, int r) {
        l.parseData("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9");
        assertThat(l.runProg(p)).containsExactly(r);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroIm(int p, int r) {
        l.parseData("3,3,1105,-1,9,1101,0,0,12,4,12,99,1");
        assertThat(l.runProg(p)).containsExactly(r);
    }

    @Test
    void testThreeOutputs() {
        String prog = l.readResources("three_outputs");
        l.parseData(prog);
        assertThat(l.runProg(1)).containsExactly(999);
        assertThat(l.runProg(-1)).containsExactly(999);
        assertThat(l.runProg(8)).containsExactly(1000);
        assertThat(l.runProg(9)).containsExactly(1001);
        assertThat(l.runProg(1000)).containsExactly(1001);
    }

}