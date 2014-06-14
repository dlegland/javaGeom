/**
 * File: 	CheckDrawRectangles2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 25 juil. 09
 */
package math.geom2d.polygon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;


/**
 * draw centered rectangles
 * @author dlegland
 *
 */
public class CheckDrawRectangles2D extends JPanel{
	private static final long serialVersionUID = 1L;
	
	Polygon2D rect1;
	Polygon2D rect2;
	Polygon2D rect3;
	Polygon2D rect4;
	
	public CheckDrawRectangles2D(){
		
		double length = 180;
		double width = 80;
		
	    rect1 = Polygons2D.createCenteredRectangle(new Point2D(200, 150), length, width);
	    rect2 = Polygons2D.createOrientedRectangle(new Point2D(400, 150), length, width, 0);
	    rect3 = Polygons2D.createOrientedRectangle(new Point2D(200, 350), length, width, Math.PI/2);
	    rect4 = Polygons2D.createOrientedRectangle(new Point2D(400, 350), length, width, Math.PI/6);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		rect1.fill(g2);
		rect2.fill(g2);
		rect3.fill(g2);
		rect4.fill(g2);

		g2.setColor(Color.BLACK);
		rect1.draw(g2);
		rect2.draw(g2);
		rect3.draw(g2);
		rect4.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw rectangles");
		
		JPanel panel = new CheckDrawRectangles2D();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Draw rectangles");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}

}
