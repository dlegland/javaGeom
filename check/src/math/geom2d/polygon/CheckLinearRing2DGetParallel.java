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
import math.geom2d.curve.Curve2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * @author dlegland
 *
 */
public class CheckLinearRing2DGetParallel extends JPanel{
	private static final long serialVersionUID = 1L;

	LinearRing2D ring;
	Curve2D parallel;
	
	public CheckLinearRing2DGetParallel(){
		
	    ring = new LinearRing2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(150, 100),
				new Point2D(50, 200) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		ring.draw(g2);
		
		g2.setColor(Color.BLUE);

		parallel = ring.parallel(30);
		parallel.draw(g2);
		
		parallel = ring.parallel(-10);
		parallel.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckLinearRing2DGetParallel();
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}
}
