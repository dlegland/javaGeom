/**
 * 
 */
package math.geom2d.spline;

import junit.framework.TestCase;
import math.geom2d.point.Point2D;

/**
 * @author dlegland
 *
 */
public class PolyCubicBezierCurve2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.spline.PolyCubicBezierCurve2D#PolyCubicBezierCurve2D(math.geom2d.spline.CubicBezierCurve2D[])}.
     */
    public void testPolyCubicBezierCurve2D_CubicBezierCurve2D_Array() {
        Point2D p11 = new Point2D(0, 0);
        Point2D p12 = new Point2D(0, 1);
        Point2D p13 = new Point2D(1, 1);
        Point2D p14 = new Point2D(1, 0);
        CubicBezierCurve2D sub1 = new CubicBezierCurve2D(p11, p12, p13, p14);

        Point2D p21 = new Point2D(1, 0);
        Point2D p22 = new Point2D(2, 0);
        Point2D p23 = new Point2D(2, 1);
        Point2D p24 = new Point2D(3, 1);
        CubicBezierCurve2D sub2 = new CubicBezierCurve2D(p21, p22, p23, p24);

        PolyCubicBezierCurve2D curve = new PolyCubicBezierCurve2D(sub1, sub2);

        assertEquals(2, curve.size());
    }

}
