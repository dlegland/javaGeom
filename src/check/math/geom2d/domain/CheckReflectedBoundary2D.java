/* file : CheckReflectedBoundary2D.java
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;


public class CheckReflectedBoundary2D extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Boundary2D curve = null;
	Box2D box = new Box2D(50, 450, 50, 350);
	
	public CheckReflectedBoundary2D() {
		super();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		Point2D p1 = new Point2D(100, 100);
		Point2D p2 = new Point2D(300, 100);
		Point2D p3 = new Point2D(200, 270);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		LineSegment2D seg3 = new LineSegment2D(p3, p1);
		
		StraightLine2D line = new StraightLine2D(p2, p3);
		AffineTransform2D trans =
			AffineTransform2D.createLineReflection(line);
		
		BoundaryPolyCurve2D<ContinuousOrientedCurve2D> boundary;
		boundary = new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(
				new LineSegment2D[]{seg1, seg2, seg3});
		
		Boundary2D boundary2 = boundary.transform(trans);
		
		Domain2D domain = new GenericDomain2D(boundary2);
		Domain2D domain2 = domain.complement();
		
		// fill the domain
		g2.setColor(Color.CYAN);
		Domain2D clipped = domain.clip(box);
		clipped.fill(g2);
		
		// fill the domain
		g2.setColor(Color.YELLOW);
		Domain2D clipped2 = domain2.clip(box);
		clipped2.fill(g2);
		
		g2.setColor(Color.BLACK);
		line.clip(box).draw(g2);

//		g2.setColor(Color.RED);
//		domain.clip(box).getBoundary().draw(g2);
//	
		// draw the boundary
		g2.setColor(Color.BLUE);
		boundary2.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("draw a clipped boundary");
		
		JPanel panel = new CheckReflectedBoundary2D();
		JFrame frame = new JFrame("Draw a clipped boundary");
		panel.setPreferredSize(new Dimension(500,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
