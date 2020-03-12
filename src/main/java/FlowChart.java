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
        long start = System.currentTimeMillis();
//        Thread[] items = new Thread[this.trees.size()];
//        for (int i = 0; i < this.trees.size(); i++) {
//            items[i] = new Thread(this.trees.get(i)::traceArrows);
//            items[i].start();
//        }
//
//        for (Thread i : items) {
//            try {
//                i.join();
//            } catch (InterruptedException e) {
//                System.exit(-1);
//            }
//        }
        ArrayList<Arrow> arrows = new ArrayList<>();
        for (NodeTree i:this.trees){
            arrows.addAll(i.getArrows());
            i.setArrowsValid(true);
        }

        Thread[] items = new Thread[arrows.size()];
        for (int i = 0; i < arrows.size(); i++) {
            items[i] = new Thread(arrows.get(i)::updatePath);
            items[i].start();
        }
        for (Thread i : items) {
            try {
                i.join();
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }

        System.out.println("Total trace time took " + ((System.currentTimeMillis() - start) / 1000.));
    }

    public void draw(Graphics2D g) {
        for (NodeTree i : this.trees) {
            i.draw(g);
        }
    }
}
