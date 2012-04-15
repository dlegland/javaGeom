/* file : CheckClipPolySmoothCurve2D.java
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
import math.geom2d.line.LineSegment2D;


public class CheckClipPolySmoothCurve2D extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Boundary2D curve = null;
	Box2D box = new Box2D(50, 450, 50, 350);
	
	public CheckClipPolySmoothCurve2D() {
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
		
		BoundaryPolyCurve2D<ContinuousOrientedCurve2D> boundary;
		boundary = new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(
				new LineSegment2D[]{seg1, seg2, seg3});
		Domain2D domain = new GenericDomain2D(boundary);
		
		Domain2D clipped = domain.complement().clip(box);
		
		// fill the domain
		g2.setColor(Color.CYAN);
		clipped.fill(g2);
		
		g2.setColor(Color.RED);
		domain.clip(box).boundary().draw(g2);
	
		// draw the boundary
		g2.setColor(Color.BLUE);
		boundary.clip(box).draw(g2);
		
		// draw the bounding box
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("draw a clipped boundary");
		
		JPanel panel = new CheckClipPolySmoothCurve2D();
		JFrame frame = new JFrame("Draw a clipped boundary");
		panel.setPreferredSize(new Dimension(500,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
}
