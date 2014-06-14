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
import math.geom2d.curve.Curve2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * Check if a continuous contour (here a linear ring) which self-intersects
 * is correctly spitted into disjoint parts.
 * @author dlegland
 *
 */
public class CheckSplitContinuousClosed extends JPanel{
	private static final long serialVersionUID = 1L;

	LinearRing2D curve;
	Curve2D parallel;
	
	public CheckSplitContinuousClosed(){
		
		curve = new LinearRing2D(new Point2D[]{
				new Point2D(40, 80), 
				new Point2D(140, 80),
				new Point2D(140, 160), 
				new Point2D(60, 160), 
				new Point2D(60, 100),
				new Point2D(160, 100), 
				new Point2D(160, 180), 
				new Point2D(80, 180), 
				new Point2D(80, 60), 
				new Point2D(120, 60), 
				new Point2D(120, 140), 
				new Point2D(40, 140)
		});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		curve.draw(g2);
		
		Color[] colors = new Color[]{
				Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
		int i= 0;

		g2.setColor(Color.BLUE);
		for(CirculinearContinuousCurve2D cont :
			CirculinearCurves2D.splitContinuousCurve(curve)){
			g2.setColor(colors[i++]);
			cont.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		System.out.println("check splitting of closed contour");
		
		JPanel panel = new CheckSplitContinuousClosed();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Split self-intersecting contour");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
