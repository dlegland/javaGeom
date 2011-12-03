package math.geom2d.circulinear;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.transform.CircleInversion2D;

public class CirculinearContourArray2DTest extends TestCase {

	public void testGetBuffer() {
		Circle2D circle1 = new Circle2D(new Point2D(0, 100), 30);
		Circle2D circle2 = new Circle2D(new Point2D(100, 100), 30);

		CirculinearContourArray2D<Circle2D> array =
			CirculinearContourArray2D.create(new Circle2D[]{circle1, circle2});
		
		CirculinearDomain2D buffer = array.getBuffer(10);
		assertFalse(buffer.isEmpty());
		
		CirculinearBoundary2D boundary = buffer.getBoundary();
		assertEquals(4, boundary.getContinuousCurves().size());
		for (CirculinearContour2D contour : boundary.getContinuousCurves())
			assertTrue(contour instanceof Circle2D);
	}

	public void testTransform_CircleInversion2D() {
		Circle2D circle1 = new Circle2D(new Point2D(0, 100), 30);
		Circle2D circle2 = new Circle2D(new Point2D(100, 100), 30);

		CirculinearContourArray2D<Circle2D> array =
			CirculinearContourArray2D.create(new Circle2D[]{circle1, circle2});

		CircleInversion2D inv = new CircleInversion2D(new Point2D(40, 30), 50);
		CirculinearContourArray2D<? extends CirculinearContour2D> res =
			array.transform(inv);
		
		assertFalse(res.isEmpty());
		
		assertEquals(2, res.getContinuousCurves().size());
		for (CirculinearContour2D contour : res)
			assertTrue(contour instanceof Circle2D);
	}
	
	public void testClip_Box2D() {
		Circle2D circle1 = new Circle2D(new Point2D(50, 40), 30);
		Circle2D circle2 = new Circle2D(new Point2D(150, 100), 30);

		CirculinearContourArray2D<Circle2D> array =
			CirculinearContourArray2D.create(new Circle2D[]{circle1, circle2});

		Box2D box = new Box2D(0, 100, 0, 100);
		
		CirculinearCurveSet2D<? extends CirculinearCurve2D> res = array.clip(box);
		
		assertEquals(1, res.getContinuousCurves().size());
		for (CirculinearCurve2D curve : res)
			assertTrue(curve instanceof Circle2D);
	}

}
