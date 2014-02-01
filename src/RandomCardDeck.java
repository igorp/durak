import java.util.Random;

public class RandomCardDeck extends CardDeck {
    
    public RandomCardDeck() {
        super();
        shuffle();
    }
    
    public void shuffle() {
        RenderedCard[] shuffled = new RenderedCard[cards.length];
        Random rand = new Random();
        int cIndex;
        boolean placed;
        for (int c=0; c < cards.length; c++) {
            do {
                placed = false;
                cIndex = rand.nextInt(cards.length);
                if (shuffled[cIndex] == null) {
                    shuffled[cIndex] = cards[c];
                    placed = true;
                }
            } while(!placed);
        }
        cards = shuffled;
        top = 0;
    }
    
    public void reset() {
        super.reset();
        shuffle();
    }
}