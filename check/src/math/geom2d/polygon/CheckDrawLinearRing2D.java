/**
 * File: 	CheckDrawLinearRing2D.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 25 juil. 09
 */
package math.geom2d.polygon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;


/**
 * Check if not-too-basic linear ring (or closed polyline) draw correctly.
 * @author dlegland
 *
 */
public class CheckDrawLinearRing2D extends JPanel{
	private static final long serialVersionUID = 1L;
	
	LinearRing2D ring;
	
	public CheckDrawLinearRing2D(){
		
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
	}
	
	public final static void main(String[] args){
		System.out.println("draw linear ring");
		
		JPanel panel = new CheckDrawLinearRing2D();
		JFrame frame = new JFrame("Draw linear ring");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}

}
