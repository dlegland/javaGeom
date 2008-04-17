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
}
