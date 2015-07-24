/**
 * 
 */
package math.geom2d.line;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;

/**
 * @author dlegland
 *
 */
public class DrawClippedLinesDemo extends JPanel {

	private static final long serialVersionUID = 1L;

	Box2D outerBox = new Box2D(50, 550, 50, 450);
	Box2D innerBox = new Box2D(100, 500, 100, 400);
	
	CurveArray2D<StraightLine2D> lines = 
		new CurveArray2D<StraightLine2D>();
	
	public DrawClippedLinesDemo(){
		Point2D point;
		double angle;
		// Generate random lines from a point and an angle.
		for(int i=0; i<50; i++){
			point = new Point2D(Math.random()*600, Math.random()*500);
			angle = Math.random()*2*Math.PI;
			lines.add(new StraightLine2D(point, angle));
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		Curve2D clipped;
		
		// Draw lines in outer box
		g2.setColor(Color.BLUE);
		for(StraightLine2D line : lines){
			clipped = line.clip(outerBox);
			if(!clipped.isEmpty())
				clipped.draw(g2);
		}
		
		// Draw lines in inner box
		g2.setStroke(new BasicStroke(3.0f));
		g2.setColor(Color.BLACK);
		for(StraightLine2D line : lines){
			clipped = line.clip(innerBox);
			if(!clipped.isEmpty())
				clipped.draw(g2);
		}
	}
	
	public final static void main(String[] args){
		JPanel panel = new DrawClippedLinesDemo();
		JFrame frame = new JFrame("Draw clipped lines demo");
		frame.setContentPane(panel);
		frame.setSize(650, 550);
		frame.setVisible(true);		
	}	
}
