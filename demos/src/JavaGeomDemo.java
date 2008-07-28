/* file : JavaGeomDemo.java
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
 * Created on 26 Jul. 2008
 *
 */

import java.awt.*;
import java.util.Collection;

import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.transform.AffineTransform2D;


public class JavaGeomDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
		
	public JavaGeomDemo() {
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		// Create some points
		Point2D p1 = new Point2D(40, 30);
		Point2D p2 = new Point2D(180, 100);
				
		// Draw the points
		g2.draw(new Circle2D(p1, 2));
		g2.draw(new Circle2D(p2, 2));
		
		// Create and draw a circle
		Circle2D circle1 = new Circle2D(80, 120, 40);
		g2.draw(circle1);
		
		// Create a line
		StraightLine2D line1 = new StraightLine2D(p1, p2);
		
		// Draw the line, after clipping
		Box2D box = new Box2D(0, 200, 0, 200);
		g2.draw(line1.clip(box));
		
		Point2D p3 = new Point2D(20, 120);
		Point2D p4 = new Point2D(40, 140);
		
		// Create line segment
		LineSegment2D  edge  = new LineSegment2D(p3, p4);
		g2.draw(edge);
		
		// Compute a median line, and draw it
		StraightLine2D line2 = StraightLine2D.createMedian2D(p3, p4);
		g2.draw(new Circle2D(p3, 2));
		g2.draw(new Circle2D(p4, 2));
		g2.draw(line2.clip(box));

		// Compute intersection between 2 lines
		Point2D intLine = line2.getIntersection(line1);
		g2.draw(new Circle2D(intLine, 2));
		
		// Compute intersections between a circle and lines
		Collection<Point2D> intCircle = circle1.getIntersections(line2);
		for(Point2D point : intCircle)
			g2.draw(new Circle2D(point, 2));
		
		// Create some affine transforms
		AffineTransform2D sca = AffineTransform2D.createScaling(p4, 2, .8);
		AffineTransform2D tra = AffineTransform2D.createTranslation(30, 40);
		AffineTransform2D rot = AffineTransform2D.createRotation(p4, Math.PI/2);
		
		// Display the transformed shapes.
		g2.draw(circle1.transform(sca));
		g2.draw(circle1.transform(tra));
		g2.draw(line2.transform(rot).clip(box));
	}

	public final static void main(String[] args){
		JPanel panel = new JavaGeomDemo();
		JFrame frame = new JFrame("JavaGeom Demo");
		frame.setContentPane(panel);
		frame.setSize(250, 250);
		frame.setVisible(true);
	}
}
