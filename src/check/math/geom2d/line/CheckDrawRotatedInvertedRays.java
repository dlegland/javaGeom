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
import math.geom2d.curve.CurveArray2D;

/**
 * @author dlegland
 *
 */
public class CheckDrawRotatedInvertedRays extends JPanel{
	private static final long serialVersionUID = 1L;

	double x0=150, y0=150;
	Box2D box = new Box2D(50, 250, 50, 250);
	
	CurveArray2D<InvertedRay2D> rays;
	
	public CheckDrawRotatedInvertedRays(){
		rays = new CurveArray2D<InvertedRay2D>();
		
		InvertedRay2D ray0 = new InvertedRay2D(x0+20, y0, 1, 0);
		int N = 11;
		for(int i=0; i<N; i++){
			double theta = 2*Math.PI*i/N;
			AffineTransform2D rot = AffineTransform2D.createRotation(x0, y0, theta);
			rays.add(ray0.transform(rot));
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		for(InvertedRay2D ray : rays)
			ray.clip(box).draw(g2);
		
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw rotated rays");
		
		JPanel panel = new CheckDrawRotatedInvertedRays();
		JFrame frame = new JFrame("Draw rotated rays");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
