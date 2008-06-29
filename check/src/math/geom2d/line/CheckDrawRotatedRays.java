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
import math.geom2d.curve.CurveSet2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawRotatedRays extends JPanel{
	private static final long serialVersionUID = 1L;

	double x0=150, y0=150;
	Box2D box = new Box2D(50, 250, 50, 250);
	
	CurveSet2D<Ray2D> rays;
	
	public CheckDrawRotatedRays(){
		rays = new CurveSet2D<Ray2D>();
		
		Ray2D ray0 = new Ray2D(x0+20, y0, 1, 0);
		int N = 11;
		for(int i=0; i<N; i++){
			double theta = 2*Math.PI*(double)i/(double)N;
			AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, theta);
			rays.addCurve(ray0.transform(rot));
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		for(Ray2D ray : rays)
			g2.draw(ray.clip(box));
		
		g2.setColor(Color.BLACK);
		g2.draw(box.getBoundary());
	}
	
	public final static void main(String[] args){
		System.out.println("draw rotated rays");
		
		JPanel panel = new CheckDrawRotatedRays();
		JFrame frame = new JFrame("Draw rotated rays");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}