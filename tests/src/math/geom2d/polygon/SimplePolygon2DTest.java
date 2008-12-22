/*
 * File : SimplePolygon2DTest.java
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
 * Created on 3 janv. 2004
 */
package math.geom2d.polygon;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class SimplePolygon2DTest extends TestCase {

	/**
	 * Constructor for SimplePolygon2DTest.
	 * @param arg0
	 */
	public SimplePolygon2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(SimplePolygon2DTest.class);
	}

	public void testGetComplement(){
		// start with a simple rectangle
		Point2D points[] = new Point2D[4];
		points[0] = new Point2D(10, 10);
		points[1] = new Point2D(20, 10);
		points[2] = new Point2D(20, 20);
		points[3] = new Point2D(10, 20);
		SimplePolygon2D poly = new SimplePolygon2D(points);
		
		SimplePolygon2D poly2 = poly.complement();
		assertEquals(poly2.getSignedArea(), -poly.getSignedArea());
	}
	
	/*
	 * Test for boolean contains(double, double)
	 */
	public void testContainsdoubledouble(){
		
		// start with a simple rectangle
		Point2D points[] = new Point2D[4];
		points[0] = new Point2D(20, 20);
		points[1] = new Point2D(40, 20);
		points[2] = new Point2D(40, 60);
		points[3] = new Point2D(20, 60);
		SimplePolygon2D poly = new SimplePolygon2D(points);
		
		assertTrue(poly.contains(20, 20));
		assertTrue(poly.contains(40, 20));
		assertTrue(poly.contains(40, 60));
		assertTrue(poly.contains(20, 60));
		assertTrue(poly.contains(25, 20));
		assertTrue(poly.contains(25, 40));
		assertTrue(!poly.contains(10, 20));
		assertTrue(!poly.contains(50, 20));
		assertTrue(!poly.contains(10, 10));
		assertTrue(poly.contains(25, 25));

		// try some more complicated figures, in order to test particular
		// cases of the algorithm
		points = new Point2D[6];
		points[0] = new Point2D(40, 70);
		points[1] = new Point2D(40, 50);
		points[2] = new Point2D(20, 50);
		points[3] = new Point2D(60, 10);
		points[4] = new Point2D(60, 30);
		points[5] = new Point2D(80, 30);
		poly = new SimplePolygon2D(points);
		
		// classic case
		assertTrue(poly.contains(60, 40));
		// problematic case
		assertTrue(poly.contains(50, 40));

		points = new Point2D[8];
		points[0] = new Point2D(10, 60);
		points[1] = new Point2D(10, 40);
		points[2] = new Point2D(20, 50);
		points[3] = new Point2D(20, 20);
		points[4] = new Point2D(10, 30);
		points[5] = new Point2D(10, 10);
		points[6] = new Point2D(40, 10);
		points[7] = new Point2D(40, 60);
		poly = new SimplePolygon2D(points);
		
		// classic cases
		assertTrue(poly.contains(15, 15));
		assertTrue(poly.contains(25, 40));
		assertTrue(!poly.contains(15, 37));
		
		// problematic cases
		assertTrue(poly.contains(30, 35));
		assertTrue(!poly.contains(10, 35));
		assertTrue(!poly.contains(5, 35));		
	}

	public void testGetCentroiddoubledouble(){
		// start with a simple rectangle
		Point2D points[] = new Point2D[4];
		points[0] = new Point2D(20, 20);
		points[1] = new Point2D(40, 20);
		points[2] = new Point2D(40, 60);
		points[3] = new Point2D(20, 60);
		SimplePolygon2D poly = new SimplePolygon2D(points);

		Point2D centro = new Point2D(30, 40);
		assertTrue(centro.equals(poly.getCentroid()));
		
		
		// a cross centered around (15, 15), in reverse order
		poly = new SimplePolygon2D(new Point2D[]{
				new Point2D(10, 0),
				new Point2D(10, 10),
				new Point2D(0, 10),
				new Point2D(0, 20),
				new Point2D(10, 20),
				new Point2D(10, 30),
				new Point2D(20, 30),
				new Point2D(20, 20),
				new Point2D(30, 20),
				new Point2D(30, 10),
				new Point2D(20, 10),
				new Point2D(20, 0)});
		centro = new Point2D(15, 15);
		assertTrue(centro.equals(poly.getCentroid()));
	}
	
	public void testGetBounds2D(){
		Point2D points[] = new Point2D[6];
		points[0] = new Point2D(40, 70);
		points[1] = new Point2D(40, 50);
		points[2] = new Point2D(20, 50);
		points[3] = new Point2D(60, 10);
		points[4] = new Point2D(60, 30);
		points[5] = new Point2D(80, 30);
		SimplePolygon2D poly = new SimplePolygon2D(points);
		
		Box2D box = new Box2D(20, 80, 10, 70);
		Box2D bounds = new Box2D(poly.getBounds2D());
		assertTrue(box.equals(bounds));
	}

	public void testContainsBounds(){
		Point2D points[] = new Point2D[6];
		points[0] = new Point2D(40, 70);
		points[1] = new Point2D(40, 50);
		points[2] = new Point2D(20, 50);
		points[3] = new Point2D(60, 10);
		points[4] = new Point2D(60, 30);
		points[5] = new Point2D(80, 30);
		SimplePolygon2D poly = new SimplePolygon2D(points);
		
		Rectangle2D rect = new Rectangle2D(20, 10, 60, 60);
		//Rectangle2D bounds = new Rectangle2D(poly.getBounds2D());
		assertTrue(rect.containsBounds(poly));
	}
}
