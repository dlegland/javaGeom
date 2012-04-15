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

import math.geom2d.conic.Ellipse2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * creates an ellipse, convert to linear ring, and computes buffer.
 * @author dlegland
 *
 */
public class CheckBufferEllipseAsLinearRing extends JPanel{
	private static final long serialVersionUID = 1L;

	Ellipse2D ellipse;
	LinearRing2D ring;
	
	double dist = 20;
	
	public CheckBufferEllipseAsLinearRing(){
		
		ellipse = new Ellipse2D(300, 200, 200, 100, Math.PI/6);
		ring = ellipse.asPolyline(60);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// compute buffer
		Domain2D buffer = ring.buffer(dist);
		
		// fill the buffer
		g2.setColor(Color.CYAN);
		buffer.fill(g2);

//		// Draw the 2 parallels of the ring
//		g2.setColor(Color.RED);
//		ring.getParallel(dist).draw(g2);
//		g2.setColor(Color.GREEN);
//		ring.getParallel(-dist).draw(g2);

		g2.setColor(Color.BLACK);
		ellipse.draw(g2);
			
		g2.setColor(Color.BLUE);
		buffer.boundary().draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("check buffer of an approximated ellipse");
		
		JPanel panel = new CheckBufferEllipseAsLinearRing();
		panel.setPreferredSize(new Dimension(600, 600));
		JFrame frame = new JFrame("Approximated ellipse buffer");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
