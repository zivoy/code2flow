package Elements.Nodes;

import java.awt.*;

// functions that are run by the program
public class PredefinedProcess extends Node {
    private final static Color innerFunctionColor = new Color(213, 232, 212);
    private final static Color borderFunctionColor = new Color(130, 179, 102);

    private final static Color innerClassColor = new Color(255, 242, 204);
    private final static Color borderClassColor = new Color(214, 182, 86);

    public PredefinedProcess(String text) {
        super(text);
        this.setFunctionColors();
    }

    public PredefinedProcess(String text, int x, int y) {
        this(text);
        this.setCords(x, y);
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
        g.setColor(color);
        Rectangle shape = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.fill(shape);
        this.drawBorder(g, shape);
        g.setColor(this.border);

        int top = this.getY();
        int bottom = this.getY() + this.getHeight();
        int line1 = (int) Math.round(this.getX() + this.W() * 1.5 / 20);
        int line2 = (int) Math.round(this.getX() + this.W() * 18.5 / 20);
        g.drawLine(line1, top, line1, bottom);
        g.drawLine(line2, top, line2, bottom);

        this.drawText(g, this.text, this.W() * 1 / 10, 0);
        g.setColor(oldColor);
    }
}
