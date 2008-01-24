package math.geom2d.conic;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import junit.framework.TestCase;

public class HyperbolaBranch2DTest extends TestCase {

	public void testGetTangent() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertEquals(branch.getTangent(0), new Vector2D(0, 1));
	}

	public void testGetPointDouble() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertEquals(branch.getPoint(0), new Point2D(1, 0));
	}
	
	public void testContains() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertTrue(branch.contains(branch.getPoint(0)));
		assertTrue(branch.contains(branch.getPoint(10)));
		assertTrue(branch.contains(branch.getPoint(-10)));
	}


}
