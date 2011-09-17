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
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Compute parallel of a simple polyline, with various joins.
 * @author dlegland
 *
 */
public class CheckBufferPolylines extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D curve1;
	Polyline2D curve2;
	Polyline2D curve3;
	Polyline2D curve4;
	
	public CheckBufferPolylines(){
		
		curve1 = new Polyline2D(new Point2D[]{
				new Point2D(50, 50), 
				new Point2D(50, 100), 
				new Point2D(100, 100), 
				new Point2D(100, 50), 
				new Point2D(150, 100), 
				new Point2D(150, 50), 
				});

		AffineTransform2D tx =
			AffineTransform2D.createTranslation(200, 0);
		AffineTransform2D ty =
			AffineTransform2D.createTranslation(0, 150);
		curve2 = curve1.transform(tx);
		curve3 = curve1.transform(ty);
		curve4 = curve2.transform(ty);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc1 = new BufferCalculator(
				new RoundJoinFactory(), new RoundCapFactory());
		BufferCalculator bc2 = new BufferCalculator(
				new MiterJoinFactory(), new SquareCapFactory());
		BufferCalculator bc3 = new BufferCalculator(
				new BevelJoinFactory(), new TriangleCapFactory());
		BufferCalculator bc4 = new BufferCalculator(
				new MiterJoinFactory(), new ButtCapFactory());
		
		Domain2D buffer1, buffer2, buffer3, buffer4;
		
		double dist = 15;
		buffer1 = bc1.computeBuffer(curve1, dist);
		buffer2 = bc2.computeBuffer(curve2, dist);
		buffer3 = bc3.computeBuffer(curve3, dist);
		buffer4 = bc4.computeBuffer(curve4, dist);

		g2.setColor(Color.CYAN);
		buffer1.fill(g2);
		buffer2.fill(g2);
		buffer3.fill(g2);
		buffer4.fill(g2);

		g2.setColor(Color.BLUE);
		buffer1.draw(g2);
		buffer2.draw(g2);
		buffer3.draw(g2);
		buffer4.draw(g2);
		
		g2.setColor(Color.BLACK);
		curve1.draw(g2);
		curve2.draw(g2);
		curve3.draw(g2);
		curve4.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckBufferPolylines();
		panel.setPreferredSize(new Dimension(600, 400));
		JFrame frame = new JFrame("Several buffers of polylines");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
