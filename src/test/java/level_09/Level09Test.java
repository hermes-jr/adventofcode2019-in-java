package level_09;

import common.IntComp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(10)
class Level09Test {
    static Level09 l;

    @BeforeAll
    static void before() {
        l = new Level09();
    }

    @Test
    void testSampleProg1() {
        String s = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        IntComp comp = new IntComp(s, 0);
        IntComp.ReturnReason rr = comp.run();
        assertThat(rr).isEqualTo(IntComp.ReturnReason.HALTED);
        assertThat(comp.getOutput().stream().map(Objects::toString).collect(Collectors.joining(","))).isEqualTo(s);
    }

    @Test
    void testSampleProg2() {
        String prog = "1102,34915192,34915192,7,4,7,99,0";
        IntComp comp = new IntComp(prog, 0);
        comp.run();
        assertThat(comp.getOutput().peek()).isNotNull();
        assertThat(Objects.requireNonNull(comp.getOutput().peek()).toString()).hasSize(16);
    }

    @Test
    void testSampleProg3() {
        String prog = "104,1125899906842624,99";
        IntComp comp = new IntComp(prog, 0);
        comp.addToInput(0L);
        comp.run();
        assertThat(comp.getOutput().peek()).isEqualTo(1125899906842624L);
    }

    @Test
    void testRelativeBaseInstruction() {
        String prog = "109,19,99";
        IntComp comp = new IntComp(prog, 0);
        comp.setRelBase(2000L);
        comp.run();
        assertThat(comp.getRelBase()).isEqualTo(2019L);

        prog = "109,19,204,-34,99";
        comp = new IntComp(prog, 1);
        comp.setRelBase(2000L);

/*
        // not throwing ioobe anymore
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(comp::run)
                .withMessageStartingWith("Index 1985 out of bounds");
*/
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
        IntComp ic = new IntComp(s, -1);
        ic.run();
        assertThat(ic.getData().get(0L)).isEqualTo(result);
    }

    @Test
    void wysiwygTest() {
        IntComp ic1 = new IntComp("3,0,4,0,99", 0);
        IntComp ic2 = new IntComp("3,0,4,0,99", 1);
        ic1.addToInput(1L);
        ic1.run();
        assertThat(ic1.getOutput()).containsExactly(1L);
        long in2 = 123987L;
        ic2.addToInput(in2);
        ic2.run();
        assertThat(ic2.getOutput()).containsExactly(in2);
    }

    // Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    @ParameterizedTest
    @CsvSource(value = {
            "8,1",
            "9,0",
            "-1,0"
    })
    void conditionalProgramsEqEightPm(long p, long r) {
        IntComp ic = new IntComp("3,9,8,9,10,9,4,9,99,-1,8", 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

    // Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "-1,1",
            "1,1",
            "8,0",
            "100,0"
    })
    void conditionalProgramsLessEightPm(long p, long r) {
        IntComp ic = new IntComp("3,9,7,9,10,9,4,9,99,-1,8", 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

    // Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "8,1",
            "9,0",
            "-1,0"
    })
    void conditionalProgramsEqEightIm(long p, long r) {
        IntComp ic = new IntComp("3,3,1108,-1,8,3,4,3,99", 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

    // Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "-1,1",
            "1,1",
            "8,0",
            "100,0"
    })
    void conditionalProgramsLessEightIm(long p, long r) {
        IntComp ic = new IntComp("3,3,1107,-1,8,3,4,3,99", 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
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
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroIm(long p, long r) {
        IntComp ic = new IntComp("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,999",
            "-1,999",
            "8,1000",
            "9,1001",
            "1000,1001"
    })
    void testThreeOutputs(long p, long r) {
        IntComp ic = new IntComp(l.readResources("three_outputs"), 0);
        ic.addToInput(p);
        ic.run();
        assertThat(ic.getOutput()).containsExactly(r);
    }

}