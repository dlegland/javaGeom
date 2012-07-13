/**
 * 
 */
package math.geom2d.polygon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;

/**
 * @author dlegland
 *
 */
public class CheckIntersectPolyline2D extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline1;
	LinearRing2D polyline2;
	
	Collection<Point2D> points;

	public CheckIntersectPolyline2D(){
	    
		polyline1 = new Polyline2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(150, 50),
				new Point2D(150, 150),
				new Point2D(50, 150) });
		
        polyline2 = new LinearRing2D(new Point2D[]{
                new Point2D(80, 80),
                new Point2D(200, 80),
                new Point2D(200, 120),
                new Point2D(120, 120),
                new Point2D(120, 200),
                new Point2D(80, 200) });
        
        points = Polylines2D.intersect(polyline1, polyline2);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		polyline1.draw(g2);
		
        g2.setColor(Color.RED);
        polyline2.draw(g2);
        
        g2.setColor(Color.BLACK);
        for(Point2D point : points) {
            point.draw(g2, 2.5);
        }
    }
	
	public final static void main(String[] args){
		System.out.println("draw intersection of polylines");
		
		JPanel panel = new CheckIntersectPolyline2D();
		panel.setPreferredSize(new Dimension(400, 300));
		JFrame frame = new JFrame("Check intersection of polyline");
		frame.setBackground(Color.WHITE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
