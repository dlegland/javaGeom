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
import math.geom2d.domain.ContourArray2D;

/**
 * @author dlegland
 *
 */
public class CheckClipBand2D extends JPanel{
	private static final long serialVersionUID = 1L;

	double x0=150, y0=150;
	double d = 50;
	Box2D box = new Box2D(50, 250, 50, 250);
	
	StraightLine2D line1 = new StraightLine2D(x0-d, y0-d, -1, 1);
	StraightLine2D line2 = new StraightLine2D(x0+d, y0+d, 1, -1);

	ContourArray2D<StraightLine2D> band; 
	
	
	public CheckClipBand2D(){
		band = new ContourArray2D<StraightLine2D>(
				new StraightLine2D[]{line1, line2});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.CYAN);
		band.domain().clip(box).fill(g2);	
		
		g2.setColor(Color.BLUE);
		line1.clip(box).draw(g2);
		line2.clip(box).draw(g2);
		
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}
	
	public final static void main(String[] args){
		System.out.println("draw band");
		
		JPanel panel = new CheckClipBand2D();
		JFrame frame = new JFrame("Draw band");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);		
	}
}
