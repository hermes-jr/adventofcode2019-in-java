package level_05;

import common.IntComp;
import common.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(10)
class Level05 extends Level {
    @ParameterizedTest(name = "0th element of \"{0}\" should be set to {1}")
    @CsvSource(value = {
            "1,0,0,0,99:2",
            "2,3,0,3,99:2",
            "2,4,4,5,99,0:2",
            "1,1,1,4,99,5,6,0,99:30",
            "1,9,10,3,2,3,11,0,99,30,40,50:3500"
    }, delimiter = ':')
    void testFirstNumber(String s, long result) {
        IntComp ic = new IntComp(s, 0);
        ic.run();
        assertThat(ic.getData().get(0L)).isEqualTo(result);
    }

    @Test
    void wysiwygTest() {
        IntComp ic = new IntComp("3,0,4,0,99", 0);
        assertExactOutputWithGivenInput(ic, 0L, 0L);
        assertExactOutputWithGivenInput(ic, 123987L, 123987L);
    }

    // Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    @Test
    void conditionalProgramsEqEightPm() {
        IntComp ic = new IntComp("3,9,8,9,10,9,4,9,99,-1,8", 0);
        assertExactOutputWithGivenInput(ic, 8L, 1L);
        assertExactOutputWithGivenInput(ic, 9L, 0L);
        assertExactOutputWithGivenInput(ic, -1L, 0L);
    }

    // Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsLessEightPm() {
        IntComp ic = new IntComp("3,9,7,9,10,9,4,9,99,-1,8", 0);
        assertExactOutputWithGivenInput(ic, -1L, 1L);
        assertExactOutputWithGivenInput(ic, 1L, 1L);
        assertExactOutputWithGivenInput(ic, 8L, 0L);
        assertExactOutputWithGivenInput(ic, 100L, 0L);
    }

    // Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsEqEightIm() {
        IntComp ic = new IntComp("3,3,1108,-1,8,3,4,3,99", 0);
        assertExactOutputWithGivenInput(ic, 8L, 1L);
        assertExactOutputWithGivenInput(ic, 9L, 0L);
        assertExactOutputWithGivenInput(ic, -1L, 0L);
    }

    // Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @Test
    void conditionalProgramsLessEightIm() {
        IntComp ic = new IntComp("3,3,1107,-1,8,3,4,3,99", 0);
        assertExactOutputWithGivenInput(ic, -1L, 1L);
        assertExactOutputWithGivenInput(ic, 1L, 1L);
        assertExactOutputWithGivenInput(ic, 8L, 0L);
        assertExactOutputWithGivenInput(ic, 100L, 0L);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroPm(long p, long r) {
        IntComp ic = new IntComp("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", 0);
        assertExactOutputWithGivenInput(ic, p, r);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroIm(int p, int r) {
        IntComp ic = new IntComp("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", 0);
        assertExactOutputWithGivenInput(ic, p, r);
    }

    @Test
    void testThreeOutputs() {
        String prog = readResourcesFirstLine("three_outputs");
        IntComp ic = new IntComp(prog, 0);
        assertExactOutputWithGivenInput(ic, 1L, 999L);
        assertExactOutputWithGivenInput(ic, -1L, 999L);
        assertExactOutputWithGivenInput(ic, 8L, 1000L);
        assertExactOutputWithGivenInput(ic, 9L, 1001L);
        assertExactOutputWithGivenInput(ic, 1000L, 1001L);
    }

    void assertExactOutputWithGivenInput(IntComp ic, long input, long output) {
        ic.reset();
        ic.addToInput(input);
        ic.run();
        assertThat(ic.getOutput().poll()).isEqualTo(output);
    }
}