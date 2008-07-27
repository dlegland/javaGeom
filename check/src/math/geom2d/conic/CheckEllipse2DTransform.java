/* file : CheckEllipse2DTransform.java
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
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.domain.Boundary2DUtils;
import math.geom2d.transform.AffineTransform2D;

/**
 * Check the transformation of Ellipses by affine transforms.
 * @author dlegland
 *
 */
public class CheckEllipse2DTransform extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double a  = 50;
	double b  = 30;
	double theta  = Math.PI/3;

	Point2D origin = new Point2D(x0, y0);
	
	Ellipse2D ellipse = null;
	Box2D box = null;
	
	public CheckEllipse2DTransform() {
		super();
		
		ellipse = new Ellipse2D(x0, y0+100, a, b, theta);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
			
		for(int i=0; i<8; i++){
			double theta = i*Math.PI*2/8;
			AffineTransform2D rot = AffineTransform2D.createRotation(origin, theta);
			Ellipse2D rotated = ellipse.transform(rot);

			g2.setColor(Color.CYAN);
			g2.fill(Boundary2DUtils.clipBoundary(rotated, box));
			g2.setColor(Color.BLUE);
			g2.draw(rotated);
		}

		// Also draw a scaled version of the ellipse
		AffineTransform2D rot = AffineTransform2D.createScaling(origin, 2, .5);
		Ellipse2D scaled = ellipse.transform(rot);
		
		g2.setColor(Color.CYAN);
		g2.fill(Boundary2DUtils.clipBoundary(scaled, box));
		g2.setColor(Color.BLUE);
		g2.draw(scaled.clip(box));

		// draw the bounding box
		g2.setColor(Color.BLACK);
		g2.draw(box.getAsRectangle());

		// Draw transform origin
		Point2D p1 = new Point2D(x0, y0);
		g2.fill(new Circle2D(p1, 4));
	}

	public final static void main(String[] args){
		System.out.println("Transform ellipse");
		
		JPanel panel = new CheckEllipse2DTransform();
		JFrame frame = new JFrame("Check rotations of ellipse");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
