package math.geom2d.spline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.polygon.Polyline2D;

public class CheckPolyCubicBezierCurve2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	Point2D[] points;
	
	
	public CheckPolyCubicBezierCurve2D() {
		
		Point2D p11 = new Point2D(100, 100);
		Point2D p12 = new Point2D(200, 100);
		Point2D p13 = new Point2D(300, 100);
		Point2D p20 = new Point2D(000, 200);
		Point2D p21 = new Point2D(100, 200);
		Point2D p22 = new Point2D(200, 200);
		Point2D p23 = new Point2D(300, 200);
		Point2D p30 = new Point2D(000, 300);
		Point2D p31 = new Point2D(100, 300);
		Point2D p32 = new Point2D(200, 300);
		
		points = new Point2D[]{
				p11, p12, p22, p21, p20, p30, p31, p32, p23, p13};
	}
	
	public void paintComponent(Graphics g){
		
		PolyCubicBezierCurve2D curve = PolyCubicBezierCurve2D.create(points);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1.0f));
		new Polyline2D(points).draw(g2);
		
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		curve.draw(g2);
		
		
//		CubicBezierCurve2D bezier2 = bezier1.getParallel(20);
//		
//		g2.setStroke(new BasicStroke());
//		g2.draw(bezier2);
		
	}

	public final static void main(String[] args){
		JPanel panel = new CheckPolyCubicBezierCurve2D();
		JFrame frame = new JFrame("Check Poly Bezier Curve");
		panel.setPreferredSize(new Dimension(400, 400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}}
