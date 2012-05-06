/**
 * 
 */
package math.geom2d.line;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.point.PointArray2D;

/**
 * @author dlegland
 *
 */
public class DrawLineSegmentIntersectionsDemo extends JPanel {

	private static final long serialVersionUID = 1L;

	Box2D box = new Box2D(100, 500, 100, 400);
	
	CurveArray2D<LineSegment2D> lines = new CurveArray2D<LineSegment2D>();
	PointArray2D points = new PointArray2D();
	
	public DrawLineSegmentIntersectionsDemo(){
		Point2D point1, point2;
		// Generate random line segments from 2 points
		for(int i=0; i<30; i++){
			point1 = new Point2D(Math.random()*400+100, Math.random()*300+100);
			point2 = new Point2D(Math.random()*400+100, Math.random()*300+100);
			lines.add(new LineSegment2D(point1, point2));
		}
		
		// compute line intersections
		Point2D intersection;
		for(LineSegment2D line1 : lines)
			for(LineSegment2D line2 : lines){
				intersection = line1.intersection(line2);
				if(intersection!=null)
					points.add(intersection);
			}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw lines in outer box
		g2.setColor(Color.BLUE);
		for(LineSegment2D line : lines)
			line.draw(g2);
		
		// Draw points in inner box
		g2.setStroke(new BasicStroke(3.0f));
		g2.setColor(Color.RED);
		for(Point2D point : points){
			if(box.contains(point))
				 point.draw(g2, 3);
		}
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f));
		box.boundary().draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new DrawLineSegmentIntersectionsDemo();
		JFrame frame = new JFrame("Draw line segment intersections demo");
		frame.setContentPane(panel);
		frame.setSize(650, 550);
		frame.setVisible(true);		
	}	
}
