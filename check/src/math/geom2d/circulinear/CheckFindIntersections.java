/**
 * 
 */
package math.geom2d.circulinear;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;

/**
 * @author dlegland
 *
 */
public class CheckFindIntersections extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCirculinearCurve2D<?> curve;
	CirculinearContinuousCurve2D parallel1;
	CirculinearContinuousCurve2D parallel2;
	
	public CheckFindIntersections(){
		double d90 = Math.PI/2;
		double d180 = Math.PI;
		double d270 = 3*Math.PI/2;
		
		CircleArc2D arc1 = new CircleArc2D(
				new Point2D(150, 150), 50, d270, -d90);
		CircleArc2D arc2 = new CircleArc2D(
				new Point2D(50, 150), 50, 0, -d90);
		CircleArc2D arc3 = new CircleArc2D(
				new Point2D(100, 100), 50, d180, d180);
		
		curve = new PolyCirculinearCurve2D<CirculinearElement2D>(
				new CirculinearElement2D[]{arc1, arc2, arc3}, true);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
		
		g2.setColor(Color.BLUE);

		parallel1 = curve.parallel(20);
		parallel1.draw(g2);
		
		parallel2 = curve.parallel(-20);
//		parallel2.draw(g2);
		
		Collection<Point2D> points;
		
		for(CirculinearContinuousCurve2D cont : 
			CirculinearCurves2D.splitContinuousCurve(parallel2)) {
			points = CirculinearCurves2D.findIntersections(curve, cont);
			if(points.size()==0) {
				cont.draw(g2);
			}
		}
	}
	
	public final static void main(String[] args){
		System.out.println("draw parallel polyline");
		
		JPanel panel = new CheckFindIntersections();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
