/**
 * 
 */
package math.geom2d.conic;

import java.awt.Color;
import java.awt.Dimension;
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
public class CheckGetBufferCircle extends JPanel{
	private static final long serialVersionUID = 1L;

	Circle2D circle;
	Domain2D domain;
	
	public CheckGetBufferCircle(){
		
		circle = new Circle2D(new Point2D(100, 100), 50);
		
		domain = circle.buffer(20);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.fill(g2);
		g2.setColor(Color.BLUE);
		domain.draw(g2);
		
		g2.setColor(Color.BLACK);
		circle.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferCircle();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of single circle");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
