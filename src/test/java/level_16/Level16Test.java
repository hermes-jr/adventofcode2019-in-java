package level_16;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Level16Test {
    static Level16 l;

    @BeforeAll
    static void before() {
        l = new Level16();
    }

    @Test
    void exampleWithFirstFourTransformationsShouldWork() {
        int[] data = l.parseString("12345678");
        l.fft(data, 1);
        assertThat(data).containsExactly(4, 8, 2, 2, 6, 1, 5, 8);
        l.fft(data, 1);
        assertThat(data).containsExactly(3, 4, 0, 4, 0, 4, 3, 8);
        l.fft(data, 1);
        assertThat(data).containsExactly(0, 3, 4, 1, 5, 5, 1, 8);
        l.fft(data, 1);
        assertThat(data).containsExactly(0, 1, 0, 2, 9, 4, 9, 8);
    }

    @Test
    void biggerExamplesShouldGiveProperResults() {
        int[] data;
        data = l.parseString("80871224585914546619083218645595");
        l.fft(data, 100);
        assertThat(data).startsWith(2, 4, 1, 7, 6, 1, 7, 6);

        data = l.parseString("19617804207202209144916044189917");
        l.fft(data, 100);
        assertThat(data).startsWith(7, 3, 7, 4, 5, 4, 1, 8);

        data = l.parseString("69317163492948606335995924319873");
        l.fft(data, 100);
        assertThat(data).startsWith(5, 2, 4, 3, 2, 1, 3, 3);
    }

    @Test
    void properMultiplierShouldBeChosen() {
        int cycle = 1; // 0 1 0 -1
        assertThat(l.getMultiplier(0, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(1, cycle)).isEqualTo(1);
        assertThat(l.getMultiplier(2, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(3, cycle)).isEqualTo(-1);
        assertThat(l.getMultiplier(4, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(5, cycle)).isEqualTo(1);
        cycle = 2; // 0 0 1 1 0 0 -1 -1
        assertThat(l.getMultiplier(0, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(1, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(2, cycle)).isEqualTo(1);
        assertThat(l.getMultiplier(4, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(6, cycle)).isEqualTo(-1);
        assertThat(l.getMultiplier(10, cycle)).isEqualTo(1);
        cycle = 4; // cycle 4: 0 0 0 0 1 1 1 1 0 0 0 0 -1 -1 -1 -1
        assertThat(l.getMultiplier(3, cycle)).isEqualTo(0);
        assertThat(l.getMultiplier(4, cycle)).isEqualTo(1);
        assertThat(l.getMultiplier(20, cycle)).isEqualTo(1);
        assertThat(l.getMultiplier(28, cycle)).isEqualTo(-1);
    }

    @Test
    void testWithOffsets() {
        int[] data = l.parseString("03036732577212944063491565474664");
        assertThat(l.p2(data, 303673)).isEqualTo("84462026");

        data = l.parseString("02935109699940807407585447034323");
        assertThat(l.p2(data, 293510)).isEqualTo("78725270");

        data = l.parseString("03081770884921959731165446850517");
        assertThat(l.p2(data, 308177)).isEqualTo("53553731");
    }
}
