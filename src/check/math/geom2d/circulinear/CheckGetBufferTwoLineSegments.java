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
	Box2D box = new Box2D(50, 350, 50, 350);
	
	public CheckGetBufferTwoLineSegments(){
		Point2D p11 = new Point2D(100, 200);
		Point2D p12 = new Point2D(300, 200);
		Point2D p21 = new Point2D(200, 100);
		Point2D p22 = new Point2D(200, 300);
		LineSegment2D line1 = new LineSegment2D(p11, p12);
		LineSegment2D line2 = new LineSegment2D(p21, p22);
		
		curve = CirculinearCurveArray2D.create(line1, line2);
		domain = curve.buffer(30);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		domain.boundary().clip(box).draw(g2);
		
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
