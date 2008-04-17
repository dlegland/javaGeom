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
package math.geom2d;

import junit.framework.TestCase;

import math.geom2d.curve.Boundary2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.transform.AffineTransform2D;

public class Box2DTest extends TestCase {

	public void testContains_DoubleDouble(){
		Box2D box = new Box2D(-1, 1, -1, 1);
		assertTrue(box.contains(0, 0));
		assertTrue(!box.contains(-1, 2));
	}	
	
	public void testTransform(){
		Box2D box = new Box2D(-1, 1, -1, 1);
		
		AffineTransform2D trans = AffineTransform2D.createTranslation(1, 1);		
		assertTrue(box.transform(trans).equals(new Box2D(0, 2, 0, 2)));
		
		AffineTransform2D rot = AffineTransform2D.createRotation(0, 0, Math.PI/2);		
		assertTrue(box.transform(rot).equals(new Box2D(-1, 1, -1, 1)));
	}
	
	public void testGetBoundary(){
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
		Box2D box0000 = new Box2D(x1, x2, y1, y2);
		Boundary2D bnd0000 = box0000.getBoundary();
		assertTrue(bnd0000.equals(new ClosedPolyline2D(
				new Point2D[]{p11, p21, p22, p12})));
		
		// case of totally unbounded box
		Box2D box1111 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		Boundary2D bnd1111 = box1111.getBoundary();
		assertTrue(bnd1111.isEmpty());
		
		// both y bounded		
		Box2D box1100 = new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, y1, y2);
		Boundary2D bnd1100 = box1100.getBoundary();
		assertTrue(bnd1100.equals(new StraightLine2D(x1, y1, 1, 0)));
	}
}
