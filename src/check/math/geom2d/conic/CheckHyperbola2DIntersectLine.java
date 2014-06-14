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
import math.geom2d.conic.Hyperbola2D;
import math.geom2d.conic.HyperbolaBranch2D;
import math.geom2d.conic.HyperbolaBranchArc2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Boundaries2D;
import math.geom2d.line.StraightLine2D;


public class CheckHyperbola2DIntersectLine extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Hyperbola2D hyperbola = null;
	Box2D box = null;
	
	public CheckHyperbola2DIntersectLine() {
		super();
		
		double x0 = 150;
		double y0 = 150;
		double a  = 50;
		double b  = 30;
		double theta = Math.PI/6;
		//double theta = 0;
		hyperbola = new Hyperbola2D(x0, y0, a, b, theta, true);
		
		box = new Box2D(50, 250, 50, 250);
	
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
//		// Draw the asymptotes
//		StraightLine2D asymp1 = new StraightLine2D(150, 150, 1, 1);
//		StraightLine2D asymp2 = new StraightLine2D(150, 150, -1, 1);
//		g2.setColor(Color.GREEN);
//		g2.draw(asymp1.clip(box));
//		g2.draw(asymp2.clip(box));
		
		// isolate first branch and an arc
		double tmax = 1.5;
		HyperbolaBranch2D branch1 = hyperbola.firstCurve();
		HyperbolaBranchArc2D arc1 = new HyperbolaBranchArc2D(branch1, -tmax, tmax);
	
		// isolate second branch and an arc
		HyperbolaBranch2D branch2 = hyperbola.lastCurve();
		//HyperbolaBranchArc2D arc2 = new HyperbolaBranchArc2D(branch2, -tmax, tmax);

		g2.setColor(Color.BLUE);
		//g2.fill(Boundary2DUtil.clipBoundary(branch1, box));
		//g2.fill(Boundary2DUtil.clipBoundary(branch2, box));
		Boundary2D clippedBoundary =
			Boundaries2D.clipBoundary(hyperbola, box);
		//boolean b1 = clippedBoundary.contains(new Point2D(50, 50));
//		g2.setColor(Color.BLUE);
//		g2.draw(clippedBoundary);
		g2.setColor(Color.CYAN);
		clippedBoundary.domain().fill(g2);
		
		// Draw the arcs
		g2.setColor(Color.BLACK);
		arc1.draw(g2);
		arc1.draw(g2);
		
		// Get focal points
		Point2D focus1 = hyperbola.getFocus1();
		Point2D focus2 = hyperbola.getFocus2();
		
		// Draw focal points
		g2.setColor(Color.BLUE);
		focus1.draw(g2, 4);
		focus2.draw(g2, 4);
		
		// Get intersections with some lines
		StraightLine2D line1 = new StraightLine2D(50, 50, 10, 0);
		Collection<Point2D> points1 = hyperbola.intersections(line1);
		StraightLine2D line2 = new StraightLine2D(50, 50, 0, 10);
		Collection<Point2D> points2 = hyperbola.intersections(line2);
		StraightLine2D line3 = new StraightLine2D(50, 250, 10, 0);
		Collection<Point2D> points3 = hyperbola.intersections(line3);
		StraightLine2D line4 = new StraightLine2D(250, 250, 0, 10);
		Collection<Point2D> points4 = hyperbola.intersections(line4);
		
		// Draw point sets
		g2.setColor(Color.RED);
		for(Point2D point : points1)
			point.draw(g2, 2);
		for(Point2D point : points2)
			point.draw(g2, 2);
		for(Point2D point : points3)
			point.draw(g2, 2);
		for(Point2D point : points4)
			point.draw(g2, 2);
		
		
		// Compute intersections of branches with lines
		points2 = hyperbola.intersections(line2);
		points4 = hyperbola.intersections(line4);
		
		for(Point2D point : points2)
			point.draw(g2, 4);
		for(Point2D point : points4)
			point.draw(g2, 4);

		
		// The clipping of first branch
		CurveSet2D<SmoothCurve2D> clipped = 
			Curves2D.clipSmoothCurve(branch2, line4);
		if (clipped!=null){
			SmoothCurve2D clippedCurve = clipped.firstCurve();
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.RED);
			clippedCurve.asPolyline(4).draw(g2);
		}
				
//		// Draw the clipped hyperbola
//		g2.setStroke(new BasicStroke(1.0f));
//		g2.setColor(Color.BLUE);
//		CurveSet2D<?> clipped2 = hyperbola.clip(box);
//		if (!clipped2.isEmpty()){
//			g2.draw(clipped2);
//		}
		
		// The clipping of first branch
		clipped = Curves2D.clipSmoothCurve(branch2, line4);
		if (clipped!=null){
			SmoothCurve2D clippedCurve = clipped.firstCurve();
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.RED);
			clippedCurve.asPolyline(4).draw(g2);
		}
			
		// Draw parabola origin
		Point2D p1 = hyperbola.getCenter();
		p1.draw(g2, 4);
		
		// Draw the bounding box
		g2.setStroke(new BasicStroke(1.0f));
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a hyperbola");
		
		JPanel panel = new CheckHyperbola2DIntersectLine();
		JFrame frame = new JFrame("Draw hyperbola demo");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
	}
}
