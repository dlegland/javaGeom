/**
 * File: 	PolyCirculinearCurve2DTest.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 30 juin 09
 */
package math.geom2d.circulinear;


import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class PolyCirculinearCurve2DTest extends TestCase {

	/*
	 * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#getParallel(double)}
	 */
	public void testGetParallel_Wedge() {
		Point2D point = Point2D.create(20, 10);
		Vector2D v1 = Vector2D.create(-2, 0);
		Vector2D v2 = Vector2D.create(0, 3);
		AbstractLine2D[] array = new AbstractLine2D[]{
				InvertedRay2D.create(point, v1),
				Ray2D.create(point, v2)
		};
		PolyCirculinearCurve2D<AbstractLine2D> curve = 
			new PolyCirculinearCurve2D<AbstractLine2D>(array);
		
		double dist = 5;
		
		CirculinearContinuousCurve2D parallel1 = curve.getParallel(dist);
		assertEquals(3, parallel1.getSmoothPieces().size());
		CirculinearContinuousCurve2D parallel2 = curve.getParallel(-dist);
		assertEquals(3, parallel2.getSmoothPieces().size());
	}
	
	/**
	 * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#getLength()}.
	 */
	public void testGetLength() {
		LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), 
				new Point2D(10, 0));
		Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
		LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), 
				new Point2D(20, 10));
		PolyCirculinearCurve2D<?> curve = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
				new CirculinearContinuousCurve2D[]{line1, line2, line3});
		
		assertEquals(curve.getLength(), 30, 1e-14);
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#getLength(double)}.
	 */
	public void testGetLengthDouble() {
		LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), 
				new Point2D(10, 0));
		Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
		LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), 
				new Point2D(20, 10));
		PolyCirculinearCurve2D<?> curve = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
				new CirculinearContinuousCurve2D[]{line1, line2, line3});
		
		assertEquals(curve.getLength(1), 10, 1e-14);
		assertEquals(curve.getLength(2), 10, 1e-14);
		assertEquals(curve.getLength(3), 20, 1e-14);
		assertEquals(curve.getLength(4), 20, 1e-14);
		assertEquals(curve.getLength(5), 30, 1e-14);
		assertEquals(curve.getLength(0), 0, 1e-14);
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#getPosition(double)}.
	 */
	public void testGetPositionDouble() {
		LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), 
				new Point2D(10, 0));
		Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
		LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), 
				new Point2D(20, 10));
		PolyCirculinearCurve2D<?> curve = 
			new PolyCirculinearCurve2D<CirculinearContinuousCurve2D>(
				new CirculinearContinuousCurve2D[]{line1, line2, line3});
		
		assertEquals(curve.getPosition(0), 0, 1e-14);
		assertEquals(curve.getPosition(5), .5, 1e-14);
		assertEquals(curve.getPosition(10), 1, 1e-14);
		assertEquals(curve.getPosition(15), 2.5, 1e-14);
		assertEquals(curve.getPosition(20), 3, 1e-14);
		assertEquals(curve.getPosition(25), 4.5, 1e-14);
		assertEquals(curve.getPosition(30), 5, 1e-14);
	}

}
