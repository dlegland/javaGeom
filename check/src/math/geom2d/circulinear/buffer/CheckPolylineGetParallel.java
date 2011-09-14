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

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Compute parallel of a simple polyline, with various joins.
 * @author dlegland
 *
 */
public class CheckPolylineGetParallel extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D curve1;
	Polyline2D curve2;
	Polyline2D curve3;
	
	public CheckPolylineGetParallel(){
		
		curve1 = new Polyline2D(new Point2D[]{
				new Point2D(50, 250), 
				new Point2D(130, 250), 
				new Point2D(130, 190), 
				new Point2D(50, 110), 
				new Point2D(50, 50), 
				new Point2D(130, 50), 
				});

		AffineTransform2D trans =
			AffineTransform2D.createTranslation(150, 0);
		curve2 = curve1.transform(trans);
		curve3 = curve2.transform(trans);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc1 = new BufferCalculator(
				new RoundJoinFactory(), new RoundCapFactory());
		BufferCalculator bc2 = new BufferCalculator(
				new BevelJoinFactory(), new TriangleCapFactory());
		BufferCalculator bc3 = new BufferCalculator(
				new MiterJoinFactory(), new SquareCapFactory());
		
		Curve2D parallel1,parallel1i, parallel2, parallel2i, parallel3, parallel3i;
		
		parallel1 = bc1.createContinuousParallel(curve1, 20);
		parallel1i = bc1.createContinuousParallel(curve1, -20);
		parallel2 = bc2.createContinuousParallel(curve2, 20);
		parallel2i = bc2.createContinuousParallel(curve2, -20);
		parallel3 = bc3.createContinuousParallel(curve3, 20);
		parallel3i = bc3.createContinuousParallel(curve3, -20);

		g2.setColor(Color.BLUE);
		parallel1.draw(g2);
		parallel1i.draw(g2);
		parallel2.draw(g2);
		parallel2i.draw(g2);
		parallel3.draw(g2);
		parallel3i.draw(g2);
		
		g2.setColor(Color.BLACK);
		curve1.draw(g2);
		curve2.draw(g2);
		curve3.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckPolylineGetParallel();
		panel.setPreferredSize(new Dimension(650, 400));
		JFrame frame = new JFrame("Several parallels of a polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
