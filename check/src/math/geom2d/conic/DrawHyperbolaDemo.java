/* file : DrawHyperbolaDemo.java
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
import java.util.Collection;

import javax.swing.*;


import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.Hyperbola2D;
import math.geom2d.conic.HyperbolaBranch2D;
import math.geom2d.conic.HyperbolaBranchArc2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.CurveUtil;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.line.StraightLine2D;


public class DrawHyperbolaDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Hyperbola2D hyperbola = null;
	Box2D box = null;
	
	public DrawHyperbolaDemo() {
		super();
		
		double x0 = 150;
		double y0 = 150;
		double a  = 20;
		double b  = 20;
		hyperbola = new Hyperbola2D(x0, y0, a, b, 0, true);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw the bounding box
		g2.setColor(Color.BLUE);
		g2.draw(box.getAsRectangle());

		// Draw the asymptotes
		StraightLine2D asymp1 = new StraightLine2D(150, 150, 1, 1);
		StraightLine2D asymp2 = new StraightLine2D(150, 150, -1, 1);
		g2.setColor(Color.GREEN);
		g2.draw(asymp1.clip(box));
		g2.draw(asymp2.clip(box));
		
		// isolate first branch and an arc
		double tmax = 2.5;
		HyperbolaBranch2D branch1 = (HyperbolaBranch2D) hyperbola.getFirstCurve();
		HyperbolaBranchArc2D arc1 = new HyperbolaBranchArc2D(branch1, -tmax, tmax);
	
		// isolate second branch and an arc
		HyperbolaBranch2D branch2 = (HyperbolaBranch2D) hyperbola.getLastCurve();
		HyperbolaBranchArc2D arc2 = new HyperbolaBranchArc2D(branch2, -tmax, tmax);

		// Draw the arcs
		g2.setColor(Color.BLACK);
		g2.draw(arc1);
		g2.draw(arc2);
		
		// Get focal points
		Point2D focus1 = hyperbola.getFocus1();
		Point2D focus2 = hyperbola.getFocus2();
		
		// Draw focal points
		g2.setColor(Color.BLUE);
		g2.fill(new Circle2D(focus1, 4));
		g2.fill(new Circle2D(focus2, 4));
		
		// Get intersections with some lines
		StraightLine2D line1 = new StraightLine2D(50, 50, 10, 0);
		Collection<Point2D> points1 = hyperbola.getIntersections(line1);
		StraightLine2D line2 = new StraightLine2D(50, 50, 0, 10);
		Collection<Point2D> points2 = hyperbola.getIntersections(line2);
		StraightLine2D line3 = new StraightLine2D(50, 250, 10, 0);
		Collection<Point2D> points3 = hyperbola.getIntersections(line3);
		StraightLine2D line4 = new StraightLine2D(250, 250, 0, 10);
		Collection<Point2D> points4 = hyperbola.getIntersections(line4);
		
		// Draw point sets
		g2.setColor(Color.RED);
		for(Point2D point : points1)
			g2.fill(new Circle2D(point, 2));
		for(Point2D point : points2)
			g2.fill(new Circle2D(point, 2));
		for(Point2D point : points3)
			g2.fill(new Circle2D(point, 2));
		for(Point2D point : points4)
			g2.fill(new Circle2D(point, 2));
		
		
		// Compute intersections of branches with lines
		points2 = branch2.getIntersections(line2);
		points4 = branch1.getIntersections(line4);
		
		for(Point2D point : points2)
			g2.fill(new Circle2D(point, 4));
		for(Point2D point : points4)
			g2.fill(new Circle2D(point, 4));

		
		// The clipping of first branch
		CurveSet2D<SmoothCurve2D> clipped = 
			CurveUtil.clipSmoothCurve(branch2, line4);
		if (clipped!=null){
			SmoothCurve2D clippedCurve = clipped.getFirstCurve();
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.RED);
			g2.draw(clippedCurve.getAsPolyline(4));
		}
			
		// Draw the clipped hyperbola
		CurveSet2D<?> clipped2 = hyperbola.clip(box);
		if (!clipped2.isEmpty()){
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.BLUE);
			g2.draw(clipped2);
		}
		
		// Draw parabola origin
		Point2D p1 = hyperbola.getCenter();
		g2.fill(new Circle2D(p1, 4));
		
	}

	public final static void main(String[] args){
		System.out.println("should draw a hyperbola");
		
		JPanel panel = new DrawHyperbolaDemo();
		JFrame frame = new JFrame("Draw hyperbola demo");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
