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
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class PolyCirculinearCurve2DTest extends TestCase {

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
			new PolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
				new ContinuousCirculinearCurve2D[]{line1, line2, line3});
		
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
			new PolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
				new ContinuousCirculinearCurve2D[]{line1, line2, line3});
		
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
			new PolyCirculinearCurve2D<ContinuousCirculinearCurve2D>(
				new ContinuousCirculinearCurve2D[]{line1, line2, line3});
		
		assertEquals(curve.getPosition(0), 0, 1e-14);
		assertEquals(curve.getPosition(5), .5, 1e-14);
		assertEquals(curve.getPosition(10), 1, 1e-14);
		assertEquals(curve.getPosition(15), 2.5, 1e-14);
		assertEquals(curve.getPosition(20), 3, 1e-14);
		assertEquals(curve.getPosition(25), 4.5, 1e-14);
		assertEquals(curve.getPosition(30), 5, 1e-14);
	}

}
