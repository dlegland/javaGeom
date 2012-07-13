/**
 * 
 */
package math.geom2d.circulinear;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.conic.Circle2D;

/**
 * Check if a two continuous contours (here two circles) which intersect
 * each other are correctly splitted into disjoint parts.
 * @author dlegland
 *
 */
public class CheckSplit3Circles extends JPanel{
	private static final long serialVersionUID = 1L;

	CirculinearContour2D ring1, ring2, ring3;
	
	public CheckSplit3Circles(){
		
		ring1 = new Circle2D(150, 150, 100);
		ring2 = new Circle2D(250, 150, 100);
		ring3 = new Circle2D(200, 230, 100);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		ring1 = new Circle2D(150, 150, 100);
		ring2 = new Circle2D(250, 150, 100);
		ring3 = new Circle2D(200, 230, 100);
		
		g2.setColor(Color.BLACK);
		ring1.draw(g2);
		ring2.draw(g2);
		ring3.draw(g2);
		
		ArrayList<CirculinearContour2D> rings = new ArrayList<CirculinearContour2D>(3);
		rings.add(ring1);
		rings.add(ring2);
		rings.add(ring3);

		Color[] colors = new Color[]{
				Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE};
		int i= 0;

		g2.setColor(Color.BLUE);
		for(CirculinearContour2D cont :
			CirculinearCurves2D.splitIntersectingContours(rings)){
			g2.setColor(colors[i++]);
			cont.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		System.out.println("check splitting of 3 intersecting circles");
		
		JPanel panel = new CheckSplit3Circles();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Split 3 intersecting circles");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
