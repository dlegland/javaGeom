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

import junit.framework.TestCase;
import math.geom2d.Shape2D;

/**
 * @author Legland
 */
@Deprecated
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
		assertEquals(rect.getVertexNumber(), 4);
	}

	public void testGetDistance(){
		Rectangle2D rect = new Rectangle2D(10, 20, 30, 40);
		assertEquals(rect.getDistance(10, 20), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(40, 20), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(40, 60), 0, Shape2D.ACCURACY);		
		assertEquals(rect.getDistance(10, 60), 0, Shape2D.ACCURACY);		
	}

	/*
	 * Test for boolean equals(Rectangle2D)
	 */
	public void testEqualsRectangle2D() {
		Rectangle2D rect1 = new Rectangle2D(10, 20, 30, 40);
		Rectangle2D rect2 = new Rectangle2D(40, 20, 40, 30, Math.PI/2);
		assertTrue(rect1.equals(rect2));
	}
}
