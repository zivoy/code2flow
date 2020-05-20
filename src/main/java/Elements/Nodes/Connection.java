package Elements.Nodes;

import Elements.Side;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Connection {
    Side side;
    Node node;

    public Connection(Node node, Side side) {
        this.side = side;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Point2D getSide() {
        return this.getSide(false);
    }

    public Point2D getSide(boolean offset) {
        Point2D point;
        switch (this.side) {
            case LEFT -> {
                point = this.getLeft();
                if (offset)
                    point = new Point2D.Double(this.node.getTraceBounds().getMinX() - 1, point.getY());
            }
            case TOP -> {
                point = this.getTop();
                if (offset)
                    point = new Point2D.Double(point.getX(), this.node.getTraceBounds().getMinY() - 1);
            }
            case RIGHT -> {
                point = this.getRight();
                if (offset)
                    point = new Point2D.Double(this.node.getTraceBounds().getMaxX() + 1, point.getY());
            }
            default -> {
                point = this.getBottom();
                if (offset)
                    point = new Point2D.Double(point.getX(), this.node.getTraceBounds().getMaxY() + 1);
            }
        }
        return point;
    }

    public Point2D getLeft() {
        return this.node.getLeft();
    }

    public Point2D getRight() {
        return this.node.getRight();
    }

    public Point2D getTop() {
        return this.node.getTop();
    }

    public Point2D getBottom() {
        return this.node.getBottom();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return side == that.side &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(side, node);
    }
}
