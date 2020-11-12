/* file : PolyCurve2DTest.java
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
 * Created on 25 mars 2007
 *
 */
package net.javageom.geom2d.curve;

import junit.framework.TestCase;
import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.conic.CircleArc2D;
import net.javageom.geom2d.curve.ContinuousCurve2D;
import net.javageom.geom2d.curve.Curve2D;
import net.javageom.geom2d.curve.CurveSet2D;
import net.javageom.geom2d.curve.PolyCurve2D;

public class PolyCurve2DTest extends TestCase {

	/*
	 * Test method for 'math.geom2d.PolyCurve2D.getSubCurve(double, double)'
	 */
	public void testGetSubCurve() {
		double r = 10;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		PolyCurve2D<CircleArc2D> set = new PolyCurve2D<CircleArc2D>();
		set.add(arc1);
		set.add(arc2);
		
		Curve2D sub1 = set.subCurve(0, 2);
		assertTrue(sub1 instanceof CurveSet2D<?>);
		assertTrue(sub1 instanceof PolyCurve2D<?>);
		
		CurveSet2D<?> subset = (PolyCurve2D<?>) sub1;
		assertEquals(subset.size(), 2);
	}
	
	/*
	 * Test method for 'math.geom2d.PolyCurve2D.getClippedShape()'
	 */
	public void testClip() {
		double r = 10;
		double L = 40;

		Box2D box1 = new Box2D(-L/2, L/2, -L/2, L/2);
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		PolyCurve2D<CircleArc2D> set = new PolyCurve2D<CircleArc2D>();
		set.add(arc1);
		set.add(arc2);
		
		CurveSet2D<?> clipped = set.clip(box1);
		Curve2D curve1 = clipped.firstCurve();
		assertTrue(curve1 instanceof ContinuousCurve2D);
	}

}
