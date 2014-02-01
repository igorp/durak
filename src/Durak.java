import java.util.Vector;
import java.awt.event.*;
import java.awt.*;
import javax.swing.ImageIcon;

public class Durak extends Frame {

    public static final String STRING_WINDOW_NAME = "Durak";
    private DurakDeck deck;
    private Vector<Card> computerHand;
    private Vector<Card> playerHand;
    private Vector<Card> table;
    private CardLayout cardLayout;
    private Panel splashScreen;
    private Panel mainPanel;
    private Panel gamePanel;
    private Panel computerPanel;
    private Panel playerPanel;
    private Panel tablePanel;
    private Panel sidePanel;
    private Panel testPanel;
    private Splash endTurnImg;
    private Splash pickUp;
    private char highSuit;
    private MouseListener ml;
    private RenderedCard clickedCard;
    private boolean playersTurn = true;
    private boolean playerHasDefended = false;
    private boolean endGame = false;
    private boolean quitGame = false;
    private int n;
    private RenderedCard highCard;

    private void initCards() {
        computerHand = new Vector<Card>();
        playerHand = new Vector<Card>();
        table = new Vector<Card>();
        deck = new DurakDeck();
        deck.addMouseListenersToCards(ml);
        playerHand.clear();
        computerHand.clear();
        table.clear();
        dealCards();
    }

    private void initFrame() {

        setSize(750, 500);
        setResizable(false);
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        Color c = new Color(222, 0, 0);

        Splash splashImg = new Splash("picture_data/ss3.gif", 750, 500);

        endTurnImg = new Splash("picture_data/endturn3.gif", 100, 50);
        pickUp = new Splash("picture_data/pick_up.gif", 100, 50);

        mainPanel = new Panel();
        splashScreen = new Panel();
        gamePanel = new Panel();
        computerPanel = new Panel();
        tablePanel = new Panel();
        playerPanel = new Panel();
        sidePanel = new Panel();

        testPanel = new Panel();
        testPanel.setLayout(new BorderLayout(10, 90));
        testPanel.setBackground(c);

        mainPanel.setLayout(new BorderLayout());
        gamePanel.setLayout(new BorderLayout());
        computerPanel.setLayout(new FlowLayout());
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 100));
        playerPanel.setLayout(new FlowLayout());
        sidePanel.setLayout(new BorderLayout());

        sidePanel.add(testPanel, BorderLayout.CENTER);

        mainPanel.setBackground(c);
        computerPanel.setBackground(c);
        playerPanel.setBackground(c);
        tablePanel.setBackground(c);
        sidePanel.setBackground(c);

        gamePanel.add(computerPanel, BorderLayout.NORTH);
        gamePanel.add(tablePanel, BorderLayout.CENTER);
        gamePanel.add(playerPanel, BorderLayout.SOUTH);
        gamePanel.add(sidePanel, BorderLayout.EAST);


        mainPanel.add(gamePanel);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        splashScreen.setBackground(c);
        splashScreen.add(splashImg);
        splashImg.addMouseListener(ml);

        add("C1", splashScreen);
        add("C2", mainPanel);
        endTurnImg.addMouseListener(ml);
        pickUp.addMouseListener(ml);
        updateFrame();
    }

    public void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
                dispose();
            }
        });

        ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (quitGame) {
                    System.exit(0);
                    dispose();                  
                } else {
                    if (n++ == 0) {
                        startGame();
                    } else if (e.getSource() instanceof RenderedCard) {

                        clickedCard = (RenderedCard) e.getSource();

                        if (clickedCard.getParent() == playerPanel) {
                            if (playersTurn) {

                                endGame = playersTurn();
                            } else {
                                playerDefends(table);
                                if (playerHasDefended) {

                                    computersTurn();
                                    endGame = checkWinningConditions();
                                    playerHasDefended = false;
                                }
                            }
                        }
                    } else if (e.getSource() instanceof Splash) {
                        if (playersTurn) {

                            playersTurn = false;
                            table.clear();
                            refillCards(true);
                            computersFirstTurn();
                            endGame = checkWinningConditions();
                        } else {
                            playerPicksUpCards();
                            refillCards(false);
                            computersFirstTurn();
                            endGame = checkWinningConditions();
                        }
                    }
                }
                updateFrame();
                if (endGame) {
                    displayEndGamePicture();
                    quitGame = true;
                }
            }
        };
    }

    public void displayEndGamePicture() {
        tablePanel.removeAll();
        if (playerHand.size() == 0) {
            Splash won = new Splash("picture_data/won.gif", 600, 250);
            tablePanel.add(won);
        } else {
            Splash lost = new Splash("picture_data/lost.gif", 600, 250);
            tablePanel.add(lost);
        }
        tablePanel.doLayout();
        gamePanel.doLayout();
    }

    public void playerPicksUpCards() {
        playerHand.addAll(table);
        table.clear();
        refillCards(false);
        playersTurn = false;
    }

    public void startGame() {
        cardLayout.next(this);
        setSize(750, 500);
        setResizable(true);
    }

    public Durak() {
        //initalize general settings for frame
        super(STRING_WINDOW_NAME);
        setIconImage(new ImageIcon("picture_data/kh.gif").getImage());

        initListeners();
        initCards();
        initFrame();
        setVisible(true);
    }

    public boolean playersTurn() {
        clickedCard = playerSelectedCardToHit();
        if (clickedCard != null) {
            table.add(clickedCard);
            playersTurn = computerDefend(table);
        }
        return checkWinningConditions();
    }

    public boolean computersFirstTurn() {
        clickedCard = computerSelectCardToHit(true);
        computerHand.remove(clickedCard);
        table.add(clickedCard);
        return checkWinningConditions();
    }

    public boolean computersTurn() {
        clickedCard = computerSelectCardToHit(false);
        if (clickedCard != null) {
            System.out.println(clickedCard);
            table.add(clickedCard);
        } else {
            table.clear();
            playersTurn = true;
            refillCards(true);
        }
        return checkWinningConditions();
    }

    public static void main(String[] args) {
        Durak d = new Durak();
    }

    public void updateFrame() {
        showComputerCards();
        showTableCards();
        showPlayersCards();

        updateSidePanel();

        //reset their layout
        computerPanel.doLayout();
        tablePanel.doLayout();
        playerPanel.doLayout();
    }

    public void updateSidePanel() {
        int numbah = deck.getNumCardsLeft();
        String num = Integer.toString(numbah);
        Splash cardsLeft = new Splash("picture_data/" + num + ".gif", 135, 100);
        RenderedCard bottomCard = new RenderedCard(highCard.getFaceValue(), highSuit, true);

        testPanel.removeAll();

        if (deck.getNumCardsLeft() > 0) {
            testPanel.add(bottomCard);
            System.out.println("high card: " + highCard);
        }
        testPanel.add(cardsLeft, BorderLayout.NORTH);
        sidePanel.remove(endTurnImg);
        
        if (playersTurn) {
            sidePanel.remove(pickUp);
            sidePanel.add(endTurnImg, BorderLayout.SOUTH);
        } else {
            sidePanel.remove(endTurnImg);
            sidePanel.add(pickUp, BorderLayout.SOUTH);
            sidePanel.doLayout();
        }
        testPanel.doLayout();
    }

    protected boolean checkWinningConditions() {
        if (playerHand.size() == 0 && deck.getNumCardsLeft() == 0) {
            return true;
        }
        if (computerHand.size() == 0 && deck.getNumCardsLeft() == 0) {
            return true;
        }
        return false;
    }

    protected void getHighCard() {
        highCard = (RenderedCard) deck.getCard(deck.getNumCards() - 1);
    }

    protected void dealCards() {
        getHighCard();
        for (int c = 0; c < 6; c++) {
            playerHand.add(deck.deal());
            computerHand.add(deck.deal());
        }
        //set the highsuit and mark appropriate cards
        highSuit = highCard.getSuit();
        for (int i = 0; i < deck.getNumCards(); i++) {
            RenderedCard card = (RenderedCard) deck.getCard(i);
            if (card.getSuit() == highSuit) {
                card.setHighSuit(true);
            }
        }
    }

    protected void showPlayersCards() {
        playerPanel.removeAll();
        for (int c = 0; c < playerHand.size(); c++) {
            RenderedCard temp = (RenderedCard) playerHand.elementAt(c);
            playerPanel.add(temp);
        }
    }

    protected void showTableCards() {
        tablePanel.removeAll();
        for (int d = 0; d < table.size(); d++) {
            RenderedCard temp = (RenderedCard) table.elementAt(d);
            temp.setVisible(true);
            tablePanel.add((RenderedCard) table.elementAt(d));
        }
    }

    protected void showComputerCards() {
        computerPanel.removeAll();
        for (int d = 0; d < computerHand.size(); d++) {
            RenderedCard temp = (RenderedCard) computerHand.elementAt(d);
            temp.setVisible(false);
            computerPanel.add((RenderedCard) computerHand.elementAt(d));
        }
    }

    //checks if the the clicked card is allowed to hit, if it is returns it, otherwise returns null
    protected RenderedCard playerSelectedCardToHit() {

        if (table.size() == 0) {
            playerHand.remove(clickedCard);
            return clickedCard;
        } else if (isAllowedToHit(clickedCard)) {
            playerHand.remove(clickedCard);
            return clickedCard;
        }
        return null;
    }

    protected RenderedCard computerSelectCardToHit(boolean firstCard) {
        boolean firstTime = true;
        RenderedCard cardToHit = null;
        RenderedCard[] computerCards = new RenderedCard[computerHand.size()];
        computerHand.copyInto(computerCards);
        int index = -1;

        boolean includeHighSuits = computersCardsAreHighSuit();


        if (firstCard) {
            // this for loop searchs through cards to find the smallest one, excluding high suits
            for (int c = 0; c < computerCards.length; c++) {
                if (firstTime) {
                    if (includeHighSuits) {
                        cardToHit = computerCards[c];
                        index = c;
                        firstTime = false;
                    } else {
                        if (computerCards[c].getSuit() != highSuit) {
                            cardToHit = computerCards[c];
                            index = c;
                            firstTime = false;
                        }
                        if (computerCards[c].getSuit() == highSuit) {
                            firstTime = true;
                        }
                    }
                }
                if (!firstTime) {
                    if (cardToHit.getFaceValue() > computerCards[c].getFaceValue() && computerCards[c].getSuit() != highSuit) {
                        cardToHit = computerCards[c];
                        index = c;
                    }
                    if (includeHighSuits && cardToHit.getFaceValue() > computerCards[c].getFaceValue()) {
                        cardToHit = computerCards[c];
                        index = c;
                    }
                }
            }
        } else {
            for (int c = 0; c < computerCards.length; c++) {
                if (computerCards[c].getSuit() != highSuit && isAllowedToHit(computerCards[c])) {
                    cardToHit = computerCards[c];
                    index = c;
                }
            }
        }

        if (cardToHit != null) {
            if (table.size() == 1) {
                computerHand.remove(index);
            }
            if (table.size() != 0 && isAllowedToHit(cardToHit)) {
                //never gets here because it doesnt search for "isAllowedToHit" cards in the first place
                computerHand.remove(index);
            }
        }
        return cardToHit;
    }

    protected void playerDefends(Vector v) {
        RenderedCard cardToHit = (RenderedCard) v.elementAt(v.size() - 1);
        System.out.println(cardToHit.toString());
        if (clickedCard.getSuit() == cardToHit.getSuit() && clickedCard.getFaceValue() > cardToHit.getFaceValue()) {
            table.add(clickedCard);
            playerHand.remove(clickedCard);
            playerHasDefended = true;
        }
        if (clickedCard.getSuit() == highSuit && cardToHit.getSuit() != highSuit) {
            table.add(clickedCard);
            playerHand.remove(clickedCard);
            playerHasDefended = true;
        }
    }

    protected void makeExchange(RenderedCard dk, int number) {
        table.add(dk);
        playerHand.remove(number - 1);
    }

    //if there already is a card with that face on the table: return true
    protected boolean isAllowedToHit(RenderedCard dk) {
        for (int c = 0; c < table.size(); c++) {
            RenderedCard temp = (RenderedCard) table.elementAt(c);
            if (dk.getFaceValue() == temp.getFaceValue()) {
                return true;
            }
        }
        return false;
    }

    protected boolean computerDefend(Vector v) {
        Card cardToHit = (RenderedCard) v.elementAt(v.size() - 1);
        Card[] computerCards = new Card[computerHand.size()];
        computerHand.copyInto(computerCards);
        Card lowest = null;
        int index = -1;
        boolean firstTime = true;

        //check if card is larger
        for (int c = 0; c < computerCards.length; c++) {
            if (computerCards[c].getSuit() == cardToHit.getSuit()) {
                if (computerCards[c].getFaceValue() > cardToHit.getFaceValue()) {
                    if (firstTime) {
                        lowest = computerCards[c];
                        firstTime = false;
                        index = c;
                    }
                    if (computerCards[c].getFaceValue() < lowest.getFaceValue()) {
                        lowest = computerCards[c];
                        index = c;
                    }
                }
            }
        }
        firstTime = true;
        //if still no cards found, try high suits, unless the card to his is a high suit itself
        if (lowest == null && cardToHit.getSuit() != highSuit) {
            for (int c = 0; c < computerCards.length; c++) {
                if (computerCards[c].getSuit() == highSuit) {
                    if (firstTime) {
                        lowest = computerCards[c];
                        firstTime = false;
                        index = c;
                    }
                    if (computerCards[c].getFaceValue() < lowest.getFaceValue()) {
                        lowest = computerCards[c];
                        index = c;
                    }
                }
            }
        }
        if (lowest != null) {
            table.add(lowest);
            computerHand.remove(index);
            return true;
        } else {
            //computer picks up cards
            computerHand.addAll(table);
            table.clear();
            refillCards(true);
            return true;
        }
    }

    protected void refillCards(boolean playerPicksUpFirst) {

        deck.list();
        if (playerPicksUpFirst) {
            while (playerHand.size() < 6 && deck.getNumCardsLeft() > 0) {
                playerHand.add(deck.deal());
            }

            while (computerHand.size() < 6 && deck.getNumCardsLeft() > 0) {
                computerHand.add(deck.deal());
            }
        } else {
            while (computerHand.size() < 6 && deck.getNumCardsLeft() > 0) {
                computerHand.add(deck.deal());
            }

            while (playerHand.size() < 6 && deck.getNumCardsLeft() > 0) {
                playerHand.add(deck.deal());
            }
        }
        System.out.println();
        System.out.println();
        System.out.println("TOP CARD: " + deck.getTopIndex());
        System.out.println("CARDS LEFT: " + deck.getNumCardsLeft());
        for (int c = 0; c < computerHand.size(); c++) {
            System.out.print(computerHand.elementAt(c) + " ");
        }
        System.out.println();
    }

    protected boolean computersCardsAreHighSuit() {
        Card[] computerCards = new Card[computerHand.size()];
        computerHand.copyInto(computerCards);
        if (deck.getNumCardsLeft() != 0) {
            for (int c = 0; c < computerCards.length; c++) {
                if (computerCards[c].getSuit() != highSuit) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setVisible(boolean visible) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
        super.setVisible(visible);
    }
}