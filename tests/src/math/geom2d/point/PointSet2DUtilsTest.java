package math.geom2d.point;

import math.geom2d.Point2D;
import junit.framework.TestCase;

public class PointSet2DUtilsTest extends TestCase {

	public void testHasMultipleVertices_True() {
		PointArray2D pointSet = new PointArray2D(new Point2D[] {
				new Point2D(10, 10), 
				new Point2D(20, 10), 
				new Point2D(20, 30), 
				new Point2D(10, 30), 
				new Point2D(10, 30), 
				new Point2D(10, 10)	});
		boolean res = PointSet2DUtils.hasAdjacentMultipleVertices(pointSet.points);
		assertTrue(res);
	}

	public void testHasMultipleVertices_False() {
		PointArray2D pointSet = new PointArray2D(new Point2D[] {
				new Point2D(10, 10), 
				new Point2D(20, 10), 
				new Point2D(20, 30), 
				new Point2D(10, 30), 
				new Point2D(10, 10)	});
		boolean res = PointSet2DUtils.hasAdjacentMultipleVertices(pointSet.points);
		assertFalse(res);
	}

	public void testCountMultipleVertices() {
		PointArray2D pointSet = new PointArray2D(new Point2D[] {
				new Point2D(10, 10), 
				new Point2D(20, 10), 
				new Point2D(20, 30), 
				new Point2D(10, 30), // one multiple
				new Point2D(10, 30), 
				new Point2D(10, 10)	});
		int count;
		
		count = PointSet2DUtils.countAdjacentMultipleVertices(pointSet.points);
		assertEquals(1, count);
		
		count = PointSet2DUtils.countAdjacentMultipleVertices(pointSet.points, false);
		assertEquals(1, count);
		
		count = PointSet2DUtils.countAdjacentMultipleVertices(pointSet.points, true);
		assertEquals(2, count);
	}
}
