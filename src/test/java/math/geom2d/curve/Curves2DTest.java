/* file : Curve2DUtilsTest.java
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
package math.geom2d.curve;

import java.util.Iterator;

import junit.framework.TestCase;
import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Polyline2D;
import static java.lang.Math.PI;

public class Curves2DTest extends TestCase {

    /*
     * Test method for 'math.geom2d.Box2D.clipCurve(Curve2D)'
     */
    public void testClipCurve_LineSegment2D() {
        Box2D box = new Box2D(0, 10, 0, 10);
        LineSegment2D line1 = new LineSegment2D(2, -2, 12, 8);
        LineSegment2D clip1 = new LineSegment2D(4, 0, 10, 6);

        ICurveSet2D<? extends ICurve2D> curveSet = Curves2D.clipCurve(line1, box);
        ICurve2D curve = curveSet.firstCurve();
        assertTrue(clip1.equals(curve));
    }

    public void testClipCurve_Circle2D() {
        Box2D box = new Box2D(0, 10, 0, 10);
        Circle2D line1 = new Circle2D(0, 0, 5, true);
        CircleArc2D clip1 = new CircleArc2D(0, 0, 5, 0, PI / 2);

        ICurveSet2D<? extends ICurve2D> curveSet = Curves2D.clipCurve(line1, box);
        ICurve2D curve = curveSet.firstCurve();
        assertTrue(clip1.equals(curve));
    }

    public void testClipCurve_CircleArc2D() {
        double r = 10;
        double L = 40;
        double l = 10;
        Box2D box = new Box2D(-L / 2, L / 2, -l / 2, l / 2);
        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * PI / 3, 2 * PI / 3);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * PI / 3, 2 * PI / 3);

        ICurveSet2D<? extends ICurve2D> clipped1 = Curves2D.clipCurve(arc1, box);
        ICurve2D curve1 = clipped1.firstCurve();

        ICurveSet2D<? extends ICurve2D> clipped2 = Curves2D.clipCurve(arc2, box);
        ICurve2D curve2 = clipped2.firstCurve();

        double alpha = Math.asin(l / 2 / r);
        CircleArc2D arc1c = new CircleArc2D(0, 0, r, Angle2DUtil.formatAngle(PI * 2 - alpha), 2 * alpha);
        CircleArc2D arc2c = new CircleArc2D(r, 0, r, Angle2DUtil.formatAngle(PI - alpha), 2 * alpha);

        assertTrue(arc1c.almostEquals(curve1, IShape2D.ACCURACY));
        assertTrue(arc2c.almostEquals(curve2, IShape2D.ACCURACY));
    }

    public void testClipCurve_CurveSet2D() {
        double r = 10;
        double L = 40;
        double l = 10;

        Box2D box = new Box2D(-L / 2, L / 2, -l / 2, l / 2);

        CircleArc2D arc1 = new CircleArc2D(0, 0, r, 5 * PI / 3, 2 * PI / 3);
        CircleArc2D arc2 = new CircleArc2D(r, 0, r, 2 * PI / 3, 2 * PI / 3);
        CurveArray2D<CircleArc2D> set = new CurveArray2D<>(2);
        set.add(arc1);
        set.add(arc2);

        ICurveSet2D<? extends ICurve2D> clippedSet = Curves2D.clipCurve(set, box);
        Iterator<? extends ICurve2D> iter = clippedSet.curves().iterator();
        ICurve2D curve1 = iter.next();
        ICurve2D curve2 = iter.next();

        double alpha = Math.asin(l / 2 / r);
        CircleArc2D arc1c = new CircleArc2D(0, 0, r, Angle2DUtil.formatAngle(PI * 2 - alpha), 2 * alpha);
        CircleArc2D arc2c = new CircleArc2D(r, 0, r, Angle2DUtil.formatAngle(PI - alpha), 2 * alpha);

        assertTrue(arc1c.almostEquals(curve1, IShape2D.ACCURACY));
        assertTrue(arc2c.almostEquals(curve2, IShape2D.ACCURACY));

    }

    public void testClipCurve_Polyline2D() {
        Polyline2D poly1 = new Polyline2D(new Point2D[] { new Point2D(10, -5), new Point2D(25, 10), new Point2D(10, 25), new Point2D(-5, 10), new Point2D(10, -5), });
        Box2D box = new Box2D(0, 20, 0, 20);

        ICurveSet2D<? extends ICurve2D> clip1 = Curves2D.clipCurve(poly1, box);
        Polyline2D sub1 = new Polyline2D(new Point2D[] { new Point2D(15, 0), new Point2D(20, 5) });
        Polyline2D sub2 = new Polyline2D(new Point2D[] { new Point2D(20, 15), new Point2D(15, 20) });
        Polyline2D sub3 = new Polyline2D(new Point2D[] { new Point2D(5, 20), new Point2D(0, 15) });
        Polyline2D sub4 = new Polyline2D(new Point2D[] { new Point2D(0, 5), new Point2D(5, 0) });
        CurveArray2D<ICurve2D> set1 = new CurveArray2D<>(4);
        set1.add(sub1);
        set1.add(sub2);
        set1.add(sub3);
        set1.add(sub4);
        assertTrue(set1.almostEquals(clip1, IShape2D.ACCURACY));
    }

    public void testFindNextCurveIndex() {
        double nan = Double.NaN;
        double[] tab1 = new double[] { nan, nan, .6, .2, nan, .4 };
        assertEquals(Curves2D.findNextCurveIndex(tab1, .1), 3);
        assertEquals(Curves2D.findNextCurveIndex(tab1, .5), 2);
        assertEquals(Curves2D.findNextCurveIndex(tab1, .3), 5);
        assertEquals(Curves2D.findNextCurveIndex(tab1, .8), 3);
    }

    public void testGetJunctionType_LineLineAcute() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(10, 10), new Point2D(30, 10));
        LineSegment2D line2 = new LineSegment2D(new Point2D(30, 10), new Point2D(30, 50));
        Curves2D.JunctionType type = Curves2D.getJunctionType(line1, line2);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_LineLineObtuse() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(10, 10), new Point2D(30, 10));
        LineSegment2D line2 = new LineSegment2D(new Point2D(30, 10), new Point2D(30, 0));
        Curves2D.JunctionType type = Curves2D.getJunctionType(line1, line2);
        assertEquals(Curves2D.JunctionType.REENTRANT, type);
    }

    public void testGetJunctionType_LineLineFlat() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(10, 10), new Point2D(30, 10));
        LineSegment2D line2 = new LineSegment2D(new Point2D(30, 10), new Point2D(50, 10));
        Curves2D.JunctionType type = Curves2D.getJunctionType(line1, line2);
        assertEquals(Curves2D.JunctionType.FLAT, type);
    }

    public void testGetJunctionType_LineArcAcute() {
        LineSegment2D line = new LineSegment2D(new Point2D(10, 10), new Point2D(30, 10));
        CircleArc2D arc = new CircleArc2D(new Point2D(30, 10), 10, -PI / 2, -PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(line, arc);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_LineArcObtuse() {
        LineSegment2D line = new LineSegment2D(new Point2D(10, 10), new Point2D(30, 10));
        CircleArc2D arc = new CircleArc2D(new Point2D(30, 0), 10, PI / 2, PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(line, arc);
        assertEquals(Curves2D.JunctionType.REENTRANT, type);
    }

    public void testGetJunctionType_ArcLineAcute() {
        CircleArc2D arc = new CircleArc2D(new Point2D(30, 0), 10, PI, -PI / 2);
        LineSegment2D line = new LineSegment2D(new Point2D(30, 10), new Point2D(10, 10));
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc, line);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_Arcs_Reentrant() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(10, 10), 10, PI, PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(10, 10), 10, PI / 2, PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.REENTRANT, type);
    }

    public void testGetJunctionType_Arcs_Salient() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(10, 30), 10, 0, -PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(10, 10), 10, PI / 2, -PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_ArcsSmallLarge_Salient() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(5, 20), 10, 0, -PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(5, 30), 20, 3 * PI / 2, PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_ArcsSmallLarge_Reentrant() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(5, 20), 10, 0, PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(5, 10), 20, PI / 2, -PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.REENTRANT, type);
    }

    public void testGetJunctionType_ArcsLargeSmall_Salient() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(5, 30), 20, 0, PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(5, 20), 10, PI / 2, -PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.SALIENT, type);
    }

    public void testGetJunctionType_ArcsLargeSmall_Reentrant() {
        CircleArc2D arc1 = new CircleArc2D(new Point2D(5, 30), 20, 0, -PI / 2);
        CircleArc2D arc2 = new CircleArc2D(new Point2D(5, 20), 10, -PI / 2, PI / 2);
        Curves2D.JunctionType type = Curves2D.getJunctionType(arc1, arc2);
        assertEquals(Curves2D.JunctionType.REENTRANT, type);
    }

}
