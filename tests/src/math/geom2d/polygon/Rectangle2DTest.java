/*
 * File : Rectangle2DTest.java
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
package math.geom2d.polygon;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.transform.AffineTransform2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class Rectangle2DTest extends TestCase {

	/**
	 * Constructor for Rectangle2DTest.
	 * @param arg0
	 */
	public Rectangle2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(Rectangle2DTest.class);
	}

	public void testGetVerticesNumber() {
		Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
		assertEquals(rect.getVerticesNumber(), 4);
	}

	public void testGetDistance(){
		Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
		assertEquals(rect.getDistance(10, 20), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(40, 20), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(40, 60), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(10, 60), 0, Shape2D.ACCURACY);		
	}

	public void testGetSignedDistance() {
	}

	/*
	 * Test for boolean equals(Rectangle2D)
	 */
	public void testEqualsRectangle2D() {
		Rectangle2D rect1 = new Rectangle2D(10, 20, 30, 40);
		Rectangle2D rect2 = new Rectangle2D(40, 20, 40, 30, Math.PI/2);
		assertTrue(rect1.equals(rect2));
	}

	public void testTransform(){
		// just to test if Rectangle2D is still a FillShape2D after transform
		Rectangle2D rect = new Rectangle2D(10, 20, 40, 50);
		AffineTransform2D aff = new AffineTransform2D(2, 1, 0, 3, 2, 0);
		AffineTransform2D aff2 = new AffineTransform2D(1, 3, .5, 2, .3, 1);
		assertTrue((rect.transform(aff) instanceof math.geom2d.domain.Domain2D));
		assertTrue((rect.transform(aff).transform(aff2) instanceof Domain2D));
	}
	
	public void testClip_Box2D(){
		double L = Math.hypot(100, 50);
		double W = Math.hypot(80, 40);
		double theta = Math.atan2(1, 2);
		Rectangle2D rect = new Rectangle2D(20, -20, L, W, theta);
		Box2D box = new Box2D(0, 100, 0, 90);
		
		// clip the shape, and check the type
		Shape2D clipped = rect.clip(box);
		assertTrue(clipped instanceof SimplePolygon2D);
		
		// create polygon to compare with
		Point2D[] points = new Point2D[]{
				new Point2D(60, 0), 
				new Point2D(100, 20),
				new Point2D(100, 70),
				new Point2D(90, 90),
				new Point2D(40, 90),
				new Point2D(0, 70),
				new Point2D(0, 20),
				new Point2D(10, 0)
		};
		SimplePolygon2D poly = new SimplePolygon2D(points);
		
		assertTrue(clipped.equals(poly));
	}
}
