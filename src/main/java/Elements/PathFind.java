package Elements;

import Elements.Nodes.Node;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class PathFind {
    ArrayList<Node> obstacles;
    Bounds maxBorder;

    public PathFind(ArrayList<Node> obstacles) {
        this.obstacles = obstacles;
        this.maxBorder = new BoundingBox(0, 0, 0, 0);
    }

    public static int[] getX(ArrayList<Point2D> points) {
        int[] path = new int[points.size()];
        for (int i = 0; i < points.size(); i++)
            path[i] = (int) Math.round(points.get(i).getX());
        return path;
    }

    public static int[] getY(ArrayList<Point2D> points) {
        int[] path = new int[points.size()];
        for (int i = 0; i < points.size(); i++)
            path[i] = (int) Math.round(points.get(i).getY());
        return path;
    }

    public void addObstacles(Node node) {
        this.obstacles.add(node);
    }

    public void setMaxBorder(Bounds bounds) {
        this.maxBorder = bounds;
    }

    public void setObstacles(ArrayList<Node> obstacles) {
        this.obstacles = obstacles;
    }

    public ArrayList<Point2D> transverse(Point2D start, Point2D end) {
        HashSet<Point2D> visited = new HashSet<>();
        PriorityQueue<Point> pq = new PriorityQueue<>(new PointComparator());
        Point s = new Point(start, 0);
        pq.add(s);
        Point curr = s;
        while (!pq.isEmpty() && !curr.equalsPoint(end)) {
            curr = pq.poll();
            if (curr == null) {
                curr = new Point(end, 0, new PointHistory(start));
                break;
            }

            visited.add(curr.cords);

            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++) {
                    if (!(x == 0 || y == 0))
                        continue;
                    double xPos = curr.cords.getX() + x;
                    double yPos = curr.cords.getY() + y;
                    Point2D point = new Point2D.Double(xPos, yPos);

                    if (visited.contains(point))
                        continue;

                    if (!this.maxBorder.contains(xPos, yPos))
                        continue;

                    boolean passed = true;
                    for (Node i : this.obstacles) {
                        passed = false;
                        if (i.getTraceBounds().contains(xPos, yPos))
                            break;
                        passed = true;
                    }
                    if (!passed) {
                        continue;
                    }

                    pq.add(new Point(point, calculateCost(point, curr.history, end), curr.history));
                }
            if (curr.equalsPoint(end))
                break;
        }
        return curr.history.getHistory();
    }

    private double calculateCost(Point2D point, PointHistory history, Point2D goal) {
        return calculateCost(new Point(point, 0, history), goal);
    }

    private double calculateCost(Point point, Point2D goal) {
//        double cost = point.cords.distance(goal);//distanceSq
        double ady = Math.abs(point.cords.getY() - goal.getY());
        double adx = Math.abs(point.cords.getX() - goal.getX());
        double cost = ady + adx;//Math.max(adx, ady);
        double firstDeduction = .7;
        double latterDeduction = .9;
//        int maxDepth = 25;
        //double length =point.history.length/300.;
        //double lengthPenalty = length*length+1;
        // Math.pow(Math.E, point.history.length / 100.) / 0.5;//10 * Math.log(point.history.length + 1) + 1;

        double xFactor = Math.sin(Math.atan2(ady, adx));
        double yFactor = Math.cos(Math.atan2(ady, adx));
        double turnFactor = xFactor * yFactor;
        turnFactor = turnFactor / 8;

        PointHistory hist = point.history;
        Point2D last = point.cords;
        Point2D curr = last;
//        int depth = 0;
//        int flatLength = 0;
        double dirX = 10;
        double dirY = 10;
        boolean firstFlat = true;
        while (hist != null) {
//            if (firstFlat && depth >= maxDepth)
//                firstFlat = false;
//            cost = cost * lengthPenalty;
            if (curr != last) {
                if (dirX == 10 || dirY == 10) {
                    dirX = Math.signum(curr.getX() - last.getX());
                    dirY = Math.signum(curr.getY() - last.getY());
                    continue;
                }
                double currDirX = Math.signum(point.cords.getX() - last.getX());
                double currDirY = Math.signum(point.cords.getY() - last.getY());
                if (currDirX == dirX && currDirY == dirY) {
                    if (((point.cords.getX() - last.getX()) == 0 ||
                            (point.cords.getY() - last.getY()) == 0)) {
//                        flatLength++;
                        if (firstFlat)
                            cost = cost * firstDeduction;
                        else cost = cost * latterDeduction;
                    } else {
                        firstFlat = false;
//                        flatLength = 0;
                    }
                } else {
                    firstFlat = false;
                    curr = last;
//                    flatLength = 0;
                    dirY = dirX = 10;
                    cost = cost * 5;
                }
            }
            //cost = cost * 100./(flatLength+100);

            last = hist.point;
            hist = hist.history;
//            depth++;
            if (cost < -200)
                return Double.MAX_VALUE;
        }

        cost = cost + 1 * cost * turnFactor;
        //cost = cost * lengthPenalty;
//        System.out.println(turns);
        return cost;
    }
}

class Point {
    Point2D cords;
    double cost;
    PointHistory history;

    Point(Point2D cords, double cost, PointHistory history) {
        this.cords = cords;
        this.cost = cost;
        this.history = new PointHistory(history, cords);
    }

    Point(Point2D cords, double cost) {
        this.cords = cords;
        this.cost = cost;
        this.history = new PointHistory(cords);
    }

    public Boolean equalsPoint(Point2D other) {
        if (this.cords == other)
            return true;
        double dist = .05;//1.3;
        return Math.abs(this.cords.getX() - other.getX()) <= dist &&
                Math.abs(this.cords.getY() - other.getY()) <= dist;
    }

    public Boolean equalsPoint(Point other) {
        return equalsPoint(other.cords);
    }
}

class PointHistory {
    PointHistory history;
    Point2D point;
    long length;

    PointHistory(Point2D point) {
        this.history = null;
        this.point = point;
        this.length = 0;
    }

    PointHistory(PointHistory history, Point2D point) {
        this.history = history;
        this.point = point;
        this.length = history.length + 1;
    }

    public ArrayList<Point2D> getHistory() {
        ArrayList<Point2D> path = new ArrayList<>();
        PointHistory curr = this;
        Point2D last = this.point;
        path.add(last);
        Point2D lst = last;
        while (true) {
            if (curr.point != last) {
                if (lst != last)
                    if (!((curr.point.getX() - last.getX()) == 0 ||
                            (curr.point.getY() - last.getY()) == 0)) {
                        path.add(lst);
                        last = lst;
                    }
            }
            if (curr.history == null)
                break;
            lst = curr.point;
            curr = curr.history;
        }
        if (path.get(path.size() - 1) != curr.point)
            path.add(curr.point);
        return path;
    }
}


class PointComparator implements Comparator<Point> {
    @Override
    public int compare(Point p1, Point p2) {
        if (p1.cost < p2.cost)
            return -1;
        else if (p1.cost > p2.cost)
            return 1;
        return 0;
    }
}
