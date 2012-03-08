/*
 * File : Ray2DTest.java
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
 * Created on 31 déc. 2003
 */

package math.geom2d.line;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.UnboundedShape2DException;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.transform.CircleInversion2D;
import junit.framework.TestCase;


/**
 * @author Legland
 */
public class Ray2DTest extends TestCase {

	/**
	 * Constructor for Ray2DTest.
	 * @param arg0
	 */
	public Ray2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Ray2DTest.class);
	}

	public void testGetBufferDouble() {
    	Point2D p1 = new Point2D(10, 10);
    	Point2D p2 = new Point2D(20, 30);
    	Ray2D ray = new Ray2D(p1, p2);
		
    	double dist = 5;
    	Domain2D buffer = ray.getBuffer(dist);
    	
    	assertFalse(buffer.isEmpty());
    	assertFalse(buffer.isBounded());
	}
	
	public void testTransformInversion() {
		// create an infinite curve, here a straight line
		Point2D p0 = new Point2D(30, 40);
		Vector2D v0 = new Vector2D(10, 20);
		Ray2D ray = new Ray2D(p0, v0);
		
		Point2D center = new Point2D(50, 0);
		Circle2D circle = new Circle2D(center, 50);
		CircleInversion2D inv = new CircleInversion2D(circle);
		
		CirculinearCurve2D res = ray.transform(inv);
		assertNotNull(res);
		assertTrue(res instanceof CircleArc2D);
		assertTrue(res.contains(center));
		// TODO: make test taking into account accuracy
		// assertTrue(res.getVertices().contains(center));
	}

	public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	Ray2D line1 = new Ray2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	Ray2D line1p = new Ray2D(p1p, p2p);
    	
    	assertTrue(line1.getParallel(1).equals(line1p));
    }

	public void testIsBounded(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		assertTrue(!ray1.isBounded());
	}
	
	public void testGetFirstPoint(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		assertTrue(ray1.getFirstPoint().equals(new Point2D(2, 2)));
	}
	
	public void testGetLastPoint(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		try {
            ray1.getLastPoint();
            fail("Should throw an UnboundedShape2DException");
        }catch(UnboundedShape2DException ex){         
        }
	}
	
	public void testGetBoundingBox(){
		double plus = Double.POSITIVE_INFINITY;
		double minus = Double.NEGATIVE_INFINITY;
		double eps = Shape2D.ACCURACY;
		
		Ray2D ray1 = new Ray2D(2, 2, 1, 1);
		assertTrue(ray1.getBoundingBox().equals(new Box2D(2, plus, 2, plus)));
		
		Ray2D ray2 = new Ray2D(2, 2, 1, 0);
		Box2D ray2Box = new Box2D(2, plus, 2, 2);
		assertTrue(ray2.getBoundingBox().almostEquals(ray2Box, eps));
		
		Ray2D ray3 = new Ray2D(2, 2, -1, -1);
		Box2D ray3Box = new Box2D(minus, 2, minus, 2);
		assertTrue(ray3.getBoundingBox().almostEquals(ray3Box, eps));
	}
	
	public void testGetReverseCurve(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		InvertedRay2D inv1 = new InvertedRay2D(2, 2, -1, 0);

		Curve2D inverted = ray1.getReverseCurve();
		assertTrue(inverted.equals(inv1));
		
	}
	
	public void testContainsDoubleDouble(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		assertTrue(ray1.contains(2, 2));
		assertTrue(ray1.contains(14, 2));
		
		Ray2D ray2 = new Ray2D(2, 2, 1, 2);
		assertTrue(ray2.contains(2, 2));
		assertTrue(ray2.contains(8, 14));
	}
	
	
	public void testClipBox2D(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		Box2D box = new Box2D(-10, 10, -10, 10);
		CurveSet2D<?> clipped = ray1.clip(box);
		assertTrue(clipped.iterator().next().equals(
				new LineSegment2D(2, 2, 10, 2)));
	}
	
	public void testGetViewAnglePoint2D(){
		Ray2D ray1 = new Ray2D(2, 2, 1, 0);
		
		Point2D p1 = new Point2D(1, 1);
		Point2D p2 = new Point2D(3, 1);
		Point2D p3 = new Point2D(3, 3);
		Point2D p4 = new Point2D(1, 3);
		
		assertEquals(ray1.getWindingAngle(p1), -Math.PI/4, 1e-14);
		assertEquals(ray1.getWindingAngle(p2), -3*Math.PI/4, 1e-14);
		assertEquals(ray1.getWindingAngle(p3), 3*Math.PI/4, 1e-14);
		assertEquals(ray1.getWindingAngle(p4), Math.PI/4, 1e-14);
	}
	
	public void testGetDistancePoint2D() {
		Ray2D ray = new Ray2D(1, 2, 3, 4);
		Point2D pt;
		
		// test origin point
		pt = new Point2D(1, 2);
		assertEquals(0, ray.getDistance(pt), 1e-14);
		
		// point on the line (positive extent)
		pt = new Point2D(1+2*3, 2+2*4);
		assertEquals(0, ray.getDistance(pt), 1e-14);
		
		// point on the line (negative extent)
		pt = new Point2D(1-2*3, 2-2*4);
		assertEquals(10, ray.getDistance(pt), 1e-14);
		
		// point outside the line
		pt = new Point2D(5, -1);
		assertEquals(5, ray.getDistance(pt), 1e-14);	
		
		// point outside the line, in the other side
		pt = new Point2D(-3, 5);
		assertEquals(5, ray.getDistance(pt), 1e-14);	
	}

	public void testClone(){
	    Ray2D ray = new Ray2D(10, 20, 30, 40);
	    assertTrue(ray.equals(ray.clone()));
	}
}
