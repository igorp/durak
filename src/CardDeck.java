public class CardDeck {

    protected RenderedCard[] cards;
    protected int top;

    public CardDeck() {
        top = 0;
        char[] suits = Card.getSuits();
        //number of possible cards is number of suits * number of possible values
        int numValues = Card.MAX - Card.MIN + 1;
        cards = new RenderedCard[suits.length * numValues];
        int cIndex;

        for (int s = 0; s < suits.length; s++) {
            for (int v = Card.MIN; v <= Card.MAX; v++) {
                cIndex = s * numValues + v - Card.MIN;
                cards[cIndex] = new RenderedCard(v, suits[s], true);
            }
        }
    }

    public Card getCard(int index) {
        return cards[index];
    }

    public void list() {
        for (int c = this.getTopIndex(); c < this.getNumCards(); c++) {
            System.out.print(cards[c] + " ");
        }
    }

    public int getTopIndex() {
        return top;
    }

    //returns the card at the top index and moves the index
    public Card deal() {
        Card dealt = cards[top++];
        //if last card dealt, reset the top card
        if (top > cards.length)
        {
            return null;
        }
        return dealt;
    }

    public void reset() {
        top = 0;
    }

    public int getNumCards() {
        return cards.length;
    }

    public int getNumCardsLeft() {
        return cards.length - top;
    }
}
