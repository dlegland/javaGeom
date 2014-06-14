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

import math.geom2d.Point2D;
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class CheckGetBufferPointArray extends JPanel{
	private static final long serialVersionUID = 1L;

	PointArray2D set;
	Domain2D domain;
	
	public CheckGetBufferPointArray(){
		set = new PointArray2D(
				new Point2D[]{
						new Point2D(50, 50), 
						new Point2D(80, 50), 
						new Point2D(110, 50), 
						new Point2D(50, 150)});
		domain = set.buffer(20);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// draw buffer with positive distance
		g2.setColor(Color.CYAN);
		domain.fill(g2);
		g2.setColor(Color.BLUE);
		domain.draw(g2);
		
		g2.setColor(Color.BLACK);
		set.draw(g2, 5);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckGetBufferPointArray();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Compute buffer of point set");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
