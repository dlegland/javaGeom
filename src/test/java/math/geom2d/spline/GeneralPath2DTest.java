/**
 * 
 */
package math.geom2d.spline;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.curve.IContinuousCurve2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.line.StraightLine2D;
import junit.framework.TestCase;

/**
 * @author dlegland
 *
 */
public class GeneralPath2DTest extends TestCase {

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#GeneralPath2D()}.
     */
    public void testGeneralPath2D() {
        GeneralPath2D path = new GeneralPath2D();
        path.moveTo(new Point2D(200, 200));
        path.cubicTo(new Point2D(200, 300), new Point2D(300, 400), new Point2D(400, 200));
        path.lineTo(new Point2D(300, 200));
        path.quadTo(new Point2D(200, 50), new Point2D(100, 200));
        path.closePath();
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#GeneralPath2D(math.geom2d.spline.GeneralPath2D)}.
     */
    public void testGeneralPath2DGeneralPath2D() {
        GeneralPath2D path = createDefaultPath();
        GeneralPath2D path2 = new GeneralPath2D(path);
        assertEquals(path, path2);
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#AlmostEquals(Object,double)}.
     */
    public void testGeneralPath2DAlmostEquals() {
        GeneralPath2D path = createDefaultPath();
        GeneralPath2D path2 = new GeneralPath2D(path);
        assertTrue(path.almostEquals(path2, IShape2D.ACCURACY));
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#intersections(math.geom2d.line.LinearShape2D)}.
     */
    public void testIntersections() {
        GeneralPath2D path = createDefaultPath();

        StraightLine2D line = new StraightLine2D(180, 220, 3, 2);

        Collection<Point2D> intersections = path.intersections(line);
        assertEquals(4, intersections.size());
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#intersections(math.geom2d.line.LinearShape2D)}.
     */
    public void testBoundingBox() {
        GeneralPath2D path = createDefaultPath();

        Box2D box = path.boundingBox();
        assertTrue(box.isBounded());
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#distance(math.geom2d.Point2D)}.
     */
    public void testDistance() {
        GeneralPath2D path = createDefaultPath();

        Point2D p = new Point2D(400, 100);
        double dist = path.distance(p);
        assertEquals(dist, 100, IShape2D.ACCURACY);
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#contains(math.geom2d.Point2D)}.
     */
    public void testContainsPoint2D() {
        GeneralPath2D path = createDefaultPath();

        Point2D p = new Point2D(400, 200);
        assertTrue(path.contains(p));

        // point on a line segment
        assertTrue(path.contains(new Point2D(350, 200)));

        // point on a closing segment
        assertTrue(path.contains(new Point2D(350, 200)));

        // point not on the path
        assertFalse(path.contains(new Point2D(250, 200)));
    }

    /**
     * Test method for {@link math.geom2d.spline.GeneralPath2D#singularPoints()}.
     */
    public void testSingularPoints() {
        GeneralPath2D path = createDefaultPath();

        Collection<Point2D> points = path.singularPoints();
        assertEquals(4, points.size());
    }

    public void testPointDoubleMid() {
        GeneralPath2D path = new GeneralPath2D();
        path.moveTo(new Point2D(200, 200));
        path.lineTo(new Point2D(300, 200));

        Point2D p = path.point(.5);
        assertTrue(p.almostEquals(new Point2D(250, 200), IShape2D.ACCURACY));
    }

    public void testPointDoubleLast() {
        GeneralPath2D path = new GeneralPath2D();
        path.moveTo(new Point2D(200, 200));
        path.lineTo(new Point2D(300, 200));

        Point2D p = path.point(1);
        assertTrue(p.almostEquals(new Point2D(300, 200), IShape2D.ACCURACY));
    }

    public void testContinuousCurves() {
        GeneralPath2D path = createDefaultPath();
        Collection<? extends IContinuousCurve2D> curves = path.continuousCurves();
        assertFalse(curves.isEmpty());
        assertEquals(1, curves.size());
    }

    public void testClipBox() {
        GeneralPath2D path = createDefaultPath();
        Box2D box = new Box2D(150, 350, 0, 500);
        ICurve2D curve = path.clip(box);
        assertFalse(curve.isEmpty());
    }

    private GeneralPath2D createDefaultPath() {
        GeneralPath2D path = new GeneralPath2D();
        path.moveTo(new Point2D(200, 200));
        path.cubicTo(new Point2D(200, 300), new Point2D(300, 400), new Point2D(400, 200));
        path.lineTo(new Point2D(300, 200));
        path.quadTo(new Point2D(200, 50), new Point2D(100, 200));
        path.closePath();

        return path;
    }
}
