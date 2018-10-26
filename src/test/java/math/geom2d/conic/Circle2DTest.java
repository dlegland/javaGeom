/* File Circle2DTest.java 
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
 */

package math.geom2d.conic;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.ICirculinearCurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.domain.IContinuousOrientedCurve2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * @author Legland
 *
 */
public class Circle2DTest extends TestCase {

    private void assertVectorEquals(Vector2D v1, Vector2D v2, double eps) {
        assertTrue(v1.almostEquals(v2, eps));
    }

    /**
     * Constructor for Circle2DTest.
     * 
     * @param arg0
     */
    public Circle2DTest(String arg0) {
        super(arg0);
    }

    public void testIntersectionsStraightLine2D() {
        Point2D center = new Point2D(44729, 7264);
        Point2D point = new Point2D(44501, 7010);
        StraightLine2D line = new StraightLine2D(center, point);
        Circle2D circle = new Circle2D(center, 250);
        Collection<Point2D> circleIntersections = circle.intersections(line);

        assertEquals(2, circleIntersections.size());
    }

    public void testCircleStraightLine2DIntersection2a() {
        // if center and point are equals, intersections method throw an exception
        Point2D center = new Point2D(174980.60, 66497.20);
        Point2D point = new Point2D(174980.60, 66497.20);
        try {
            @SuppressWarnings("unused")
            StraightLine2D straightLine2D = new StraightLine2D(center, point);
            assertTrue(false);
        } catch (Exception ex) {
        }
    }

    // public void testCircleStraightLine2DIntersection2b() {
    // // if center and point are equals, intersections method throw an exception
    // Point2D center = new Point2D(174980.6000001, 66497.20);
    // Point2D point = new Point2D(174980.60, 66497.20);
    // StraightLine2D straightLine2D = new StraightLine2D(center, point);
    // Circle2D circle = new Circle2D(center, 274.10);
    // Collection<Point2D> circleIntersections = circle.intersections(straightLine2D);
    // assertTrue(circleIntersections.isEmpty());
    // }

    public void testCircumCircle() {
        Point2D p1 = new Point2D(6, 1);
        Point2D p2 = new Point2D(4, 3);
        Point2D p3 = new Point2D(2, 1);
        Circle2D circle = new Circle2D(4, 1, 2);

        assertTrue(Circle2D.circumCircle(p1, p2, p3).equals(circle));
        assertTrue(Circle2D.circumCircle(p1, p3, p2).equals(circle));
        assertTrue(Circle2D.circumCircle(p3, p2, p1).equals(circle));
    }

    public void testGetBuffer() {
        Circle2D circle = new Circle2D(50, 60, 50);
        IDomain2D buffer = circle.buffer(20);
        assertEquals(2, buffer.boundary().continuousCurves().size());

        buffer = circle.buffer(60);
        assertEquals(1, buffer.boundary().continuousCurves().size());
    }

    public void testGetParallel() {
        Circle2D circle = new Circle2D(10, 20, 30);
        Circle2D parallel = circle.parallel(10);
        assertEquals(parallel, new Circle2D(10, 20, 40));
        parallel = circle.parallel(-10);
        assertEquals(parallel, new Circle2D(10, 20, 20));
    }

    public void testGetParallelInverse() {
        Circle2D circle = new Circle2D(10, 20, 30, false);
        Circle2D parallel = circle.parallel(10);
        assertEquals(parallel, new Circle2D(10, 20, 20, false));
        parallel = circle.parallel(-10);
        assertEquals(parallel, new Circle2D(10, 20, 40, false));
    }

    public void testTransformInversion2D() {
        Circle2D base = new Circle2D(0, 0, 100);
        CircleInversion2D inv = new CircleInversion2D(base);

        Circle2D circle;
        ICirculinearCurve2D res;

        // Circle inside base circle
        circle = new Circle2D(60, 20, 30);
        res = circle.transform(inv);
        assertTrue(res instanceof Circle2D);
        assertFalse(((Circle2D) res).isDirect());

        // Circle outside base circle
        circle = new Circle2D(160, 20, 30);
        res = circle.transform(inv);
        assertTrue(res instanceof Circle2D);
        assertFalse(((Circle2D) res).isDirect());

        // Circle inside base circle, containing center
        circle = new Circle2D(10, 20, 30);
        res = circle.transform(inv);
        assertTrue(res instanceof Circle2D);
        assertTrue(((Circle2D) res).isDirect());

        // Circle touching base circle
        circle = new Circle2D(100, 10, 30);
        res = circle.transform(inv);
        assertTrue(res instanceof Circle2D);

        // concentric circles
        circle = new Circle2D(0, 0, 30);
        res = circle.transform(inv);
        assertTrue(res instanceof Circle2D);
        assertTrue(((Circle2D) res).isDirect());

    }

    public void testTransformInversion2D_ContainsCenter() {
        // Circle containing inversion center -> transform to line
        Point2D center = new Point2D(30, 40);
        Circle2D base = new Circle2D(center, 50);
        CircleInversion2D inv = new CircleInversion2D(base);

        // circle contains the inversion center (100-70 = 30).
        Circle2D circle = new Circle2D(100, 40, 70);
        ICirculinearCurve2D res = circle.transform(inv);
        assertTrue(res instanceof StraightLine2D);

        assertFalse(res.contains(center));

        // result should contain intersection points of original circles
        Collection<Point2D> intersects = Circle2D.circlesIntersections(base, circle);
        for (Point2D p : intersects) {
            assertTrue(res.contains(p));
        }
    }

    public void testContainsDoubleDouble() {
        Circle2D circle = new Circle2D(0, 0, 10);

        assertTrue(circle.contains(10, 0));
        assertTrue(circle.contains(-10, 0));
        assertTrue(circle.contains(0, 10));
        assertTrue(circle.contains(0, -10));
        assertTrue(!circle.contains(0, 0));
    }

    public void testGetConicType() {
        Circle2D circle = new Circle2D(0, 0, 0, true);
        assertEquals(circle.conicType(), IConic2D.Type.CIRCLE);
    }

    public void testGetTangent() {
        Point2D center = new Point2D(10, 20);
        double r = 10;
        double eps = IShape2D.ACCURACY;

        Circle2D circle = new Circle2D(center, r);
        assertVectorEquals(circle.tangent(0), new Vector2D(0, r), eps);
        assertVectorEquals(circle.tangent(Math.PI / 2), new Vector2D(-r, 0), eps);
        assertVectorEquals(circle.tangent(Math.PI), new Vector2D(0, -r), eps);
        assertVectorEquals(circle.tangent(3 * Math.PI / 2), new Vector2D(r, 0), eps);

        circle = new Circle2D(center, r, false);
        assertVectorEquals(circle.tangent(0), new Vector2D(0, -r), eps);
        assertVectorEquals(circle.tangent(Math.PI / 2), new Vector2D(-r, 0), eps);
        assertVectorEquals(circle.tangent(Math.PI), new Vector2D(0, r), eps);
        assertVectorEquals(circle.tangent(3 * Math.PI / 2), new Vector2D(r, 0), eps);
    }

    public void testGetPositionPoint2D() {
        Circle2D circle;
        Point2D point;
        double x0 = 20;
        double y0 = 30;
        double r2 = Math.sqrt(2);

        double eps = 1e-12;

        // Standard circle
        circle = new Circle2D(x0, y0, 10);
        point = new Point2D(x0 + r2, y0 + r2);
        assertEquals(circle.position(point), Math.PI / 4, eps);

        // inverted circle
        circle = new Circle2D(x0, y0, 10, false);
        point = new Point2D(x0 + r2, y0 - r2);
        assertEquals(circle.position(point), Math.PI / 4, eps);
    }

    public void testGetPointDouble() {
        Circle2D circle;
        Point2D point;
        double x0 = 20;
        double y0 = 30;
        double r = 10;
        double eps = IShape2D.ACCURACY;

        // Standard circle
        circle = new Circle2D(x0, y0, 10);
        point = circle.point(Math.PI / 2);
        assertTrue(new Point2D(x0, y0 + r).almostEquals(point, eps));

        // inverted circle
        circle = new Circle2D(x0, y0, 10, false);
        point = circle.point(3 * Math.PI / 2);
        assertTrue(new Point2D(x0, y0 + r).almostEquals(point, eps));
    }

    /*
     * Test for double getDistance(Point2D)
     */
    public void testGetDistancePoint2D() {
        Circle2D circle = new Circle2D(0, 0, 10);
        assertEquals(circle.distance(new Point2D(10, 0)), 0, 1e-14);

        circle = new Circle2D(2, 3, 4);
        assertEquals(circle.distance(new Point2D(2, 3)), 4, 1e-14);
        assertEquals(circle.distance(new Point2D(6, 3)), 0, 1e-14);
        assertEquals(circle.distance(new Point2D(2, 7)), 0, 1e-14);
        assertEquals(circle.distance(new Point2D(-2, 3)), 0, 1e-14);
        assertEquals(circle.distance(new Point2D(2, -1)), 0, 1e-14);
    }

    /*
     * Test for double getDistance(Point2D)
     */
    public void testGetSignedDistancePoint2D() {
        Circle2D circle = new Circle2D(0, 0, 10);
        assertEquals(circle.signedDistance(new Point2D(10, 0)), 0, 1e-14);

        assertEquals(circle.signedDistance(new Point2D(0, 0)), -10, 1e-14);
        assertEquals(circle.signedDistance(new Point2D(6, 0)), -4, 1e-14);
        assertEquals(circle.signedDistance(new Point2D(0, 6)), -4, 1e-14);
        assertEquals(circle.signedDistance(new Point2D(-6, 0)), -4, 1e-14);
        assertEquals(circle.signedDistance(new Point2D(0, -6)), -4, 1e-14);
    }

    /*
     * Test for boolean isInside(Point2D)
     */
    public void testIsInsidePoint2D() {
        Circle2D circle = new Circle2D(0, 0, 10);

        Point2D point1 = new Point2D(9, 0);
        assertTrue(circle.isInside(point1));

        Point2D point2 = new Point2D(11, 0);
        assertTrue(!circle.isInside(point2));

        circle = new Circle2D(20, 50, 10);
        point1 = new Point2D(29, 50);
        assertTrue(circle.isInside(point1));

        point2 = new Point2D(31, 50);
        assertTrue(!circle.isInside(point2));
    }

    /*
     * Test for Point2D[] getIntersections(StraightObject2D)
     */
    public void testGetIntersectionsStraightLine2D() {
        Circle2D circle = new Circle2D(2, 3, 4);
        Collection<Point2D> points;
        double eps = IShape2D.ACCURACY;

        // horizontal line through touching circle on one point
        StraightLine2D line0 = new StraightLine2D(6, 0, 0, 1);
        points = circle.intersections(line0);
        assertTrue(points.size() == 1);

        // horizontal line touching circle in 2 points, passing through center
        StraightLine2D line1 = new StraightLine2D(2, 0, 0, 1);
        points = circle.intersections(line1);
        assertTrue(points.size() == 2);
        Iterator<Point2D> iter = points.iterator();
        Point2D point1 = iter.next();
        assertTrue(point1.almostEquals(new Point2D(2, -1), eps));
        Point2D point2 = iter.next();
        assertTrue(point2.almostEquals(new Point2D(2, 7), eps));

        // not touching
        StraightLine2D line2 = new StraightLine2D(6.2, 0, 0, 1);
        points = circle.intersections(line2);
        assertTrue(points.size() == 0);
    }

    /*
     * Test for Point2D[] getIntersections(StraightObject2D)
     */
    public void testGetIntersectionsLineSegment2D() {
        // Test with a centered circle and 4 edges in each main direction
        Circle2D circle = new Circle2D(0, 0, 10);
        double eps = IShape2D.ACCURACY;

        Collection<Point2D> points1 = circle.intersections(new LineSegment2D(0, 0, 20, 0));
        Collection<Point2D> points2 = circle.intersections(new LineSegment2D(0, 0, 0, 20));
        Collection<Point2D> points3 = circle.intersections(new LineSegment2D(0, 0, -20, 0));
        Collection<Point2D> points4 = circle.intersections(new LineSegment2D(0, 0, 0, -20));
        assertTrue(points1.iterator().next().almostEquals(new Point2D(10, 0), eps));
        assertTrue(points2.iterator().next().almostEquals(new Point2D(0, 10), eps));
        assertTrue(points3.iterator().next().almostEquals(new Point2D(-10, 0), eps));
        assertTrue(points4.iterator().next().almostEquals(new Point2D(0, -10), eps));

        circle = new Circle2D(50, 100, 50);
        LineSegment2D line = new LineSegment2D(new Point2D(100, 0), new Point2D(100, 100));
        assertTrue(circle.intersections(line).size() == 1);
    }

    public void testGetIntersectionsCircle2DCircle2D() {
        Circle2D circle1, circle2;
        Collection<Point2D> inters;
        Iterator<Point2D> iter;
        double eps = IShape2D.ACCURACY;

        // 2 circles one inside another
        circle1 = new Circle2D(-1, 0, 5);
        circle2 = new Circle2D(1, 0, 2);
        inters = Circle2D.circlesIntersections(circle1, circle2);
        assertTrue(inters.size() == 0);

        // 2 circles which do not touch
        circle1 = new Circle2D(-4, 0, 3);
        circle2 = new Circle2D(4, 0, 3);
        inters = Circle2D.circlesIntersections(circle1, circle2);
        assertTrue(inters.size() == 0);

        // 2 circles with same radius
        circle1 = new Circle2D(-4, 0, 5);
        circle2 = new Circle2D(4, 0, 5);
        inters = Circle2D.circlesIntersections(circle1, circle2);
        assertTrue(inters.size() == 2);
        iter = inters.iterator();
        assertTrue(iter.next().almostEquals(new Point2D(0, 3), eps));
        assertTrue(iter.next().almostEquals(new Point2D(0, -3), eps));

        // 2 circles with different radius
        circle1 = new Circle2D(0, 0, 3);
        circle2 = new Circle2D(4, 0, 5);
        inters = Circle2D.circlesIntersections(circle1, circle2);
        assertTrue(inters.size() == 2);
        iter = inters.iterator();
        assertTrue(iter.next().almostEquals(new Point2D(0, 3), eps));
        assertTrue(iter.next().almostEquals(new Point2D(0, -3), eps));
    }

    /**
     * Check transform by translation.
     */
    public void testTransform_Translation2D() {
        // Transform with a translation
        Circle2D circle = new Circle2D(2, 3, 4);
        AffineTransform2D trans = AffineTransform2D.createTranslation(1, 2);
        IEllipseShape2D transformed = circle.transform(trans);

        // tests
        assertTrue(transformed instanceof Circle2D);
        assertTrue(transformed.equals(new Circle2D(3, 5, 4)));
        assertTrue(transformed.isDirect());
    }

    /**
     * Check transform by rotation.
     */
    public void testTransform_Rotation2D() {
        // Transform with a translation
        Circle2D circle = new Circle2D(2, 3, 4);
        AffineTransform2D trans = AffineTransform2D.createRotation(0, 0, Math.PI / 2);
        IEllipseShape2D transformed = circle.transform(trans);

        // tests
        assertTrue(transformed instanceof Circle2D);
        assertTrue(transformed.equals(new Circle2D(-3, 2, 4)));
        assertTrue(transformed.isDirect());
    }

    /**
     * Check transform by scaling.
     */
    public void testTransform_Scaling2D() {
        // Transform with a translation
        Circle2D circle = new Circle2D(2, 3, 4);
        AffineTransform2D trans = AffineTransform2D.createScaling(2, 2);
        IEllipseShape2D transformed = circle.transform(trans);

        // tests
        assertTrue(transformed instanceof Circle2D);
        assertTrue(transformed.equals(new Circle2D(4, 6, 8)));
        assertTrue(transformed.isDirect());
    }

    /**
     * Check transform by line reflection.
     */
    public void testTransform_LineReflection2D() {
        // Transform with a translation
        Circle2D circle = new Circle2D(2, 3, 4);
        StraightLine2D line = new StraightLine2D(0, 0, 1, 1);
        AffineTransform2D trans = AffineTransform2D.createLineReflection(line);
        IEllipseShape2D transformed = circle.transform(trans);

        // tests
        assertTrue(transformed instanceof Circle2D);
        assertTrue(transformed.equals(new Circle2D(3, 2, 4, false)));
        assertTrue(!transformed.isDirect());
    }

    public void testGetSubCurve() {
        Circle2D circle;
        CircleArc2D arc1, arc2;

        // try with a direct circle
        circle = new Circle2D(0, 0, 10, true);
        arc1 = new CircleArc2D(circle, Math.PI / 2, Math.PI / 2);
        arc2 = circle.subCurve(Math.PI / 2, Math.PI);
        assertTrue(arc1.equals(arc2));

        // try again with an indirect circle
        circle = new Circle2D(0, 0, 10, false);
        arc1 = new CircleArc2D(circle, Math.PI / 2, -Math.PI / 2);
        arc2 = circle.subCurve(3 * Math.PI / 2, 2 * Math.PI);
        assertTrue(arc1.equals(arc2));
    }

    /**
     * Clip with a box when circle is totally inside the box
     */
    public void testClipBox_Inside() {
        Circle2D circle = new Circle2D(5, 5, 2);
        Box2D box = new Box2D(0, 10, 0, 10);

        ICurveSet2D<? extends IContinuousOrientedCurve2D> clipped = circle.clip(box);
        Collection<? extends IContinuousOrientedCurve2D> curves = clipped.curves();
        assertTrue(curves.size() == 1);

        IContinuousOrientedCurve2D curve = curves.iterator().next();
        assertTrue(curve instanceof Circle2D);
    }

    /**
     * Clip with a box when circle is totally outside the box
     */
    public void testClipBox_Outside() {
        Circle2D circle = new Circle2D(5, 5, 2);
        Box2D box = new Box2D(10, 20, 0, 10);

        ICurveSet2D<? extends IContinuousOrientedCurve2D> clipped = circle.clip(box);
        Collection<? extends IContinuousOrientedCurve2D> curves = clipped.curves();
        assertTrue(curves.size() == 0);
    }

    /**
     * Clip with a box which keeps only circle arc from 0 to PI.
     */
    public void testClipBox_Half() {
        Circle2D circle = new Circle2D(100, 150, 10);
        Box2D box = new Box2D(50, 150, 50, 150);

        ICurveSet2D<? extends IContinuousOrientedCurve2D> clipped = circle.clip(box);
        Collection<? extends IContinuousOrientedCurve2D> curves = clipped.curves();
        assertTrue(curves.size() == 1);

        IContinuousOrientedCurve2D curve = curves.iterator().next();
        assertTrue(curve instanceof CircleArc2D);

        CircleArc2D arc = (CircleArc2D) curve;
        assertTrue(new CircleArc2D(circle, Math.PI, Math.PI).equals(arc));
    }

    /**
     * Clip with a box which keeps only circle arc from 0 to PI/2.
     */
    public void testClipBox_Quarter() {
        Circle2D circle = new Circle2D(0, 0, 10);
        Box2D box = new Box2D(0, 20, 0, 20);

        ICurveSet2D<? extends IContinuousOrientedCurve2D> clipped = circle.clip(box);
        Collection<? extends IContinuousOrientedCurve2D> curves = clipped.curves();
        assertTrue(curves.size() == 1);

        IContinuousOrientedCurve2D curve = curves.iterator().next();
        assertTrue(curve instanceof CircleArc2D);

        CircleArc2D arc = (CircleArc2D) curve;
        assertTrue(new CircleArc2D(circle, 0, Math.PI / 2).equals(arc));
    }

    /**
     * Clip with a box when circle is totally inside the box, but that touches the box.
     */
    public void testClipBox_Touch() {
        Circle2D circle = new Circle2D(50, 50, 50);
        Box2D box = new Box2D(0, 100, 0, 100);

        ICurveSet2D<? extends ISmoothCurve2D> clipped = circle.clip(box);
        Collection<?> curves = clipped.curves();
        assertTrue(curves.size() == 1);
        assertTrue(curves.iterator().next().equals(circle));
    }

    public void testClipBox_TouchOutsideLeft() {
        Circle2D circle = new Circle2D(40, 100, 10);
        Box2D box = new Box2D(50, 150, 50, 150);

        ICurveSet2D<? extends ISmoothCurve2D> clipped = circle.clip(box);
        Collection<?> curves = clipped.curves();
        assertTrue(curves.size() == 0);
    }

    public void testClipBox_Problem() {
        Circle2D circle = new Circle2D(6, 0, 6 + 6.4);
        Box2D box = new Box2D(-10, 10, -10, 10);

        ICurveSet2D<? extends ISmoothCurve2D> clipped = circle.clip(box);
        Collection<?> curves = clipped.curves();
        assertTrue(curves.size() == 1);
    }

    public void testCirclesIntersections() {
        Circle2D ca = new Circle2D(0, -10, 10);
        Circle2D cb = new Circle2D(0, 10, 10);

        Collection<Point2D> points = Circle2D.circlesIntersections(ca, cb);

        assertEquals(1, points.size());
    }
}
