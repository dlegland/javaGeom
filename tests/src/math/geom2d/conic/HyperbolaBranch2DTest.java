package math.geom2d.conic;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.Curve2DUtils;
import math.geom2d.line.StraightLine2D;
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
		
		point = branch.getPoint(0);
		pos = branch.getPosition(point);
		assertEquals(pos, 0, 1e-14);
		point = branch.getPoint(pos);
		
		point = branch.getPoint(-1);
		pos = branch.getPosition(point);
		assertEquals(pos, -1, 1e-14);
		
		point = branch.getPoint(-10);
		assertEquals(branch.getPosition(point), -10, 1e-7);
	}
	
	public void testClipLine(){
		double x0 	= 50;
		double y0 	= 50;
		double a  	= 10;
		double b 	= 10;

		Hyperbola2D hyper = new Hyperbola2D(x0, y0, a, b, 0, true);
		HyperbolaBranch2D branch = new HyperbolaBranch2D(hyper, true);
		
		StraightLine2D line = new StraightLine2D(x0+2*a, y0, 0, 1);
		
		CurveSet2D<?> clipped = Curve2DUtils.clipSmoothCurve(branch, line);
		
		assertTrue(clipped.getCurveNumber()==1);
		Curve2D curve = clipped.getFirstCurve();
		
		Point2D p1 = new Point2D(x0+2*a, y0-b*Math.sqrt(3));
		assertTrue(curve.getFirstPoint().equals(p1));
		
		Point2D p2 = new Point2D(x0+2*a, y0+b*Math.sqrt(3));
		assertTrue(curve.getLastPoint().equals(p2));
		
		
		// shifted and scaled hyperbola
		x0 	= 50;	y0 	= 50; a = 10;	b 	= 10;
		hyper =  new Hyperbola2D(x0, y0, a, b, 0, true);
		branch = new HyperbolaBranch2D(hyper, true);
		line = new StraightLine2D(x0+2*a, y0, 0, 1);
		
		clipped = Curve2DUtils.clipSmoothCurve(branch, line);
		assertTrue(clipped.getCurveNumber()==1);

		curve = clipped.getFirstCurve();
		
		p1 = new Point2D(x0+2*a, y0-b*Math.sqrt(3));
		assertTrue(curve.getFirstPoint().equals(p1));
		
		p2 = new Point2D(x0+2*a, y0+b*Math.sqrt(3));
		assertTrue(curve.getLastPoint().equals(p2));

	}

}
