/**
 * File: 	PolyCirculinearCurve2DTest.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 30 juin 09
 */
package math.geom2d.circulinear;

import java.util.LinkedList;

import junit.framework.TestCase;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.InvertedRay2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Ray2D;

/**
 * @author dlegland
 *
 */
public class PolyCirculinearCurve2DTest extends TestCase {

    /*
     * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#parallel(double)}
     */
    public void testGetParallel_Wedge() {
        Point2D point = new Point2D(20, 10);
        Vector2D v1 = new Vector2D(-2, 0);
        Vector2D v2 = new Vector2D(0, 3);
        AbstractLine2D[] array = new AbstractLine2D[] { new InvertedRay2D(point, v1), new Ray2D(point, v2) };
        PolyCirculinearCurve2D<AbstractLine2D> curve = new PolyCirculinearCurve2D<>(array);

        double dist = 5;

        ICirculinearContinuousCurve2D parallel1 = curve.parallel(dist);
        assertEquals(3, parallel1.smoothPieces().size());
        ICirculinearContinuousCurve2D parallel2 = curve.parallel(-dist);
        assertEquals(3, parallel2.smoothPieces().size());
    }

    /**
     * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#length()}.
     */
    public void testGetLength() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0));
        Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
        LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), new Point2D(20, 10));
        PolyCirculinearCurve2D<?> curve = new PolyCirculinearCurve2D<>(new ICirculinearContinuousCurve2D[] { line1, line2, line3 });

        assertEquals(curve.length(), 30, 1e-14);
    }

    /**
     * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#length(double)}.
     */
    public void testGetLengthDouble() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0));
        Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
        LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), new Point2D(20, 10));
        PolyCirculinearCurve2D<?> curve = new PolyCirculinearCurve2D<>(new ICirculinearContinuousCurve2D[] { line1, line2, line3 });

        assertEquals(curve.length(1), 10, 1e-14);
        assertEquals(curve.length(2), 10, 1e-14);
        assertEquals(curve.length(3), 20, 1e-14);
        assertEquals(curve.length(4), 20, 1e-14);
        assertEquals(curve.length(5), 30, 1e-14);
        assertEquals(curve.length(0), 0, 1e-14);
    }

    /**
     * Test method for {@link math.geom2d.circulinear.PolyCirculinearCurve2D#position(double)}.
     */
    public void testGetPositionDouble() {
        LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(10, 0));
        Line2D line2 = new Line2D(new Point2D(10, 0), new Point2D(10, 10));
        LineSegment2D line3 = new LineSegment2D(new Point2D(10, 10), new Point2D(20, 10));
        PolyCirculinearCurve2D<?> curve = new PolyCirculinearCurve2D<>(new ICirculinearContinuousCurve2D[] { line1, line2, line3 });

        assertEquals(curve.position(0), 0, 1e-14);
        assertEquals(curve.position(5), .5, 1e-14);
        assertEquals(curve.position(10), 1, 1e-14);
        assertEquals(curve.position(15), 2.5, 1e-14);
        assertEquals(curve.position(20), 3, 1e-14);
        assertEquals(curve.position(25), 4.5, 1e-14);
        assertEquals(curve.position(30), 5, 1e-14);
    }

    public void testGetReverseCurve() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(20, 10);
        Point2D p3 = new Point2D(20, 20);
        Point2D p4 = new Point2D(10, 20);

        LineSegment2D[] edges = new LineSegment2D[] { new LineSegment2D(p1, p2), new LineSegment2D(p2, p3), new LineSegment2D(p3, p4), new LineSegment2D(p4, p1), };

        PolyCirculinearCurve2D<LineSegment2D> curve = PolyCirculinearCurve2D.create(edges, true);

        assertTrue(curve.isClosed());

        ICirculinearContinuousCurve2D reversed = curve.reverse();
        assertTrue(reversed.isClosed());

    }

    public void testBoundingBox() {
        Point2D p1 = new Point2D(5, 1);
        Point2D p2 = new Point2D(-7, 2);

        LineSegment2D segment = new LineSegment2D(p1, p2);
        LinkedList<ICirculinearElement2D> lineList = new LinkedList<>();
        lineList.add(segment);
        PolyCirculinearCurve2D<ICirculinearElement2D> curve = new PolyCirculinearCurve2D<>(lineList);

        Box2D refBox = new Box2D(p1, p2);
        Box2D box = curve.boundingBox();

        assertTrue(refBox.equals(box));
    }

    public void testBoundingBox2() {
        LinkedList<ICirculinearElement2D> lineList = new LinkedList<>();
        lineList.add(new LineSegment2D(new Point2D(-1, 3), new Point2D(-2, 1)));
        lineList.add(new LineSegment2D(new Point2D(-2, 1), new Point2D(-3, 3)));
        PolyCirculinearCurve2D<ICirculinearElement2D> curv = new PolyCirculinearCurve2D<>(lineList);

        Box2D box = curv.boundingBox();

        Box2D refBox = new Box2D(-3, -1, 1, 3);
        assertTrue(refBox.equals(box));
    }
}
