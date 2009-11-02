package math.geom2d.spline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;

public class CheckBezierCurve2D_getParallel  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	Point2D p1 = new Point2D(100, 100);
	Point2D c1 = new Point2D(100, 200);
	Point2D c2 = new Point2D(200, 100);
	Point2D p2 = new Point2D(300, 200);
	
	@Override
	public void paintComponent(Graphics g){
		
		CubicBezierCurve2D bezier1 = new CubicBezierCurve2D(p1, c1, c2, p2);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		bezier1.draw(g2);
		
		
//		CubicBezierCurve2D bezier2 = bezier1.getParallel(20);
//		
//		g2.setStroke(new BasicStroke());
//		g2.draw(bezier2);
		
	}

	public final static void main(String[] args){
		JPanel panel = new CheckBezierCurve2D_getParallel();
		JFrame frame = new JFrame("Check parallel Bezier Curve");
		frame.setContentPane(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
	}}
