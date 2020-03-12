import Elements.NodeTree;
import Elements.Nodes.Process;
import Elements.Nodes.*;
import Elements.Side;
import javafx.geometry.Bounds;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 370, 500);
        window.getContentPane().add(new MyCanvas(window));
        window.setVisible(true);
    }
}

class MyCanvas extends JComponent {
    FlowChart chart = new FlowChart();
    JFrame window;
    boolean load;
    Thread loadArrows;

    MyCanvas(JFrame window) {
        this.window = window;
        this.load = true;

        NodeTree tree = new NodeTree();
        chart.add(tree);

        Node a = new Process("goodbye world! covid-19 is coming", 100, 70);
        a.setHeight(55);
        tree.add(a);

        tree.add(new ConnectionNode(250, 140));
        tree.add(new PredefinedProcess("the task to be done", 70, 200));
        tree.add(new UserInteraction("something to do with user", 250, 200));
        tree.add(new Decision("there is a choice to be made", 100, 350));
        tree.add(new TerminalNode("Not a start or an end", 270, 300));
        PredefinedProcess b = new PredefinedProcess("Class here", 100, 280);
        b.setClassColors();
        tree.add(b);

        tree.add(new DataStore("some information store", 250, 70));

        tree.transpose(23, 100);
        tree.transpose(0, -50);
        tree.transpose(-20, -40);
        tree.addChild(a, Side.BOTTOM, b, Side.TOP);
        tree.addChild(a, Side.LEFT, b, Side.BOTTOM);
//        tree.addChild(a,Side.BOTTOM,b, Side.LEFT);
        tree.addChild(a, Side.BOTTOM, b, Side.RIGHT);
        tree.addChild(a, Side.RIGHT, tree.get(6), Side.LEFT);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Font openSans = new Font("Open Sans", Font.PLAIN, 12);
        g.setFont(openSans);
        FontRenderContext context= g2d.getFontRenderContext();

        String message = "LOADING...";
        Rectangle2D messageSize = openSans.getStringBounds(message, context);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension d = this.window.getSize();

        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, d.width, d.height);

        if (this.load) {
            g2d.drawString(message, (float) (d.width / 2. - messageSize.getWidth() / 2.), (float) (d.height / 2. - messageSize.getHeight() / 2.));
            this.loadArrows = new Thread(chart::update);
            this.loadArrows.start();
            this.load =false;
        } else {
            if (this.loadArrows != null){
                try {
                    this.loadArrows.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.loadArrows = null;
            }
            chart.draw(g2d);
        }
    }

}