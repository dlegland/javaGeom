package math.geom2d.conic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.StraightLine2D;

public class CheckCircleArc2DTransform  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	double x0 = 100;
	double y0 = 100;
	double r = 50;

	Box2D clipping = new Box2D(10, 390, 10, 390);
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;

		Circle2D circle = new Circle2D(x0, y0, r);
		CircleArc2D arc = new CircleArc2D(circle, -Math.PI/4, Math.PI/2);
		
		StraightLine2D line = new StraightLine2D(new Point2D(200, 200), new Vector2D(0, 1));
		AffineTransform2D symmetry = AffineTransform2D.createLineReflection(line);
		
		g2.setColor(Color.blue);
		circle.draw(g2);
		circle.transform(symmetry).draw(g2);
		
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(2f));
		arc.draw(g2);
		arc.transform(symmetry).draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("transfrom a circle arc");
		
		JPanel panel = new CheckCircleArc2DTransform();
		JFrame frame = new JFrame("Check Circle arc transform");
		frame.setContentPane(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
	}}
