/**
 * File: 	BoundaryPolyCurve2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 nov. 08
 */
package math.geom2d.domain;

import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.LineSegment2D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class BoundaryPolyCurve2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.domain.PolyOrientedCurve2D#getSignedDistance(double, double)}.
	 * Test the method getSignedDistance. Use a clinical case based on line
	 * segments. The test point is outside the curve, but inside the domain
	 * bounded by the 'closest' curve. Actually, 2 curves are closest.
	 */
	public void testIsInsidePoint2D() {
		// vertices of the triangle
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(2, 1);
		Point2D p3 = new Point2D(3, 3);
		Point2D p4 = new Point2D(1, 2);

		// the test points
		Point2D pt0 = new Point2D(1, 1);
		Point2D pt1 = new Point2D(-1, -4);
		Point2D pt2 = new Point2D(-4, -1);
		Point2D pt3 = new Point2D(-3, -3);

		LineSegment2D line1 = new LineSegment2D(p4, p1);
		LineSegment2D line2 = new LineSegment2D(p1, p2);
		LineSegment2D line3 = new LineSegment2D(p2, p3);
		LineSegment2D line4 = new LineSegment2D(p3, p4);
		BoundaryPolyCurve2D<LineSegment2D> boundary = 
			new BoundaryPolyCurve2D<LineSegment2D>(new LineSegment2D[]{
					line1, line2, line3, line4
			});
		
		assertTrue(boundary.isInside(pt0));
		assertTrue(!boundary.isInside(pt1));
		assertTrue(!boundary.isInside(pt2));
		assertTrue(!boundary.isInside(pt3));
		
		// Try on the inverse curve
		Boundary2D reverse = boundary.getReverseCurve();
		assertTrue(!reverse.isInside(pt0));
		assertTrue(reverse.isInside(pt1));
		assertTrue(reverse.isInside(pt2));
		assertTrue(reverse.isInside(pt3));
		
		// Try with a different order of curves
		BoundaryPolyCurve2D<LineSegment2D> boundary2 = 
			new BoundaryPolyCurve2D<LineSegment2D>(new LineSegment2D[]{
					line2, line3, line4, line1
			});
		
		assertTrue(boundary2.isInside(pt0));
		assertTrue(!boundary2.isInside(pt1));
		assertTrue(!boundary2.isInside(pt2));
		assertTrue(!boundary2.isInside(pt3));
		
		// Try on the inverse curve
		Boundary2D reverse2 = boundary.getReverseCurve();
		assertTrue(!reverse2.isInside(pt0));
		assertTrue(reverse2.isInside(pt1));
		assertTrue(reverse2.isInside(pt2));
		assertTrue(reverse2.isInside(pt3));
		
	}
	
	public void testIsInsidePoint2D_CircleArc2D() {
		double x1 = -20;
		double x2 = 20;
		double y = 0;
		double r = 40;
		
		CircleArc2D arc1 = new CircleArc2D(x1, y, r, Math.PI/3, 4*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(x2, y, r, 4*Math.PI/3, -2*Math.PI/3);
		
		BoundaryPolyCurve2D<CircleArc2D> boundary = 
			new BoundaryPolyCurve2D<CircleArc2D>(new CircleArc2D[]{
					arc1, arc2});
		
		Point2D pt1 = new Point2D(10, 10);
		assertTrue(!boundary.isInside(pt1));
		Point2D pt2 = new Point2D(20, 30);
		assertTrue(!boundary.isInside(pt2));
		Point2D pt3 = new Point2D(-10, 10);
		assertTrue(!boundary.isInside(pt3));
		Point2D pt4 = new Point2D(0, 20);
		assertTrue(!boundary.isInside(pt4));
		Point2D pt5 = new Point2D(-20, 30);
		assertTrue(boundary.isInside(pt5));
	}

}
