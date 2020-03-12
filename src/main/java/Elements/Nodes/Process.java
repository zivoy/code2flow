package Elements.Nodes;

import java.awt.*;

// process that are run by the program
public class Process extends Node {
    private final static Color innerColor = new Color(218, 232, 252);
    private final static Color borderColor = new Color(108, 142, 191);

    public Process(String text) {
        super(text);
        this.color = innerColor;
        this.border = borderColor;
    }

    public Process(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        Rectangle shape = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.fill(shape);
        this.drawBorder(g, shape);
        this.drawText(g, this.text);
        g.setColor(oldColor);
    }
}
