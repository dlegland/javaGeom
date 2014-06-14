/**
 * File: 	CheckDrawRectangles2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 25 juil. 09
 */
package math.geom2d.polygon;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;


/**
 * draw centered rectangles
 * @author dlegland
 *
 */
public class CheckSimplePolygon2DAddVertex extends JPanel{
	private static final long serialVersionUID = 1L;
	
	Polygon2D poly;
	Polygon2D poly2;
	Polygon2D poly3;
	
	public CheckSimplePolygon2DAddVertex(){
		
		// Create a polygon with 4 vertices
		poly = new SimplePolygon2D(
				new Point2D(100, 100), new Point2D(300, 100),
				new Point2D(300, 200), new Point2D(100, 200));
		
		poly2 = new SimplePolygon2D(poly.vertices());
		poly2.addVertex(new Point2D(50, 150));

		poly3 = new SimplePolygon2D(poly.vertices());
		poly3.insertVertex(2, new Point2D(350, 150));
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(3));
		poly2.draw(g2);

		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2));
		poly3.draw(g2);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		poly.draw(g2);

	}
	
	public final static void main(String[] args){
		System.out.println("draw rectangles");
		
		JPanel panel = new CheckSimplePolygon2DAddVertex();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Draw rectangles");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}

}
