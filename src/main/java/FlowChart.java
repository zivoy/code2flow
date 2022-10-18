import Elements.Arrow;
import Elements.NodeTree;

import java.awt.*;
import java.util.ArrayList;

public class FlowChart {
    ArrayList<NodeTree> trees;

    public FlowChart() {
        trees = new ArrayList<>();
    }

    public void add(NodeTree e) {
        this.trees.add(e);
    }

    public void update() {
        // sort nodes
        for (NodeTree i : this.trees.subList(1,this.trees.size())) {
            i.getBounds();
        }

        // trace arrows
        long start = System.currentTimeMillis();
        ArrayList<Arrow> arrows = new ArrayList<>();
        for (NodeTree i : this.trees) {
            arrows.addAll(i.getArrows());
            i.setArrowsValid(true);
        }

        Thread[] items = new Thread[arrows.size()];
        for (int i = 0; i < arrows.size(); i++) {
            items[i] = new Thread(arrows.get(i)::updatePath, "Arrow tracer-" + i);
            items[i].start();
        }
        for (Thread i : items) {
            try {
                i.join();
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }

        System.out.println("Total trace time took " + ((System.currentTimeMillis() - start) / 1000.) + " seconds");
    }

    public void draw(Graphics2D g) {
        for (NodeTree i : this.trees) {
            i.draw(g);
        }
    }
}
