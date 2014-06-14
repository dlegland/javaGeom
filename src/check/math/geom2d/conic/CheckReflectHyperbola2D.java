/* file : CheckReflectHyperbola2D.java
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.line.StraightLine2D;

/**
 * Check the transformation of Hyperbolas by affine transforms.
 * @author dlegland
 *
 */
public class CheckReflectHyperbola2D extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	private final static Point2D center = new Point2D(200, 200);
	
	private final static Vector2D Ox = new Vector2D(1, 0);
	private final static Vector2D Oy = new Vector2D(0, 1);

	double x0 = 150;
	double y0 = 150;
	double a  = 30;
	double b  = 10;
	double theta  = 0;

	Point2D origin = new Point2D(x0, y0);
	
	Hyperbola2D hyperbola = null;
	Box2D box = null;
	
	public CheckReflectHyperbola2D() {
		super();
		
		hyperbola = new Hyperbola2D(x0, y0, a, b, theta);
		box = new Box2D(50, 350, 50, 350);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// The rotation of the base hyperbola
		double theta = Math.PI/3;
		AffineTransform2D rot = 
			AffineTransform2D.createRotation(origin, theta);
		Hyperbola2D rotated = hyperbola.transform(rot);

		// the reflection of the hyperbola 
		AffineTransform2D refOy = 
			AffineTransform2D.createLineReflection(
					new StraightLine2D(center, Oy));
		AffineTransform2D refOx = 
			AffineTransform2D.createLineReflection(
					new StraightLine2D(center, Ox));
		
		g2.setColor(Color.CYAN);
		rotated.domain().clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		rotated.clip(box).draw(g2);
		
		// draw the clip of the transform
		Hyperbola2D reflected = rotated.transform(refOy);
		g2.setColor(Color.BLACK);
		reflected.clip(box).draw(g2);
		
		// draw the clip of the transform
		Hyperbola2D reflectedOx = rotated.transform(refOx);
		g2.setColor(Color.MAGENTA);
		reflectedOx.clip(box).draw(g2);
		
		// draw the transform of the clip
		Curve2D clipped = rotated.clip(box).transform(refOy);
		g2.setColor(Color.RED);
		clipped.draw(g2);
		for(ContinuousCurve2D cont : clipped.continuousCurves()){
			cont.asPolyline(30).draw(g2);
//			System.out.println(cont.getFirstPoint());
//			System.out.println(cont.getLastPoint());
		}
		
//		// Draw the original hyperbola
//		g2.setColor(Color.BLACK);
//		hyperbola.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);

		// Draw the origin point
		Point2D p1 = new Point2D(x0, y0);
		p1.draw(g2, 4);
	}

	public final static void main(String[] args){
		System.out.println("Transform hyperbola");
		
		JPanel panel = new CheckReflectHyperbola2D();
		panel.setPreferredSize(new Dimension(400, 400));
		JFrame frame = new JFrame("Check reflection of hyperbola");
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);		
	}
}
