/* file : CurveArray2DTest.java
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
package math.geom2d.curve;

import junit.framework.TestCase;
import java.util.*;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.LineSegment2D;

public class CurveArray2DTest extends TestCase {

    public void testCreateArray() {
        Point2D p1 = new Point2D(0, 0);
        Point2D p2 = new Point2D(10, 0);
        Point2D p3 = new Point2D(10, 10);
        Point2D p4 = new Point2D(0, 10);
        LineSegment2D seg1 = new LineSegment2D(p1, p2);
        LineSegment2D seg2 = new LineSegment2D(p2, p3);
        LineSegment2D seg3 = new LineSegment2D(p3, p4);
        CurveArray2D<LineSegment2D> set = CurveArray2D.create(seg1, seg2, seg3);
        assertEquals(3, set.size());
        assertTrue(set.contains(seg1));
        assertTrue(set.contains(seg2));
        assertTrue(set.contains(seg3));
    }

    public void testGetPosition() {
        double r = 10;
        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * Math.PI / 3, 2 * Math.PI / 3);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * Math.PI / 3, 2 * Math.PI / 3);
        CurveArray2D<CircleArc2D> set = new CurveArray2D<>(2);
        set.add(arc1);
        set.add(arc2);

        assertEquals(set.size(), 2);
        assertEquals(set.t0(), 0, 1e-14);
        assertEquals(set.t1(), 3, 1e-14);

        double pos1 = .3;
        Point2D point1 = set.point(pos1);
        assertEquals(set.position(point1), pos1, 1e-10);

        pos1 = 1.2;
        point1 = set.point(pos1);
        assertEquals(set.position(point1), 1, 1e-10);

        pos1 = 1.6;
        point1 = set.point(pos1);
        assertEquals(set.position(point1), 2, 1e-10);

        pos1 = 2.3;
        point1 = set.point(pos1);
        assertEquals(set.position(point1), pos1, 1e-10);
    }

    public void testGetLocalPosition() {
        double r = 10;
        double extent = 2 * Math.PI / 3;
        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * Math.PI / 3, extent);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * Math.PI / 3, extent);
        CurveArray2D<CircleArc2D> set = new CurveArray2D<>(2);
        set.add(arc1);
        set.add(arc2);

        assertEquals(set.localPosition(0), 0, IShape2D.ACCURACY);
        assertEquals(set.localPosition(1), extent, IShape2D.ACCURACY);
        assertEquals(set.localPosition(2), 0, IShape2D.ACCURACY);
        assertEquals(set.localPosition(3), extent, IShape2D.ACCURACY);
    }

    public void testGetGlobalPosition() {
        double r = 10;
        double extent = 2 * Math.PI / 3;
        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * Math.PI / 3, extent);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * Math.PI / 3, extent);
        CurveArray2D<CircleArc2D> set = new CurveArray2D<>(2);
        set.add(arc1);
        set.add(arc2);

        assertEquals(set.globalPosition(0, 0), 0, IShape2D.ACCURACY);
        assertEquals(set.globalPosition(0, extent), 1, IShape2D.ACCURACY);
        assertEquals(set.globalPosition(1, 0), 2, IShape2D.ACCURACY);
        assertEquals(set.globalPosition(1, extent), 3, IShape2D.ACCURACY);
    }

    /*
     * Test method for 'math.geom2d.CurveSet2D.getSubCurve(double, double)'
     */
    public void testGetSubCurve() {
        double r = 10;
        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * Math.PI / 3, 2 * Math.PI / 3);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * Math.PI / 3, 2 * Math.PI / 3);
        CircleArc2D arc1h1 = new CircleArc2D(0, 0, r, 0, Math.PI / 3);
        CircleArc2D arc2h1 = new CircleArc2D(r, 0, r, 2 * Math.PI / 3, Math.PI / 3);
        CircleArc2D arc1h2 = new CircleArc2D(0, 0, r, 5 * Math.PI / 3, Math.PI / 3);
        CircleArc2D arc2h2 = new CircleArc2D(r, 0, r, Math.PI, Math.PI / 3);

        CurveArray2D<CircleArc2D> set = new CurveArray2D<>(2);
        set.add(arc1);
        set.add(arc2);

        ICurve2D sub1 = set.subCurve(0, 2);
        assertTrue(sub1 instanceof ICurveSet2D<?>);
        ICurveSet2D<?> subset = (ICurveSet2D<?>) sub1;
        assertEquals(subset.size(), 2);

        double pos0 = set.position(new Point2D(r, 0));
        double pos1 = set.position(new Point2D(0, 0));
        sub1 = set.subCurve(pos0, pos1);
        assertTrue(sub1 instanceof ICurveSet2D<?>);
        subset = (ICurveSet2D<?>) sub1;
        assertEquals(subset.size(), 2);
        Iterator<?> iter = subset.curves().iterator();
        sub1 = (ICurve2D) iter.next();
        assertTrue(arc1h1.almostEquals(sub1, IShape2D.ACCURACY));
        sub1 = (ICurve2D) iter.next();
        assertTrue(arc2h1.almostEquals(sub1, IShape2D.ACCURACY));

        sub1 = set.subCurve(pos1, pos0);
        assertTrue(sub1 instanceof ICurveSet2D<?>);
        subset = (ICurveSet2D<?>) sub1;
        assertEquals(subset.size(), 2);
        iter = subset.curves().iterator();
        sub1 = (ICurve2D) iter.next();
        assertTrue(arc2h2.almostEquals(sub1, IShape2D.ACCURACY));
        sub1 = (ICurve2D) iter.next();
        assertTrue(arc1h2.almostEquals(sub1, IShape2D.ACCURACY));
    }

    public void testIsSingular() {
        // Create a curve set with 2 line segments
        LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(2, 2));
        LineSegment2D line2 = new LineSegment2D(new Point2D(2, 0), new Point2D(-2, 4));
        CurveArray2D<LineSegment2D> set = new CurveArray2D<>(new LineSegment2D[] { line1, line2 });

        assertTrue(set.isSingular(0));
        assertTrue(!set.isSingular(0.5));
        assertTrue(set.isSingular(1));
        assertTrue(set.isSingular(1.5));
        assertTrue(set.isSingular(2));
        assertTrue(!set.isSingular(2.5));
        assertTrue(set.isSingular(3));
    }

    public void testClipEmptyCurveSet() {
        Box2D box = new Box2D(-10, 10, -10, 10);

        ICurveSet2D<ICurve2D> set1 = new CurveArray2D<>();
        ICurveSet2D<?> clipped = set1.clip(box);
        assertTrue(clipped.isEmpty());
    }

}
