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

import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.Polyline2D;

/**
 * Compute parallel of a polyline with very acute angle
 *
 */
public class CheckPolylineSmallAngle extends JPanel{
	private static final long serialVersionUID = 1L;

	Polyline2D polyline;
	
	public CheckPolylineSmallAngle(){
		
		polyline = new Polyline2D(new Point2D[]{
				new Point2D(250, 200), 
				new Point2D(150, 200), 
				new Point2D(230, 230), 
				});

	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		BufferCalculator bc = new BufferCalculator(
				new RoundJoinFactory(), new RoundCapFactory());
		
		Curve2D parallel1, parallel2 ;
		
		parallel1 = bc.createContinuousParallel(polyline, 50);
		parallel2 = bc.createContinuousParallel(polyline, -50);

		Domain2D buffer = bc.computeBuffer(polyline, 40);
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.draw(g2);

		g2.setColor(Color.MAGENTA);
		parallel1.draw(g2);
		parallel2.draw(g2);
		
		g2.setColor(Color.BLACK);
		polyline.draw(g2);
	}
	
	public final static void main(String[] args){
		JPanel panel = new CheckPolylineSmallAngle();
		panel.setPreferredSize(new Dimension(650, 400));
		JFrame frame = new JFrame("Parallels of a polyline");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
