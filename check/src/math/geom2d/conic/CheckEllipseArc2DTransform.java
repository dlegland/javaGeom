/* file : CheckEllipseArc2DTransform.java
 * 
 * Project : Euclide
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 1 avr. 2007
 *
 */

package math.geom2d.conic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.StraightLine2D;

/**
 * Check the transformation of Ellipses by affine transforms.
 * @author dlegland
 *
 */
public class CheckEllipseArc2DTransform extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 50;
	double y0 = 50;
	double a  = 50;
	double b  = 30;
	double theta  = Math.PI/3;

	Point2D origin = new Point2D(x0, y0);
	
	double tx = 100;
	double ty = 100;
	
	Ellipse2D ellipse = null;
	Box2D box = null;
	
	public CheckEllipseArc2DTransform() {
		super();
		
		ellipse = new Ellipse2D(x0, y0, a, b, theta, false);
		
		box = new Box2D(x0-a, x0+a, y0-b, y0+b);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// Clip the ellipse to produce ellipse arcs
		CurveSet2D<? extends Curve2D> clipped = ellipse.clip(box);
		g2.setColor(Color.CYAN);
		ellipse.draw(g2);
		g2.setColor(Color.BLUE);
		clipped.draw(g2);
		
		AffineTransform2D trans = AffineTransform2D.createLineReflection(
				new StraightLine2D(150, 150, 1, 0));
		g2.setColor(Color.CYAN);
		ellipse.transform(trans).draw(g2);
		g2.setColor(Color.BLUE);
		CurveSet2D<? extends Curve2D> transformed = clipped.transform(trans);
		//g2.draw(clipped.getFirstCurve().transform(trans));
		transformed.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("Transform ellipse arc");
		
		JPanel panel = new CheckEllipseArc2DTransform();
		JFrame frame = new JFrame("Check transformation of ellipse arc");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
}
