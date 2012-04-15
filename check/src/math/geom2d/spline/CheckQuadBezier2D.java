package math.geom2d.spline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;

public class CheckQuadBezier2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	Point2D p1 = new Point2D(100, 100);
	Point2D c  = new Point2D(100, 300);
	Point2D p2 = new Point2D(300, 200);
	Point2D[] points;
	
	QuadBezierCurve2D bezier;
	
	public CheckQuadBezier2D(){
		points = new Point2D[]{
				new Point2D(100, 100),
				new Point2D(200, 300),
				new Point2D(300, 100)
		};
		
		bezier = new QuadBezierCurve2D(points);
	}
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		bezier.draw(g2);
		
		g2.setPaint(Color.RED);
		for(Point2D point : points)
			point.draw(g2, 5);
		
		QuadBezierCurve2D bezier2 = bezier.subCurve(.5, .75);
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(2.0f));
		bezier2.draw(g2);
		
	}

	public final static void main(String[] args){
		JPanel panel = new CheckQuadBezier2D();
		JFrame frame = new JFrame("Check Quadratic Bezier Curve");
		frame.setContentPane(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
	}}
