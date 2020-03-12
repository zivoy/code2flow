package Elements.Nodes;

import java.awt.*;

// any choices that have to happen
public class Decision extends Node {
    private final static Color innerColor = new Color(255, 230, 204);
    private final static Color borderColor = new Color(215, 155, 0);

    public Decision(String text) {
        super(text);
        this.color = innerColor;
        this.border = borderColor;
    }

    public Decision(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
    }


    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        Polygon shape = new Polygon(new int[]{(int) Math.round(this.X()), (int) Math.round(this.X() + this.W() / 2),
                (int) Math.round(this.X() + this.W()), (int) Math.round(this.X() + this.W() / 2)},
                new int[]{(int) Math.round(this.Y() + this.H() / 2), this.getY(), (int) Math.round(this.Y() + this.H() / 2),
                        this.getY() + this.getHeight()}, 4);
        g.fill(shape);
        this.drawBorder(g, shape);
        this.drawText(g, this.text);
        g.setColor(oldColor);
    }
}
