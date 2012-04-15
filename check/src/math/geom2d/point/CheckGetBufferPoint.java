/**
 * 
 */
package math.geom2d.point;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class CheckGetBufferPoint extends JPanel{
	private static final long serialVersionUID = 1L;

	Point2D point;
	Domain2D domain1;
	Domain2D domain2;
	
	public CheckGetBufferPoint(){
		point = new Point2D(150, 150);
		domain1 = point.buffer(20);
		domain2 = point.buffer(-50);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// draw buffer with posiive distance
		g2.setColor(Color.CYAN);
		domain1.fill(g2);
		g2.setColor(Color.BLUE);
		domain1.draw(g2);
		
		// draw buffer with negative distance
		Box2D box = new Box2D(50, 250, 50, 250);
		g2.setColor(Color.CYAN);
		domain2.clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		domain2.draw(g2);
		
		g2.setColor(Color.BLACK);
		point.draw(g2, 5);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferPoint();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of circles");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
