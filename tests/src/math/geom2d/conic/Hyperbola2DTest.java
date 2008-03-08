package math.geom2d.conic;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
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
	
	public void testClipBox(){
		Hyperbola2D hyperbola = new Hyperbola2D(0, 0, 1, 1, 0, true);
		Box2D box = new Box2D(-2, 2, -2, 2);
		
		// test number of intersections
		CurveSet2D<?> clipped = hyperbola.clip(box);
		assertTrue(clipped.getCurveNumber()==2);
		
		// test class of curve portions
		Curve2D curve1 = clipped.getFirstCurve();
		assertTrue(curve1 instanceof HyperbolaBranchArc2D);
		Curve2D curve2 = clipped.getLastCurve();
		assertTrue(curve2 instanceof HyperbolaBranchArc2D);
		
		// extract curve arcs
		HyperbolaBranchArc2D arc1 = (HyperbolaBranchArc2D) curve1;
		HyperbolaBranchArc2D arc2 = (HyperbolaBranchArc2D) curve2;
		assertTrue(arc1.getHyperbolaBranch().getHyperbola().equals(hyperbola));
		assertTrue(arc2.getHyperbolaBranch().getHyperbola().equals(hyperbola));
		
		// Translate and scale the hypebola
		
		double x0 = 50, y0=50, a=10, b=10, theta=0;
		boolean direct = true;
		hyperbola = new Hyperbola2D(x0, y0, a, b, theta, direct);
		box = new Box2D(x0-2*a, x0+2*a, y0-2*b, y0+2*b);
		
		// test number of intersections
		clipped = hyperbola.clip(box);
		assertTrue(clipped.getCurveNumber()==2);
		
		// test class of curve portions
		curve1 = clipped.getFirstCurve();
		assertTrue(curve1 instanceof HyperbolaBranchArc2D);
		curve2 = clipped.getLastCurve();
		assertTrue(curve2 instanceof HyperbolaBranchArc2D);
		
		// extract curve arcs
		arc1 = (HyperbolaBranchArc2D) curve1;
		arc2 = (HyperbolaBranchArc2D) curve2;
		assertTrue(arc1.getHyperbolaBranch().getHyperbola().equals(hyperbola));
		assertTrue(arc2.getHyperbolaBranch().getHyperbola().equals(hyperbola));
	}
	
//	public void testGetPathIterator(){
//		Hyperbola2D hyperbola = new Hyperbola2D(0, 0, 1, 1, 0, true);
//		Box2D box = new Box2D(-2, 2, -2, 2);
//		
//		// test number of intersections
//		CurveSet2D<?> clipped = hyperbola.clip(box);
//		assertTrue(clipped.getCurveNumber()==2);
//		
//		java.awt.geom.PathIterator path = clipped.getPathIterator(
//				new java.awt.geom.AffineTransform());
//	}
}
