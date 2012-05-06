package math.geom2d;

import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import junit.framework.TestCase;

public class ShapeArray2DTest extends TestCase {

	public void testShapeArray2DTArray() {
		Point2D p1 = new Point2D(30, 40);
		Circle2D c1 = new Circle2D(new Point2D(10, 20), 30);
		ShapeArray2D<Shape2D> set = ShapeArray2D.<Shape2D>create(p1, c1);
		
		assertEquals(2, set.shapes().size());
		
		set.add(new LineSegment2D(new Point2D(10, 20), new Point2D(30, 50)));
		assertEquals(3, set.shapes().size());

		set.remove(c1);
		assertEquals(2, set.shapes().size());
	}


}
