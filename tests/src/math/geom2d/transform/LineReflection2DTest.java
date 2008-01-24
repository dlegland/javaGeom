/* File LineReflection2DTest.java 
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
 * Created on 24 oct. 2004
 */

package math.geom2d.transform;

import math.geom2d.Point2D;
import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class LineReflection2DTest extends TestCase {

	/**
	 * Constructor for LineSymmetry2DTest.
	 * @param arg0
	 */
	public LineReflection2DTest(String arg0) {
		super(arg0);
	}
	
	public void testGetSymmetricPoint2D(){
		LineReflection2D sym1 = new LineReflection2D(new StraightLine2D(2, 2, 5, 0));
		LineReflection2D sym2 = new LineReflection2D(new StraightLine2D(2, 2, 3, 3));
		LineReflection2D sym3 = new LineReflection2D(new StraightLine2D(2, 2, 0, 4));
		LineReflection2D sym4 = new LineReflection2D(new StraightLine2D(2, 2, 3, 1));

		Point2D p1 = new Point2D(5, 1); 
		Point2D p2 = new Point2D(5, 3); 
		Point2D p3 = new Point2D(3, 5); 
		Point2D p4 = new Point2D(2, 2);
		
		assertEquals(p1.transform(sym1), new Point2D(5, 3));
		assertEquals(p2.transform(sym1), new Point2D(5, 1));
		assertEquals(p3.transform(sym1), new Point2D(3, -1));
		assertEquals(p4.transform(sym1), new Point2D(2, 2));

		assertEquals(p1.transform(sym2), new Point2D(1, 5));
		assertEquals(p2.transform(sym2), new Point2D(3, 5));
		assertEquals(p3.transform(sym2), new Point2D(5, 3));
		assertEquals(p4.transform(sym2), new Point2D(2, 2));

		assertEquals(p1.transform(sym3), new Point2D(-1, 1));
		assertEquals(p2.transform(sym3), new Point2D(-1, 3));
		assertEquals(p3.transform(sym3), new Point2D(1, 5));
		assertEquals(p4.transform(sym3), new Point2D(2, 2));

		p1.setLocation(6, 0);
		assertEquals(p4.transform(sym4), new Point2D(2, 2));
		assertEquals(p2.transform(sym4), new Point2D(5, 3));
		assertEquals(p1.transform(sym4), new Point2D(4, 6));
		
		p1.setLocation(25, -20);
		//p1.setLocation(6, 5);
		Point2D pt1 = new Point2D(10, 20);
		Point2D pt2 = new Point2D(90, 50);
		StraightLine2D line = new StraightLine2D(pt1, pt2);
		Point2D ptSym = line.getSymmetric(p1);
		LineReflection2D sym = new LineReflection2D(line);
		//Point2D ptSyml = (Point2D)p1.transform(sym);
		assertEquals(p1.transform(sym), ptSym);
		/*
		assertEquals(p2.transform(sym4), new Point2D(-1, 3));
		assertEquals(p3.transform(sym4), new Point2D(1, 5));
		assertEquals(p4.transform(sym4), new Point2D(2, 2));*/
	}
	
	/**
	 * Check if the inverse transform is a Reflection2D.
	 */
	public void testGetInverseTransform2D(){
		StraightLine2D line = new StraightLine2D(new Point2D(0, 0), 
				new Point2D(1, 0));
		LineReflection2D ref = new LineReflection2D(line);
		AffineTransform2D inv = ref.getInverseTransform();
		assertTrue(inv instanceof LineReflection2D);
		
		LineReflection2D ref2 = (LineReflection2D) inv;
		assertTrue(ref.getReflectionLine().equals(ref2.getReflectionLine()));
	}
	
	public void testComposeLineReflection2D(){
		LineReflection2D ref1 = new LineReflection2D(
				new StraightLine2D(2, 2, 1, 0));
		LineReflection2D ref2 = new LineReflection2D(
				new StraightLine2D(2, 2, 0, 1));
		LineReflection2D ref3 = new LineReflection2D(
				new StraightLine2D(2, 4, 1, 0));
		
		AffineTransform2D comp2 = ref1.compose(ref2);
		assertTrue(comp2.equals(new Rotation2D(2, 2, Math.PI/2)));
		
		AffineTransform2D comp3 = ref1.compose(ref3);
		assertTrue(comp3.equals(new Translation2D(0, 2)));
	}

}
