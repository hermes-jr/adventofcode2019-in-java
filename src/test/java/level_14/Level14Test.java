package level_14;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Level14Test {

    @ParameterizedTest
    @CsvSource(value = {
            "in0,31",
            "in1,165",
            "in2,13312",
            "in3,180697",
            "in4,2210736",
    })
    void energiesShouldBeCalculatedProperly(String filename, int expectedResult) {
        Level14 l = new Level14(filename);
        assertThat(l.p1()).isEqualTo(expectedResult);
    }

}
