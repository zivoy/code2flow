package Elements;

import Elements.Nodes.Connection;
import Elements.Nodes.Node;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NodeTree {
    ArrayList<Node> items;
    Bounds bounds;
    Map<Connection, ArrayList<Connection>> Children;
    ArrayList<Arrow> arrows;
    PathFind pathfinder;

    boolean arrowsValid;

    boolean debug = true; // todo

    public NodeTree() {
        items = new ArrayList<>();
        this.bounds = getBounds();
        this.pathfinder = new PathFind(items);
        this.Children = new HashMap<>();
        this.arrows = new ArrayList<>();
        this.arrowsValid = false;
    }

    public Node get(int index) {
        return this.items.get(index);
    }

    public void add(Node e) {
        this.items.add(e);
        this.bounds = this.getBounds();
        this.pathfinder.addObstacles(e);
        this.arrowsValid = false;
    }

    public Bounds getBounds() {
        if (this.items.size() == 0) {
            return new BoundingBox(0, 0, 0, 0);
        }

        double smallestX = this.items.get(0).getBounds().getMinX();
        double smallestY = this.items.get(0).getBounds().getMinY();
        double biggestX = this.items.get(0).getBounds().getMaxX();
        double biggestY = this.items.get(0).getBounds().getMaxY();

        for (Node i : this.items) {
            smallestX = Math.min(smallestX, i.getBounds().getMinX());
            smallestY = Math.min(smallestY, i.getBounds().getMinY());

            biggestX = Math.max(biggestX, i.getBounds().getMaxX());
            biggestY = Math.max(biggestY, i.getBounds().getMaxY());
        }
        double width = biggestX - smallestX;
        double height = biggestY - smallestY;

        int increase = 15;

        this.pathfinder.setMaxBorder(new BoundingBox(smallestX - increase, smallestY - increase,
                width + 2 * increase, height + 2 * increase));

        return new BoundingBox(smallestX, smallestY, width, height);
    }

    public void transpose(int x, int y) {
        for (Node i : this.items) {
            Point2D cord = i.getCords();
            i.setCords(cord.getX() + x, cord.getY() + y);
        }
        this.bounds = this.getBounds();
        this.arrowsValid = false;
    }


    public void draw(Graphics2D g) {
        if (!this.arrowsValid)
            this.traceArrows();

        for (Arrow i : this.arrows)
            i.draw(g);
        for (Node i : this.items) {
            i.draw(g);

            if (debug) {
                //Bounds a = i.getBounds();
                Bounds b = i.getTraceBounds();
                //g.drawRect((int) Math.round(a.getMinX()),  (int) Math.round(a.getMinY()),
                //           (int) Math.round(a.getWidth()), (int) Math.round(a.getHeight()));
                g.drawRect((int) Math.round(b.getMinX()), (int) Math.round(b.getMinY()),
                        (int) Math.round(b.getWidth()), (int) Math.round(b.getHeight()));
                g.drawOval((int) i.getBottom().getX() - 5, (int) i.getBottom().getY() - 5, 10, 10);
                g.drawOval((int) i.getTop().getX() - 5, (int) i.getTop().getY() - 5, 10, 10);
                g.drawOval((int) i.getLeft().getX() - 5, (int) i.getLeft().getY() - 5, 10, 10);
                g.drawOval((int) i.getRight().getX() - 5, (int) i.getRight().getY() - 5, 10, 10);
            }
        }

        if (debug) {
            g.drawRect((int) Math.round(pathfinder.maxBorder.getMinX()), (int) Math.round(pathfinder.maxBorder.getMinY()),
                    (int) Math.round(pathfinder.maxBorder.getWidth()), (int) Math.round(pathfinder.maxBorder.getHeight()));
//            g.drawRect((int) Math.round(this.bounds.getMinX()), (int) Math.round(this.bounds.getMinY()),
//                    (int) Math.round(this.bounds.getWidth()), (int) Math.round(this.bounds.getHeight()));
        }
    }

    public void addChild(Node parent, Side side, Node child, Side childSide) {
        Connection parentConnection = new Connection(parent, side);
        Connection childConnection = new Connection(child, childSide);

        if (!this.Children.containsKey(parentConnection))
            this.Children.put(parentConnection, new ArrayList<>());
        Children.get(parentConnection).add(childConnection);
        parent.addChild(child);
        this.updateArrows();
    }

    private void updateArrows() {
        this.arrows.clear();
        for (Connection i : this.Children.keySet()) {
            for (Connection j : this.Children.get(i)) {
                this.arrows.add(new Arrow(i, j, this.pathfinder));
            }
        }
        this.arrowsValid = false;
    }

    public void traceArrows() {
        Thread[] items = new Thread[this.arrows.size()];
        for (int i = 0; i < this.arrows.size(); i++) {
            items[i] = new Thread(this.arrows.get(i)::updatePath);
            items[i].start();
        }
        for (Thread i : items) {
            try {
                i.join();
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }
        this.arrowsValid = true;
    }

    public void setArrowsValid(boolean arrowsValid) {
        this.arrowsValid = arrowsValid;
    }

    public Collection<Arrow> getArrows() {
        return this.arrows;
    }
}
