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

import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.point.Point2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Compute buffer of a simple polyline, but whose parallel intersects...
 * 
 * @author dlegland
 *
 */
public class CheckGetBufferPolyline3 extends JPanel {
    private static final long serialVersionUID = 1L;

    ICirculinearCurve2D curve;
    ICirculinearDomain2D domain;

    public CheckGetBufferPolyline3() {

        // create the curve
        curve = new Polyline2D(new Point2D[] { new Point2D(200, 300), new Point2D(100, 300), new Point2D(100, 100), new Point2D(400, 100), new Point2D(400, 300), new Point2D(300, 300) });

        // compute the buffer
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        domain = bc.computeBuffer(curve, 80);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // fill the buffer
        g2.setColor(Color.CYAN);
        domain.fill(g2);

        // draw the buffer boundary
        g2.setColor(Color.BLUE);
        domain.draw(g2);

        // draw the original curve over all
        g2.setColor(Color.BLACK);
        curve.draw(g2);
    }

    public final static void main(String[] args) {
        JPanel panel = new CheckGetBufferPolyline3();
        panel.setPreferredSize(new Dimension(500, 400));
        JFrame frame = new JFrame("Compute buffer of a tricky polyline");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
