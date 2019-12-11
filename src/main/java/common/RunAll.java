package common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunAll {
    public static void main(String[] args) {
        for (int i = 1; i <= 25; i++) {
            try {
                String levelClassName = String.format("level_%02d.Level%02d", i, i);
                Class<?> clazz = Class.forName(levelClassName);
                System.out.println("----------------------------------------");
                System.out.println("Executing " + levelClassName);
                Method mainMethod = clazz.getMethod("main", String[].class);
                long start = System.currentTimeMillis();
                mainMethod.invoke(null, (Object) new String[]{});
                long now = System.currentTimeMillis();
                System.out.println(String.format("Level %d execution took %d millis", i, now - start));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ignored) {
                // too early for this level, skip.
            }
        }
    }
}
