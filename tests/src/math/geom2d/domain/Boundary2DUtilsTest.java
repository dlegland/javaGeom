/* file : CurveUtilTest.java
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
package math.geom2d.domain;

import junit.framework.TestCase;
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.domain.ContinuousOrientedCurve2D;
import math.geom2d.domain.PolyOrientedCurve2D;

public class Boundary2DUtilsTest extends TestCase {


	public void testClipBoundary_Circle2D() {
		Box2D box = new Box2D(0, 10, 0, 10);

		Circle2D circle1 = new Circle2D(5, 7, 2);
		ContourArray2D<Contour2D> boundary = 
			new ContourArray2D<Contour2D>(circle1);		
		ContourArray2D<Contour2D> clipped =
			Boundary2DUtils.clipBoundary(boundary, box);
		 
		Collection<Contour2D> curves = clipped.getBoundaryCurves();
		Contour2D curve = curves.iterator().next();
		assertTrue(curve instanceof Circle2D);
		assertTrue(circle1.equals(curve));
			
		
		Circle2D circle2 = new Circle2D(5, 0, 2);
		CircleArc2D circlearc2 = new CircleArc2D(5, 0, 2, 0, Math.PI);
		//BoundarySet2D boundary2 = new BoundarySet2D(circle2);		
		ContourArray2D<Contour2D> clipped2 =
			Boundary2DUtils.clipBoundary(circle2, box);
		 
		Collection<Contour2D> curves2 = clipped2.getBoundaryCurves();
		Contour2D curve2 = curves2.iterator().next();
		assertTrue(!(curve2 instanceof CircleArc2D));
		assertTrue(curve2 instanceof PolyOrientedCurve2D);
		
		Iterator<?> iter = ((PolyOrientedCurve2D<?>) curve2).getCurves().iterator();
		ContinuousOrientedCurve2D curve3 = (ContinuousOrientedCurve2D) iter.next();
		
		assertTrue(curve3 instanceof CircleArc2D);
		assertTrue(curve3.equals(circlearc2));
		
		double r = 10;
		CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5*Math.PI/3, 2*Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2*Math.PI/3, 2*Math.PI/3);		
		BoundaryPolyCurve2D<CircleArc2D> set = new BoundaryPolyCurve2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		boundary = new ContourArray2D<Contour2D>();
		boundary.addCurve(set);
		double L = 40;
		double l = 10;
		box = new Box2D(-L/2, L/2, -l/2, l/2);
		clipped = Boundary2DUtils.clipBoundary(boundary, box);
		curves = clipped.getBoundaryCurves();
		assertTrue(curves.size()==1);
		curve = curves.iterator().next();	
		assertTrue(curve instanceof PolyOrientedCurve2D);
	}
}
