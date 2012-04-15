/* file : CheckHyperbola2DTransform.java
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

/**
 * Check the transformation of Hyperbolas by affine transforms.
 * @author dlegland
 *
 */
public class CheckHyperbola2DTransform extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double a  = 30;
	double b  = 10;
	double theta  = 0;

	Point2D origin = new Point2D(x0, y0);
	
	Hyperbola2D hyperbola = null;
	Box2D box = null;
	
	public CheckHyperbola2DTransform() {
		super();
		
		hyperbola = new Hyperbola2D(x0, y0, a, b, theta);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
			
		double theta = Math.PI/3;
		AffineTransform2D rot = AffineTransform2D.createRotation(origin, theta);
		Hyperbola2D rotated = hyperbola.transform(rot);

		g2.setColor(Color.CYAN);
		rotated.domain().clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		rotated.clip(box).draw(g2);

		g2.setColor(Color.BLACK);
		hyperbola.clip(box).draw(g2);
		
//		// Also draw a scaled version of the hyperbola
//		AffineTransform2D sca = AffineTransform2D.createScaling(origin, .5, 3);
//		Hyperbola2D scaled = hyperbola.transform(sca);
//		
//		g2.setColor(Color.CYAN);
//		g2.fill(Boundary2DUtil.clipBoundary(scaled, box));
//		g2.setColor(Color.BLUE);
//		g2.draw(scaled.clip(box));

		// Draw the original hyperbola
		g2.setColor(Color.BLACK);
		hyperbola.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);

		// Draw transform origin
		Point2D p1 = new Point2D(x0, y0);
		p1.draw(g2, 4);
	}

	public final static void main(String[] args){
		System.out.println("Transform hyperbola");
		
		JPanel panel = new CheckHyperbola2DTransform();
		JFrame frame = new JFrame("Check rotations of hyperbolas");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
