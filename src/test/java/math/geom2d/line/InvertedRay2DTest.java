/*
 * File : InvertedRay2DTest.java
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

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.ICirculinearCurve2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.exception.UnboundedShape2DException;
import math.geom2d.transform.CircleInversion2D;
import junit.framework.TestCase;

/**
 * @author Legland
 */
public class InvertedRay2DTest extends TestCase {

    /**
     * Constructor for InvertedRay2DTest.
     * 
     * @param arg0
     */
    public InvertedRay2DTest(String arg0) {
        super(arg0);
    }

    public void testGetBufferDouble() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(20, 30);
        InvertedRay2D ray = new InvertedRay2D(p1, p2);

        double dist = 5;
        IDomain2D buffer = ray.buffer(dist);

        assertFalse(buffer.isEmpty());
        assertFalse(buffer.isBounded());
    }

    public void testTransformInversion() {
        // create an infinite curve, here a straight line
        Point2D p0 = new Point2D(30, 40);
        Vector2D v0 = new Vector2D(10, 20);
        InvertedRay2D ray = new InvertedRay2D(p0, v0);

        Circle2D circle = new Circle2D(50, 0, 50);
        CircleInversion2D inv = new CircleInversion2D(circle);

        ICirculinearCurve2D res = ray.transform(inv);
        assertNotNull(res);
        assertTrue(res instanceof CircleArc2D);
    }

    public void testGetParallelDouble() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(1, 3);
        InvertedRay2D line1 = new InvertedRay2D(p1, p2);

        Point2D p1p = new Point2D(2, 1);
        Point2D p2p = new Point2D(2, 3);
        InvertedRay2D line1p = new InvertedRay2D(p1p, p2p);

        assertTrue(line1.parallel(1).equals(line1p));
    }

    public void testIsBounded() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        assertFalse(ray1.isBounded());
    }

    public void testGetFirstPoint() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        try {
            ray1.firstPoint();
            fail("Should throw an InfiniteShapeException");
        } catch (UnboundedShape2DException ex) {
            assertEquals(ex.getShape(), ray1);
        }
    }

    public void testGetLastPoint() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        Point2D lastPoint = ray1.lastPoint();
        assertTrue(lastPoint.equals(new Point2D(2, 2)));
    }

    public void testGetBoundingBox() {
        double plus = Double.POSITIVE_INFINITY;
        double minus = Double.NEGATIVE_INFINITY;
        double eps = IShape2D.ACCURACY;

        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 1);
        assertTrue(ray1.boundingBox().equals(new Box2D(minus, 2, minus, 2)));

        InvertedRay2D ray2 = new InvertedRay2D(2, 2, 1, 0);
        Box2D ray2Box = new Box2D(minus, 2, 2, 2);
        assertTrue(ray2.boundingBox().almostEquals(ray2Box, eps));

        InvertedRay2D ray3 = new InvertedRay2D(2, 2, -1, -1);
        Box2D ray3Box = new Box2D(2, plus, 2, plus);
        assertTrue(ray3.boundingBox().almostEquals(ray3Box, eps));
    }

    public void testGetReverseCurve() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        Ray2D inv1 = new Ray2D(2, 2, -1, 0);

        ICurve2D inverted = ray1.reverse();
        assertTrue(inverted.almostEquals(inv1, IShape2D.ACCURACY));

    }

    public void testContainsDoubleDouble() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        assertTrue(ray1.contains(2, 2));
        assertTrue(ray1.contains(-3, 2));

        InvertedRay2D ray2 = new InvertedRay2D(2, 2, 1, 2);
        assertTrue(ray2.contains(2, 2));
        assertTrue(ray2.contains(-2, -6));
    }

    public void testClipBox2D() {
        InvertedRay2D ray1 = new InvertedRay2D(2, 2, 1, 0);
        Box2D box = new Box2D(-10, 10, -10, 10);
        ICurveSet2D<?> clipped = ray1.clip(box);
        assertTrue(clipped.iterator().next().equals(new LineSegment2D(-10, 2, 2, 2)));
    }

    public void testGetDistancePoint2D() {
        InvertedRay2D ray = new InvertedRay2D(1, 2, 3, 4);
        Point2D pt;

        // test origin point
        pt = new Point2D(1, 2);
        assertEquals(0, ray.distance(pt), 1e-14);

        // point on the line (positive extent)
        pt = new Point2D(1 + 2 * 3, 2 + 2 * 4);
        assertEquals(10, ray.distance(pt), 1e-14);

        // point on the line (negative extent)
        pt = new Point2D(1 - 2 * 3, 2 - 2 * 4);
        assertEquals(0, ray.distance(pt), 1e-14);

        // point outside the line
        pt = new Point2D(5, -1);
        assertEquals(5, ray.distance(pt), 1e-14);

        // point outside the line, in the other side
        pt = new Point2D(-3, 5);
        assertEquals(5, ray.distance(pt), 1e-14);
    }

    public void testCopyConstructor() {
        InvertedRay2D ray = new InvertedRay2D(10, 20, 30, 40);
        InvertedRay2D copy = new InvertedRay2D(ray);
        assertTrue(ray.equals(copy));
    }
}
