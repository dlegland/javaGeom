/**
 * File: 	BoundaryPolyCurve2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 22 nov. 08
 */
package math.geom2d.domain;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;

/**
 * @author dlegland
 *
 */
public class BoundaryPolyCurve2DTest extends TestCase {

	public void testTransform_Motion() {
		Point2D p1 = new Point2D(100, 100);
		Point2D p2 = new Point2D(300, 100);
		Point2D p3 = new Point2D(200, 270);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		LineSegment2D seg3 = new LineSegment2D(p3, p1);
		Boundary2D boundary = BoundaryPolyCurve2D.create(
				new LineSegment2D[]{seg1, seg2, seg3});
		Domain2D domain = new GenericDomain2D(boundary);
		
		AffineTransform2D trans = 
			AffineTransform2D.createRotation(new Point2D(0, 0), .1);
		
		Boundary2D boundary2 = boundary.transform(trans);
		
		assertFalse(boundary2.isEmpty());
		assertTrue(boundary2.isBounded());
		
		// Create a domain from this boundary to check if the domain is bounded
		Domain2D domain2 = domain.transform(trans);
		assertFalse(domain2.isEmpty());
		assertTrue(domain2.isBounded());
	}
	
	public void testTransform_Reflection() {
		Point2D p1 = new Point2D(100, 100);
		Point2D p2 = new Point2D(300, 100);
		Point2D p3 = new Point2D(200, 270);
		
		LineSegment2D seg1 = new LineSegment2D(p1, p2);
		LineSegment2D seg2 = new LineSegment2D(p2, p3);
		LineSegment2D seg3 = new LineSegment2D(p3, p1);
		Boundary2D boundary = BoundaryPolyCurve2D.create(
				new LineSegment2D[]{seg1, seg2, seg3});
		Domain2D domain = new GenericDomain2D(boundary);
		
		StraightLine2D line = new StraightLine2D(p2, p3);
		AffineTransform2D trans =
			AffineTransform2D.createLineReflection(line);
		
		Boundary2D boundary2 = boundary.transform(trans);
		
		assertFalse(boundary2.isEmpty());
		assertTrue(boundary2.isBounded());
		
		Domain2D domain2 = domain.transform(trans);
		assertFalse(domain2.isEmpty());
		assertTrue(domain2.isBounded());
	}
	
	/*
	 * Test method for 'math.geom2d.PolyCurve2D.getSubCurve(double, double)'
	 */
	public void testIsInside_Wedge() {
		Point2D point = Point2D.create(20, 10);
		Vector2D v1 = Vector2D.create(-2, 0);
		Vector2D v2 = Vector2D.create(0, 3);
		BoundaryPolyCurve2D<AbstractLine2D> curve = 
			BoundaryPolyCurve2D.create(new AbstractLine2D[]{
					InvertedRay2D.create(point, v1),
					Ray2D.create(point, v2)
		});
		
		Point2D po1 = Point2D.create(30, 15);
		assertFalse(curve.isInside(po1));
		Point2D po2 = Point2D.create(25, 20);
		assertFalse(curve.isInside(po2));
		
		Point2D pi1 = Point2D.create(30, 5);
		assertTrue(curve.isInside(pi1));
		Point2D pi2 = Point2D.create(20, 5);
		assertTrue(curve.isInside(pi2));
		Point2D pi3 = Point2D.create(15, 10);
		assertTrue(curve.isInside(pi3));
		Point2D pi4 = Point2D.create(15, 20);
		assertTrue(curve.isInside(pi4));
	}

	/**
	 * Test method for {@link math.geom2d.domain.PolyOrientedCurve2D#distanceSigned(double, double)}.
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
			new BoundaryPolyCurve2D<LineSegment2D>(line1, line2, line3, line4);
		
		assertTrue(boundary.isInside(pt0));
		assertTrue(!boundary.isInside(pt1));
		assertTrue(!boundary.isInside(pt2));
		assertTrue(!boundary.isInside(pt3));
		
		// Try on the inverse curve
		Boundary2D reverse = boundary.reverse();
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
		Boundary2D reverse2 = boundary.reverse();
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
