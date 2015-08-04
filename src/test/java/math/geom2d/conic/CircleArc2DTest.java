/* File CircleArc2DTest.java 
 *
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * author : Legland
 * Created on 17 sept. 2004
 */

package math.geom2d.conic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.CircleInversion2D;

import static java.lang.Math.PI;


/**
 * @author Legland
 */
public class CircleArc2DTest extends TestCase {

	private void assertVectorEquals(Vector2D v1, Vector2D v2, double eps) {
		assertTrue(v1.almostEquals(v2, eps));
	}
	
	/**
	 * Constructor for CircleArc2DTest.
	 * @param arg0
	 */
	public CircleArc2DTest(String arg0) {
		super(arg0);
	}

	/**
	 * Create circle arc with various constructors, and check
	 * whether they are the same.
	 */
	public void testEquals() {
		// direct circle
		CircleArc2D arc0 = new CircleArc2D();
		Circle2D circle = new Circle2D(0, 0, 1, true);
		CircleArc2D arc1 = new CircleArc2D(circle, 0, PI/2);
		CircleArc2D arc2 = new CircleArc2D(circle, 0, PI/2, true);
		CircleArc2D arc3 = new CircleArc2D(0, 0, 1, 0, PI/2);
		CircleArc2D arc4 = new CircleArc2D(0, 0, 1, 0, PI/2, true);
		assertTrue(arc1.equals(arc0));
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));

		// direct circle, with different angles
		circle = new Circle2D(0, 0, 1, true);
		arc1 = new CircleArc2D(circle, PI/2, PI/2);
		arc2 = new CircleArc2D(circle, PI/2, PI, true);
		arc3 = new CircleArc2D(0, 0, 1, PI/2, PI/2);
		arc4 = new CircleArc2D(0, 0, 1, PI/2, PI, true);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
		
		// indirect circle, with different angles
		circle = new Circle2D(0, 0, 1, true);
		arc1 = new CircleArc2D(circle, PI/2, -PI/2);
		arc2 = new CircleArc2D(circle, PI/2, 2*PI, false);
		arc3 = new CircleArc2D(0, 0, 1, PI/2, -PI/2);
		arc4 = new CircleArc2D(0, 0, 1, PI/2, 2*PI, false);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
	}

	public void testGetBuffer_Double() {
		Point2D center = new Point2D(0, 0);
		double r = 10;
		
		// direct arc
		CircleArc2D arc = new CircleArc2D(center, r, 0, PI/2);
		Domain2D buffer = arc.buffer(2);
		
		assertFalse(buffer.isEmpty());
		Boundary2D boundary = buffer.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}
	
	public void testGetParallel() {
		Point2D center = new Point2D(0, 0);
		double r = 10;
		
		// direct arc
		CircleArc2D arc = new CircleArc2D(center, r, 0, PI/2);
		CircleArc2D par = arc.parallel(2);
		Circle2D circ = par.supportingCircle();
		assertEquals(12, circ.radius(), 1e-12);
		
		// direct arc
		CircleArc2D arc2 = new CircleArc2D(center, r, PI, -PI/2);
		CircleArc2D par2 = arc2.parallel(2);
		Circle2D circ2 = par2.supportingCircle();
		assertEquals(8, circ2.radius(), 1e-12);
	}
	
	public void testTransformInversion() {
		// create an infinite curve, here a straight line
		Point2D center = new Point2D(30, 40);
		double r = 10;
		CircleArc2D arc = new CircleArc2D(center, r, PI, -PI/2);
		
		Circle2D circle = new Circle2D(50, 0, 50);
		CircleInversion2D inv = new CircleInversion2D(circle);
		
		CirculinearCurve2D res = arc.transform(inv);
		assertNotNull(res);
		assertTrue(res instanceof CircleArc2D);		
	}
	
	public void testIsBounded() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertTrue(arc.isBounded());
	}

	public void testAsPolyline() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		Polyline2D poly = arc.asPolyline(4);
		assertEquals(5, poly.vertexNumber());
	}
	
	public void testIsClosed() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertFalse(arc.isClosed());
	}

	public void testContainsAngleDouble(){
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		assertTrue(arc.containsAngle(PI/4));
		assertFalse(arc.containsAngle(PI));
		assertFalse(arc.containsAngle(3*PI/4));
		assertFalse(arc.containsAngle(7*PI/4));

		CircleArc2D arc2 = new CircleArc2D(0, 0, 10, 7*PI/4, PI);
		assertTrue(arc2.containsAngle(0));
		assertTrue(arc2.containsAngle(PI/2));
		assertFalse(arc2.containsAngle(PI));
		assertFalse(arc2.containsAngle(3 * PI / 2));
	}
	
	public void testGetLength() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.length(), (10 * PI / 2), Shape2D.ACCURACY);
	}
	
	public void testGetT0Double(){
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.t0(), 0, Shape2D.ACCURACY);
		assertEquals(arc.t1(), PI / 2, Shape2D.ACCURACY);
	}
	
	public void testGetPointDouble() {
		double x0 = 0;
		double y0 = 0;
		Point2D p0 = new Point2D(y0, x0);
		double r = 10;
		
		CircleArc2D arc = new CircleArc2D(p0, 10, 0, PI/2);
		Point2D p1 = new Point2D(10, 0);
		assertTrue(p1.distance(arc.point(arc.t0()))<Shape2D.ACCURACY);
		Point2D p2 = new Point2D(0, 10);
		assertTrue(p2.distance(arc.point(arc.t1()))<Shape2D.ACCURACY);
		
		// Check inverted arc
		arc = new CircleArc2D(p0, 10, 3*PI/2, -3*PI/2);
		double eps = Shape2D.ACCURACY;
		assertTrue(arc.point(PI/2).almostEquals(new Point2D(x0-r, y0), eps));
		assertTrue(arc.point(PI).almostEquals(new Point2D(x0, y0+r), eps));
	}

	public void testGetPositionPoint2D(){
		Point2D origin = new Point2D(0, 0);
		CircleArc2D arc = new CircleArc2D(origin, 10, 3*PI/2, 3*PI/2);
		double pos = arc.position(new Point2D(10, 0));
		assertEquals(PI / 2, pos, 1e-12);
	}
	
	public void testGetPositionPoint2D_indirect(){
		Point2D origin = new Point2D(0, 0);
		CircleArc2D arc = new CircleArc2D(origin, 10, 3*PI/2, -3*PI/2);
		double pos = arc.position(new Point2D(0, 10));
		assertEquals(PI, pos, 1e-12);
	}
	
	/*
	 * Test for double getDistance(double, double). Use a circle arc centered
	 * on origin, from 0 to pi/2 radians, and check distances with points
	 * on (0,0), (r,0), (0,r), (r,r).
	 */
	public void testGetDistancedoubledouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.distance(0, 0), 10, Shape2D.ACCURACY);
		assertEquals(arc.distance(10, 0), 0, Shape2D.ACCURACY);
		assertEquals(arc.distance(0, 10), 0, Shape2D.ACCURACY);
		assertEquals(arc.distance(10, -10), 10, Shape2D.ACCURACY);		
		assertEquals(arc.distance(10, 10), (10*(Math.sqrt(2)-1)), Shape2D.ACCURACY);		
	}

	public void testGetTangent() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI);
		double t0 = PI/2;
		Vector2D tangent = arc.tangent(t0);
		assertVectorEquals(new Vector2D(-10, 0), tangent, Shape2D.ACCURACY);
		
		arc = new CircleArc2D(new Point2D(0, 0), 10, PI, -PI);
		t0 = PI/2;
		tangent = arc.tangent(t0);
		assertVectorEquals(new Vector2D(10, 0), tangent, Shape2D.ACCURACY);
	}
	
	public void testGetWindingAngle() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		Point2D p = new Point2D(0, 0);
		assertEquals(arc.windingAngle(p), (PI / 2), Shape2D.ACCURACY);
		p = new Point2D(0, -10);
		assertEquals(arc.windingAngle(p), (PI/4), Shape2D.ACCURACY);
		p = new Point2D(0, -20);
		assertEquals(arc.windingAngle(p), Math.atan(.5), Shape2D.ACCURACY);
	}

	public void testGetIntersectionsStraightObject2D(){
		double r = 10;
		double eps = Shape2D.ACCURACY;
		
		// Test with a centered circle arc and 4 edges in each main direction
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 3*PI/8, 15*PI/8);
		Collection<Point2D> points1 = arc.intersections(new LineSegment2D(0, 0, 20, 0));
		Collection<Point2D> points2 = arc.intersections(new LineSegment2D(0, 0, 0, 20));
		Collection<Point2D> points3 = arc.intersections(new LineSegment2D(0, 0, -20, 0));
		Collection<Point2D> points4 = arc.intersections(new LineSegment2D(0, 0, 0, -20));
		assertTrue(points1.iterator().next().almostEquals(new Point2D(r, 0), eps));
		assertTrue(points2.iterator().next().almostEquals(new Point2D(0, r), eps));
		assertTrue(points3.iterator().next().almostEquals(new Point2D(-r, 0), eps));
		assertTrue(points4.iterator().next().almostEquals(new Point2D(0, -r), eps));
		
		Collection<Point2D> points;
		arc = new CircleArc2D(0, 0, 10, PI, -PI);
		points = arc.intersections(new StraightLine2D(0, 0, 0, 1));
		assertTrue(points.iterator().next().almostEquals(new Point2D(0, r), eps));
		
		double r2 = Math.sqrt(3)*r/2;
		arc = new CircleArc2D(0, 0, 10, PI, -PI);
		points = arc.intersections(new StraightLine2D(0, .5*r, 1, 0));
		Point2D point = new Point2D(-r2, r*.5);
		Point2D inter1 = points.iterator().next();
		assertTrue(inter1.almostEquals(point, eps));
		
		arc = new CircleArc2D(50, 100, 50, PI, PI/2);
		LineSegment2D line = new LineSegment2D(100, 0, 100, 100);
		assertTrue(arc.intersections(line).size()==0);

	}
	
	/**
	 * Test with 4 circle arcs containing one extreme point on x or y axis.
	 */
	public void testGetBoundingBox(){
		double xc = 0;
		double yc = 0;
		double r  = 10;
		double r2 = r*Math.sqrt(2)/2;
		double t0 =   PI/4;
		double t1 = 3*PI/4;
		double t2 = 5*PI/4;
		double t3 = 7*PI/4;
		double dt = PI/2;
		
		// declare variables
		CircleArc2D arc0, arc1, arc2, arc3;
		Box2D box0, box1, box2, box3;
		Box2D bounds0, bounds1, bounds2, bounds3;
		
		// top
		arc0 	= new CircleArc2D(xc, yc, r, t0, dt);
		box0 	= new Box2D(xc-r2, xc+r2, r2, r);
		bounds0 = arc0.boundingBox();
		assertTrue(box0.almostEquals(bounds0, Shape2D.ACCURACY));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t1, dt);
		box1 	= new Box2D(xc-r, xc-r2, -r2, r2);
		bounds1 = arc1.boundingBox();
		assertTrue(box1.almostEquals(bounds1, Shape2D.ACCURACY));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t2, dt);
		box2 	= new Box2D(xc-r2, xc+r2, -r, -r2);
		bounds2 = arc2.boundingBox();
		assertTrue(box2.almostEquals(bounds2, Shape2D.ACCURACY));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t3, dt);
		box3 	= new Box2D(r2, r, -r2, r2);
		bounds3 = arc3.boundingBox();
		assertTrue(box3.almostEquals(bounds3, Shape2D.ACCURACY));

		/// circle arcs with extent 3*pi/2
		dt = 3*PI/2;
		
		// top
		arc0 	= new CircleArc2D(xc, yc, r, t3, dt);
		box0 	= new Box2D(xc-r, xc+r, -r2, r);
		bounds0 = arc0.boundingBox();
		assertTrue(box0.almostEquals(bounds0, Shape2D.ACCURACY));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t0, dt);
		box1 	= new Box2D(xc-r, xc+r2, -r, r);
		bounds1 = arc1.boundingBox();
		assertTrue(box1.almostEquals(bounds1, Shape2D.ACCURACY));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t1, dt);
		box2 	= new Box2D(xc-r, xc+r, -r, r2);
		bounds2 = arc2.boundingBox();
		assertTrue(box2.almostEquals(bounds2, Shape2D.ACCURACY));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t2, dt);
		box3 	= new Box2D(-r2, r, -r, r);
		bounds3 = arc3.boundingBox();
		assertTrue(box3.almostEquals(bounds3, Shape2D.ACCURACY));
	
	}
	
	public void testGetSubCurve2D() {
		Point2D p0 = new Point2D(0, 0);
		double r = 10;
		
		CircleArc2D arc1 = new CircleArc2D(p0, r, 3*PI/2, -3*PI/2);
		double t0 = PI/2;
		double t1 = PI;
		
		CircleArc2D sub = arc1.subCurve(t0, t1);
		assertTrue(sub.equals(new CircleArc2D(p0, r, PI, -PI/2)));
	}
	
	public void testClipBox2D() {
		// defines some shapes
		Point2D center = new Point2D(0, 0);
		CircleArc2D arc1 = new CircleArc2D(center, 10, 0, PI/2);
		CircleArc2D arc2 = new CircleArc2D(center, 10, 3*PI/2, 3*PI/2);
		Box2D rect1 = new Box2D(0, 20, 0, 20);
		Box2D rect2 = new Box2D(-20, 30, -20, 30);
		Shape2D clip1;

		// a rectangle totally outside the arc
		clip1 = arc1.clip(new Box2D(15, 25, 15, 25)); 
		assertTrue(clip1 instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clip1).size(), 0);

		// an rectangle totally inside a circle arc
		clip1 = arc1.clip(new Box2D(0, 7, 0, 7));
		assertTrue(clip1 instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clip1).size(), 0);

		// a circle arc totally inside the rectangle
		clip1 = arc1.clip(new Box2D(-20, 20, -20, 20));
		assertTrue(clip1 instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clip1).size(), 1);
		Curve2D clip1c1 = ((CurveSet2D<?>) clip1).firstCurve();
		assertTrue(clip1c1 instanceof CircleArc2D);

		// a circle arc clipped with both extremities outside the rectangle
		Shape2D curves = arc2.clip(rect1);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		Curve2D curve1 = ((CurveSet2D<?>) curves).firstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));		
//		Shape2D arc2c = arc2.clip(rect1);
//		System.out.println(arc2c);
		assertTrue(curve1.equals(arc1));
		//assertTrue(arc2.equals(arc1.clip(rect1)));
		
		// a circle arc clipped with both extremities inside the rectangle
		curves = arc1.clip(rect2);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		curve1 = ((CurveSet2D<?>) curves).firstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));
//		assertTrue(arc1.equals(arc1.clip(rect2)));
		assertTrue(curve1.equals(arc1));
	}
	
	public void testClipBox2DInverse() {
		double x0 = 30;
		double y0 = 20;
		double r = 10;
		Point2D p0 = new Point2D(x0, y0);
		
		// defines some shapes
		CircleArc2D arc0;
		CircleArc2D arc1 = new CircleArc2D(p0, r, PI/2, -PI/2);
		CircleArc2D arc2 = new CircleArc2D(p0, r, PI/2, -3*PI/2);
		Box2D boxOut 	= new Box2D(x0+15, 	x0+25, 	y0+15, 	y0+25);
		Box2D boxIn 	= new Box2D(x0+0, 	x0+7, 	y0+0, 	y0+7);
		Box2D boxFull	= new Box2D(x0-20, 	x0+20, 	y0-20, 	y0+20);
		Box2D box1 		= new Box2D(x0+0, 	x0+20, 	y0+0, 	y0+20);
		Box2D box2 		= new Box2D(x0-20, 	x0+30, 	y0-20, 	y0+30);
		Shape2D clipped;
		CurveSet2D<CircleArc2D> curves;

		// a box totally outside the arc
		clipped = arc1.clip(boxOut); 
		assertTrue(clipped instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clipped).size(), 0);
		assertTrue(clipped.isEmpty());

		// an box totally inside a circle arc
		clipped = arc1.clip(boxIn);
		assertTrue(clipped instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clipped).size(), 0);
		assertTrue(clipped.isEmpty());

		// a circle arc totally inside the box
		clipped = arc1.clip(boxFull);
		assertTrue(clipped instanceof CurveSet2D<?>);
		assertEquals(((CurveSet2D<?>) clipped).size(), 1);
		Curve2D clip1c1 = ((CurveSet2D<?>) clipped).firstCurve();
		assertTrue(clip1c1 instanceof CircleArc2D);
		assertTrue(arc1.equals(clip1c1));

		// a circle arc clipped with both extremities outside the rectangle
		clipped = arc2.clip(box1);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		Curve2D curve1 = ((CurveSet2D<?>) clipped).firstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));		
//		Shape2D arc2c = arc2.clip(rect1);
//		System.out.println(arc2c);
		assertTrue(curve1.equals(arc1));
		//assertTrue(arc2.equals(arc1.clip(rect1)));
		
		// a circle arc clipped with both extremities inside the rectangle
		curves = arc1.clip(box2);
		curve1 = curves.firstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));
		assertTrue(curve1.equals(arc1));
		
		arc0 = new CircleArc2D(p0, r, 7*PI/6, -11*PI/6);
		arc1 = new CircleArc2D(p0, r, 7*PI/6, -PI/6);
		arc2 = new CircleArc2D(p0, r, 3*PI/2, -PI/6);
		curves = arc0.clip(new Box2D(x0-20, x0, y0-20, y0));
		assertEquals(curves.size(), 2);
		Iterator<CircleArc2D> iter = curves.iterator();
		assertTrue(iter.next().almostEquals(arc1, Shape2D.ACCURACY));
		assertTrue(iter.next().almostEquals(arc2, Shape2D.ACCURACY));
	}

	public void testGetReverseCurve(){
		// direct arc, direct circle
		Circle2D circle1 = new Circle2D(0, 0, 10);
		CircleArc2D arc1 = new CircleArc2D(circle1, 0, PI/2);
		CircleArc2D rev1 = arc1.reverse(); 
		CircleArc2D exp1 = new CircleArc2D(circle1, PI / 2, -PI / 2);
		assertTrue(rev1.almostEquals(exp1, Shape2D.ACCURACY));

		// inverse circle arc, inverse circle
		Circle2D circle2 = new Circle2D(0, 0, 10, false);
		CircleArc2D arc2 = new CircleArc2D(circle2, PI, -PI/2);
		assertTrue(arc2.reverse().almostEquals(
				new CircleArc2D(circle2, PI/2, PI/2), Shape2D.ACCURACY));
		
		// direct arc, indirect circle
		CircleArc2D arc3 = new CircleArc2D(circle2, 0, PI/2);
		assertTrue(arc3.reverse().almostEquals(
				new CircleArc2D(circle2, PI/2, -PI/2), Shape2D.ACCURACY));
		
		// inverse circle arc, direct circle
		CircleArc2D arc4 = new CircleArc2D(circle1, PI, -PI/2);
		assertTrue(arc4.reverse().almostEquals(
				new CircleArc2D(circle1, PI/2, PI/2), Shape2D.ACCURACY));
	}
	
	public void testClass(){
		Point2D origin = new Point2D(0, 0);
		CircleArc2D arc = new CircleArc2D(origin, 10, 0, PI/2);
		assertTrue(Curve2D.class.isInstance(arc));
	}
	
	public void testTransform_Translation2D(){
		// Simple direct arc
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		
		// transform
		double tx = 20;
		double ty = 10;
		AffineTransform2D tra = AffineTransform2D.createTranslation(tx, ty);
		EllipseArcShape2D res = arc.transform(tra);
		
		// basic tests
		assertTrue(res instanceof CircleArc2D);
		CircleArc2D exp = new CircleArc2D(tx, ty, 10, 0, PI/2);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}		

	public void testTransform_Rotation2D(){
		// Simple direct arc
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		
		// transform
		double theta = PI/3;
		AffineTransform2D rot = AffineTransform2D.createRotation(theta);
		EllipseArcShape2D res = arc.transform(rot);
		
		// basic tests
		assertTrue(res instanceof CircleArc2D);
		CircleArc2D exp = new CircleArc2D(0, 0, 10, theta, PI/2);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}		

	public void testTransform_Scaling2D(){
		// Simple direct arc
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		
		// transform
		double sx = 3;
		double sy = 2;		
		AffineTransform2D sca = AffineTransform2D.createScaling(sx, sy);
		EllipseArcShape2D res = arc.transform(sca);
		
		// basic tests
		assertFalse(res instanceof CircleArc2D);
		EllipseArc2D exp = new EllipseArc2D(0, 0, 30, 20, 0, 0, PI/2);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}		

	public void testTransform_LineReflectionOx(){
		// Simple direct arc
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI / 2);

		// transform
		StraightLine2D line = new StraightLine2D(0, 0, 1, 0);
		AffineTransform2D trans = AffineTransform2D.createLineReflection(line);
		EllipseArcShape2D res = arc.transform(trans);
		
		// basic tests
		assertTrue(res instanceof CircleArc2D);
		CircleArc2D exp = new CircleArc2D(0, 0, 10, 0, -PI/2);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}		

	public void testTransform_LineReflectionOy2D(){
		// Simple direct arc
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		
		// transform
		StraightLine2D line = new StraightLine2D(0, 0, 0, 1);
		AffineTransform2D trans = AffineTransform2D.createLineReflection(line);
		EllipseArcShape2D res = arc.transform(trans);
		
		// basic tests
		assertTrue(res instanceof CircleArc2D);
		CircleArc2D exp = new CircleArc2D(0, 0, 10, PI, -PI/2);
		assertTrue(res.almostEquals(exp, Shape2D.ACCURACY));
	}		

	
	
	public void testTransformAffineTransform2DInv(){
		CircleArc2D arc;
		double x0 = 30;
		double y0 = 20;
		Point2D p0 = new Point2D(x0, y0);
		double r  = 10;
		
		double tx = 20;
		double ty = 10;
		double theta = PI/3;
		double sx = 3;
		double sy = 2;		
		
		// Simple direct arc
		arc = new CircleArc2D(x0, y0, r, PI/2, -PI/2);
		
		// translation
		AffineTransform2D tra = AffineTransform2D.createTranslation(tx, ty);
		assertTrue(arc.transform(tra).almostEquals(
				new CircleArc2D(x0+tx, y0+ty, r, PI/2, -PI/2), Shape2D.ACCURACY));
		
		// rotation
		AffineTransform2D rot = AffineTransform2D.createRotation(p0, theta);
		assertTrue(arc.transform(rot).almostEquals(
				new CircleArc2D(x0, y0, r, theta+PI/2, -PI/2), Shape2D.ACCURACY));
		
		// scaling with unequal factors
		AffineTransform2D sca = AffineTransform2D.createScaling(p0, sx, sy);
		assertTrue(arc.transform(sca).equals(
				new EllipseArc2D(x0, y0, 3*r, 2*r, 0, PI/2, -PI/2)));
		
		// line reflections
		AffineTransform2D refOx = AffineTransform2D.createLineReflection(
				new StraightLine2D(x0, y0, 1, 0));
		EllipseArcShape2D transformed = arc.transform(refOx);
		assertTrue(transformed.almostEquals(
				new CircleArc2D(x0, y0, r, 3*PI/2, PI/2), Shape2D.ACCURACY));
		AffineTransform2D refOy = AffineTransform2D.createLineReflection(
				new StraightLine2D(x0, y0, 0, 1));
		assertTrue(arc.transform(refOy).almostEquals(
				new CircleArc2D(x0, y0, r, PI/2, PI/2), Shape2D.ACCURACY));	
	}
	
	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertTrue(!arc.contains(0, 0));
		assertTrue(arc.contains(10, 0));
		assertTrue(arc.contains(0, 10));
	}
	
	/*
	 * Test for double project(Point2D)
	 */
	public void testProjectPoint2D() {
		double eps = 1e-12;
		
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		Point2D point00 = new Point2D(20, 0);
		assertEquals(0, arc.project(point00), eps);
		Point2D point45 = new Point2D(20, 20);
		assertEquals(PI / 4, arc.project(point45), eps);
		Point2D pointYX = new Point2D(40, 20);
		assertEquals(Math.atan2(1, 2), arc.project(pointYX), eps);
		Point2D pointXY = new Point2D(20, 40);
		assertEquals(Math.atan2(2, 1), arc.project(pointXY), eps);		
	}
	
	public void testProjectPoint2D_Direct() {
		double x0 = 0;
		double y0 = 0;
		double r = 10;
		double eps = 1e-12;
		double theta1 = 3*PI/8;
		double theta2 = 17*PI/8;
		CircleArc2D arc1 = new CircleArc2D(x0, y0, r, theta1, theta2-theta1);
		
		assertEquals(arc1.project(new Point2D(x0+r/2, y0+r*.8)), 0, eps);
		assertEquals(arc1.project(new Point2D(x0 + r * .8, y0 + r / 2)), theta2 - theta1, eps);
	}

	public void testArea() {
		// let's not have centre at (0,0) as that's the most straightforward
		CircleArc2D whole = new CircleArc2D(-10, -10, 5, 0, 4*PI);
		double expectedAreaWhile = PI*25; // \pi * 5^2

		assertEquals(expectedAreaWhile, whole.getArea());

		// TODO: Pull in JCheck to supercharge this.
		Random r = new Random();
		double sweepExtent = r.nextDouble();
		CircleArc2D partial = new CircleArc2D(-10, -10, 5, 45, (4*PI)/sweepExtent);
		double expectedAreaParial = expectedAreaWhile / sweepExtent;

		assertEquals(expectedAreaParial, partial.getArea(), 0.0001);
	}

	public void testPoint() {
		double radius = 5.0, xc = 10.0, yc = 10.0, angle = -(1/4)*PI;
		CircleArc2D arc = new CircleArc2D(10, 10, radius, angle, PI*(1/4));
		Point2D o2 = arc.point(0.2), o8 = arc.point(0.8);

		assertFalse(o2.equals(o8));
	}

	public void testIsPointOnArc() {
		double radius = 5.0, xc = 10.0, yc = 10.0, angle = -(1/4)*PI;
		CircleArc2D arc = new CircleArc2D(xc, yc, radius, angle, PI *(1/4));
		Point2D on = new Point2D(xc + radius * Math.cos(angle), yc + radius * Math.sin(angle));
		Point2D off = new Point2D(xc + radius * Math.cos(angle), 0.001 + yc + radius * Math.sin(angle));

		assertTrue(arc.isOnArc(on));
		assertFalse(arc.isOnArc(off));
	}

	public void testIntersections() {
		double radius = 5.0, xc = 10.0, yc = 10.0, angle = -(1/4)*PI;
		CircleArc2D arc = new CircleArc2D(xc, yc, radius, angle, (2*PI)-(PI/16));  // almost a full circle

		Circle2D a = new Circle2D(10, 10, 5);
		Circle2D b = new Circle2D(12, 10, 5);
		Circle2D t = new Circle2D(20, 10, 5);
		Optional<Collection<Point2D>> xs = arc.intersections(a);

		assertTrue(!xs.isPresent());

		xs = arc.intersections(b);
		assertEquals(2, xs.get().size());

		xs = arc.intersections(t);
		Collection<Point2D> uniq = xs.get().stream().distinct().collect(Collectors.toList());
		assertEquals(1, uniq.size());
	}
}
