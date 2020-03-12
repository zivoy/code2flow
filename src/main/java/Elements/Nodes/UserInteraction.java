package Elements.Nodes;

import java.awt.*;
import java.awt.geom.Point2D;

// any user interaction that has to happen input/output
public class UserInteraction extends Node {
    private final static Color innerColor = new Color(245, 245, 245);
    private final static Color borderColor = new Color(102, 102, 102);

    public UserInteraction(String text) {
        super(text);
        this.color = innerColor;
        this.border = borderColor;
    }

    public UserInteraction(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
    }

    @Override
    void updateConnectionPoints() {
        super.updateConnectionPoints();
        double offset = this.W() * 1 / 10;

        this.left = new Point2D.Double(this.X() + offset / 2, this.Y() + this.H() / 2);
        this.right = new Point2D.Double(this.X() + this.W() - offset / 2, this.Y() + this.H() / 2);
    }


    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        double offset = this.W() * 1 / 10;
        Polygon shape = new Polygon(new int[]{(int) Math.round(this.X() + offset), (int) Math.round(this.X() + this.W()),
                (int) Math.round(this.X() - offset + this.W()), (int) Math.round(this.X())},
                new int[]{this.getY(), this.getY(), this.getY() + this.getHeight(), this.getY() + this.getHeight()}, 4);
        g.fill(shape);
        this.drawBorder(g, shape);
        this.drawText(g, this.text, this.W() * 2.1 / 20, 0);
        g.setColor(oldColor);
    }
}
