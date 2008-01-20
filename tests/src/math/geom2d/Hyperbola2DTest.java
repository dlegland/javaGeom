package math.geom2d;

import junit.framework.TestCase;

public class Hyperbola2DTest extends TestCase {

	public void testGetCenter() {
		Hyperbola2D hyper = new Hyperbola2D();
		assertEquals(hyper.getCenter(), new Point2D(0, 0));
	}

	public void testGetFocus1() {
		Hyperbola2D hyper = new Hyperbola2D();
		assertEquals(hyper.getFocus1(), new Point2D(Math.sqrt(2), 0));
	}

	public void testGetFocus2() {
		Hyperbola2D hyper = new Hyperbola2D();
		assertEquals(hyper.getFocus2(), new Point2D(-Math.sqrt(2), 0));
	}
	
}
