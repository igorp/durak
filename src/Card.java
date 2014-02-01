import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Card extends Canvas implements MouseListener {

    public static final int JACK = 11, QUEEN = 12, KING = 13, ACE = 14, MAX = 14, MIN = 2;
    protected int faceValue;
    protected int actualValue;
    protected char suit;
    protected boolean visible;

    public Card(int fv, int av, char s, boolean v) {
        faceValue = (fv >= MIN && fv <= MAX) ? fv : 2;
        actualValue = av;
        if (s == 'C' || s == 'D' || s == 'H' || s == 'S') {
            suit = s;
        } else {
            suit = 'C';
        }
        visible = v;
    }

    public Card(int fv, char s, boolean v) {
        this(fv, fv, s, v);
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean getVisible() {
        return visible;
    }

    public String toString() {
        if (!visible) {
            return "??";
        }
        String face;
        if (faceValue >= 2 && faceValue <= 10) {
            face = String.valueOf(faceValue);
        } else {
            switch (faceValue) {
                case JACK:
                    face = "J";
                    break;
                case QUEEN:
                    face = "Q";
                    break;
                case KING:
                    face = "K";
                    break;
                case ACE:
                    face = "A";
                    break;
                default:
                    face = "2";
            }
        }
        face += suit;
        return face;
    }

    public void setValue(int av) {
        actualValue = av;
    }

    public int getValue() {
        return actualValue;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public char getSuit() {
        return suit;
    }

    public static char[] getSuits() {
        char[] suits = {'C', 'D', 'H', 'S'};
        return suits;
    }

    //returns true if the card is a picture card
    public boolean isPictureCard() {
        if (faceValue >= JACK && faceValue <= KING) {
            return true;
        }
        return false;
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked in card");
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
