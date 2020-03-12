package Elements.Nodes;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// node for storing information in file
public class DataStore extends Node {
    private final static Color innerColor = new Color(248, 206, 204);
    private final static Color borderColor = new Color(184, 84, 80);

    public DataStore(String text) {
        super(text);
        double width = this.W();
        double height = this.H();
        this.setSize((int) Math.round(height + Math.round(width * 0.2)), (int) Math.round(width * 0.8));
        this.updateConnectionPoints();
        this.color = innerColor;
        this.border = borderColor;
    }

    public DataStore(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        RoundRectangle2D shape = new RoundRectangle2D.Double(this.X(), this.Y(), this.W(), this.H(), this.W(), this.H() / 3);
        g.fill(shape);
        this.drawBorder(g, shape);
        g.setColor(this.border);
        g.drawArc((int) Math.round(this.X()),
                (int) Math.round(this.Y()),
                (int) Math.round(this.W()),
                (int) Math.round(this.H() / 3),
                180,
                360);

        this.drawText(g, this.text, 4, this.H() / 8);
        g.setColor(oldColor);
    }
}
