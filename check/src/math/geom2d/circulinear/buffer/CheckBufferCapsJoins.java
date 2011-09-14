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
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Compute buffer of a simple polyline, with various end cap and join.
 * @author dlegland
 *
 */
public class CheckBufferCapsJoins extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D curve1;
	Polyline2D curve2;
	Polyline2D curve3;
	Polyline2D curve4;
	
	public CheckBufferCapsJoins(){
		
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
		curve4 = curve3.transform(trans);
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc1 = new BufferCalculator(
				new RoundJoinFactory(), new RoundCapFactory());
		BufferCalculator bc2 = new BufferCalculator(
				new RoundJoinFactory(), new ButtCapFactory());
		BufferCalculator bc3 = new BufferCalculator(
				new BevelJoinFactory(), new TriangleCapFactory());
		BufferCalculator bc4 = new BufferCalculator(
				new MiterJoinFactory(), new SquareCapFactory());
		
		Domain2D domain1, domain2, domain3, domain4;
		
		//domain1 = bc1.computeBuffer(curve, 30); fails...
		domain1 = bc1.computeBuffer(curve1, 20);
		domain2 = bc2.computeBuffer(curve2, 20);
		domain3 = bc3.computeBuffer(curve3, 20);
		domain4 = bc4.computeBuffer(curve4, 20);
		
		g2.setColor(Color.CYAN);
		domain1.fill(g2);
		domain2.fill(g2);
		domain3.fill(g2);
		domain4.fill(g2);
		
		g2.setColor(Color.BLUE);
		domain1.draw(g2);
		domain2.draw(g2);
		domain3.draw(g2);
		domain4.draw(g2);
	
		g2.setColor(Color.BLACK);
		curve1.draw(g2);
		curve2.draw(g2);
		curve3.draw(g2);
		curve4.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckBufferCapsJoins();
		panel.setPreferredSize(new Dimension(650, 500));
		JFrame frame = new JFrame("Compute buffer caps and joins");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
