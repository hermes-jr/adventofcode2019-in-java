package level_18;

import common.Level;
import common.Point2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Level18Test extends Level {

    @ParameterizedTest
    @CsvSource(value = {
            "in0,8",
            "in1,86",
            "in2,132",
            "in3,136",
            "in4,81"
    })
    void partOneNumberOfTotalStepsShouldBeCalculatedProperly(String filename, int result) {
        Level18 l = new Level18(filename);
        assertThat(l.p1()).isEqualTo(result);
    }

    @Test
    void simplestExample() {
        Level18 l = new Level18("in0");
        assertThat(l.p1()).isEqualTo(8);
    }

    @Test
    void cacheTest() {
        Set<ImmutablePair<Point2D, Set<Point2D>>> cache = new HashSet<>();
        Point2D a = new Point2D(10, 20);
        Set<Point2D> doorsLocked = new HashSet<>();
        doorsLocked.add(new Point2D(10, 1));
        doorsLocked.add(new Point2D(3, 3));
        doorsLocked.add(new Point2D(1, 1));
        cache.add(ImmutablePair.of(a, Collections.unmodifiableSet(doorsLocked)));
        Set<Point2D> doorsLocked2 = new HashSet<>();
        doorsLocked2.add(new Point2D(3, 3));
        doorsLocked2.add(new Point2D(1, 1));
        doorsLocked2.add(new Point2D(10, 1));
        assertThat(cache.contains(ImmutablePair.of(new Point2D(10, 20), Collections.unmodifiableSet(doorsLocked2)))).isTrue();
    }

}

