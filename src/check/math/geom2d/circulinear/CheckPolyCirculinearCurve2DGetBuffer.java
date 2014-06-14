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
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyCirculinearCurve2DGetBuffer extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCirculinearCurve2D<?> curve;
	Curve2D parallel;
	
	public CheckPolyCirculinearCurve2DGetBuffer(){
		// constants
		double x0 	= 50;
		double y0 	= 50;
		double R 	= 100;
		
		// arcs center
		Point2D center = new Point2D(x0, y0);
		
		// the different elements of the curve
		LineSegment2D line1 = new LineSegment2D(
				new Point2D(x0+R, y0), 
				new Point2D(x0+2*R, y0));
		CircleArc2D arc1 = new CircleArc2D(
				center, 2*R, 0, Math.PI/2);
		LineSegment2D line2 = new LineSegment2D(
				new Point2D(x0, y0+2*R), 
				new Point2D(x0, y0+R));
		CircleArc2D arc2 = new CircleArc2D(
				center, R, Math.PI/2, -Math.PI/2);
		
		// create the curve by concatenating elements
		curve = new PolyCirculinearCurve2D<CirculinearElement2D>(
				new CirculinearElement2D[]{line1, arc1, line2, arc2}, true);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		Domain2D buffer = bc.computeBuffer(curve, 10);
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.draw(g2);
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
		
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckPolyCirculinearCurve2DGetBuffer();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel of a circulinear curve");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
