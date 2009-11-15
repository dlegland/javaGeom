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

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

/**
 * Compute buffer of a simple polyline.
 * @author dlegland
 *
 */
public class CheckGetBufferTwoLineSegments extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	Box2D box = Box2D.create(50, 350, 50, 350);
	
	public CheckGetBufferTwoLineSegments(){
		Point2D p11 = Point2D.create(100, 200);
		Point2D p12 = Point2D.create(300, 200);
		Point2D p21 = Point2D.create(200, 100);
		Point2D p22 = Point2D.create(200, 300);
		LineSegment2D line1 = LineSegment2D.create(p11, p12);
		LineSegment2D line2 = LineSegment2D.create(p21, p22);
		
		curve = CirculinearCurveSet2D.create(
				new LineSegment2D[]{line1, line2});
		domain = curve.getBuffer(30);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		domain.getBoundary().clip(box).draw(g2);
		
		g2.setColor(Color.BLACK);
		curve.clip(box).draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferTwoLineSegments();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of two intersecting line segments");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
