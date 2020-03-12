package Elements.Nodes;

import Elements.Drawable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

// pos of object is going to be the center
public abstract class Node implements Drawable {
    String text;
    ArrayList<Node> Children;

    Point2D cords = new Point2D.Double(0, 0);
    Dimension size = new Dimension(100, 50);
    Color color;
    Color border = Color.BLACK;
    Color textColor = Color.BLACK;
    int borderSize = 1;

    Point2D left;
    Point2D top;
    Point2D right;
    Point2D bottom;

    public Node(String text) {
        this.text = text;
        updateConnectionPoints();

        this.Children = new ArrayList<>();
    }

    public Node(String text, int red, int green, int blue) {
        this(text);
        this.color = new Color(red, green, blue);
    }

    public Point2D getRight() {
        return right;
    }

    public Point2D getLeft() {
        return left;
    }

    public Point2D getTop() {
        return top;
    }

    public Point2D getBottom() {
        return bottom;
    }

    void updateConnectionPoints() {
        this.top = new Point2D.Double(this.X() + this.W() / 2, this.Y());
        this.bottom = new Point2D.Double(this.X() + this.W() / 2, this.Y() + this.H());
        this.left = new Point2D.Double(this.X(), this.Y() + this.H() / 2);
        this.right = new Point2D.Double(this.X() + this.W(), this.Y() + this.H() / 2);
    }

    public void addChild(Node child) {
        Children.add(child);
    }

    public void setCords(double x, double y) {
        setCords(new Point2D.Double(x, y));
        this.updateConnectionPoints();
    }

    public Point2D getCords() {
        return this.cords;
    }

    public void setCords(Point2D cords) {
        this.cords = cords;
        this.updateConnectionPoints();
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Color getBorderColor() {
        return this.border;
    }

    public double X() {
        return this.cords.getX() - this.getWidth() / 2.;
    }

    public double Y() {
        return this.cords.getY() - this.getHeight() / 2.;
    }

    public double W() {
        return this.size.getWidth();
    }

    public double H() {
        return this.size.getHeight();
    }

    public int getX() {
        return (int) Math.round(this.X());
    }

    public int getY() {
        return (int) Math.round(this.Y());
    }

    public int getWidth() {
        return (int) Math.round(this.W());
    }

    public void setWidth(int width) {
        setSize(width, this.getHeight());
        this.updateConnectionPoints();
    }

    public int getHeight() {
        return (int) Math.round(this.H());
    }

    public void setHeight(int height) {
        setSize(this.getWidth(), height);
        this.updateConnectionPoints();
    }

    public void setSize(int width, int height) {
        if (width % 2 != 0)
            width += 1;
        if (height % 2 != 0)
            height += 1;
        this.size = new Dimension(width, height);
        this.updateConnectionPoints();
    }


    public Bounds getBounds() {
        return new BoundingBox(this.X(), this.Y(), this.W(), this.H());
    }

    public Bounds getTraceBounds() {
        int offset = 7;
        return new BoundingBox(this.X() - offset, this.Y() - offset, this.W() + 2*offset, this.H() + 2*offset);
    }


    void drawBorder(Graphics2D g, Shape shape) {
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();
        g.setStroke(new BasicStroke(this.borderSize));
        g.setColor(this.border);
        g.draw(shape);
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    void drawText(Graphics2D g, String text) {
        drawText(g, text, 4, 0);
    }

    void drawText(Graphics2D g, String text, double padding, double down) {
        Color oldColor = g.getColor();
        g.setColor(this.textColor);
        StringTools metrics = new StringTools(g);
        final double fontHeight = metrics.getHeight(text) / 1.25;
        ArrayList<String> parts = metrics.split(text, this.getWidth() - padding * 2);

        double x = this.cords.getX();
        double totalLength = fontHeight * (parts.size() - 1);
        double y = this.cords.getY() - totalLength / 2 + down;

        for (int i = 0; i < parts.size(); i++) {
            String string = parts.get(i);
            int xPart = (int) Math.round(x - metrics.getWidth(string) / 2.);
            int yPart = (int) Math.round(y + (.5 + i) * fontHeight - fontHeight * .1);
            g.drawString(string, xPart, yPart);
        }
        g.setColor(oldColor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return borderSize == node.borderSize &&
                Objects.equals(text, node.text) &&
                Objects.equals(cords, node.cords) &&
                Objects.equals(size, node.size) &&
                Objects.equals(color, node.color) &&
                Objects.equals(border, node.border);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, cords, size, color, border, borderSize);
    }
}

class StringTools {

    Font font;
    FontRenderContext context;

    public StringTools(Graphics2D g2) {
        font = g2.getFont();
        context = g2.getFontRenderContext();
    }

    Rectangle2D getBounds(String message) {
        return font.getStringBounds(message, context);
    }

    public double getWidth(String message) {

        Rectangle2D bounds = getBounds(message);
        return bounds.getWidth();
    }

    public double getHeight(String message) {
        Rectangle2D bounds = getBounds(message);
        return bounds.getHeight();
    }

    public ArrayList<String> split(String string, double width) {
        ArrayList<String> prts = new ArrayList<>();
        int offset = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '\n') {
                prts.add(string.substring(offset, i));
                offset = i + 1;
            }
        }
        prts.add(string.substring(offset));
        ArrayList<String> parts = new ArrayList<>();
        for (String i : prts) {
            parts.addAll(splitString(i, width));
        }
        return parts;
    }

    ArrayList<String> splitString(String string, double width) {
        int offset = 0;
        String prt;
        ArrayList<String> parts = new ArrayList<>();
        for (int end = 0; end < string.length(); end++) {
            prt = string.substring(offset, end);

            if (this.getWidth(prt) >= width) {
                for (; end - offset > 0; end--)
                    if (string.charAt(end) == ' ') {
                        break;
                    }
                if (end - offset <= 0) {
                    for (end = end + 1; end < string.length(); end++)
                        if (string.charAt(end) == ' ') {
                            break;
                        }
                }
                prt = string.substring(offset, end);
                parts.add(prt);
                offset = end;
            }
        }
        parts.add(string.substring(offset));
        if (parts.size() > 1 && parts.get(parts.size() - 1).equals(""))
            parts.remove(parts.size() - 1);
        return parts;
    }
}