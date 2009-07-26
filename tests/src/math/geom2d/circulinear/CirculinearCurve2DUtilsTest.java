/* file : CirculinearCurve2DUtilsTest.java
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
package math.geom2d.circulinear;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;

public class CirculinearCurve2DUtilsTest extends TestCase {


	public void testSplitContinuousCurve() {
		// elements
		LineSegment2D edge1 = new LineSegment2D(
				new Point2D(50, 100), new Point2D(150, 100));
		CircleArc2D arc1 = new CircleArc2D(new Point2D(100, 100), 50, 0, Math.PI/2);
		LineSegment2D edge2 = new LineSegment2D(
				new Point2D(100, 150), new Point2D(100, 50));
		
		// create the curve
		PolyCirculinearCurve2D<CirculinearElement2D> curve  = 
			new PolyCirculinearCurve2D<CirculinearElement2D>(
					new CirculinearElement2D[]{edge1, arc1, edge2} );
		
		// split the curve
		Collection<ContinuousCirculinearCurve2D> set = 
			CirculinearCurve2DUtils.splitContinuousCurve(curve);
		
		// should be two parts
		assertTrue(set.size()==2);
	}
	
	public void testSplitIntersectingContours_Circles() {
		Circle2D circle1 = new Circle2D(0, 0, 10);
		Circle2D circle2 = new Circle2D(10, 0, 10);
		Collection<? extends CirculinearContour2D> contours = 
			CirculinearCurve2DUtils.splitIntersectingContours(circle1, circle2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_Rectangles() {
		LinearRing2D poly1 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		LinearRing2D poly2 = new LinearRing2D(new Point2D[]{
				new Point2D(10, 20), new Point2D(40, 20), 
				new Point2D(40, 30), new Point2D(10, 30) });
		
		Collection<CirculinearContour2D> contours = 
			CirculinearCurve2DUtils.splitIntersectingContours(poly1, poly2);
		assertEquals(contours.size(), 2);
	}
	
	public void testSplitIntersectingContours_3Circles() {
		Circle2D ring1 = new Circle2D(150, 150, 100);
		Circle2D ring2 = new Circle2D(250, 150, 100);
		Circle2D ring3 = new Circle2D(200, 220, 100);
		
		ArrayList<CirculinearContour2D> rings = 
			new ArrayList<CirculinearContour2D>(3);
		rings.add(ring1);
		rings.add(ring2);
		rings.add(ring3);

		Collection<CirculinearContour2D> result = 
			CirculinearCurve2DUtils.splitIntersectingContours(rings);
		assertTrue(result.size()==3);
	}
}
