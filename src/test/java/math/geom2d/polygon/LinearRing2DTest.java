/*
 * File : LinearLinearRing2DTest.java
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
 * Created on 16 Apr. 2007
 */

package math.geom2d.polygon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.IShape2D;
import math.geom2d.circulinear.ICirculinearContinuousCurve2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.Point2D;

/**
 * @author Legland
 */
public class LinearRing2DTest extends TestCase {

    /**
     * Constructor for LinearRing2DTest.
     * 
     * @param arg0
     */
    public LinearRing2DTest(String arg0) {
        super(arg0);
    }

    public void testSimplify() {
        Circle2D circle = new Circle2D(0, 0, 10);
        LinearRing2D poly = circle.asPolyline(32);
        LinearRing2D poly2 = poly.simplify(2.5);

        assertEquals(8, poly2.vertexNumber());
    }

    public void testParallelPositiveDouble() {
        // create polyline
        LinearRing2D ring = new LinearRing2D(new Point2D[] { new Point2D(100, 100), new Point2D(200, 100), new Point2D(200, 200), new Point2D(100, 200), new Point2D(100, 100) });

        ICirculinearContinuousCurve2D parallel = ring.parallel(20);
        assertEquals(8, parallel.smoothPieces().size());
        assertTrue(parallel.isClosed());
    }

    public void testParallelNegativeDouble() {
        // create polyline
        LinearRing2D ring = new LinearRing2D(new Point2D[] { new Point2D(100, 100), new Point2D(200, 100), new Point2D(200, 200), new Point2D(100, 200), new Point2D(100, 100) });

        ICirculinearContinuousCurve2D parallel = ring.parallel(-20);
        assertEquals(8, parallel.smoothPieces().size());
        assertTrue(parallel.isClosed());
    }

    public void testBuffer() {
        // create polyline
        LinearRing2D line = new LinearRing2D(new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) });

        // one loop makes two boundaries
        IDomain2D buffer = line.buffer(3);
        IBoundary2D boundary = buffer.boundary();
        assertEquals(boundary.continuousCurves().size(), 2);

        // one loop makes two boundaries
        buffer = line.buffer(6);
        boundary = buffer.boundary();
        assertEquals(boundary.continuousCurves().size(), 1);

        // 8 parts: 4 circle arcs, and 4 line segments
        Collection<? extends ISmoothCurve2D> smoothCurves = boundary.continuousCurves().iterator().next().smoothPieces();
        assertEquals(smoothCurves.size(), 8);
    }

    public void testBuffer_MShape() {
        LinearRing2D ring = LinearRing2D.create(new Point2D[] { new Point2D(100, 100), new Point2D(300, 100), new Point2D(300, 300), new Point2D(200, 200), new Point2D(100, 300) });
        double dist = 51;

        IDomain2D buffer = ring.buffer(dist);
        IBoundary2D boundary = buffer.boundary();

        // should have 1 outer and 2 inner boundaries
        // Fails for the moment
        assertEquals(3, boundary.continuousCurves().size());
    }

    public void testBuffer_SelfIntersect() {
        // create polyline
        LinearRing2D line = new LinearRing2D(new Point2D[] { new Point2D(0, 0), new Point2D(0, 100), new Point2D(200, 100), new Point2D(200, 200), new Point2D(100, 200), new Point2D(100, 0) });

        // should be 3 parts for boundary
        IDomain2D buffer = line.buffer(30);
        IBoundary2D boundary = buffer.boundary();
        assertEquals(boundary.continuousCurves().size(), 3);
    }

    /**
     * Test buffer for a polyline with first and last point equal.
     */
    public void testBuffer_LastPointDoubled() {
        // polyline with 4 vertices,
        LinearRing2D curve = new LinearRing2D(new Point2D[] { new Point2D(100, 100), new Point2D(200, 100), new Point2D(200, 200), new Point2D(100, 200), new Point2D(100, 100) });
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        ICirculinearDomain2D buffer = bc.computeBuffer(curve, 20);

        assertFalse(buffer == null);
        assertFalse(buffer.isEmpty());
        assertEquals(2, buffer.boundary().continuousCurves().size());
    }

    public void testLength() {
        // create polyline
        LinearRing2D line = new LinearRing2D(new Point2D[] { new Point2D(0, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(0, 10) });

        // length is 4*10=40
        assertEquals(line.length(), 40, IShape2D.ACCURACY);
    }

    public void testAddVertex() {
        Point2D[] points = new Point2D[4];
        points[0] = new Point2D(0, 0);
        points[1] = new Point2D(10, 0);
        points[2] = new Point2D(10, 10);
        points[3] = new Point2D(20, 20);
        LinearRing2D line = new LinearRing2D(points);
        line.addVertex(new Point2D(30, 20));
        assertEquals(line.vertexNumber(), points.length + 1);
    }

    public void testPoint() {
        // initialize points
        Point2D[] points = new Point2D[4];
        points[0] = new Point2D(0, 0);
        points[1] = new Point2D(10, 0);
        points[2] = new Point2D(10, 10);
        points[3] = new Point2D(20, 20);

        // create polyline
        LinearRing2D line1 = new LinearRing2D(points);

        // find the first point
        Point2D p00 = new Point2D(0, 0);
        assertTrue(p00.equals(line1.point(-.5)));
        assertTrue(p00.equals(line1.point(0)));

        // find intermediate points
        Point2D p05 = new Point2D(5, 0);
        assertTrue(p05.equals(line1.point(.5)));
        Point2D p15 = new Point2D(10, 5);
        assertTrue(p15.equals(line1.point(1.5)));
        Point2D p25 = new Point2D(15, 15);
        assertTrue(p25.equals(line1.point(2.5)));
        Point2D p30 = new Point2D(20, 20);
        assertTrue(p30.equals(line1.point(3)));
        Point2D p35 = new Point2D(10, 10);
        assertTrue(p35.equals(line1.point(3.5)));

        // find the last point
        Point2D p40 = new Point2D(0, 0);
        assertTrue(p40.equals(line1.point(4)));
        assertTrue(p40.equals(line1.point(4.1)));
    }

    public void testIntersections() {
        // initialize points
        Point2D[] points = new Point2D[3];
        points[0] = new Point2D(0, -5);
        points[1] = new Point2D(10, 5);
        points[2] = new Point2D(20, -5);

        // create polyline
        LinearRing2D poly = new LinearRing2D(points);

        // line to intersect with
        StraightLine2D line = new StraightLine2D(0, 0, 1, 0);
        StraightLine2D edge = new StraightLine2D(0, 0, 20, 0);

        Collection<Point2D> inters1 = poly.intersections(line);
        assertTrue(inters1.size() == 2);
        Iterator<Point2D> iter1 = inters1.iterator();
        assertTrue(iter1.next().equals(new Point2D(5, 0)));
        assertTrue(iter1.next().equals(new Point2D(15, 0)));

        Collection<Point2D> inters2 = poly.intersections(edge);
        assertTrue(inters2.size() == 2);
        Iterator<Point2D> iter2 = inters2.iterator();
        assertTrue(iter2.next().equals(new Point2D(5, 0)));
        assertTrue(iter2.next().equals(new Point2D(15, 0)));
    }

    public void testSubCurve() {
        // initialize points
        Point2D[] points = new Point2D[4];
        points[0] = new Point2D(0, 0);
        points[1] = new Point2D(10, 0);
        points[2] = new Point2D(10, 10);
        points[3] = new Point2D(0, 10);

        // create polyline
        LinearRing2D line1 = new LinearRing2D(points);

        // Check with a polyline whith t1<t2
        Polyline2D sub = line1.subCurve(.5, 2.5);
        Polyline2D line2 = new Polyline2D(new Point2D[] { new Point2D(5, 0), new Point2D(10, 0), new Point2D(10, 10), new Point2D(5, 10) });
        assertTrue(line2.equals(sub));

        // Check with a polyline whith t1>t2
        sub = line1.subCurve(3.5, .5);
        line2 = new Polyline2D(new Point2D[] { new Point2D(0, 5), new Point2D(0, 0), new Point2D(5, 0) });
        assertTrue(line2.equals(sub));

    }

    public void testClip_Box2D() {
        LinearRing2D polyline1 = new LinearRing2D(new Point2D[] { new Point2D(-5, -5), new Point2D(5, -5), new Point2D(5, 5), new Point2D(-5, 5) });

        Box2D box = new Box2D(0, 10, 0, 10);
        Polyline2D line1 = new Polyline2D(new Point2D[] { new Point2D(5, 0), new Point2D(5, 5), new Point2D(0, 5) });

        ICurveSet2D<? extends LinearCurve2D> clipped = polyline1.clip(box);
        assertTrue(clipped.size() == 1);
        assertTrue(clipped.firstCurve().equals(line1));

        // Oblic polyline, cutting the box in two points
        polyline1 = new LinearRing2D(new Point2D[] { new Point2D(0, 0), new Point2D(20, -20), new Point2D(40, 0), new Point2D(20, 20) });
        box = new Box2D(-30, 30, -30, 30);
        line1 = new Polyline2D(new Point2D[] { new Point2D(30, 10), new Point2D(20, 20), new Point2D(0, 0), new Point2D(20, -20), new Point2D(30, -10) });
        clipped = polyline1.clip(box);

        assertTrue(clipped.size() == 1);
        assertTrue(clipped.firstCurve().equals(line1));
    }

    public void testClipBox2D_TouchesAtCorners() {
        LinearRing2D polyline = LinearRing2D.create(new Point2D[] { new Point2D(0, 5), new Point2D(5, 0), new Point2D(10, 5), new Point2D(5, 10) });

        Box2D box = new Box2D(0, 10, 0, 10);

        ICurveSet2D<?> clipped = polyline.clip(box);
        assertEquals(1, clipped.size());
    }

    public void testArea_CCW() {
        LinearRing2D polyline = new LinearRing2D(new Point2D(20, 10), new Point2D(20, 20), new Point2D(10, 20), new Point2D(10, 10));
        assertEquals(100, polyline.area(), 1e-10);
    }

    public void testArea_CW() {
        LinearRing2D invert = new LinearRing2D(new Point2D(20, 10), new Point2D(10, 10), new Point2D(10, 20), new Point2D(20, 20));
        assertEquals(-100, invert.area(), 1e-10);
    }

    public void testSignedDistance_CW() {
        LinearRing2D ring = new LinearRing2D(new Point2D(4, 4), new Point2D(4, -4), new Point2D(-4, -4), new Point2D(-4, 4));
        Point2D p0 = new Point2D(6, 4);

        double dist = ring.signedDistance(p0);

        assertEquals(-2, dist, IShape2D.ACCURACY);
    }

    public void testIsInside() {
        LinearRing2D polyline = new LinearRing2D(new Point2D[] { new Point2D(150, 50), new Point2D(150, 150), new Point2D(100, 100), new Point2D(50, 150), new Point2D(50, 50) });

        assertTrue(polyline.isInside(new Point2D(60, 60)));
        assertTrue(polyline.isInside(new Point2D(140, 60)));
        assertTrue(polyline.isInside(new Point2D(55, 140)));
        assertTrue(polyline.isInside(new Point2D(145, 140)));

        assertTrue(!polyline.isInside(new Point2D(100, -50)));
        assertTrue(!polyline.isInside(new Point2D(100, 110)));
        assertTrue(!polyline.isInside(new Point2D(200, 50)));
        assertTrue(!polyline.isInside(new Point2D(0, 50)));

        // Try the other orientation
        polyline = new LinearRing2D(new Point2D[] { new Point2D(50, 50), new Point2D(50, 150), new Point2D(100, 100), new Point2D(150, 150), new Point2D(150, 50), });

        // assertTrue(!polyline.isInside(new Point2D(60, 60)));
        // assertTrue(!polyline.isInside(new Point2D(140, 60)));
        // assertTrue(!polyline.isInside(new Point2D(55, 140)));
        // assertTrue(!polyline.isInside(new Point2D(145, 140)));
        //
        // assertTrue(polyline.isInside(new Point2D(100, -50)));
        // assertTrue(polyline.isInside(new Point2D(100, 110)));
        // assertTrue(polyline.isInside(new Point2D(200, 50)));
        // assertTrue(polyline.isInside(new Point2D(0, 50)));

    }

    /*
     * Test for boolean contains(double, double)
     */
    public void testIsInside2() {

        // start with a simple rectangle
        Point2D points[] = new Point2D[4];
        points[0] = new Point2D(20, 20);
        points[1] = new Point2D(40, 20);
        points[2] = new Point2D(40, 60);
        points[3] = new Point2D(20, 60);
        LinearRing2D poly = new LinearRing2D(points);

        assertTrue(poly.isInside(20, 20));
        assertTrue(poly.isInside(40, 20));
        assertTrue(poly.isInside(40, 60));
        assertTrue(poly.isInside(20, 60));
        assertTrue(poly.isInside(25, 20));
        assertTrue(poly.isInside(25, 40));
        assertTrue(!poly.isInside(10, 20));
        assertTrue(!poly.isInside(50, 20));
        assertTrue(!poly.isInside(10, 10));
        assertTrue(poly.isInside(25, 25));

        // try some more complicated figures, in order to test particular
        // cases of the algorithm
        points = new Point2D[6];
        points[0] = new Point2D(40, 70);
        points[1] = new Point2D(40, 50);
        points[2] = new Point2D(20, 50);
        points[3] = new Point2D(60, 10);
        points[4] = new Point2D(60, 30);
        points[5] = new Point2D(80, 30);
        poly = new LinearRing2D(points);

        // classic case
        assertTrue(poly.isInside(60, 40));
        // problematic case
        assertTrue(poly.isInside(50, 40));

        points = new Point2D[8];
        points[0] = new Point2D(10, 60);
        points[1] = new Point2D(10, 40);
        points[2] = new Point2D(20, 50);
        points[3] = new Point2D(20, 20);
        points[4] = new Point2D(10, 30);
        points[5] = new Point2D(10, 10);
        points[6] = new Point2D(40, 10);
        points[7] = new Point2D(40, 60);
        poly = new LinearRing2D(points);

        // classic cases
        assertTrue(poly.isInside(15, 15));
        assertTrue(poly.isInside(25, 40));
        assertFalse(poly.isInside(15, 37));

        // problematic cases
        assertTrue(poly.isInside(30, 35));
        assertFalse(poly.isInside(10, 35));
        assertFalse(poly.isInside(5, 35));
    }

    public void testCreate_Collection() {
        ArrayList<Point2D> array = new ArrayList<>(4);
        array.add(new Point2D(10, 10));
        array.add(new Point2D(20, 10));
        array.add(new Point2D(20, 20));
        array.add(new Point2D(10, 20));
        LinearRing2D ring = LinearRing2D.create(array);
        assertNotNull(ring);
    }

    public void testCreate_Array() {
        Point2D[] array = new Point2D[4];
        array[0] = new Point2D(10, 10);
        array[1] = new Point2D(20, 10);
        array[2] = new Point2D(20, 20);
        array[3] = new Point2D(10, 20);
        LinearRing2D ring = LinearRing2D.create(array);
        assertNotNull(ring);
    }

    public void testCopyConstructor() {
        LinearRing2D ring = new LinearRing2D(new Point2D[] { new Point2D(150, 50), new Point2D(150, 150), new Point2D(100, 100), new Point2D(50, 150), new Point2D(50, 50) });
        LinearRing2D copy = new LinearRing2D(ring);
        assertTrue(ring.equals(copy));
    }
}
