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
 * Check if a two continuous contours (here two linear rings) which intersect
 * each other are correctly splitted into disjoint parts.
 * @author dlegland
 *
 */
public class CheckSplit2Contours extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearContour2D ring1, ring2;
	Curve2D parallel;
	
	public CheckSplit2Contours(){
		
		ring1 = new LinearRing2D(new Point2D[]{
				new Point2D(100, 100),
				new Point2D(250, 100),
				new Point2D(250, 300),
				new Point2D(200, 300),
				new Point2D(200, 150),
				new Point2D(150, 150),
				new Point2D(150, 300),
				new Point2D(100, 300) });
		
		ring2 = new LinearRing2D(new Point2D[]{
				new Point2D(50, 200),
				new Point2D(300, 200),
				new Point2D(300, 250),
				new Point2D(50, 250) });
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		ring1.draw(g2);
		ring2.draw(g2);
		
		Color[] colors = new Color[]{
				Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.ORANGE};
		int i= 0;

		g2.setColor(Color.BLUE);
		for(CirculinearContinuousCurve2D cont :
			CirculinearCurves2D.splitIntersectingContours(ring1, ring2)){
			g2.setColor(colors[i++]);
			cont.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		System.out.println("check splitting of closed contours");
		
		JPanel panel = new CheckSplit2Contours();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Split intersecting contours");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
