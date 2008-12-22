package math.geom2d.conic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.curve.Curve2D;

public class CheckCircle2DClipBox2D  extends JPanel{

	/** */
	private static final long serialVersionUID = 1L;
	
	double r0 = 10;
	double r1 = 20;
	double r2 = 40;
	
	double[] x 	= new double[]{50, 150, 250, 50, 150, 250, 50, 150, 250};
	double[] y 	= new double[]{50, 50, 50, 150, 150, 150, 250, 250, 250};
	double[] x0 = new double[]{40, 150, 260, 40, 260, 40, 150, 260};
	double[] y0 = new double[]{40, 40, 40, 150, 150, 260, 260, 260};
	
	public void paintComponent(Graphics g){
		Box2D box1 = new Box2D(50, 250, 50, 250);
		Box2D box2 = new Box2D(30, 270, 30, 270);
		
		// init circles
		Collection<Circle2D> circles1 = new ArrayList<Circle2D>();
		Collection<Circle2D> circles2 = new ArrayList<Circle2D>();
		Collection<Circle2D> circles0 = new ArrayList<Circle2D>();
		for(int i=0; i<9; i++){
			circles1.add(new Circle2D(x[i], y[i], r1));
			circles2.add(new Circle2D(x[i], y[i], r2));
		}
		
		for(int i=0; i<8; i++)
			circles0.add(new Circle2D(x0[i], y0[i], r0));
		
		
		Graphics2D g2 = (Graphics2D) g;

		// draw box outlines
		g2.setColor(Color.BLACK);
		box1.draw(g2);
		box2.draw(g2);
		
		// draw each circle
		g2.setColor(Color.BLUE);
		for(Circle2D circle : circles1)
			circle.clip(box1).draw(g2);
		
		// draw each circle
		for(Circle2D circle : circles2)
			circle.clip(box2).draw(g2);
		
		// draw each circle
		g2.setColor(Color.BLUE);
		for(Circle2D circle : circles0)
			circle.clip(box2).draw(g2);
		
		// draw each circle
		g2.setColor(Color.RED);
		for(Circle2D circle : circles0){
			Curve2D clipped = circle.clip(box1);
			if(!clipped.isEmpty())
				clipped.draw(g2);
		}
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new CheckCircle2DClipBox2D();
		panel.setPreferredSize(new Dimension(400, 400));
		JFrame frame = new JFrame("Check Clip Circle2D");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}}
