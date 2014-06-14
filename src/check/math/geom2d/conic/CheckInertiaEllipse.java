/**
 * 
 */
package math.geom2d.conic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;

/**
 * @author dlegland
 *
 */
public class CheckInertiaEllipse extends JPanel{
	private static final long serialVersionUID = 1L;

	Ellipse2D ellipse;
	Collection<Point2D> points;
	
	public CheckInertiaEllipse(){
		
		Point2D center = new Point2D(300, 250);
		double ra = 100;
		double rb = 40;
		double theta = Math.toRadians(30);
		ellipse = new Ellipse2D(center, ra, rb, theta);
		
		AffineTransform2D rot = AffineTransform2D.createRotation(theta);
		AffineTransform2D tra = AffineTransform2D.createTranslation(center.getX(), center.getY());
		AffineTransform2D trans = rot.preConcatenate(tra);
		
		java.util.Random random = new java.util.Random();
		int np = 100;
		points = new ArrayList<Point2D>(np);
		for (int i = 0; i < np; i++) {
			double x = random.nextGaussian() * ra;
			double y = random.nextGaussian() * rb;
			points.add(new Point2D(x, y).transform(trans));
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		ellipse.draw(g2);
		
		g2.setColor(Color.BLUE);
		for (Point2D p : points)
			p.draw(g2);
		
		Ellipse2D inel = Ellipse2D.inertiaEllipse(points);
		System.out.println(inel);
		inel.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckInertiaEllipse();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Inertia ellipse of a set of points");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
