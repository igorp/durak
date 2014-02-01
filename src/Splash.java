//Class for loading and displaying images

import java.awt.*;
import java.awt.event.*;

public class Splash extends Canvas implements ActionListener {

    protected Image image;
    private String imageName;
    protected Dimension dim;

    public Splash(String name, int x, int y) {
        setSize(x, y);
        imageName = name;
    }
    
    public void paint(Graphics g) {

        image = Toolkit.getDefaultToolkit().getImage(imageName);
        g.drawImage(image, 0, 0, this);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void actionPerformed(ActionEvent e) {
        
    }
}