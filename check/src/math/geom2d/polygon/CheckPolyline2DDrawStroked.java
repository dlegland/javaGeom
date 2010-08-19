/**
 * 
 */
package math.geom2d.polygon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyline2DDrawStroked extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline;
	
	public CheckPolyline2DDrawStroked(){
		
		polyline = new Polyline2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(150, 100),
				new Point2D(50, 200), 
				new Point2D(150, 200) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(20.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER));
		polyline.draw(g2);
		
		AffineTransform2D trans = AffineTransform2D.createRotation(
				new Point2D(200, 200), -2*Math.PI/3);
		
		polyline.transform(trans).draw(g2);
		
		g2.setStroke(new BasicStroke(1.0f));
		g2.setColor(Color.BLACK);
		polyline.draw(g2);
		
	}
	
	public final static void main(String[] args){
		System.out.println("draw polyline with style");
		
		JPanel panel = new CheckPolyline2DDrawStroked();
		panel.setPreferredSize(new Dimension(500, 500));
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
