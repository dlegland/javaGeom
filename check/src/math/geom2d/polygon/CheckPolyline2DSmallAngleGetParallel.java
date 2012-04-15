/**
 * 
 */
package math.geom2d.polygon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyline2DSmallAngleGetParallel extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline;
	Curve2D parallel;
	
	public CheckPolyline2DSmallAngleGetParallel(){
		
		polyline = new Polyline2D(new Point2D[]{
				new Point2D(200, 100),
				new Point2D(100, 100),
				new Point2D(180, 140) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		polyline.draw(g2);
		
		g2.setColor(Color.BLUE);
		parallel = polyline.parallel(30);
		parallel.draw(g2);
		
		g2.setColor(Color.RED);
		parallel = polyline.parallel(-30);
		parallel.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckPolyline2DSmallAngleGetParallel();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
