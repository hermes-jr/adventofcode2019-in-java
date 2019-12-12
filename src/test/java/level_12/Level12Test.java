package level_12;

import common.Point3D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Level12Test {

    @ParameterizedTest
    @CsvSource(value = {
            "2,1,3,3,2,1,6,6,36",
            "1,8,0,1,1,3,9,5,45",
            "3,6,1,3,2,3,10,8,80",
            "2,0,4,1,1,1,6,3,18",
            "2,0,-4,1,1,-1,6,3,18"
    })
    void energiesShouldBeCalculatedProperly(int x, int y, int z, int dx, int dy, int dz, int p, int k, int t) {
        Level12.Moon m1 = new Level12.Moon(new Point3D(x, y, z), new Point3D(dx, dy, dz));
        assertThat(m1.getPotentialEnergy()).isEqualTo(p);
        assertThat(m1.getKineticEnergy()).isEqualTo(k);
        assertThat(m1.getTotalEnergy()).isEqualTo(t);
    }

    @Test
    void expectSimulationToWorkProperly() {
        Level12 l = new Level12("in0");
        assertThat(l.moons).hasSize(4);
        // ? for each step expect trimmed moon.toString to be equal to given string, also trimmed of spaces
    }

    @Test
    void expectCorrectTotalEnergyInTenSteps() {
        Level12 l = new Level12("in0");
        long totalEnergy = l.p1(10);
        assertThat(totalEnergy).isEqualTo(179);
    }

}
