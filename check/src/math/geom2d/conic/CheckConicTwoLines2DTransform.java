/* file : CheckConicTwoLines2DTransform.java
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
public class CheckConicTwoLines2DTransform extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double d  = 30;
	double theta  = 0;

	Point2D origin = new Point2D(x0, y0);
	
	Conic2D conic = null;
	Box2D box = null;
	
	public CheckConicTwoLines2DTransform() {
		super();
		
		double y1=y0+d, y2=y0-d;
		conic = Conics2D.reduceConic(
				new double[]{0, 0, 1/(y1*y2), 0, -(y1+y2)/y1/y2, 1});
		
		box = new Box2D(50, 250, 50, 250);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
			
		double theta = Math.PI/3;
		AffineTransform2D rot = AffineTransform2D.createRotation(origin, theta);
		Conic2D rotated = conic.transform(rot);

//		g2.setColor(Color.CYAN);
//		g2.fill(Boundary2DUtil.clipBoundary(rotated, box));
		g2.setColor(Color.BLUE);
		rotated.clip(box).draw(g2);

		g2.setColor(Color.BLACK);
		conic.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("Transform conic");
		
		JPanel panel = new CheckConicTwoLines2DTransform();
		JFrame frame = new JFrame("Check rotations of two-lines conic");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
