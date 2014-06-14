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

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.Ray2D;

/**
 * @author dlegland
 *
 */
public class CheckPolyCirculinearCurve2DGetBuffer2 extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCirculinearCurve2D<CirculinearElement2D> curve;
	Curve2D parallel1;
	Curve2D parallel2;
	
	Box2D box;
	
	public CheckPolyCirculinearCurve2DGetBuffer2(){
		// constants
		box = new Box2D(10, 390, 10, 390);
		
		// first defines some constants
		Point2D origin = new Point2D(200, 200);
		Vector2D v1 = new Vector2D(2, 4);
		Vector2D v2 = new Vector2D(4, 2);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		curve =	new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.add(ray1);
		curve.add(ray2);
		
		parallel1 = curve.parallel(100);
		parallel2 = curve.parallel(-100);
		}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		Domain2D buffer = curve.buffer(100);
		g2.setColor(Color.CYAN);
		buffer.clip(box).fill(g2);
		
		g2.setColor(Color.BLACK);
		curve.clip(box).draw(g2);
		
//		g2.setColor(Color.RED);
//		parallel1.clip(box).draw(g2);
//		
//		g2.setColor(Color.BLUE);
//		parallel2.clip(box).draw(g2);
		
		g2.setColor(Color.BLUE);
		buffer.boundary().clip(box).draw(g2);
		
	}
	
	public final static void main(String[] args){
		System.out.println("parallel of wedges");
		
		JPanel panel = new CheckPolyCirculinearCurve2DGetBuffer2();
		panel.setPreferredSize(new Dimension(500, 400));
		JFrame frame = new JFrame("Draw parallel of a wedge curve");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
