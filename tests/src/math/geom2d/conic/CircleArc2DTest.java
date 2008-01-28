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

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.line.LineSegment2D;

import junit.framework.TestCase;

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
	public void TestCircleArc2D() {
		// direct circle
		CircleArc2D arc0 = new CircleArc2D();
		Circle2D circle = new Circle2D(0, 0, 1, true);
		CircleArc2D arc1 = new CircleArc2D(circle, 0, Math.PI/2);
		CircleArc2D arc2 = new CircleArc2D(circle, 0, Math.PI/2, true);
		CircleArc2D arc3 = new CircleArc2D(0, 0, 1, 0, Math.PI/2);
		CircleArc2D arc4 = new CircleArc2D(0, 0, 1, 0, Math.PI/2, true);
		assertTrue(arc1.equals(arc0));
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));

		// direct circle, with different angles
		circle = new Circle2D(0, 0, 1, true);
		arc1 = new CircleArc2D(circle, Math.PI/2, Math.PI/2);
		arc2 = new CircleArc2D(circle, Math.PI/2, Math.PI, true);
		arc3 = new CircleArc2D(0, 0, 1, Math.PI/2, Math.PI/2);
		arc4 = new CircleArc2D(0, 0, 1, Math.PI/2, Math.PI, true);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
		
		// indirect circle, with different angles
		circle = new Circle2D(0, 0, 1, true);
		arc1 = new CircleArc2D(circle, Math.PI/2, -Math.PI/2);
		arc2 = new CircleArc2D(circle, Math.PI/2, 2*Math.PI, false);
		arc3 = new CircleArc2D(0, 0, 1, Math.PI/2, -Math.PI/2);
		arc4 = new CircleArc2D(0, 0, 1, Math.PI/2, 2*Math.PI, false);
		assertTrue(arc1.equals(arc2));
		assertTrue(arc1.equals(arc3));
		assertTrue(arc1.equals(arc4));
	}

	public void testIsBounded() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertTrue(arc.isBounded());
	}

	public void testIsClosed() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertTrue(!arc.isClosed());
	}

	public void testContainsAngleDouble(){
		CircleArc2D arc = new CircleArc2D(0, 0, 10, 0, Math.PI/2);
		assertTrue(arc.containsAngle(Math.PI/4));
		assertTrue(!arc.containsAngle(Math.PI));
		assertTrue(!arc.containsAngle(3*Math.PI/4));
		assertTrue(!arc.containsAngle(7*Math.PI/4));

		CircleArc2D arc2 = new CircleArc2D(0, 0, 10, 7*Math.PI/4, Math.PI);
		assertTrue(arc2.containsAngle(0));
		assertTrue(arc2.containsAngle(Math.PI/2));
		assertTrue(!arc2.containsAngle(Math.PI));
		assertTrue(!arc2.containsAngle(3*Math.PI/2));
	}
	
	public void testGetLength() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertEquals(arc.getLength(), (10*Math.PI/2), Shape2D.ACCURACY);
	}
	
	public void testGetT0Double(){
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertEquals(arc.getT0(), 0, Shape2D.ACCURACY);
		assertEquals(arc.getT1(), Math.PI/2, Shape2D.ACCURACY);
	}
	
	public void testGetPointDouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		Point2D p1 = new Point2D(10, 0);
		assertTrue(p1.getDistance(arc.getPoint(arc.getT0()))<Shape2D.ACCURACY);
		Point2D p2 = new Point2D(0, 10);
		assertTrue(p2.getDistance(arc.getPoint(arc.getT1()))<Shape2D.ACCURACY);
	}

	/*
	 * Test for double getDistance(double, double). Use a circle arc centered
	 * on origin, from 0 to pi/2 radians, and check distances with points
	 * on (0,0), (r,0), (0,r), (r,r).
	 */
	public void testGetDistancedoubledouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertEquals(arc.getDistance(0, 0), 10, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(10, 0), 0, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(0, 10), 0, Shape2D.ACCURACY);
		assertEquals(arc.getDistance(10, -10), 10, Shape2D.ACCURACY);		
		assertEquals(arc.getDistance(10, 10), (10*(Math.sqrt(2)-1)), Shape2D.ACCURACY);		
	}

	public void testGetWindingAngle() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		Point2D p = new Point2D(0, 0);
		assertEquals(arc.getWindingAngle(p), (Math.PI/2), Shape2D.ACCURACY);
		p.setLocation(0, -10);
		assertEquals(arc.getWindingAngle(p), (Math.PI/4), Shape2D.ACCURACY);
		p.setLocation(0, -20);
		assertEquals(arc.getWindingAngle(p), Math.atan(.5), Shape2D.ACCURACY);
	}

	public void testGetIntersectionsStraightObject2D(){
		// Test with a centered circle arc and 4 edges in each main direction
		CircleArc2D circle2 = new CircleArc2D(0, 0, 10, 3*Math.PI/8, 15*Math.PI/8);
		Collection<Point2D> points1 = circle2.getIntersections(new LineSegment2D(0, 0, 20, 0));
		Collection<Point2D> points2 = circle2.getIntersections(new LineSegment2D(0, 0, 0, 20));
		Collection<Point2D> points3 = circle2.getIntersections(new LineSegment2D(0, 0, -20, 0));
		Collection<Point2D> points4 = circle2.getIntersections(new LineSegment2D(0, 0, 0, -20));
		assertTrue(points1.iterator().next().equals(new Point2D(10, 0)));
		assertTrue(points2.iterator().next().equals(new Point2D(0, 10)));
		assertTrue(points3.iterator().next().equals(new Point2D(-10, 0)));
		assertTrue(points4.iterator().next().equals(new Point2D(0, -10)));		
	}
	
	/**
	 * Test with 4 circle arcs containing one extreme point on x or y axis.
	 */
	public void testGetBounds2D(){
		double xc = 0;
		double yc = 0;
		double r  = 10;
		double r2 = r*Math.sqrt(2)/2;
		double t0 =   Math.PI/4;
		double t1 = 3*Math.PI/4;
		double t2 = 5*Math.PI/4;
		double t3 = 7*Math.PI/4;
		double dt = Math.PI/2;
		
		// declare variables
		CircleArc2D arc0, arc1, arc2, arc3;
		java.awt.geom.Rectangle2D box0, box1, box2, box3;
		java.awt.geom.Rectangle2D bounds0, bounds1, bounds2, bounds3;
		
		// top
		arc0 	= new CircleArc2D(xc, yc, r, t0, dt);
		box0 	= (new Box2D(xc-r2, xc+r2, r2, r)).getAsAWTRectangle2D();
		bounds0 = arc0.getBounds2D();
		assertTrue(box0.equals(bounds0));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t1, dt);
		box1 	= (new Box2D(xc-r, xc-r2, -r2, r2)).getAsAWTRectangle2D();
		bounds1 = arc1.getBounds2D();
		assertTrue(box1.equals(bounds1));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t2, dt);
		box2 	= (new Box2D(xc-r2, xc+r2, -r, -r2)).getAsAWTRectangle2D();
		bounds2 = arc2.getBounds2D();
		assertTrue(box2.equals(bounds2));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t3, dt);
		box3 	= (new Box2D(r2, r, -r2, r2)).getAsAWTRectangle2D();
		bounds3 = arc3.getBounds2D();
		assertTrue(box3.equals(bounds3));

		/// circle arcs with extent 3*pi/2
		dt = 3*Math.PI/2;
		
		// top
		arc0 	= new CircleArc2D(xc, yc, r, t3, dt);
		box0 	= (new Box2D(xc-r, xc+r, -r2, r)).getAsAWTRectangle2D();
		bounds0 = arc0.getBounds2D();
		assertTrue(box0.equals(bounds0));

		// left
		arc1 	= new CircleArc2D(xc, yc, r, t0, dt);
		box1 	= (new Box2D(xc-r, xc+r2, -r, r)).getAsAWTRectangle2D();
		bounds1 = arc1.getBounds2D();
		assertTrue(box1.equals(bounds1));

		// bottom
		arc2 	= new CircleArc2D(xc, yc, r, t1, dt);
		box2 	= (new Box2D(xc-r, xc+r, -r, r2)).getAsAWTRectangle2D();
		bounds2 = arc2.getBounds2D();
		assertTrue(box2.equals(bounds2));

		// right
		arc3 	= new CircleArc2D(xc, yc, r, t2, dt);
		box3 	= (new Box2D(-r2, r, -r, r)).getAsAWTRectangle2D();
		bounds3 = arc3.getBounds2D();
		assertTrue(box3.equals(bounds3));
	
	}
	
	
	public void testGetClippedShape() {
		// defines some shapes
		CircleArc2D arc1 = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		CircleArc2D arc2 = new CircleArc2D(new Point2D(0, 0), 10, 3*Math.PI/2, 3*Math.PI/2);
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

	public void testClass(){
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertTrue(Curve2D.class.isInstance(arc));
	}
	
	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		CircleArc2D arc = new CircleArc2D(new Point2D(0, 0), 10, 0, Math.PI/2);
		assertTrue(!arc.contains(0, 0));
		assertTrue(arc.contains(10, 0));
		assertTrue(arc.contains(0, 10));
	}

}
