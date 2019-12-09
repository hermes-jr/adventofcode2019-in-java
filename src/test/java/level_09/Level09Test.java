package level_09;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import java.util.Map;
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
    void digitAtWorksProperly() {
        assertThat(Level09.getDigitAt(BigInteger.valueOf(2), 1)).isEqualTo(2);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(10), 1)).isEqualTo(0);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(10), 2)).isEqualTo(1);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(10), 3)).isEqualTo(0);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(1503), 1)).isEqualTo(3);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(1503), 4)).isEqualTo(1);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(1503), 7)).isEqualTo(0);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(0), 1)).isEqualTo(0);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(-1), 1)).isEqualTo(1);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(-1), 2)).isEqualTo(0);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(-15), 1)).isEqualTo(5);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(-15), 2)).isEqualTo(1);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(1), 0)).isEqualTo(1);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(99), 0)).isEqualTo(99);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(183), 0)).isEqualTo(83);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(-15), 0)).isEqualTo(15);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(6711111111111111111L), 18)).isEqualTo(7);
        assertThat(Level09.getDigitAt(BigInteger.valueOf(6711111111111111111L), 19)).isEqualTo(6);
    }

    @Test
    void testSampleProg1() {
        String s = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        Map<BigInteger, BigInteger> prog = l.parseData(s);
        Level09.IntComp comp = new Level09.IntComp(prog, 0);
        Level09.ReturnReason rr = comp.run();
        assertThat(rr).isEqualTo(Level09.ReturnReason.HALTED);
        assertThat(comp.output.stream().map(Objects::toString).collect(Collectors.joining(","))).isEqualTo(s);
    }

    @Test
    void testSampleProg2() {
        Map<BigInteger, BigInteger> prog = l.parseData("1102,34915192,34915192,7,4,7,99,0");
        Level09.IntComp comp = new Level09.IntComp(prog, 0);
        comp.run();
        assertThat(comp.output.peek()).isNotNull();
        assertThat(comp.output.peek().toString()).hasSize(16);
    }

    @Test
    void testSampleProg3() {
        Map<BigInteger, BigInteger> prog = l.parseData("104,1125899906842624,99");
        Level09.IntComp comp = new Level09.IntComp(prog, 0);
        comp.input.add(BigInteger.ZERO);
        comp.run();
        assertThat(comp.output.peek()).isEqualTo(BigInteger.valueOf(1125899906842624L));
    }

    @Test
    void testRelativeBaseInstruction() {
        Map<BigInteger, BigInteger> prog = l.parseData("109,19,99");
        Level09.IntComp comp = new Level09.IntComp(prog, 0);
        comp.relBase = BigInteger.valueOf(2000);
        comp.run();
        assertThat(comp.relBase).isEqualTo(2019);

        prog = l.parseData("109,19,204,-34,99");
        comp = new Level09.IntComp(prog, 1);
        comp.relBase = BigInteger.valueOf(2000);

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
        Level09.IntComp ic = new Level09.IntComp(l.parseData(s), -1);
        ic.run();
        assertThat(ic.data.get(BigInteger.ZERO)).isEqualTo(BigInteger.valueOf(result));
    }

    @Test
    void wysiwygTest() {
        Level09.IntComp ic1 = new Level09.IntComp(l.parseData("3,0,4,0,99"), 0);
        Level09.IntComp ic2 = new Level09.IntComp(l.parseData("3,0,4,0,99"), 1);
        ic1.input.add(BigInteger.ONE);
        ic1.run();
        assertThat(ic1.output).containsExactly(BigInteger.ONE);
        BigInteger in2 = BigInteger.valueOf(123987);
        ic2.input.add(in2);
        ic2.run();
        assertThat(ic2.output).containsExactly(in2);
    }

    // Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not).
    @ParameterizedTest
    @CsvSource(value = {
            "8,1",
            "9,0",
            "-1,0"
    })
    void conditionalProgramsEqEightPm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,9,8,9,10,9,4,9,99,-1,8"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

    // Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "-1,1",
            "1,1",
            "8,0",
            "100,0"
    })
    void conditionalProgramsLessEightPm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,9,7,9,10,9,4,9,99,-1,8"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

    // Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "8,1",
            "9,0",
            "-1,0"
    })
    void conditionalProgramsEqEightIm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,3,1108,-1,8,3,4,3,99"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

    // Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
    @ParameterizedTest
    @CsvSource(value = {
            "-1,1",
            "1,1",
            "8,0",
            "100,0"
    })
    void conditionalProgramsLessEightIm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,3,1107,-1,8,3,4,3,99"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }


    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroPm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "-1,1",
            "1,1",
            "99,1"
    })
    void testForZeroIm(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData("3,3,1105,-1,9,1101,0,0,12,4,12,99,1"), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,999",
            "-1,999",
            "8,1000",
            "9,1001",
            "1000,1001"
    })
    void testThreeOutputs(int p, int r) {
        Level09.IntComp ic = new Level09.IntComp(l.parseData(l.readResources("three_outputs")), 0);
        ic.input.add(BigInteger.valueOf(p));
        ic.run();
        assertThat(ic.output).containsExactly(BigInteger.valueOf(r));
    }

}