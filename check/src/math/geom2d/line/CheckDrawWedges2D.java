/**
 * 
 */
package math.geom2d.line;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundary2DUtil;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawWedges2D extends JPanel{
	private static final long serialVersionUID = 1L;

	double x0=150, y0=150;
	Box2D box = new Box2D(50, 250, 50, 250);
	
	InvertedRay2D inv1 = new InvertedRay2D(x0, y0, -1, -2);
	Ray2D ray1 = new Ray2D(x0, y0, 2, 1);
	
	BoundaryPolyCurve2D<StraightObject2D> wedge1; 
	
	
	public CheckDrawWedges2D(){
		wedge1 = new BoundaryPolyCurve2D<StraightObject2D>(
				new StraightObject2D[]{inv1, ray1});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, Math.PI/3);

		Boundary2D clipped = Boundary2DUtil.clipBoundary(wedge1, box);
		Boundary2D rotated = wedge1.transform(rot);
		
		g2.setColor(Color.CYAN);
		g2.fill(clipped);
		g2.fill(Boundary2DUtil.clipBoundary(rotated, box));	
		
		g2.setColor(Color.BLUE);
		g2.draw(wedge1.clip(box));
		g2.draw(rotated.clip(box));
		
		g2.setColor(Color.BLACK);
		g2.draw(box.getBoundary());
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
