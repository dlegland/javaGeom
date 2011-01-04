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
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * Check if a continuous contour (here a linear ring) which self-intersects
 * is correctly spitted into disjoint parts.
 * @author dlegland
 *
 */
public class CheckBufferSelfIntersectingContour extends JPanel{
	private static final long serialVersionUID = 1L;

	LinearRing2D curve;
	Curve2D parallel;
	double dist = 60;
	
	public CheckBufferSelfIntersectingContour(){
		
		// Create a curve which self intersect
		curve = new LinearRing2D(new Point2D[]{
				new Point2D(100, 300), 
				new Point2D(500, 300),
				new Point2D(500, 500), 
				new Point2D(300, 500), 
				new Point2D(300, 100),
				new Point2D(100, 100)});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		Domain2D buffer = bc.computeBuffer(curve, dist);
		g2.setColor(Color.CYAN);
		buffer.fill(g2);

		g2.setColor(Color.BLACK);
		curve.draw(g2);
		
		
		g2.setColor(Color.BLUE);
		buffer.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("check buffer of intersecting closed contour");
		
		JPanel panel = new CheckBufferSelfIntersectingContour();
		panel.setPreferredSize(new Dimension(600, 600));
		JFrame frame = new JFrame("buffer of self-intersecting contour");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
