/**
 * 
 */
package math.geom2d.line;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawWedges2D extends JPanel{
	private static final long serialVersionUID = 1L;

	double x0=150, y0=150;
	double d = 20;
	Box2D box = new Box2D(50, 250, 50, 250);
	
	InvertedRay2D inv1 	= new InvertedRay2D(x0, y0-d, 1, 2);
	Ray2D ray1 			= new Ray2D(x0, y0-d, 1, -2);
	InvertedRay2D inv2 	= new InvertedRay2D(x0, y0+d, -1, -2);
	Ray2D ray2 			= new Ray2D(x0, y0+d, -1, 2);
	
	BoundaryPolyCurve2D<AbstractLine2D> wedge1; 
	BoundaryPolyCurve2D<AbstractLine2D> wedge2; 
	ContourArray2D<BoundaryPolyCurve2D<AbstractLine2D>> boundary;
	
	@SuppressWarnings("unchecked")
	public CheckDrawWedges2D(){
		wedge1 = new BoundaryPolyCurve2D<AbstractLine2D>(
				new AbstractLine2D[]{inv1, ray1});
		wedge2 = new BoundaryPolyCurve2D<AbstractLine2D>(
				new AbstractLine2D[]{inv2, ray2});
		// Unchecked type cast
		boundary = new ContourArray2D<BoundaryPolyCurve2D<AbstractLine2D>>(
			new BoundaryPolyCurve2D[]{wedge1, wedge2});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, Math.PI/3);

		Domain2D domain = new GenericDomain2D(boundary);
		Domain2D rotated = domain.transform(rot);
		
		g2.setColor(Color.CYAN);
		domain.clip(box).fill(g2);
		
		g2.setColor(Color.BLUE);
		rotated.clip(box).draw(g2);
		
		g2.setColor(Color.BLACK);
		domain.boundary().clip(box).draw(g2);
		box.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw wedges");
		
		JPanel panel = new CheckDrawWedges2D();
		JFrame frame = new JFrame("Draw wedges");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}
}
