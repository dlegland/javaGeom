package math.geom2d.polygon.convhull;

import math.geom2d.Point2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.polygon.Polygon2D;
import junit.framework.TestCase;

public class GrahamScan2DTest extends TestCase {

	public void testConvexHull_Lozenge() {
	    PointArray2D pointSet = new PointArray2D(new Point2D[]{
				new Point2D(1, 0),
				new Point2D(2, 1),
				new Point2D(1, 2),
				new Point2D(0, 1)});
		
		Polygon2D hull = new GrahamScan2D().convexHull(pointSet.points());
		
		assertTrue(hull!=null);
		assertTrue(hull.vertices().size()==4);
	}

	public void testConvexHull_Square() {
	    PointArray2D pointSet = new PointArray2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(1, 0),
				new Point2D(.2, .2),
				new Point2D(.7, .7),
				new Point2D(.2, .7),
				new Point2D(.7, .2),
				new Point2D(.5, .5),
				new Point2D(1, 1),
				new Point2D(0, 1)});
		
		Polygon2D hull = new GrahamScan2D().convexHull(pointSet.points());
		
		assertTrue(hull!=null);
		assertTrue(hull.vertices().size()==4);		
	}	
}
