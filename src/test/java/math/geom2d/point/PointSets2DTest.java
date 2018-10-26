package math.geom2d.point;

import junit.framework.TestCase;

public class PointSets2DTest extends TestCase {

    public void testHasMultipleVertices_True() {
        PointArray2D pointSet = new PointArray2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 30), new Point2D(10, 30), new Point2D(10, 30), new Point2D(10, 10) });
        boolean res = PointSets2DUtil.hasMultipleVertices(pointSet.points);
        assertTrue(res);
    }

    public void testHasMultipleVertices_False() {
        PointArray2D pointSet = new PointArray2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 30), new Point2D(10, 30), new Point2D(10, 10) });
        boolean res = PointSets2DUtil.hasMultipleVertices(pointSet.points);
        assertFalse(res);
    }

    public void testCountMultipleVertices() {
        PointArray2D pointSet = new PointArray2D(new Point2D[] { new Point2D(10, 10), new Point2D(20, 10), new Point2D(20, 30), new Point2D(10, 30), // one multiple
                new Point2D(10, 30), new Point2D(10, 10) });
        int count;

        count = PointSets2DUtil.countMultipleVertices(pointSet.points);
        assertEquals(1, count);

        count = PointSets2DUtil.countMultipleVertices(pointSet.points, false);
        assertEquals(1, count);

        count = PointSets2DUtil.countMultipleVertices(pointSet.points, true);
        assertEquals(2, count);
    }
}
