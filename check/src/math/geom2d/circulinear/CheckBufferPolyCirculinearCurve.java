/**
 * 
 */
package math.geom2d.circulinear;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;

/**
 * Compute buffer of a polycurve with colinear adjacent curves
 * @author dlegland
 *
 */
public class CheckBufferPolyCirculinearCurve extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearCurve2D curve;
	CirculinearDomain2D domain;
	
	double dist = 30;
	
	public CheckBufferPolyCirculinearCurve(){
		
		// create curve elements
		CircleArc2D arc1 = 
			new CircleArc2D(new Point2D(300, 200), 100, 0, -Math.PI);
		CircleArc2D arc2 = 
			new CircleArc2D(new Point2D(200, 300), 100, 3*Math.PI/2, -Math.PI);
		
		// Create a new closed polycurve with 4 elements
		curve = new PolyCirculinearCurve2D<CircleArc2D>(
				new CircleArc2D[]{arc1, arc2}, false);
		
		domain = curve.buffer(dist);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		domain.fill(g2);
		g2.setColor(Color.BLUE);
		domain.draw(g2);
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckBufferPolyCirculinearCurve();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Buffer of a circulinear polycurve");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
