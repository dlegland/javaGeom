/**
 * File: 	CirculinearCurveArray2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 26 juil. 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.CircleInversion2D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class CirculinearCurveArray2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.circulinear.ICirculinearCurveSet2D#length()}.
     */
    public void testGetLength() {
        LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
        LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);
        ICirculinearCurveSet2D<LineSegment2D> set = CirculinearCurveArray2D.create(line1, line2);
        assertEquals(set.length(), 200, IShape2D.ACCURACY);
    }

    /**
     * Test method for {@link math.geom2d.circulinear.ICirculinearCurveSet2D#buffer(double)}.
     */
    public void testGetBuffer() {
        // create the two line segments
        LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
        LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);

        // gather line segments into a curve set
        CirculinearCurveArray2D<LineSegment2D> set = CirculinearCurveArray2D.create(line1, line2);

        // only one continuous boundary
        IDomain2D buffer = set.buffer(30);
        IBoundary2D boundary = buffer.boundary();
        assertEquals(boundary.continuousCurves().size(), 1);

        // 12 parts: 4 circle arcs, and 4*2 line segments
        Collection<? extends ISmoothCurve2D> smoothCurves = boundary.continuousCurves().iterator().next().smoothPieces();
        assertEquals(12, smoothCurves.size());
    }

    /**
     * Test method for {@link math.geom2d.circulinear.CirculinearCurveArray2D#transform(CircleInversion2D)}.
     */
    public void testTransform_CircleInversion2D() {
        // create the two line segments
        LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
        LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);

        // gather line segments into a curve set
        CirculinearCurveArray2D<LineSegment2D> set = CirculinearCurveArray2D.create(line1, line2);

        Circle2D circle = new Circle2D(new Point2D(0, 0), 50);
        CircleInversion2D inv = new CircleInversion2D(circle);

        CirculinearCurveArray2D<? extends ICirculinearCurve2D> res = set.transform(inv);
        assertEquals(2, res.size());

        for (ICirculinearCurve2D curve : res.curves())
            assertTrue(curve instanceof CircleArc2D);
    }

    /**
     * Test method for {@link math.geom2d.circulinear.CirculinearCurveArray2D#transform(CircleInversion2D)}.
     */
    public void testClip_Box2D() {
        // create the two line segments
        LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
        LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);
        LineSegment2D line3 = new LineSegment2D(300, 50, 300, 150);

        // gather line segments into a curve set
        CirculinearCurveArray2D<LineSegment2D> set = CirculinearCurveArray2D.create(line1, line2, line3);

        Box2D box = new Box2D(0, 200, 0, 200);

        CirculinearCurveArray2D<? extends ICirculinearCurve2D> res = set.clip(box);
        assertEquals(2, res.size());

        for (ICirculinearCurve2D curve : res.curves())
            assertTrue(curve instanceof LineSegment2D);
    }

}
