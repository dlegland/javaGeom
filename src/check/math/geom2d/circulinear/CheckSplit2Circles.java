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

import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curve2D;

/**
 * Check if a two continuous contours (here two circles) which intersect
 * each other are correctly splitted into disjoint parts.
 * @author dlegland
 *
 */
public class CheckSplit2Circles extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearContour2D ring1, ring2;
	Curve2D parallel;
	
	public CheckSplit2Circles(){
		
		ring1 = new Circle2D(100, 100, 50);
		ring2 = new Circle2D(150, 100, 50);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		ring1.draw(g2);
		ring2.draw(g2);
		
		Color[] colors = new Color[]{
				Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE};
		int i= 0;

		g2.setColor(Color.BLUE);
		for(CirculinearContinuousCurve2D cont :
			CirculinearCurves2D.splitIntersectingContours(ring1, ring2)){
			g2.setColor(colors[i++]);
			cont.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		System.out.println("check splitting of intersecting circles");
		
		JPanel panel = new CheckSplit2Circles();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Split intersecting circles");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
