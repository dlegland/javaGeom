/**
 * 
 */
package math.geom2d.curve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawPolyCurve2D extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCurve2D<LineSegment2D> polycurve = null;
	
	public CheckDrawPolyCurve2D(){
		// create the points
		Point2D p1 = new Point2D(50, 50);
		Point2D p2 = new Point2D(50, 150);
		Point2D p3 = new Point2D(100, 100);
		Point2D p4 = new Point2D(200, 200);
		
		// create the lines
		LineSegment2D line1 = new LineSegment2D(p1, p2);
		LineSegment2D line2 = new LineSegment2D(p2, p3);
		LineSegment2D line3 = new LineSegment2D(p3, p4);
		
		// create the polycurve
		polycurve = new PolyCurve2D<LineSegment2D> (2);
		polycurve.add(line1);
		polycurve.add(line2);
		polycurve.add(line3);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(1.0f));
	
		g2.setColor(Color.BLUE);
		polycurve.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw a curve set");
		
		JPanel panel = new CheckDrawPolyCurve2D();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw polycurve demo");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
