/**
 * 
 */
package math.geom2d.curve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

/**
 * Check (1) if a line is drawn between each portion, (2) if each portion
 * starts at the right position, and (3) if the curve is drawn closed.
 * @author dlegland
 *
 */
public class CheckDrawClosedPolyCurve2D extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCurve2D<LineSegment2D> polycurve = null;
	
	public CheckDrawClosedPolyCurve2D(){
		// create the points
		Point2D p11 = new Point2D(400, 500);
		Point2D p12 = new Point2D(200, 500);
		Point2D p21 = new Point2D(100, 400);
		Point2D p22 = new Point2D(100, 200);
		Point2D p31 = new Point2D(200, 100);
		Point2D p32 = new Point2D(400, 100);
		
		// create the lines
		LineSegment2D line1 = new LineSegment2D(p11, p12);
		LineSegment2D line2 = new LineSegment2D(p21, p22);
		LineSegment2D line3 = new LineSegment2D(p31, p32);
		
		// create the polycurve
		polycurve = new PolyCurve2D<LineSegment2D> (3);
		polycurve.add(line1);
		polycurve.add(line2);
		polycurve.add(line3);
		polycurve.setClosed(true);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(20.0f));
	
		g2.setColor(Color.BLUE);
		polycurve.draw(g2);
		
		Point2D center = new Point2D(200, 200);
		AffineTransform2D trans = 
			AffineTransform2D.createRotation(center, Math.PI/7);
		Curve2D curve2 = polycurve.transform(trans);
		g2.setColor(Color.DARK_GRAY);
		curve2.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw a curve set");
		
		JPanel panel = new CheckDrawClosedPolyCurve2D();
		panel.setPreferredSize(new Dimension(600, 600));
		JFrame frame = new JFrame("Draw polycurve demo");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
