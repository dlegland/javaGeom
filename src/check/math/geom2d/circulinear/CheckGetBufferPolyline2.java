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
import math.geom2d.polygon.Polyline2D;

/**
 * Compute buffer of a simple polyline, but whose parallel intersects...
 * @author dlegland
 *
 */
public class CheckGetBufferPolyline2 extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	
	public CheckGetBufferPolyline2(){
		
		// create the curve
		curve = new Polyline2D(new Point2D[]{
				new Point2D(80, 180), 
				new Point2D(140, 180), 
				new Point2D(140, 80), 
				new Point2D(260, 80), 
				new Point2D(260, 200), 
				new Point2D(160, 200), 
				new Point2D(160, 260)});
		
		// compute the buffer
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		domain = bc.computeBuffer(curve, 30);
	}
	
	public void paintComponent(Graphics g){
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
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferPolyline2();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of a tricky polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
