package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public abstract class Level {
    final Map<String, List<String>> cached = new HashMap<>();

    public String readResourcesFirstLine(String filename) {
        return readResources(filename).get(0);
    }

    public List<String> readResources(String filename) {
        if (cached.get(filename) != null) {
            return cached.get(filename);
        }
        try (BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("level_" + getClass().getSimpleName().substring(5) + "/" + filename))))) {
            List<String> result = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            cached.put(filename, Collections.unmodifiableList(result));
            return Collections.unmodifiableList(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read resources", e);
        }
    }
}
