/*
 * File : LineObject2DTest.java
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
 * Created on 29 déc. 2003
 */
package math.geom2d.line;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class LineObject2DTest extends TestCase {

	/**
	 * Constructor for LineObject2DTest.
	 * @param arg0
	 */
	public LineObject2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(LineObject2DTest.class);
	}

	/*
	 * Test for double getDistance(Point2D)
	 */
	public void testInstanceOfStraightObject2D() {
		Point2D p1 = new Point2D(2, 3);
		Point2D p2 = new Point2D(4, 7);
		LineObject2D line = new LineObject2D(p1, p2);
		assertTrue(line instanceof LinearShape2D);
		assertTrue(LinearShape2D.class.isInstance(line));
	}
	
	public void testGetViewAngle() {
		Point2D p1 = new Point2D(2, 2);
		Point2D p2 = new Point2D(0, 2);
		Point2D pr = new Point2D(1, 1);
		
		LineObject2D line1 = new LineObject2D(p1, p2);
		assertEquals(line1.getWindingAngle(pr), Math.PI/2, Shape2D.ACCURACY);
		line1.setPoint2(null);
		assertEquals(line1.getWindingAngle(pr), 3*Math.PI/4, Shape2D.ACCURACY);
		 
	}

}
