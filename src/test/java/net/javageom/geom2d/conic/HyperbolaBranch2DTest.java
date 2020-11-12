package net.javageom.geom2d.conic;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.curve.Curve2D;
import net.javageom.geom2d.curve.CurveSet2D;
import net.javageom.geom2d.curve.Curves2D;
import net.javageom.geom2d.line.StraightLine2D;

public class HyperbolaBranch2DTest extends TestCase {

	public void testGetTangent() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertEquals(branch.tangent(0), new Vector2D(0, 1));
	}

	public void testGetPointDouble() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertEquals(branch.point(0), new Point2D(1, 0));
	}
	
	public void testContains() {
		Hyperbola2D hyper = new Hyperbola2D();
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		assertTrue(branch.contains(branch.point(0)));
		assertTrue(branch.contains(branch.point(10)));
		assertTrue(branch.contains(branch.point(-10)));
	}

	public void testSinh(){
		double t = 0;
		
		double y = Math.sinh(t);
		double t2= Math.log(y + Math.hypot(y, 1));
		
		assertEquals(t, t2, 1e-14);
	}
	
	public void testGetPositionPoint() {
		Hyperbola2D hyper = new Hyperbola2D(0, 0, 1, 1, 0, true);
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		
		Point2D point;
		double pos;
		
		point = branch.point(0);
		pos = branch.position(point);
		assertEquals(pos, 0, 1e-14);
		point = branch.point(pos);
		
		point = branch.point(-1);
		pos = branch.position(point);
		assertEquals(pos, -1, 1e-14);
		
		point = branch.point(-10);
		assertEquals(branch.position(point), -10, 1e-7);
	}
	
	public void testClipLine(){
		double x0 	= 50;
		double y0 	= 50;
		double a  	= 10;
		double b 	= 10;
		double eps = Shape2D.ACCURACY;
		
		Hyperbola2D hyper = new Hyperbola2D(x0, y0, a, b, 0, true);
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		
		StraightLine2D line = new StraightLine2D(x0+2*a, y0, 0, 1);
		
		CurveSet2D<?> clipped = Curves2D.clipSmoothCurve(branch, line);
		
		assertTrue(clipped.size()==1);
		Curve2D curve = clipped.firstCurve();
		
		Point2D p1 = new Point2D(x0+2*a, y0-b*Math.sqrt(3));
		assertTrue(curve.firstPoint().almostEquals(p1, eps));
		
		Point2D p2 = new Point2D(x0+2*a, y0+b*Math.sqrt(3));
		assertTrue(curve.lastPoint().almostEquals(p2, eps));
		
		
		// shifted and scaled hyperbola
		x0 	= 50;	y0 	= 50; a = 10;	b 	= 10;
		hyper =  new Hyperbola2D(x0, y0, a, b, 0, true);
		branch = new HyperbolaBranch2D(hyper, true);
		line = new StraightLine2D(x0+2*a, y0, 0, 1);
		
		clipped = Curves2D.clipSmoothCurve(branch, line);
		assertTrue(clipped.size()==1);

		curve = clipped.firstCurve();
		
		p1 = new Point2D(x0+2*a, y0-b*Math.sqrt(3));
		assertTrue(curve.firstPoint().almostEquals(p1, eps));
		
		p2 = new Point2D(x0+2*a, y0+b*Math.sqrt(3));
		assertTrue(curve.lastPoint().almostEquals(p2, eps));

	}

}
