/**
 * 
 */
package math.geom2d.circulinear.buffer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.circulinear.PolyCirculinearCurve2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.Ray2D;

/**
 * Compute parallel of a simple polyline, with various joins.
 * @author dlegland
 *
 */
public class CheckBiRayGetBuffer extends JPanel{
	private static final long serialVersionUID = 1L;

	PolyCirculinearCurve2D<CirculinearElement2D>  curve;
	
	public CheckBiRayGetBuffer(){
		
		// first defines some constants
		Point2D origin = new Point2D(310, 200);
		Vector2D v1 = new Vector2D(2, 3);
		Vector2D v2 = new Vector2D(3, 2);
		
		// create elements of the curve
		InvertedRay2D ray1 = new InvertedRay2D(origin, v1);
		Ray2D ray2 = new Ray2D(origin, v2);
		
		// create the curve
		curve = new PolyCirculinearCurve2D<CirculinearElement2D>();
		curve.add(ray1);
		curve.add(ray2);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		
		// computes the parallel
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		CirculinearContinuousCurve2D parallel1 =
			bc.createContinuousParallel(curve, 50);
		CirculinearContinuousCurve2D parallel2 =
			bc.createContinuousParallel(curve, -50);

		Box2D box = new Box2D(10, 590, 10, 390);
		
		g2.setColor(Color.BLUE);
		parallel1.clip(box).draw(g2);
		parallel2.clip(box).draw(g2);
		
		g2.setColor(Color.BLACK);
		curve.clip(box).draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckBiRayGetBuffer();
		panel.setPreferredSize(new Dimension(600, 400));
		JFrame frame = new JFrame("Several parallels of a bi-ray");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
