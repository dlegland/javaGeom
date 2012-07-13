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
import math.geom2d.conic.Circle2D;

/**
 * @author dlegland
 *
 */
public class CheckGetBufferDiscs extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearDomain2D domain;
	CirculinearDomain2D buffer;
	
	public CheckGetBufferDiscs(){
		Circle2D circle1 = new Circle2D(new Point2D(100, 100), 50);
		Circle2D circle2 = new Circle2D(new Point2D(200, 100), 40);
		domain = new GenericCirculinearDomain2D(
				new CirculinearContourArray2D<Circle2D>(
						new Circle2D[]{circle1, circle2}));
		
		buffer = CirculinearDomains2D.computeBuffer(domain, 30);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.draw(g2);
		
		g2.setColor(Color.BLACK);
		domain.boundary().draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferDiscs();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of discs");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
