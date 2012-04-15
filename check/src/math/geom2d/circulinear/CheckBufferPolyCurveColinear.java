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

import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.LineSegment2D;

/**
 * Compute buffer of a polycurve with colinear adjacent curves
 * @author dlegland
 *
 */
public class CheckBufferPolyCurveColinear extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	
	double dist = 30;
	
	public CheckBufferPolyCurveColinear(){
		
		// create curve elements
		LineSegment2D line1 = 
			new LineSegment2D(new Point2D(200, 100), new Point2D(400, 100));
		CircleArc2D arc1 = 
			new CircleArc2D(new Point2D(400, 200), 100, 3*Math.PI/2, Math.PI);
		LineSegment2D line2 = 
			new LineSegment2D(new Point2D(400, 300), new Point2D(200, 300));
		CircleArc2D arc2 = 
			new CircleArc2D(new Point2D(200, 200), 100, Math.PI/2, Math.PI);
		
		// Create a new closed polycurve with 4 elements
		curve = new PolyCirculinearCurve2D<CirculinearElement2D>(
				new CirculinearElement2D[]{line1, arc1, line2, arc2}, true);
		
		domain = curve.buffer(dist);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.fill(g2);
		g2.setColor(Color.BLUE);
		domain.draw(g2);
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckBufferPolyCurveColinear();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Buffer of a colinear polycurve");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
