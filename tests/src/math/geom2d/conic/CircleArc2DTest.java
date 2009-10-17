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

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;

import static java.lang.Math.PI;


/**
 * @author Legland
 */
public class CircleArc2DTest extends TestCase {

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

	public void testGetParallel() {
		Point2D center = new Point2D(0, 0);
		double r = 10;
		
		// direct arc
		CircleArc2D arc = new CircleArc2D(center, r, 0, PI/2);
		CircleArc2D par = arc.getParallel(2);
		Circle2D circ = par.getSupportingCircle();
		assertEquals(12, circ.getRadius(), 1e-12);
		
		// direct arc
		CircleArc2D arc2 = new CircleArc2D(center, r, PI, -PI/2);
		CircleArc2D par2 = arc2.getParallel(2);
		Circle2D circ2 = par2.getSupportingCircle();
		assertEquals(8, circ2.getRadius(), 1e-12);
	}
	
	public void testIsBounded() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertTrue(arc.isBounded());
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
		assertFalse(arc2.containsAngle(3*PI/2));
	}
	
	public void testGetLength() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.getLength(), (10*PI/2), Shape2D.ACCURACY);
	}
	
	public void testGetT0Double(){
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.getT0(), 0, Shape2D.ACCURACY);
		assertEquals(arc.getT1(), PI/2, Shape2D.ACCURACY);
	}
	
	public void testGetPointDouble() {
		double x0 = 0;
		double y0 = 0;
		Point2D p0 = new Point2D(y0, x0);
		double r = 10;
		
		CircleArc2D arc = new CircleArc2D(p0, 10, 0, PI/2);
		Point2D p1 = new Point2D(10, 0);
		assertTrue(p1.getDistance(arc.getPoint(arc.getT0()))<Shape2D.ACCURACY);
		Point2D p2 = new Point2D(0, 10);
		assertTrue(p2.getDistance(arc.getPoint(arc.getT1()))<Shape2D.ACCURACY);
		
		// Check inverted arc
		arc = new CircleArc2D(p0, 10, 3*PI/2, -3*PI/2);
		assertTrue(arc.getPoint(PI/2).equals(new Point2D(x0-r, y0)));
		assertTrue(arc.getPoint(PI).equals(new Point2D(x0, y0+r)));
	}

	public void testGetPositionPoint2D(){
		Point2D origin = new Point2D(0, 0);
		CircleArc2D arc = new CircleArc2D(origin, 10, 3*PI/2, 3*PI/2);
		double t = arc.getPosition(new Point2D(10, 0));
		assertEquals(t, PI/2, 1e-12);
	}
	
	/*
	 * Test for double getDistance(double, double). Use a circle arc centered
	 * on origin, from 0 to pi/2 radians, and check distances with points
	 * on (0,0), (r,0), (0,r), (r,r).
	 */
	public void testGetDistancedoubledouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		assertEquals(arc.getDistance(0, 0), 10, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(10, 0), 0, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(0, 10), 0, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(10, -10), 10, Shape2D.ACCURACY);		
		assertEquals(arc.getDistance(10, 10), (10*(Math.sqrt(2)-1)), Shape2D.ACCURACY);		
	}

	public void testGetWindingAngle() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, PI/2);
		Point2D p = new Point2D(0, 0);
		assertEquals(arc.getWindingAngle(p), (PI/2), Shape2D.ACCURACY);
		p.setLocation(0, -10);
		assertEquals(arc.getWindingAngle(p), (PI/4), Shape2D.ACCURACY);
		p.setLocation(0, -20);
		assertEquals(arc.getWindingAngle(p), Math.atan(.5), Shape2D.ACCURACY);
	}

	public void testGetIntersectionsStraightObject2D(){
		double r = 10;
		
		// Test with a centered circle arc and 4 edges in each main direction
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 3*PI/8, 15*PI/8);
		Collection<Point2D> points1 = arc.getIntersections(new LineSegment2D(0, 0, 20, 0));
		Collection<Point2D> points2 = arc.getIntersections(new LineSegment2D(0, 0, 0, 20));
		Collection<Point2D> points3 = arc.getIntersections(new LineSegment2D(0, 0, -20, 0));
		Collection<Point2D> points4 = arc.getIntersections(new LineSegment2D(0, 0, 0, -20));
		assertTrue(points1.iterator().next().equals(new Point2D(r, 0)));
		assertTrue(points2.iterator().next().equals(new Point2D(0, r)));
		assertTrue(points3.iterator().next().equals(new Point2D(-r, 0)));
		assertTrue(points4.iterator().next().equals(new Point2D(0, -r)));
		
		Collection<Point2D> points;
		arc = new CircleArc2D(0, 0, 10, PI, -PI);
		points = arc.getIntersections(new StraightLine2D(0, 0, 0, 1));
		assertTrue(points.iterator().next().equals(new Point2D(0, r)));
		
		double r2 = Math.sqrt(3)*r/2;
		arc = new CircleArc2D(0, 0, 10, PI, -PI);
		points = arc.getIntersections(new StraightLine2D(0, .5*r, 1, 0));
		Point2D point = new Point2D(-r2, r*.5);
		Point2D inter1 = points.iterator().next();
		assertTrue(inter1.equals(point));
		
		arc = new CircleArc2D(50, 100, 50, PI, PI/2);
		LineSegment2D line = new LineSegment2D(100, 0, 100, 100);
		assertTrue(arc.getIntersections(line).size()==0);

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
		bounds0 = arc0.getBoundingBox();
		assertTrue(box0.equals(bounds0));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t1, dt);
		box1 	= new Box2D(xc-r, xc-r2, -r2, r2);
		bounds1 = arc1.getBoundingBox();
		assertTrue(box1.equals(bounds1));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t2, dt);
		box2 	= new Box2D(xc-r2, xc+r2, -r, -r2);
		bounds2 = arc2.getBoundingBox();
		assertTrue(box2.equals(bounds2));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t3, dt);
		box3 	= new Box2D(r2, r, -r2, r2);
		bounds3 = arc3.getBoundingBox();
		assertTrue(box3.equals(bounds3));

		/// circle arcs with extent 3*pi/2
		dt = 3*PI/2;
		
		// top
		arc0 	= new CircleArc2D(xc, yc, r, t3, dt);
		box0 	= new Box2D(xc-r, xc+r, -r2, r);
		bounds0 = arc0.getBoundingBox();
		assertTrue(box0.equals(bounds0));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t0, dt);
		box1 	= new Box2D(xc-r, xc+r2, -r, r);
		bounds1 = arc1.getBoundingBox();
		assertTrue(box1.equals(bounds1));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t1, dt);
		box2 	= new Box2D(xc-r, xc+r, -r, r2);
		bounds2 = arc2.getBoundingBox();
		assertTrue(box2.equals(bounds2));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t2, dt);
		box3 	= new Box2D(-r2, r, -r, r);
		bounds3 = arc3.getBoundingBox();
		assertTrue(box3.equals(bounds3));
	
	}
	
	public void testGetSubCurve2D() {
		Point2D p0 = new Point2D(0, 0);
		double r = 10;
		
		CircleArc2D arc1 = new CircleArc2D(p0, r, 3*PI/2, -3*PI/2);
		double t0 = PI/2;
		double t1 = PI;
		
		CircleArc2D sub = arc1.getSubCurve(t0, t1);
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
		assertTrue(clip1 instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clip1).getCurveNumber(), 0);

		// an rectangle totally inside a circle arc
		clip1 = arc1.clip(new Box2D(0, 7, 0, 7));
		assertTrue(clip1 instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clip1).getCurveNumber(), 0);

		// a circle arc totally inside the rectangle
		clip1 = arc1.clip(new Box2D(-20, 20, -20, 20));
		assertTrue(clip1 instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clip1).getCurveNumber(), 1);
		Curve2D clip1c1 = (Curve2D) ((CurveSet2D<?>) clip1).getFirstCurve();
		assertTrue(clip1c1 instanceof CircleArc2D);

		// a circle arc clipped with both extremities outside the rectangle
		Shape2D curves = arc2.clip(rect1);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		Curve2D curve1 = ((CurveSet2D<?>) curves).getFirstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));		
//		Shape2D arc2c = arc2.clip(rect1);
//		System.out.println(arc2c);
		assertTrue(curve1.equals(arc1));
		//assertTrue(arc2.equals(arc1.clip(rect1)));
		
		// a circle arc clipped with both extremities inside the rectangle
		curves = arc1.clip(rect2);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		curve1 = ((CurveSet2D<?>) curves).getFirstCurve();
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
		assertTrue(clipped instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clipped).getCurveNumber(), 0);
		assertTrue(clipped.isEmpty());

		// an box totally inside a circle arc
		clipped = arc1.clip(boxIn);
		assertTrue(clipped instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clipped).getCurveNumber(), 0);
		assertTrue(clipped.isEmpty());

		// a circle arc totally inside the box
		clipped = arc1.clip(boxFull);
		assertTrue(clipped instanceof CurveSet2D);
		assertEquals(((CurveSet2D<?>) clipped).getCurveNumber(), 1);
		Curve2D clip1c1 = (Curve2D) ((CurveSet2D<?>) clipped).getFirstCurve();
		assertTrue(clip1c1 instanceof CircleArc2D);
		assertTrue(arc1.equals(clip1c1));

		// a circle arc clipped with both extremities outside the rectangle
		clipped = arc2.clip(box1);
		//assertTrue(curves.getClass().equals(CurveSet2D.class));
		Curve2D curve1 = ((CurveSet2D<?>) clipped).getFirstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));		
//		Shape2D arc2c = arc2.clip(rect1);
//		System.out.println(arc2c);
		assertTrue(curve1.equals(arc1));
		//assertTrue(arc2.equals(arc1.clip(rect1)));
		
		// a circle arc clipped with both extremities inside the rectangle
		curves = arc1.clip(box2);
		curve1 = curves.getFirstCurve();
		assertTrue(curve1.getClass().equals(CircleArc2D.class));
		assertTrue(curve1.equals(arc1));
		
		arc0 = new CircleArc2D(p0, r, 7*PI/6, -11*PI/6);
		arc1 = new CircleArc2D(p0, r, 7*PI/6, -PI/6);
		arc2 = new CircleArc2D(p0, r, 3*PI/2, -PI/6);
		curves = arc0.clip(new Box2D(x0-20, x0, y0-20, y0));
		assertEquals(curves.getCurveNumber(), 2);
		Iterator<CircleArc2D> iter = curves.iterator();
		assertTrue(iter.next().equals(arc1));
		assertTrue(iter.next().equals(arc2));
	}

	public void testGetReverseCurve(){
		// direct arc, direct circle
		Circle2D circle1 = new Circle2D(0, 0, 10);
		CircleArc2D arc1 = new CircleArc2D(circle1, 0, PI/2);
		assertTrue(arc1.getReverseCurve().equals(
				new CircleArc2D(circle1, PI/2, -PI/2)));
		
		// inverse circle arc, inverse circle
		Circle2D circle2 = new Circle2D(0, 0, 10, false);
		CircleArc2D arc2 = new CircleArc2D(circle2, PI, -PI/2);
		assertTrue(arc2.getReverseCurve().equals(
				new CircleArc2D(circle2, PI/2, PI/2)));
		
		// direct arc, indirect circle
		CircleArc2D arc3 = new CircleArc2D(circle2, 0, PI/2);
		assertTrue(arc3.getReverseCurve().equals(
				new CircleArc2D(circle2, PI/2, -PI/2)));
		
		// inverse circle arc, direct circle
		CircleArc2D arc4 = new CircleArc2D(circle1, PI, -PI/2);
		assertTrue(arc4.getReverseCurve().equals(
				new CircleArc2D(circle1, PI/2, PI/2)));
	}
	
	public void testClass(){
		Point2D origin = new Point2D(0, 0);
		CircleArc2D arc = new CircleArc2D(origin, 10, 0, PI/2);
		assertTrue(Curve2D.class.isInstance(arc));
	}
	
	public void testTransformAffineTransform2D(){
		CircleArc2D arc;
		
		double tx = 20;
		double ty = 10;
		double theta = PI/3;
		double sx = 3;
		double sy = 2;		
		
		// Simple direct arc
		arc = new CircleArc2D(0, 0, 10, 0, PI/2);
		
		// translation
		AffineTransform2D tra = AffineTransform2D.createTranslation(tx, ty);
		assertTrue(arc.transform(tra).equals(
				new CircleArc2D(tx, ty, 10, 0, PI/2)));
		
		// rotation
		AffineTransform2D rot = AffineTransform2D.createRotation(theta);
		assertTrue(arc.transform(rot).equals(
				new CircleArc2D(0, 0, 10, theta, PI/2)));
		
		// scaling with unequal factors
		AffineTransform2D sca = AffineTransform2D.createScaling(sx, sy);
		assertTrue(arc.transform(sca).equals(
				new EllipseArc2D(0, 0, 30, 20, 0, 0, PI/2)));
		
		// line reflections
		AffineTransform2D refOx = AffineTransform2D.createLineReflection(
				new StraightLine2D(0, 0, 1, 0));
		assertTrue(arc.transform(refOx).equals(
				new CircleArc2D(0, 0, 10, 0, -PI/2)));
		AffineTransform2D refOy = AffineTransform2D.createLineReflection(
				new StraightLine2D(0, 0, 0, 1));
		assertTrue(arc.transform(refOy).equals(
				new CircleArc2D(0, 0, 10, PI, -PI/2)));	
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
		assertTrue(arc.transform(tra).equals(
				new CircleArc2D(x0+tx, y0+ty, r, PI/2, -PI/2)));
		
		// rotation
		AffineTransform2D rot = AffineTransform2D.createRotation(p0, theta);
		assertTrue(arc.transform(rot).equals(
				new CircleArc2D(x0, y0, r, theta+PI/2, -PI/2)));
		
		// scaling with unequal factors
		AffineTransform2D sca = AffineTransform2D.createScaling(p0, sx, sy);
		assertTrue(arc.transform(sca).equals(
				new EllipseArc2D(x0, y0, 3*r, 2*r, 0, PI/2, -PI/2)));
		
		// line reflections
		AffineTransform2D refOx = AffineTransform2D.createLineReflection(
				new StraightLine2D(x0, y0, 1, 0));
		EllipseArc2D transformed = arc.transform(refOx);
		assertTrue(transformed.equals(
				new CircleArc2D(x0, y0, r, 3*PI/2, PI/2)));
		AffineTransform2D refOy = AffineTransform2D.createLineReflection(
				new StraightLine2D(x0, y0, 0, 1));
		assertTrue(arc.transform(refOy).equals(
				new CircleArc2D(x0, y0, r, PI/2, PI/2)));	
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
		assertEquals(PI/4, arc.project(point45), eps);
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
		assertEquals(arc1.project(new Point2D(x0+r*.8, y0+r/2)), theta2-theta1, eps);
	}
	
    public void testClone() {
        Circle2D circle = new Circle2D(10, 20, 30);
        CircleArc2D arc = new CircleArc2D(circle, PI/2, PI);
        assertTrue(arc.equals(arc.clone()));
        
        arc = new CircleArc2D(circle, PI/2, -PI);
        assertTrue(arc.equals(arc.clone()));
    }
}
