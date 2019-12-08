package level_07;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Timeout(10)
class Level07Test {
    static Level07 l;

    @BeforeAll
    static void before() {
        l = new Level07();
    }

    @Test
    void testSampleProg1() {
        l.parseData("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0");
        assertThat(l.calcForPhase(Arrays.asList(4, 3, 2, 1, 0))).isEqualTo(43210);
    }

    @Test
    void testSampleProg2() {
        l.parseData("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0");
        assertThat(l.calcForPhase(Arrays.asList(0, 1, 2, 3, 4))).isEqualTo(54321);
    }

    @Test
    void testSampleProg3() {
        String p = l.readResources("in3");
        l.parseData(p);
        assertThat(l.calcForPhase(Arrays.asList(1, 0, 4, 3, 2))).isEqualTo(65210);
    }

    @Test
    void testSampleProg1AutoDetectBestPerm() {
        l.parseData("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0");
        assertThat(l.p1()).isEqualTo(43210);
    }

    @Test
    void testSampleProg4() {
        String p = l.readResources("in4");
        l.parseData(p);
        assertThat(l.calcForPhaseCyclic(Arrays.asList(9, 8, 7, 6, 5))).isEqualTo(139629729);
    }

    @Test
    void testSampleProg5() {
        String p = l.readResources("in5");
        l.parseData(p);
        assertThat(l.calcForPhaseCyclic(Arrays.asList(9, 7, 8, 5, 6))).isEqualTo(18216);
    }

}