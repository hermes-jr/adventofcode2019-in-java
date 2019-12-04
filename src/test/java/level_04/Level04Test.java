package level_04;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level04Test {
    static Level04 l;

    @BeforeAll
    static void before() {
        l = new Level04();
    }

    @Test
    void testRulesPart1() {
        assertThat(l.validatePassword(111111)[0]).isTrue(); // meets these criteria (double 11, never decreases).
        assertThat(l.validatePassword(223450)[0]).isFalse(); // does not meet these criteria (decreasing pair of digits 50).
        assertThat(l.validatePassword(123789)[0]).isFalse(); // does not meet these criteria (no double).
    }

    @Test
    void testRulesPart2() {
        assertThat(l.validatePassword(112233)[1]).isTrue();  // meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
        assertThat(l.validatePassword(123444)[1]).isFalse(); // no longer meets the criteria (the repeated 44 is part of a larger group of 444).
        assertThat(l.validatePassword(111122)[1]).isTrue(); // meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
    }

}