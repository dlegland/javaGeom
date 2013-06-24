/**
 * 
 */
package math.geom2d.spline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.spline.QuadBezierCurve2D;

/**
 * Draw and fill the interior of an astroid defined from several quad curves.
 * @author dlegland
 *
 */
public class DrawAstroid2Demo extends JPanel {

	private static final long serialVersionUID = 1L;

	double x0 = 200;
	double y0 = 200;
	double r = 150;
	
	Contour2D astroid;
	
	Box2D box = new Box2D(10, 410, 10, 410);
	
	public DrawAstroid2Demo(){
		Point2D p0 = new Point2D(x0, y0);
		Point2D p1 = new Point2D(x0+r, y0);
		Point2D p2 = new Point2D(x0, y0+r);
		Point2D p3 = new Point2D(x0-r, y0);
		Point2D p4 = new Point2D(x0, y0-r);
		QuadBezierCurve2D quad1 = new QuadBezierCurve2D(p1, p0, p2);
		QuadBezierCurve2D quad2 = new QuadBezierCurve2D(p2, p0, p3);
		QuadBezierCurve2D quad3 = new QuadBezierCurve2D(p3, p0, p4);
		QuadBezierCurve2D quad4 = new QuadBezierCurve2D(p4, p0, p1);
		
		astroid = new BoundaryPolyCurve2D<QuadBezierCurve2D>(new QuadBezierCurve2D[]{
				quad1, quad2, quad3, quad4});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// fill the astoid's interior
		g2.setColor(Color.CYAN);
		astroid.fill(g2);
		
		// fill the astoid's exterior
		g2.setColor(Color.YELLOW);
		new GenericDomain2D(astroid).complement().clip(box).fill(g2);
		
		// Draw the astroid boundary
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		astroid.draw(g2);
		
		
	}
	
	public final static void main(String[] args){
		JPanel panel = new DrawAstroid2Demo();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Draw Astroid Demo (2)");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}	
}
