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
        String map = String.join("\n", l.readResources("scaffold0"));
        assertThat(l.findIntersectionsIn(map)).isEqualTo(76);
    }
}