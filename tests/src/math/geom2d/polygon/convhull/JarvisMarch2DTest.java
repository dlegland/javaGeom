package math.geom2d.polygon.convhull;

import math.geom2d.Point2D;
import math.geom2d.PointSet2D;
import math.geom2d.polygon.Polygon2D;
import junit.framework.TestCase;

public class JarvisMarch2DTest extends TestCase {

	public void testConvexHull_Lozenge() {
		PointSet2D pointSet = new PointSet2D(new Point2D[]{
				new Point2D(1, 0),
				new Point2D(2, 1),
				new Point2D(1, 2),
				new Point2D(0, 1)});
		
		Polygon2D hull = new JarvisMarch2D().convexHull(pointSet.getPoints());
		
		assertTrue(hull!=null);
		assertTrue(hull instanceof Polygon2D);
		assertTrue(hull.getVertices().size()==4);
	}

	public void testConvexHull_Square() {
		PointSet2D pointSet = new PointSet2D(new Point2D[]{
				new Point2D(0, 0),
				new Point2D(1, 0),
				new Point2D(.2, .2),
				new Point2D(.7, .7),
				new Point2D(.2, .7),
				new Point2D(.7, .2),
				new Point2D(.5, .5),
				new Point2D(1, 1),
				new Point2D(0, 1)});
		
		Polygon2D hull = new JarvisMarch2D().convexHull(pointSet.getPoints());
		
		assertTrue(hull!=null);
		assertTrue(hull instanceof Polygon2D);
		assertTrue(hull.getVertices().size()==4);		
	}	
}
