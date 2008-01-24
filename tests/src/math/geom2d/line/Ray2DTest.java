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
import math.geom2d.Point2D;
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
}
