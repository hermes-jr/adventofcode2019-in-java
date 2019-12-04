package level_04;

// Brute force solution (facepalm)
public class Level04 {

    public static void main(String[] args) {
        int result1 = 0;
        int result2 = 0;
        for (int i = 197487; i <= 673251; i++) {
            if (!isAsc(i)) {
                continue;
            }
            if (hasDoubles(i)) {
                result1++;
            }
            if (hasUniqueDoubles(i)) {
                result2++;
            }
        }

        System.out.println("Result1: " + result1);
        System.out.println("Result2: " + result2);
    }

    static boolean isAsc(int num) {
        int prev = 10;
        while (num != 0) {
            int rem = num % 10;
            num /= 10;
            if (rem > prev)
                return false;
            prev = rem;
        }

        return true;
    }

    static boolean hasDoubles(int num) {
        int prev = 10;
        while (num != 0) {
            int rem = num % 10;
            num /= 10;
            if (rem == prev)
                return true;
            prev = rem;
        }

        return false;
    }

    static boolean hasUniqueDoubles(int num) {
        int[] pairsSeen = new int[10];
        int prev = 10;
        while (num != 0) {
            int rem = num % 10;
            num /= 10;
            if (rem == prev)
                pairsSeen[prev]++;
            prev = rem;
        }

        for (int i : pairsSeen) {
            if (i == 1) return true;
        }
        return false;
    }

}
