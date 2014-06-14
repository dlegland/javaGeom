/**
 * 
 */
package math.geom2d.curve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Domain2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawCurveSet2D extends JPanel{
	private static final long serialVersionUID = 1L;

	ContourArray2D<Circle2D> circleSet = new ContourArray2D<Circle2D>();
	
	public CheckDrawCurveSet2D(){
		circleSet.add(new Circle2D(50, 50, 40));
		circleSet.add(new Circle2D(150, 50, 40));
		circleSet.add(new Circle2D(100, 140, 50));
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(2.0f));
	
		Box2D box = new Box2D(0, 200, 0, 200);
		Domain2D domain =  circleSet.domain().complement();

		g2.setColor(Color.CYAN);
		Domain2D clipped = domain.clip(box);
		clipped.fill(g2);
		
		g2.setColor(Color.BLUE);
		domain.boundary().clip(box).draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw a curve set");
		
		JPanel panel = new CheckDrawCurveSet2D();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw curve set demo");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
