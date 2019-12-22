package level_22;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Level22Test {

    @Test
    void stackOperationTest() {
        int[] deck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Level22.stack(deck);
        assertThat(deck).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
    }

    @Test
    void incrementDealOperationTest() {
        int[] deck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Level22.increment(deck, 3);
        assertThat(restoreDeckFromIndices(deck)).containsExactly(0, 7, 4, 1, 8, 5, 2, 9, 6, 3);
    }

    @Test
    void cutOperationTest() {
        int[] deck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Level22.cut(deck, 3);
        assertThat(restoreDeckFromIndices(deck)).containsExactly(3, 4, 5, 6, 7, 8, 9, 0, 1, 2);

        deck = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Level22.cut(deck, -4);
        assertThat(restoreDeckFromIndices(deck)).containsExactly(6, 7, 8, 9, 0, 1, 2, 3, 4, 5);
    }

    private int[] restoreDeckFromIndices(int[] deck) {
        int[] result = new int[deck.length];
        for (int i = 0; i < deck.length; i++) {
            result[deck[i]] = i;
        }
        return result;
    }
}