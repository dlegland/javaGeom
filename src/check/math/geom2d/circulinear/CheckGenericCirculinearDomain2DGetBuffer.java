/**
 * 
 */
package math.geom2d.circulinear;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.conic.Circle2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.point.Point2D;

/**
 * @author dlegland
 *
 */
public class CheckGenericCirculinearDomain2DGetBuffer extends JPanel {
    private static final long serialVersionUID = 1L;

    ICirculinearDomain2D domain;
    ICurve2D parallel;

    public CheckGenericCirculinearDomain2DGetBuffer() {
        // constants
        double x0 = 200;
        double y0 = 200;
        double R = 100;

        // boundary circle
        Point2D center = new Point2D(x0, y0);
        Circle2D circle = new Circle2D(center, R);

        // generic domain
        domain = GenericCirculinearDomain2D.create(circle);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        IDomain2D buffer = domain.buffer(30);
        g2.setColor(Color.CYAN);
        buffer.fill(g2);
        g2.setColor(Color.BLUE);
        buffer.boundary().draw(g2);

        g2.setColor(Color.BLACK);
        domain.boundary().draw(g2);

    }

    public final static void main(String[] args) {
        System.out.println("draw domain buffer");

        JPanel panel = new CheckGenericCirculinearDomain2DGetBuffer();
        panel.setPreferredSize(new Dimension(500, 400));
        JFrame frame = new JFrame("Buffer of a generic domain");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
