/**
 * 
 */
package math.geom2d.spline;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.line.StraightLine2D;

/**
 * Draw and fill the interior of an astroid defined from several quad curves.
 * 
 * @author dlegland
 *
 */
public class CheckGeneralPath2D_Intersections extends JPanel {

    private static final long serialVersionUID = 1L;

    double x0 = 200;
    double y0 = 200;
    double r = 150;

    ICurve2D curve;
    StraightLine2D line;

    Box2D box = new Box2D(10, 410, 10, 410);

    public CheckGeneralPath2D_Intersections() {

        Point2D p0 = new Point2D(200, 200);
        Point2D p1 = new Point2D(200, 300);
        Point2D p2 = new Point2D(300, 400);
        Point2D p3 = new Point2D(400, 200);
        Point2D p4 = new Point2D(300, 200);
        Point2D p5 = new Point2D(200, 50);
        Point2D p6 = new Point2D(100, 200);

        GeneralPath2D path = new GeneralPath2D();
        path.moveTo(p0);
        path.cubicTo(p1, p2, p3);
        path.lineTo(p4);
        path.quadTo(p5, p6);
        path.closePath();
        this.curve = path;

        this.line = new StraightLine2D(180, 220, 3, 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Draw the curve
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        this.curve.draw(g2);

        g2.setColor(Color.BLUE);
        this.line.clip(box).draw(g2);

        g2.setColor(Color.RED);
        for (Point2D pt : curve.intersections(line)) {
            pt.draw(g2, 4.5);
        }
    }

    public final static void main(String[] args) {
        JPanel panel = new CheckGeneralPath2D_Intersections();
        panel.setPreferredSize(new Dimension(600, 500));
        JFrame frame = new JFrame("Draw Intersections of General Path");
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
