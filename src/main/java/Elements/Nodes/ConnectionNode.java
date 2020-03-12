package Elements.Nodes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

// node used to connect multiple lines
public class ConnectionNode extends Node {
    final static Color innerColor = Color.BLACK;

    public ConnectionNode() {
        super("");
        this.color = innerColor;
        int size = (int) (this.H() / 6);
        this.setSize(size, size);
    }

    public ConnectionNode(int x, int y) {
        this();
        this.setCords(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        Ellipse2D circle = new Ellipse2D.Double(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.fill(circle);
        g.setColor(oldColor);
    }
}
