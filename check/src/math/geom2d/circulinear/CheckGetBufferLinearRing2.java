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
import math.geom2d.polygon.LinearRing2D;

/**
 * Compute buffer of a linear ring with parallel adjacent edges.
 * @author dlegland
 *
 */
public class CheckGetBufferLinearRing2 extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	
	public CheckGetBufferLinearRing2(){
		
		curve = new LinearRing2D(new Point2D[]{
				new Point2D(200, 100), 
				new Point2D(300, 100), 
				new Point2D(300, 200), 
				new Point2D(200, 200),
				new Point2D(100, 200),
				new Point2D(100, 100)	});
		
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		domain = bc.computeBuffer(curve, 30);
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
		JPanel panel = new CheckGetBufferLinearRing2();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Linear ring buffer");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
