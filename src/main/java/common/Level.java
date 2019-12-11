package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Level {
    public static String readResourcesFirstLine(Class<?> clazz, String filename) {
        return readResources(clazz, filename).get(0);
    }

    public static List<String> readResources(Class<?> clazz, String filename) {
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(clazz.getClassLoader().getResourceAsStream("level_" + clazz.getSimpleName().substring(5) + "/" + filename))))) {
            List<String> result = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }

}
