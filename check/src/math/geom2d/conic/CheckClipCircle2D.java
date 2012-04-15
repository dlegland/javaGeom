package math.geom2d.conic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.AffineTransform2D;

public class CheckClipCircle2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	
	public void paintComponent(Graphics g){		
		Graphics2D g2 = (Graphics2D) g;
		
		Box2D box = new Box2D(100, 300, 100, 300);
		box.boundary().draw(g2);
		
		
		Circle2D circle0 = new Circle2D(200, 200, 80);
		circle0.clip(box).draw(g2);
		
		double x0 = 260;
		double y0 = 200;
		double r = 124;
		Circle2D circle = new Circle2D(x0, y0, r);
		
		circle.draw(g2);
		
		g2.setColor(Color.RED);
		
		Curve2D clipped = circle.clip(box);
		clipped.draw(g2);
		
		double x1 = 200;
		double y1 = 200;
		StraightLine2D line = new StraightLine2D(new Point2D(x1, y1),
				new Vector2D(0, 1));
		AffineTransform2D sym = AffineTransform2D.createLineReflection(line);
		
		Curve2D transformed = clipped.transform(sym);
		transformed.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new CheckClipCircle2D();
		panel.setPreferredSize(new Dimension(400, 400));
		JFrame frame = new JFrame("Check Clip Circle2D");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}}
