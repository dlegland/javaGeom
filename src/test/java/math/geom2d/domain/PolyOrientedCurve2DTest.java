/**
 * File: 	PolyOrientedCurve2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 16 nov. 08
 */
package math.geom2d.domain;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.conic.CircleArc2D;
import junit.framework.TestCase;

import static java.lang.Math.*;

/**
 * @author dlegland
 *
 */
public class PolyOrientedCurve2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.domain.PolyOrientedCurve2D#windingAngle(java.awt.geom.Point2D)}. Use a curve set formed by two circle arcs.
     */
    public void testGetWindingAnglePoint() {
        double x0 = 150;
        double x1 = 100;
        double x2 = 200;
        double r = 100;
        double y0 = 100;

        Point2D c0 = new Point2D(x0, y0);
        Point2D c1 = new Point2D(x1, y0);
        Point2D c2 = new Point2D(x2, y0);

        double extent = 4 * Math.PI / 3;
        CircleArc2D arc1 = new CircleArc2D(c1, r, Math.PI / 3, extent);
        CircleArc2D arc2 = new CircleArc2D(c2, r, 4 * Math.PI / 3, extent);

        PolyOrientedCurve2D<CircleArc2D> curve = PolyOrientedCurve2D.create(new CircleArc2D[] { arc1, arc2 });

        double eps = 1e-14;
        assertEquals(2 * PI, curve.windingAngle(c0), eps);
        assertEquals(2 * PI, curve.windingAngle(c1), eps);
        assertEquals(2 * PI, curve.windingAngle(c2), eps);

        assertEquals(0, curve.windingAngle(new Point2D(0, 0)), eps);
        assertEquals(0, curve.windingAngle(new Point2D(150, 200)), eps);
        assertEquals(0, curve.windingAngle(new Point2D(250, 200)), eps);

        IOrientedCurve2D reverse = curve.reverse();
        assertEquals(-2 * PI, reverse.windingAngle(c0), eps);
        assertEquals(-2 * PI, reverse.windingAngle(c1), eps);
        assertEquals(-2 * PI, reverse.windingAngle(c2), eps);

        assertEquals(0, reverse.windingAngle(new Point2D(0, 0)), eps);
        assertEquals(0, reverse.windingAngle(new Point2D(150, 200)), eps);
        assertEquals(0, reverse.windingAngle(new Point2D(250, 200)), eps);
    }

    public void testEquals() {
        double x1 = 100;
        double x2 = 200;
        double r = 100;
        double y0 = 100;

        Point2D c1 = new Point2D(x1, y0);
        Point2D c2 = new Point2D(x2, y0);

        double extent = 4 * Math.PI / 3;
        CircleArc2D arc1 = new CircleArc2D(c1, r, Math.PI / 3, extent);
        CircleArc2D arc2 = new CircleArc2D(c2, r, 4 * Math.PI / 3, extent);

        PolyOrientedCurve2D<CircleArc2D> curve = PolyOrientedCurve2D.create(new CircleArc2D[] { arc1, arc2 });

        assertEquals(curve, curve);

        arc1 = new CircleArc2D(c1, r, Math.PI / 3, extent);
        arc2 = new CircleArc2D(c2, r, 4 * Math.PI / 3, extent);

        PolyOrientedCurve2D<CircleArc2D> curve2 = PolyOrientedCurve2D.create(new CircleArc2D[] { arc1, arc2 });

        assertEquals(curve, curve2);
    }

    public void testTransformClosed() {
        double x1 = 100;
        double x2 = 200;
        double r = 100;
        double y0 = 100;

        Point2D c1 = new Point2D(x1, y0);
        Point2D c2 = new Point2D(x2, y0);

        double extent = 4 * Math.PI / 3;
        CircleArc2D arc1 = new CircleArc2D(c1, r, Math.PI / 3, extent);
        CircleArc2D arc2 = new CircleArc2D(c2, r, 4 * Math.PI / 3, extent);

        PolyOrientedCurve2D<CircleArc2D> curve = PolyOrientedCurve2D.create(new CircleArc2D[] { arc1, arc2 });
        curve.setClosed(true);

        AffineTransform2D trans = AffineTransform2D.createRotation(10, 20, Math.PI / 3);
        PolyOrientedCurve2D<?> curve2 = curve.transform(trans);

        assertNotNull(curve2);
        assertTrue(curve2.isClosed());
    }
}
