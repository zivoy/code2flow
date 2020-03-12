package Elements.Nodes;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TerminalNode extends Node {
    private final static Color innerSystemColor = new Color(225, 213, 231);
    private final static Color borderSystemColor = new Color(150, 115, 166);

    private final static Color innerFunctionColor = new Color(213, 232, 212);
    private final static Color borderFunctionColor = new Color(130, 179, 102);

    private final static Color innerClassColor = new Color(255, 242, 204);
    private final static Color borderClassColor = new Color(214, 182, 86);

    public TerminalNode(String text) {
        super(text);
        this.setSystemColors();
    }

    public TerminalNode(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
    }

    public void setSystemColors() {
        this.color = innerSystemColor;
        this.border = borderSystemColor;
    }

    public void setFunctionColors() {
        this.color = innerFunctionColor;
        this.border = borderFunctionColor;
    }

    public void setClassColors() {
        this.color = innerClassColor;
        this.border = borderClassColor;
    }

    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(this.color);
        RoundRectangle2D shape = new RoundRectangle2D.Double(this.X(), this.Y(), this.W(), this.H(), this.W() / 2., this.H());
        g.fill(shape);
        this.drawBorder(g, shape);
        this.drawText(g, this.text);
        g.setColor(oldColor);
    }
}
