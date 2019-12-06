package level_02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class Level02Test {
    private Level02 l;

    @BeforeEach
    void before() {
        l = new Level02();
    }

    /**
     * As initialization is happening inside of {@link Level02#runProg} method
     *
     * @param s      instructions set
     * @param result expected result in 0th cell
     */
    @ParameterizedTest(name = "0th element of \"{0}\" should be set to {1}")
    @CsvSource(value = {
            "1,0,0,0,99:2",
            "2,3,0,3,99:2",
            "2,4,4,5,99,0:2",
            "1,1,1,4,99,5,6,0,99:30",
            "1,9,10,3,2,3,11,0,99,30,40,50:3500"
    }, delimiter = ':')
    void testFirstNumber(String s, int result) {

        int[] data = l.parseData(s);
        assertThat(l.runProg(data, data[1], data[2])).isEqualTo(result);

    }
}