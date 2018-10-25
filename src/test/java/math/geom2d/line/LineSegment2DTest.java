/*
 * File : LineSegment2DTest.java
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
 * Created on 29 déc. 2003
 */
package math.geom2d.line;

import java.util.Collection;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.circulinear.ICirculinearCurve2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * @author Legland
 */
public class LineSegment2DTest extends TestCase {

    /**
     * Constructor for Edge2DTest.
     * 
     * @param arg0
     */
    public LineSegment2DTest(String arg0) {
        super(arg0);
    }

    public void testGetBuffer() {
        // create a line segment, and computes its buffer
        LineSegment2D line = new LineSegment2D(50, 100, 150, 100);
        IDomain2D buffer = line.buffer(20);

        // The buffer should be defined by a single boundary curve
        IBoundary2D boundary = buffer.boundary();
        assertEquals(1, boundary.continuousCurves().size());

        // The contour is composed of 4 smooth pieces (2 line segments, and
        // two circle arcs)
        Collection<? extends ISmoothCurve2D> smoothCurves = boundary.continuousCurves().iterator().next().smoothPieces();
        assertEquals(4, smoothCurves.size());
    }

    public void testGetOtherPoint() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        Point2D p3 = new Point2D(20, 30);
        LineSegment2D edge = new LineSegment2D(p1, p2);

        assertEquals(p2, edge.opposite(p1));
        assertEquals(p1, edge.opposite(p2));
        assertEquals(null, edge.opposite(p3));
    }

    /**
     * Tests if the result of the transform by an inversion is a circle arc.
     */
    public void testTransformInversion() {
        Point2D p0 = new Point2D(30, 40);
        Point2D v0 = new Point2D(40, 60);
        LineSegment2D seg = new LineSegment2D(p0, v0);

        Point2D center = new Point2D(50, 0);
        Circle2D circle = new Circle2D(center, 50);
        CircleInversion2D inv = new CircleInversion2D(circle);

        ICirculinearCurve2D res = seg.transform(inv);
        assertNotNull(res);
        assertTrue(res instanceof CircleArc2D);

        // the new shape does not contains the circle center
        assertFalse(res.contains(center));
    }

    public void testTransformInversion2() {
        Point2D p1 = new Point2D(20, 0);
        Point2D p2 = new Point2D(0, 20);
        LineSegment2D seg = new LineSegment2D(p1, p2);

        Point2D center = new Point2D(0, 0);
        Circle2D circle = new Circle2D(center, 10);
        CircleInversion2D inv = new CircleInversion2D(circle);

        ICirculinearCurve2D res = seg.transform(inv);
        assertNotNull(res);
        assertTrue(res instanceof CircleArc2D);

        Point2D pt1 = p1.transform(inv);
        assertTrue(pt1.distance(res.firstPoint()) < IShape2D.ACCURACY);
        Point2D pt2 = p2.transform(inv);
        assertTrue(pt2.distance(res.lastPoint()) < IShape2D.ACCURACY);

    }

    public void testTransformInversion3() {
        Point2D p1 = new Point2D(10, 20);
        Point2D p2 = new Point2D(30, 20);
        LineSegment2D seg = new LineSegment2D(p1, p2);

        Point2D center = new Point2D(0, 0);
        Circle2D circle = new Circle2D(center, 10);
        CircleInversion2D inv = new CircleInversion2D(circle);

        ICirculinearCurve2D res = seg.transform(inv);
        assertNotNull(res);
        assertTrue(res instanceof CircleArc2D);

        Point2D pt1 = p1.transform(inv);
        Point2D res0 = res.firstPoint();
        assertTrue(pt1.distance(res0) < IShape2D.ACCURACY);
        Point2D pt2 = p2.transform(inv);
        Point2D res1 = res.lastPoint();
        assertTrue(pt2.distance(res1) < IShape2D.ACCURACY);
    }

    public void testIsBounded() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertTrue(edge.isBounded());
    }

    public void testIntersects() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(20, 30);
        Point2D p3 = new Point2D(00, 30);
        Point2D p4 = new Point2D(40, 10);
        Point2D p5 = new Point2D(15, 20);
        Point2D p6 = new Point2D(30, 00);

        LineSegment2D edge1 = new LineSegment2D(p1, p2);
        LineSegment2D edge2 = new LineSegment2D(p3, p4);
        LineSegment2D edge3 = new LineSegment2D(p5, p6);

        assertTrue(LineSegment2D.intersects(edge1, edge2));
        assertTrue(LineSegment2D.intersects(edge2, edge1));
        assertTrue(LineSegment2D.intersects(edge1, edge3));
        assertTrue(LineSegment2D.intersects(edge3, edge1));
        assertTrue(!LineSegment2D.intersects(edge2, edge3));
        assertTrue(!LineSegment2D.intersects(edge3, edge2));
    }

    public void testIsColinear() {
        // lines roughly horizontal
        LineSegment2D lineA1 = new LineSegment2D(new Point2D(2, 1), new Point2D(6, 3));
        LineSegment2D lineA2 = new LineSegment2D(new Point2D(6, 3), new Point2D(8, 4));
        LineSegment2D lineA3 = new LineSegment2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isColinear(lineA2));
        assertTrue(lineA2.isColinear(lineA1));
        assertFalse(lineA1.isColinear(lineA3));

        // lines roughly vertical
        LineSegment2D lineB1 = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 6));
        LineSegment2D lineB2 = new LineSegment2D(new Point2D(3, 6), new Point2D(4, 8));
        LineSegment2D lineB3 = new LineSegment2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isColinear(lineB2));
        assertTrue(lineB2.isColinear(lineB1));
        assertFalse(lineB1.isColinear(lineB3));
    }

    public void testIsParallel() {
        // lines roughly horizontal
        LineSegment2D lineA1 = new LineSegment2D(new Point2D(2, 1), new Point2D(6, 3));
        LineSegment2D lineA2 = new LineSegment2D(new Point2D(6, 3), new Point2D(8, 4));
        LineSegment2D lineA3 = new LineSegment2D(new Point2D(6, 4), new Point2D(8, 5));
        assertTrue(lineA1.isParallel(lineA2));
        assertTrue(lineA2.isParallel(lineA1));
        assertTrue(lineA1.isParallel(lineA3));

        // lines roughly vertical
        LineSegment2D lineB1 = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 6));
        LineSegment2D lineB2 = new LineSegment2D(new Point2D(3, 6), new Point2D(4, 8));
        LineSegment2D lineB3 = new LineSegment2D(new Point2D(4, 6), new Point2D(5, 8));
        assertTrue(lineB1.isParallel(lineB2));
        assertTrue(lineB2.isParallel(lineB1));
        assertTrue(lineB1.isParallel(lineB3));
    }

    public void testGetLength() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertEquals(50, edge.length(), 1e-14);
    }

    public void testGetParallelDouble() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(1, 3);
        LineSegment2D line1 = new LineSegment2D(p1, p2);

        Point2D p1p = new Point2D(2, 1);
        Point2D p2p = new Point2D(2, 3);
        LineSegment2D line1p = new LineSegment2D(p1p, p2p);

        assertTrue(line1.parallel(1).equals(line1p));
    }

    public void testProjectPoint2D() {
        // Test on a basic line segment (diagonal +1;+1)
        LineSegment2D line1 = new LineSegment2D(new Point2D(-1, 2), new Point2D(1, 4));
        Point2D point = new Point2D(1, 2);

        assertEquals(line1.project(point), .5, 1e-12);

        // Create a line segment with (1,2)*d direction vector
        double x0 = 20;
        double y0 = 30;
        double d = 10;
        LineSegment2D line2 = new LineSegment2D(new Point2D(x0, y0), new Point2D(x0 + d, y0 + 2 * d));

        // Test points which project 'nicely' on the line segment
        Point2D p0_0 = new Point2D(x0, y0);
        Point2D p1_0 = new Point2D(x0 + d, y0);
        Point2D p0_1 = new Point2D(x0, y0 + d);
        Point2D p1_1 = new Point2D(x0 + d, y0 + d);
        Point2D p0_2 = new Point2D(x0, y0 + 2 * d);
        Point2D p1_2 = new Point2D(x0 + d, y0 + 2 * d);

        assertEquals(line2.project(p0_0), 0, 1e-12);
        assertEquals(line2.project(p1_0), .2, 1e-12);
        assertEquals(line2.project(p0_1), .4, 1e-12);
        assertEquals(line2.project(p1_1), .6, 1e-12);
        assertEquals(line2.project(p0_2), .8, 1e-12);
        assertEquals(line2.project(p1_2), 1, 1e-12);

        // Test points which project on first point
        Point2D pl1 = new Point2D(x0, y0 - d);
        Point2D pl2 = new Point2D(x0 - d, y0);
        Point2D pl3 = new Point2D(x0 + d, y0 - d);
        assertEquals(line2.project(pl1), 0, 1e-12);
        assertEquals(line2.project(pl2), 0, 1e-12);
        assertEquals(line2.project(pl3), 0, 1e-12);

        // Test points which project on last point
        Point2D pu1 = new Point2D(x0 + d, y0 + 3 * d);
        Point2D pu2 = new Point2D(x0 + 2 * d, y0 + 2 * d);
        Point2D pu3 = new Point2D(x0, y0 + 3 * d);
        assertEquals(line2.project(pu1), 1, 1e-12);
        assertEquals(line2.project(pu2), 1, 1e-12);
        assertEquals(line2.project(pu3), 1, 1e-12);
    }

    /**
     * Tests distance between a point and a line segment for large coordinate values.
     */
    public void testDistancePoint_LargeValues() {
        LineSegment2D line = new LineSegment2D(44154, 37711, 45754, 30113);
        Point2D point = new Point2D(44767, 34320);

        Point2D projPoint = line.projectedPoint(point);

        double dist = line.distance(point);
        double dist2 = point.distance(projPoint);

        assertEquals(dist, dist2, 1e-5);
    }

    public void testDistancePoint_SameExtremities() {
        final LineSegment2D lineA = new LineSegment2D(2400, 1500, 2400, 1500);
        double dist = lineA.distance(new Point2D(2440, 1530));
        assertEquals(50, dist, .01);
    }

    /*
     * Test for double distance(double, double)
     */
    public void testDistanceDoubleDouble() {

        // basic
        LineSegment2D edge = new LineSegment2D(1, 1, 4, 3);

        assertEquals(edge.distance(1, 1), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(2.5, 2), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(4, 3), 0, IShape2D.ACCURACY);

        double d1 = Math.sqrt(13) / 2;
        assertEquals(edge.distance(-.5, 0), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, -.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(3.5, .5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(5, 1.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(5.5, 4), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(3, 4.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(1.5, 3.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 2.5), d1, IShape2D.ACCURACY);

        double d2 = Math.sqrt(26) / 2;
        assertEquals(edge.distance(0.5, -1.5), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(-1.5, 1.5), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(6.5, 2.5), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(4.5, 5.5), d2, IShape2D.ACCURACY);

        // horizontal edge
        edge = new LineSegment2D(1, 1, 4, 1);
        assertEquals(edge.distance(1, 1), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(2.5, 1), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(4, 1), 0, IShape2D.ACCURACY);

        d1 = 1;
        assertEquals(edge.distance(0, 1), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(5, 1), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(1, 0), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2.5, 0), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(4, 0), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(1, 2), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2.5, 2), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(4, 2), d1, IShape2D.ACCURACY);

        d2 = Math.sqrt(2);
        assertEquals(edge.distance(0, 0), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 2), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(5, 0), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(5, 2), d2, IShape2D.ACCURACY);

        // vertical edge
        edge = new LineSegment2D(1, 1, 1, 4);
        assertEquals(edge.distance(1, 1), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(1, 2.5), 0, IShape2D.ACCURACY);
        assertEquals(edge.distance(1, 4), 0, IShape2D.ACCURACY);

        assertEquals(edge.distance(1, 0), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(1, 5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 1), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 2.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 4), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, 1), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, 2.5), d1, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, 4), d1, IShape2D.ACCURACY);

        assertEquals(edge.distance(0, 0), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, 0), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(2, 5), d2, IShape2D.ACCURACY);
        assertEquals(edge.distance(0, 5), d2, IShape2D.ACCURACY);
    }

    public void testContainsPoint() {
        LineSegment2D line = new LineSegment2D(-123707.89774439273, 157924.65808795238, -125829.21808795238, 155803.33774439275);
        Point2D point2d = new Point2D(-125034.27520951361, 157129.7152095136);
        Point2D projectedPoint = line.projectedPoint(point2d);

        assertEquals("Point need to be the same", new Point2D(-124768.55791617256, 156863.99791617255), projectedPoint);
        assertTrue("Point is on line", line.containsProjection(point2d));
        assertTrue("Projected point has to be located on the line", line.contains(projectedPoint));
    }

    /*
     * Test for boolean contains(double, double)
     */
    public void testContainsdoubledouble() {
        LineSegment2D edge;

        // diagonal edge
        edge = new LineSegment2D(1, 1, 3, 2);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(3, 2));
        assertTrue(edge.contains(2, 1.5));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(-1, 0));
        assertFalse(edge.contains(5, 3));

        // horizontal edge
        edge = new LineSegment2D(1, 1, 3, 1);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(3, 1));
        assertTrue(edge.contains(2, 1));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(0, 1));
        assertFalse(edge.contains(4, 1));

        // vertical edge
        edge = new LineSegment2D(1, 1, 1, 3);
        assertTrue(edge.contains(1, 1));
        assertTrue(edge.contains(1, 3));
        assertTrue(edge.contains(1, 2));
        assertFalse(edge.contains(0, 0));
        assertFalse(edge.contains(1, 0));
        assertFalse(edge.contains(1, 4));
    }

    public void testGetEdgeAngleLineSegment2DLineSegment2D() {
        Point2D p1, p2, p3;
        p1 = new Point2D(0, 0);
        p2 = new Point2D(50, 50);
        p3 = new Point2D(100, 50);

        LineSegment2D edge1d = new LineSegment2D(p1, p2);
        LineSegment2D edge1i = new LineSegment2D(p2, p1);
        LineSegment2D edge2d = new LineSegment2D(p2, p3);
        LineSegment2D edge2i = new LineSegment2D(p3, p2);

        assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2d), Math.PI * 3 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge1d, edge2i), Math.PI * 3 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2d), Math.PI * 3 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge1i, edge2i), Math.PI * 3 / 4, IShape2D.ACCURACY);

        assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1d), Math.PI * 5 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge2d, edge1i), Math.PI * 5 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1d), Math.PI * 5 / 4, IShape2D.ACCURACY);
        assertEquals(LineSegment2D.getEdgeAngle(edge2i, edge1i), Math.PI * 5 / 4, IShape2D.ACCURACY);
    }

    /*
     * Test for Point2D getIntersection(StraightObject2D)
     */
    public void testGetIntersectionStraightObject2D() {
        LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
        LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);
        assertTrue(edge1.intersection(edge2).equals(new Point2D(1, 1)));
        assertTrue(edge2.intersection(edge1).equals(new Point2D(1, 1)));

        LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
        assertTrue(edge1.intersection(edge3).equals(new Point2D(3, 2)));
        assertTrue(edge3.intersection(edge1).equals(new Point2D(3, 2)));
        assertTrue(edge2.intersection(edge3).equals(new Point2D(0, 4)));
        assertTrue(edge3.intersection(edge2).equals(new Point2D(0, 4)));

        LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
        assertEquals(edge1.intersection(edge4), null);
        assertEquals(edge2.intersection(edge4), null);
        assertEquals(edge3.intersection(edge4), null);

        edge1 = new LineSegment2D(1, 1, 5, 5);
        edge2 = new LineSegment2D(1, 5, 5, 1);
        assertTrue(edge1.intersection(edge2).equals(new Point2D(3, 3)));
        assertTrue(edge2.intersection(edge1).equals(new Point2D(3, 3)));
    }

    public void testGetBoundingBox() {
        // diagonal edge
        LineSegment2D edge = new LineSegment2D(1, 1, 3, 2);
        Box2D box = edge.boundingBox();
        assertTrue(box.equals(new Box2D(1, 3, 1, 2)));
    }

    public void testGetBoundingBox_Reverse() {
        // diagonal edge
        LineSegment2D edge = new LineSegment2D(3, 2, 1, 1);
        Box2D box = edge.boundingBox();
        assertTrue(box.equals(new Box2D(1, 3, 1, 2)));
    }

    public void testGetReverseCurve() {
        LineSegment2D edge = new LineSegment2D(1, 1, 3, 2);
        assertEquals(edge.reverse(), new LineSegment2D(3, 2, 1, 1));
    }

    public void testEquals() {
        LineSegment2D edge1 = new LineSegment2D(1, 2, 3, 4);
        assertTrue(edge1.equals(edge1));
        LineSegment2D edge2 = new LineSegment2D(3, 4, 1, 2);
        assertTrue(!edge1.equals(edge2));
        assertTrue(!edge2.equals(edge1));

        LineSegment2D edge3 = new LineSegment2D(1, 4, 3, 2);
        assertTrue(!edge1.equals(edge3));
        assertTrue(!edge2.equals(edge3));
        assertTrue(!edge3.equals(edge1));
        assertTrue(!edge3.equals(edge2));

        LineSegment2D edge4 = new LineSegment2D(1, 2, 3, 2);
        assertTrue(!edge1.equals(edge4));
        assertTrue(!edge2.equals(edge4));
        assertTrue(!edge4.equals(edge1));
        assertTrue(!edge4.equals(edge2));
    }

    public void testGetViewAnglePoint2D() {

        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(3, 1);
        Point2D p3 = new Point2D(1, 1);
        Point2D p4 = new Point2D(1, 3);

        LineSegment2D edge1 = new LineSegment2D(2, 0, 2, 2);
        assertEquals(edge1.windingAngle(p1), Math.PI / 2, 1e-14);
        assertEquals(edge1.windingAngle(p2), -Math.PI / 2, 1e-14);

        LineSegment2D edge2 = new LineSegment2D(2, 2, 2, 0);
        assertEquals(edge2.windingAngle(p1), -Math.PI / 2, 1e-14);
        assertEquals(edge2.windingAngle(p2), Math.PI / 2, 1e-14);

        LineSegment2D edge3 = new LineSegment2D(0, 2, 2, 2);
        assertEquals(edge3.windingAngle(p3), -Math.PI / 2, 1e-14);
        assertEquals(edge3.windingAngle(p4), Math.PI / 2, 1e-14);

        LineSegment2D edge4 = new LineSegment2D(2, 2, 0, 2);
        assertEquals(edge4.windingAngle(p3), Math.PI / 2, 1e-14);
        assertEquals(edge4.windingAngle(p4), -Math.PI / 2, 1e-14);
    }
}
