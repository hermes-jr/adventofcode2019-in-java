package level_04;

public class Level04 {
    static int b1 = 197487;
    static int b2 = 673251;

    public static void main(String[] args) {
        Level04 l = new Level04();

        int result1 = 0;
        int result2 = 0;
        for (int i = b1; i <= b2; i++) {
            boolean[] r = l.validatePassword(i);
            if (r[0]) {
                result1++;
            }
            if (r[1]) {
                result2++;
            }
        }

        System.out.println("Part1: " + result1);
        System.out.println("Part2: " + result2);
    }

    public boolean[] validatePassword(int num) {
        boolean[] result = new boolean[]{
                false, false
        };
        int[] pairsSeen = new int[10];
        int prev = 10;
        while (num != 0) {
            int rem = num % 10;
            num /= 10;
            if (rem > prev)
                return new boolean[]{false, false};
            else if (rem == prev) {
                result[0] = true;
                pairsSeen[prev]++;
            }
            prev = rem;
        }

        for (int z : pairsSeen) {
            if (z == 1) {
                result[1] = true;
                break;
            }
        }
        return result;
    }
}
