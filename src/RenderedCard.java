import java.awt.*;

public class RenderedCard extends Card {

    protected boolean isHighSuit;
    protected Image image;
   
    protected Image offscreen;
    protected Dimension dim;

    public RenderedCard(int fv, char s, boolean v) {
        super(fv, s, v);
        isHighSuit = false;
        setSize(73, 97);        
    }

    public RenderedCard(int fv, char s, boolean v, boolean hs) {
        super(fv, s, v);
        isHighSuit = hs;
    }

    public void setHighSuit(boolean hs) {
        isHighSuit = hs;
    }

    public boolean getHighSuit() {
        return isHighSuit;
    }

    public void paint(Graphics g) {
        if (!visible) {
            image = Toolkit.getDefaultToolkit().getImage("picture_data/b3.gif");
            g.drawImage(image, 0, 0, this);
        } else {

            String s = "picture_data/" + this.toString() + ".gif";
            image = Toolkit.getDefaultToolkit().getImage(s);
            g.drawImage(image, 0, 0, this);
        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
