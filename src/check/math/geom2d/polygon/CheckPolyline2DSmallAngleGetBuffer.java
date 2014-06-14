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
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyline2DSmallAngleGetBuffer extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline;
	Domain2D buffer;
	
	public CheckPolyline2DSmallAngleGetBuffer(){
		
		polyline = new Polyline2D(new Point2D[]{
				new Point2D(200, 100),
				new Point2D(100, 100),
				new Point2D(180, 140) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		polyline.draw(g2);
		
		buffer = polyline.buffer(30);
		
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckPolyline2DSmallAngleGetBuffer();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
