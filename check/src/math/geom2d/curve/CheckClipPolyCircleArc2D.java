/**
 * 
 */
package math.geom2d.curve;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.line.StraightLine2D;

/**
 * @author dlegland
 *
 */
public class CheckClipPolyCircleArc2D extends JPanel{
	private static final long serialVersionUID = 1L;

	ContourArray2D<Circle2D> circleSet = new ContourArray2D<Circle2D>();
	
	public CheckClipPolyCircleArc2D(){
		circleSet.add(new Circle2D(50, 50, 40));
		circleSet.add(new Circle2D(150, 50, 40));
		circleSet.add(new Circle2D(100, 140, 50));
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		double x0 = 200;
		double y0 = 200;
		double r = 100;
		
		CircleArc2D arc1 = new CircleArc2D(x0+r/2, y0, r, 2*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(x0-r/2, y0, r, 5*Math.PI/3, 2*Math.PI/3);
		
//		arc1.draw(g2);
//		arc2.draw(g2);
	
		Box2D box = new Box2D(50, 350, 50, 280);
		box.draw(g2);
		
		PolyCurve2D<CircleArc2D> curve = new PolyCurve2D<CircleArc2D>(
				new CircleArc2D[]{arc1, arc2});
		curve.draw(g2);
		Curve2D clipped = curve.clip(box);
		clipped.draw(g2);

		StraightLine2D line = new StraightLine2D(200, 250, 1, 0);
		AffineTransform2D sym = AffineTransform2D.createLineReflection(line);
		
		Curve2D transformed = clipped.transform(sym);
		transformed.draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw a clipped curve set");
		
		JPanel panel = new CheckClipPolyCircleArc2D();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw clipped curve set demo");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
