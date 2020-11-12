/* file : Box2DTest.java
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
 * Created on 7 mars 2007
 *
 */
package net.javageom.geom2d;

import junit.framework.TestCase;
import net.javageom.geom2d.domain.Boundary2D;
import net.javageom.geom2d.polygon.LinearRing2D;

public class Box2DTest extends TestCase {

	public void testContains_DoubleDouble(){
		Box2D box = new Box2D(-1, 1, -1, 1);
		assertTrue(box.contains(0, 0));
        assertFalse(box.contains(-2, 0));
        assertFalse(box.contains(2, 0));
        assertFalse(box.contains(0, 2));
        assertFalse(box.contains(0, -2));
	}	
	
	public void testTransform(){
		Box2D box = new Box2D(-1, 1, -1, 1);
		
		AffineTransform2D trans = AffineTransform2D.createTranslation(1, 1);		
		assertTrue(box.transform(trans).equals(new Box2D(0, 2, 0, 2)));
		
		AffineTransform2D rot = AffineTransform2D.createRotation(0, 0, Math.PI/2);		
		assertTrue(box.transform(rot).equals(new Box2D(-1, 1, -1, 1)));
	}
	
	public void testGetWidth() {
	    Box2D box = new Box2D(-10, 10, -20, 20);
	    assertEquals(20, box.getWidth(), 1e-14);
	}
    
    public void testGetHeight() {
        Box2D box = new Box2D(-10, 10, -20, 20);
        assertEquals(40, box.getHeight(), 1e-14);
    }

    public void testBoundary() {
		// naming convention:
		// box + 4 digits, each digit correspond to bounding info in one direction
		// in order: x0 x1 y0 y1
		// 0 = bounded, 1 = unbounded
		//double x0 = -10;
		double x1 = 0;
		double x2 = 10;
		//double x3 = 20;
		
		//double y0 = -10;
		double y1 = 0;
		double y2 = 10;
		//double y3 = 20;
		
		// The clipping box for box boundary
		//Box2D box = new Box2D(x0, x3, y0, y3);
		
		Point2D p11 = new Point2D(x1, y1);
		Point2D p12 = new Point2D(x1, y2);
		Point2D p21 = new Point2D(x2, y1);
		Point2D p22 = new Point2D(x2, y2);
		
		// case of totally bounded box
		Box2D box1111 = new Box2D(x1, x2, y1, y2);
		Boundary2D bnd1111 = box1111.boundary();
		assertTrue(bnd1111.equals(new LinearRing2D(
				new Point2D[]{p11, p21, p22, p12})));
		
		// case of totally unbounded box
		Box2D box0000 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		Boundary2D bnd0000 = box0000.boundary();
		assertTrue(bnd0000.isEmpty());
		
		
		// bounded by two y-limits
		Box2D box0011 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, y1, y2);
		Boundary2D bnd0011 = box0011.boundary();
		assertEquals(2, bnd0011.continuousCurves().size());
		
		// bounded by y lower limit	
		Box2D box0010 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				y1, Double.POSITIVE_INFINITY);
		Boundary2D bnd0010 = box0010.boundary();
		assertEquals(1, bnd0010.continuousCurves().size());
		
		// bounded by y upper limit	
		Box2D box0001 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				Double.NEGATIVE_INFINITY, y2);
		Boundary2D bnd0001 = box0001.boundary();
		assertEquals(1, bnd0001.continuousCurves().size());


		// bounded by two x-limits
		Box2D box1100 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, y1, y2);
		Boundary2D bnd1100 = box1100.boundary();
		assertEquals(2, bnd1100.continuousCurves().size());
		
		// bounded by x lower limit	
		Box2D box1000 = new Box2D(
				x1, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		Boundary2D bnd1000 = box1000.boundary();
		assertEquals(1, bnd1000.continuousCurves().size());
		
		// bounded by x upper limit	
		Box2D box0111 = new Box2D(
				Double.NEGATIVE_INFINITY, x2,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		Boundary2D bnd0111 = box0111.boundary();
		assertEquals(1, bnd0111.continuousCurves().size());
    }

}
