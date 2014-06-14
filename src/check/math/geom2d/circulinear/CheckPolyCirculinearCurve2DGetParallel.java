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
import math.geom2d.curve.Curve2D;
import math.geom2d.line.LineSegment2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyCirculinearCurve2DGetParallel extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCirculinearCurve2D<?> curve;
	Curve2D parallel;
	
	public CheckPolyCirculinearCurve2DGetParallel(){
		
		Point2D center = new Point2D(50, 50);
		LineSegment2D line1 = new LineSegment2D(
				new Point2D(100, 50), new Point2D(150, 50));
		CircleArc2D arc1 = new CircleArc2D(center, 100, 0, Math.PI/2);
		LineSegment2D line2 = new LineSegment2D(
				new Point2D(50, 150), new Point2D(50, 100));
		CircleArc2D arc2 = new CircleArc2D(center, 50, Math.PI/2, -Math.PI/2);
		
		curve = new PolyCirculinearCurve2D<CirculinearElement2D>(
				new CirculinearElement2D[]{line1, arc1, line2, arc2}, true);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
		
		g2.setColor(Color.BLUE);

		parallel = curve.parallel(30);
		parallel.draw(g2);
		
		parallel = curve.parallel(-10);
		parallel.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckPolyCirculinearCurve2DGetParallel();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
