/* file : CheckEllipseArc2DDraw.java
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

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.curve.CurveSet2D;

/**
 * Check the transformation of Ellipses by affine transforms.
 * @author dlegland
 *
 */
public class CheckEllipseArc2DDraw extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double a  = 50;
	double b  = 30;
	double theta  = Math.PI/3;

	Point2D origin = new Point2D(x0, y0);
	
	double tx = 100;
	double ty = 100;
	
	Ellipse2D ellipse = null;
	Box2D box = null;
	
	public CheckEllipseArc2DDraw() {
		super();
		
		ellipse = new Ellipse2D(x0, y0, a, b, theta);
		
		box = new Box2D(x0-a, x0+a, y0-b, y0+b);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw the support ellipse
		g2.setColor(Color.CYAN);
		g2.draw(ellipse);
		
		// create an arc
		EllipseArc2D arc1 = new EllipseArc2D(ellipse, Math.PI/6, 2*Math.PI/3);
		g2.setColor(Color.BLUE);
		//g2.draw(arc1);
		
		// create an arc
		EllipseArc2D arc2 = new EllipseArc2D(ellipse, -Math.PI/6, -2*Math.PI/3);
		g2.setColor(Color.RED);
		//g2.draw(arc2);
		

		CurveSet2D<EllipseArc2D> set = new CurveSet2D<EllipseArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		
		g2.setColor(Color.BLACK);
		g2.draw(set);
	}

	public final static void main(String[] args){
		System.out.println("Transform ellipse arc");
		
		JPanel panel = new CheckEllipseArc2DDraw();
		JFrame frame = new JFrame("Check transformation of ellipse arc");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
