/* file : CheckClipAstroid2D.java
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

package math.geom2d.domain;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.circulinear.GenericCirculinearRing2D;
import math.geom2d.conic.CircleArc2D;


import static java.lang.Math.*;

public class CheckClipAstroid2D extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Boundary2D curve = null;
	Box2D box = new Box2D(50, 450, 50, 350);
	
	public CheckClipAstroid2D() {
		super();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		Point2D center = new Point2D(200, 200);
		double radius = 100;
		
		Point2D c1 = center.translate(radius, radius);
		CircleArc2D arc1 = new CircleArc2D(c1, 100, 3*PI/2, -PI/2);
		Point2D c2 = center.translate(-radius, radius);
		CircleArc2D arc2 = new CircleArc2D(c2, 100, 0, -PI/2);
		Point2D c3 = center.translate(-radius, -radius);
		CircleArc2D arc3 = new CircleArc2D(c3, 100, PI/2, -PI/2);
		Point2D c4 = center.translate(radius, -radius);
		CircleArc2D arc4 = new CircleArc2D(c4, 100, PI, -PI/2);
		
		// draw each circle arc
		g2.setColor(Color.BLUE);
		arc1.draw(g2);
		arc2.draw(g2);
		arc3.draw(g2);
		arc4.draw(g2);
		
		// create a poly circulinear curve
		GenericCirculinearRing2D ring = new GenericCirculinearRing2D(arc1, arc2, arc3, arc4);
		
		Box2D box = new Box2D(50, 350, 50, 350);
		
		g2.setColor(Color.CYAN);
		ring.domain().clip(box).fill(g2);
		

		g2.setColor(Color.BLACK);
		ring.draw(g2);
		box.draw(g2);
		
		if (ring.isInside(center))
			System.out.println("center is inside ring");
		
//		// fill the domain
//		g2.setColor(Color.CYAN);
//		clipped.fill(g2);
//		
//		g2.setColor(Color.RED);
//		domain.clip(box).getBoundary().draw(g2);
//	
//		// draw the boundary
//		g2.setColor(Color.BLUE);
//		boundary.clip(box).draw(g2);
//		
//		// draw the bounding box
//		g2.setColor(Color.BLACK);
//		box.getBoundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("draw a clipped boundary");
		
		JPanel panel = new CheckClipAstroid2D();
		JFrame frame = new JFrame("Draw a clipped astroid");
		panel.setPreferredSize(new Dimension(500,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
