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
import math.geom2d.polygon.LinearRing2D;

/**
 * Compute buffer of a linear ring with parallel adjacent edges.
 * @author dlegland
 *
 */
public class CheckBufferLozenge extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	
	public CheckBufferLozenge(){
		
		curve = new LinearRing2D(new Point2D[]{
				new Point2D(300, 100), 
				new Point2D(500, 200), 
				new Point2D(300, 300),
				new Point2D(100, 200)	});
		
		//domain = BufferCalculator.computeBuffer(curve, 30);
		domain = curve.buffer(30);
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
		JPanel panel = new CheckBufferLozenge();
		panel.setPreferredSize(new Dimension(600, 400));
		JFrame frame = new JFrame("Linear ring buffer");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
