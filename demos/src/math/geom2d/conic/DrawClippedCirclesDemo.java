/**
 * 
 */
package math.geom2d.conic;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;

/**
 * @author dlegland
 *
 */
public class DrawClippedCirclesDemo extends JPanel {

	private static final long serialVersionUID = 1L;

	Box2D clippingBox = new Box2D(100, 500, 100, 400);
	
	CurveArray2D<Circle2D> circles = new CurveArray2D<Circle2D>();
	
	public DrawClippedCirclesDemo(){
		Point2D point;
		double radius;
		// Generate random circles from a point and a radius.
		for(int i=0; i<50; i++){
			point = new Point2D(Math.random()*600, Math.random()*500);
			radius = Math.random()*100+20;
			circles.add(new Circle2D(point, radius));
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		Curve2D clipped;
		
		// Draw circles
		g2.setColor(Color.BLUE);
		for(Circle2D circle : circles)
			 circle.draw(g2);
		
		// Draw circles in inner box
		g2.setStroke(new BasicStroke(3.0f));
		g2.setColor(Color.BLACK);
		for(Circle2D circle : circles){
			 clipped = circle.clip(clippingBox);
			 if(!clipped.isEmpty())
				 clipped.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		JPanel panel = new DrawClippedCirclesDemo();
		JFrame frame = new JFrame("Draw clipped circles demo");
		frame.setContentPane(panel);
		frame.setSize(650, 550);
		frame.setVisible(true);		
	}	
}
