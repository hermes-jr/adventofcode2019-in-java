package level_17;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level17Test {
    static Level17 l;

    @BeforeAll
    static void before() {
        l = new Level17("input");
    }

    @Test
    void intersectionsShouldBeCountedProperly() {
        // 13 x 7
        String in = String.join("\n", l.readResources("scaffold0")) + "\n";
        boolean[][] map = l.parseData(in);
        assertThat(map).hasSize(9);
        assertThat(map[0]).hasSize(15).doesNotContain(true);
        assertThat(map[3][3]).isEqualTo(true); // 2:2 + borders
        assertThat(l.findIntersectionsIn(map)).isEqualTo(76);
    }
}