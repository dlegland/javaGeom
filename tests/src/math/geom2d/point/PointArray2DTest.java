package math.geom2d.point;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;

public class PointArray2DTest extends TestCase {

	public void testGetBuffer_Disjoint() {
		Point2D p1 = new Point2D(10, 20);
		Point2D p2 = new Point2D(20, 10);
		Point2D p3 = new Point2D(30, 20);
		PointSet2D set = PointArray2D.create(new Point2D[]{p1, p2, p3});
		
		Domain2D buffer = set.getBuffer(5);
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(3, boundary.getBoundaryCurves().size());
	}
	
	public void testGetBuffer_Merge() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(10, 0);
		Point2D p3 = new Point2D(20, 0);
		PointSet2D set = PointArray2D.create(new Point2D[]{p1, p2, p3});
		
		Domain2D buffer = set.getBuffer(10);
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(1, boundary.getBoundaryCurves().size());
	}
	
	/**
	 * Test for critical case: 3 contours touching at the same point.
	 */
	public void testGetBuffer_Touch() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(20, 0);
		Point2D p3 = new Point2D(10, 20);
		PointSet2D set = PointArray2D.create(new Point2D[]{p1, p2, p3});
		
		Domain2D buffer = set.getBuffer(10);
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(1, boundary.getBoundaryCurves().size());
	}
	
	public void testGetDistancePoint2D() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		
		// test distance with points contained in the set
		assertEquals(set.getDistance(new Point2D(0, 0)), 0, 1e-14);
		assertEquals(set.getDistance(new Point2D(10, 0)), 0, 1e-14);
		assertEquals(set.getDistance(new Point2D(0, 10)), 0, 1e-14);
		assertEquals(set.getDistance(new Point2D(10, 10)), 0, 1e-14);
		
		// test distance with points outside of the set
		assertEquals(set.getDistance(new Point2D(20, 0)), 10, 1e-14);
		assertEquals(set.getDistance(new Point2D(0, 20)), 10, 1e-14);
		assertEquals(set.getDistance(new Point2D(20, 20)), 10*Math.sqrt(2), 1e-14);
		assertEquals(set.getDistance(new Point2D(5, 5)), 5*Math.sqrt(2), 1e-14);
	}

	public void testIsEmpty() {
		PointArray2D set;
		
		set = new PointArray2D();
		assertTrue(set.isEmpty());
		
		set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		assertTrue(!set.isEmpty());
		
		set.clearPoints();
		assertTrue(set.isEmpty());
	}

	public void testClip() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		Box2D box = new Box2D(-5, 5, -5, 5);
		
		PointArray2D clipped = set.clip(box);
		assertTrue(clipped.getPointNumber()==1);
	}

	public void testGetBoundingBox() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		Box2D box = new Box2D(0, 10, 0, 10);
		assertTrue(set.getBoundingBox().equals(box));
	}

	public void testContainsPoint2D() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		
		assertTrue(set.contains(new Point2D(0, 0)));
		assertTrue(set.contains(new Point2D(10, 0)));
		assertTrue(set.contains(new Point2D(0, 10)));
		assertTrue(set.contains(new Point2D(10, 10)));
		
		assertTrue(!set.contains(new Point2D(5, 5)));
		assertTrue(!set.contains(new Point2D(20, 0)));
		assertTrue(!set.contains(new Point2D(0, 20)));
		assertTrue(!set.contains(new Point2D(20, 20)));
	}

	public void testClone() {
	    PointArray2D set = new PointArray2D(new Point2D[]{
	            new Point2D(0, 0), new Point2D(10, 20), new Point2D(30, 40)
	    });
	    assertTrue(set.equals(set.clone()));
	}
}
