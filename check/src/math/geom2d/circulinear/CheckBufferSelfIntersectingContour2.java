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
public class CheckBufferSelfIntersectingContour2 extends JPanel{
	private static final long serialVersionUID = 1L;

	LinearRing2D curve;
	Curve2D parallel;
	double dist = 15;
	
	public CheckBufferSelfIntersectingContour2(){
		
		// Create a curve which self intersect many times
		curve = new LinearRing2D(new Point2D[]{
				new Point2D(50, 100), 
				new Point2D(300, 100),
				new Point2D(300, 300), 
				new Point2D(100, 300), 
				new Point2D(100, 150),
				new Point2D(350, 150), 
				new Point2D(350, 350), 
				new Point2D(150, 350), 
				new Point2D(150, 50), 
				new Point2D(250, 50), 
				new Point2D(250, 250), 
				new Point2D(50, 250)
		});
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
		
		JPanel panel = new CheckBufferSelfIntersectingContour2();
		panel.setPreferredSize(new Dimension(500, 500));
		JFrame frame = new JFrame("buffer of self-intersecting contour");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
