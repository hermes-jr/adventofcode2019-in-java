package level_10;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class Level10Test {

    @ParameterizedTest(name = "For map in \"{0}\" element {1}:{2} should have ${3} visible asteroids")
    @CsvSource(value = {
            "in1,3,4,8",
            "in1,1,0,7",
            "in1,4,0,7",
            "in1,0,2,6",
            "in1,1,2,7",
            "in1,2,2,7",
            "in1,3,2,7",
            "in1,4,2,5",
            "in1,4,3,7",
            "in1,4,4,7",
            "in2,5,8,33",
            "in3,1,2,35"
    })
    void testSinglePointVisibilityCounter(String s, int x, int y, int canBeSeen) {
        Level10 l = new Level10(s);
        assertThat(l.getSinglePointVisibleCount(new Level10.Point(x, y))).isEqualTo(canBeSeen);
    }

    @Test
    void simplestMap() {
        Level10 l = new Level10("in0");
        assertThat(l.getSinglePointVisibleCount(new Level10.Point(0, 0))).isEqualTo(2);
    }

    @Test
    void getForEach() {
        Level10 l = new Level10("in1");
        assertThat(l.getSinglePointVisibleCount(new Level10.Point(3, 4))).isEqualTo(8);
    }

    @Test
    void testEliminationOrderSimplest() {
        Level10 l = new Level10("in6");
        assertThat(l.p2(new Level10.Point(3, 8))).isEqualTo(802);
    }

    @Test
    void testEliminationOrderSimplest2() {
        Level10 l = new Level10("in0");
        assertThat(l.p2(new Level10.Point(0, 0))).isEqualTo(101);
        assertThat(l.p2(new Level10.Point(1, 1))).isEqualTo(2);
    }

    @ParameterizedTest(name = "For \"{0}\" an optimal point {1}:{2} with visibility of ${3} should be returned")
    @CsvSource(value = {
            "in1,3,4,8",
            "in2,5,8,33",
            "in3,1,2,35",
            "in4,6,3,41",
            "in5,11,13,210"
    })
    void expectResultsToBeSameAsInExamples(String s, int x, int y, int canBeSeen) {
        Level10 l = new Level10(s);
        int[] result = l.p1();
        assertThat(result[0]).isEqualTo(x);
        assertThat(result[1]).isEqualTo(y);
        assertThat(result[2]).isEqualTo(canBeSeen);
    }

}