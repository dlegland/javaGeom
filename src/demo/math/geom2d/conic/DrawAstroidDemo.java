/**
 * 
 */
package math.geom2d.conic;

import static java.lang.Math.PI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.GenericDomain2D;

/**
 * Draw and fill the interior of an astroid defined from several circle arcs.
 * @author dlegland
 *
 */
public class DrawAstroidDemo extends JPanel {

	private static final long serialVersionUID = 1L;

	double x0 = 200;
	double y0 = 200;
	double r = 150;
	
	Contour2D astroid;
	
	Box2D box = new Box2D(10, 410, 10, 410);
	
	public DrawAstroidDemo(){
		
		CircleArc2D arc1 = new CircleArc2D(x0+r, y0+r, r, 3*PI/2, -PI/2);
		CircleArc2D arc2 = new CircleArc2D(x0-r, y0+r, r, 0, -PI/2);
		CircleArc2D arc3 = new CircleArc2D(x0-r, y0-r, r, PI/2, -PI/2);
		CircleArc2D arc4 = new CircleArc2D(x0+r, y0-r, r, PI, -PI/2);
		
		astroid = new BoundaryPolyCurve2D<CircleArc2D>(new CircleArc2D[]{
					arc1, arc2, arc3, arc4});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// fill the astoid's interior
		g2.setColor(Color.CYAN);
		astroid.fill(g2);
		
		// fill the astoid's exterior
		g2.setColor(Color.YELLOW);
		new GenericDomain2D(astroid).complement().clip(box).fill(g2);
		
		// Draw the astroid boundary
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		astroid.draw(g2);
		
		
	}
	
	public final static void main(String[] args){
		JPanel panel = new DrawAstroidDemo();
		panel.setPreferredSize(new Dimension(600, 500));
		JFrame frame = new JFrame("Draw Astroid Demo");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}	
}
