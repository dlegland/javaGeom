/*
 * File : HalfPlane2DTest.java
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
 * Created on 30 déc. 2003
 */
package math.geom2d.polygon;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.LinearShape2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class HalfPlane2DTest extends TestCase {

	/**
	 * Constructor for HalfPlane2DTest.
	 * @param arg0
	 */
	public HalfPlane2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(HalfPlane2DTest.class);
	}

	public void testIsBounded() {
		Point2D p1 = new Point2D(-1, 1);
		Point2D p2 = new Point2D(1, 2);
		LinearShape2D line1 = new StraightLine2D(p2, p1);
		line1 = new Ray2D(-10, -10, 30, 20);		
		HalfPlane2D plane = new HalfPlane2D(line1);
		assertTrue(!plane.isBounded());
	}

	public void testGetBoundary() {
		Point2D p1 = new Point2D(-1, 1);
		Point2D p2 = new Point2D(1, 2);
		LinearShape2D line1 = new StraightLine2D(p2, p1);
		line1 = new Ray2D(-10, -10, 30, 20);		
		HalfPlane2D plane = new HalfPlane2D(line1);
		assertTrue(plane.getBoundary().equals(new StraightLine2D(line1)));
	}

	public void testGetDistance() {
		HalfPlane2D plane;
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 1));
		assertEquals(plane.getDistance(new Point2D(2, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, -1, -1));
		assertEquals(plane.getDistance(new Point2D(2, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(1, 3)), Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 0));
		assertEquals(plane.getDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(1, 1)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, -1, 0));
		assertEquals(plane.getDistance(new Point2D(1, 3)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(1, 1)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, 0, 1));
		assertEquals(plane.getDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(0, 2)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), 1, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, 0, -1));
		assertEquals(plane.getDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(0, 2)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);
	}

	public void testGetSignedDistance() {
		HalfPlane2D plane;
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 1));
		assertEquals(plane.getSignedDistance(new Point2D(2, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, -1, -1));
		assertEquals(plane.getSignedDistance(new Point2D(2, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), Math.sqrt(2)/2, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), -Math.sqrt(2)/2, Shape2D.ACCURACY);
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 0));
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), -1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(1, 1)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, -1, 0));
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(1, 1)), -1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), 0, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, 0, 1));
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(0, 2)), -1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), 1, Shape2D.ACCURACY);

		plane = new HalfPlane2D(new StraightLine2D(1, 2, 0, -1));
		assertEquals(plane.getSignedDistance(new Point2D(1, 3)), 0, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(0, 2)), 1, Shape2D.ACCURACY);
		assertEquals(plane.getSignedDistance(new Point2D(2, 2)), -1, Shape2D.ACCURACY);
	}

	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble() {
		HalfPlane2D plane;
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 1));
		assertTrue(plane.contains(2, 3));
		assertTrue(plane.contains(1, 3));
		assertTrue(!plane.contains(2, 2));
		
		plane = new HalfPlane2D(new StraightLine2D(1, 2, 1, 0));
		assertTrue(plane.contains(1, 3));
		assertTrue(plane.contains(2, 2));
		assertTrue(!plane.contains(1, 1));

		plane = new HalfPlane2D(new StraightLine2D(1, 2, 0, 1));
		assertTrue(plane.contains(1, 3));
		assertTrue(plane.contains(0, 2));
		assertTrue(!plane.contains(2, 2));		
	}

	public void testGetBoundingBox(){
		HalfPlane2D plane;
		Box2D box, bound;
		double plusInf = Double.POSITIVE_INFINITY;
		double minusInf  = Double.NEGATIVE_INFINITY;
		
		// horizontal (1,0)
		plane = new HalfPlane2D(new StraightLine2D(10, 20, 1, 0));
		box = new Box2D(minusInf, plusInf, 20, plusInf);
		bound = plane.getBoundingBox();
		assertTrue(box.equals(bound));
		
		// horizontal (-1,0)
		plane = new HalfPlane2D(new StraightLine2D(10, 20, -1, 0));
		box = new Box2D(minusInf, plusInf, minusInf, 20);
		bound = plane.getBoundingBox();
		assertTrue(box.equals(bound));
		
		// vertical (0,1)
		plane = new HalfPlane2D(new StraightLine2D(10, 20, 0, 1));
		box = new Box2D(minusInf, 10, minusInf, plusInf);
		bound = plane.getBoundingBox();
		assertTrue(box.equals(bound));
		
		// vertical (0, -1)
		plane = new HalfPlane2D(new StraightLine2D(10, 20, 0, -1));
		box = new Box2D(10, plusInf, minusInf, plusInf);
		bound = plane.getBoundingBox();
		assertTrue(box.equals(bound));
		
		// basic
		plane = new HalfPlane2D(new StraightLine2D(10, 20, 2, 1));
		box = new Box2D(minusInf, plusInf, minusInf, plusInf);
		bound = plane.getBoundingBox();
		assertTrue(box.equals(bound));
	}

}
