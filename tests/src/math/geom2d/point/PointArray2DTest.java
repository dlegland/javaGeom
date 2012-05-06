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
		
		Domain2D buffer = set.buffer(5);
		Boundary2D boundary = buffer.boundary();
		assertEquals(3, boundary.continuousCurves().size());
	}
	
	public void testGetBuffer_Merge() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(10, 0);
		Point2D p3 = new Point2D(20, 0);
		PointSet2D set = PointArray2D.create(new Point2D[]{p1, p2, p3});
		
		Domain2D buffer = set.buffer(10);
		Boundary2D boundary = buffer.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}
	
	/**
	 * Test for critical case: 3 contours touching at the same point.
	 */
	public void testGetBuffer_Touch() {
		Point2D p1 = new Point2D(0, 0);
		Point2D p2 = new Point2D(20, 0);
		Point2D p3 = new Point2D(10, 20);
		PointSet2D set = PointArray2D.create(new Point2D[]{p1, p2, p3});
		
		Domain2D buffer = set.buffer(10);
		Boundary2D boundary = buffer.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}
	
	public void testGetDistancePoint2D() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		
		// test distance with points contained in the set
		assertEquals(set.distance(new Point2D(0, 0)), 0, 1e-14);
		assertEquals(set.distance(new Point2D(10, 0)), 0, 1e-14);
		assertEquals(set.distance(new Point2D(0, 10)), 0, 1e-14);
		assertEquals(set.distance(new Point2D(10, 10)), 0, 1e-14);
		
		// test distance with points outside of the set
		assertEquals(set.distance(new Point2D(20, 0)), 10, 1e-14);
		assertEquals(set.distance(new Point2D(0, 20)), 10, 1e-14);
		assertEquals(set.distance(new Point2D(20, 20)), 10*Math.sqrt(2), 1e-14);
		assertEquals(set.distance(new Point2D(5, 5)), 5*Math.sqrt(2), 1e-14);
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
		
		set.clear();
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
		assertTrue(clipped.size()==1);
	}

	public void testGetBoundingBox() {
		PointArray2D set = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		Box2D box = new Box2D(0, 10, 0, 10);
		assertTrue(set.boundingBox().equals(box));
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

	public void testEquals() {
		Point2D p1 = Point2D.create(1, 2);
		Point2D p1b = Point2D.create(1, 2);
		Point2D p2 = Point2D.create(1, 3);
		Point2D p3 = Point2D.create(2, 3);
		
	    PointArray2D set1 = PointArray2D.create(new Point2D[]{p1, p2});
	    PointArray2D set2 = PointArray2D.create(new Point2D[]{p1b, p2});
	    assertTrue(set1.equals(set2));
	    assertTrue(set2.equals(set1));
	    
	    PointArray2D set3 = new PointArray2D(new Point2D[]{p1, p3});
	    assertFalse(set1.equals(set3));
	    assertFalse(set3.equals(set1));
	}

	public void testClone() {
	    PointArray2D set = new PointArray2D(new Point2D[]{
	            new Point2D(0, 0), new Point2D(10, 20), new Point2D(30, 40)
	    });
	    assertTrue(set.equals(set.clone()));
	}
}
