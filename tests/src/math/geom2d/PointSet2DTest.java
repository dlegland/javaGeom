package math.geom2d;

import junit.framework.TestCase;

public class PointSet2DTest extends TestCase {

	public void testGetDistancePoint2D() {
		PointSet2D set = new PointSet2D(new Point2D[]{
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
		PointSet2D set;
		
		set = new PointSet2D();
		assertTrue(set.isEmpty());
		
		set = new PointSet2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		assertTrue(!set.isEmpty());
		
		set.clearPoints();
		assertTrue(set.isEmpty());
	}

	public void testClip() {
		PointSet2D set = new PointSet2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		Box2D box = new Box2D(-5, 5, -5, 5);
		
		PointSet2D clipped = set.clip(box);
		assertTrue(clipped.getPointNumber()==1);
	}

	public void testGetBoundingBox() {
		PointSet2D set = new PointSet2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(10, 0),
				new Point2D(0, 10),
				new Point2D(10, 10)});
		Box2D box = new Box2D(0, 10, 0, 10);
		assertTrue(set.getBoundingBox().equals(box));
	}

	public void testContainsPoint2D() {
		PointSet2D set = new PointSet2D(new Point2D[]{
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
	    PointSet2D set = new PointSet2D(new Point2D[]{
	            new Point2D(0, 0), new Point2D(10, 20), new Point2D(30, 40)
	    });
	    assertTrue(set.equals(set.clone()));
	}
}
