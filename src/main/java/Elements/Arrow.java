package Elements;

import Elements.Nodes.Connection;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Arrow implements Drawable {
    Color color = Color.BLACK;
    Color borderColor = Color.BLACK;
    int borderSize = 2;
    PathFind pathfinder;
    ArrayList<Point2D> path;

    Connection startNode;
    Connection endNode;

    public Arrow(Connection start, Connection end, PathFind pathfinder) {
        this.startNode = start;
        this.endNode = end;
        this.pathfinder = pathfinder;
//        this.updatePath();
    }

    public void updatePath() {
        long start = System.currentTimeMillis();
        this.path = pathfinder.transverse(this.start(), this.end());
        System.out.println("Tracing took: " + ((System.currentTimeMillis() - start)) / 1000.);
    }

    private Point2D start() {
        return this.startNode.getSide(true);
    }

    private Point2D end() {
        return this.endNode.getSide(true);
    }

    @Override
    public void draw(Graphics2D g) {
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();
        g.setStroke(new BasicStroke(this.borderSize));
        g.setColor(this.color);

        Point2D startClose = this.startNode.getSide();
        Point2D startLine = this.start();
        drawArrowLine(g, startClose, startLine);

        Point2D endLine = this.endNode.getSide();
        Point2D endClose = this.end();
        drawArrowLine(g, endClose, endLine);

        g.drawPolyline(PathFind.getX(this.path), PathFind.getY(this.path), this.path.size());
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    private void drawArrowLine(Graphics2D g, Point2D p1, Point2D p2) {
        int d = 5;
        int h = 4;
        int x1 = (int) Math.round(p1.getX());
        int y1 = (int) Math.round(p1.getY());
        int x2 = (int) Math.round(p2.getX());
        int y2 = (int) Math.round(p2.getY());

        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Color getBorderColor() {
        return this.borderColor;
    }
}