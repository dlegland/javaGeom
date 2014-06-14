/**
 * 
 */
package math.geom2d.polygon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class CheckLinearRing2D_getBuffer extends JPanel{
	private static final long serialVersionUID = 1L;

	LinearRing2D ring;
	Domain2D buffer;
	
	public CheckLinearRing2D_getBuffer(){
		
	    ring = LinearRing2D.create(new Point2D[]{
				new Point2D(100, 100),
				new Point2D(300, 100),
				new Point2D(300, 300),
				new Point2D(200, 200),
				new Point2D(100, 300) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		//TODO: wrong for dist=51, wrong for dist=52
		buffer = ring.buffer(51);
		
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.draw(g2);
		
		g2.setColor(Color.BLACK);
		ring.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw linear ring buffer");
		
		JPanel panel = new CheckLinearRing2D_getBuffer();
		JFrame frame = new JFrame("Draw buffer of linear ring");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}
}
