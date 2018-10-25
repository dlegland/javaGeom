/**
 * 
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;
import junit.framework.TestCase;

/**
 * @author David
 *
 */
public class MiterJoinFactoryTest extends TestCase {

    public void testGetBufferPolyline() {
        Polyline2D curve = new Polyline2D(new Point2D[] { new Point2D(50, 50), new Point2D(50, 100), new Point2D(100, 100), new Point2D(100, 50), new Point2D(150, 100), new Point2D(150, 50), });

        BufferCalculator bc1 = new BufferCalculator(new MiterJoinFactory(), new RoundCapFactory());
        IDomain2D domain = bc1.computeBuffer(curve, 20);

        assertTrue(domain.isBounded());
        assertFalse(domain.isEmpty());

        IBoundary2D boundary = domain.boundary();
        assertEquals(1, boundary.continuousCurves().size());
    }

    public void testGetBufferLinearRing() {
        LinearRing2D curve = new LinearRing2D(new Point2D[] { new Point2D(100, 100), new Point2D(200, 100), new Point2D(200, 200), new Point2D(150, 150), new Point2D(100, 200), });

        BufferCalculator bc1 = new BufferCalculator(new MiterJoinFactory(), new RoundCapFactory());
        IDomain2D domain = bc1.computeBuffer(curve, 10);

        assertTrue(domain.isBounded());
        assertFalse(domain.isEmpty());

        IBoundary2D boundary = domain.boundary();
        assertEquals(2, boundary.continuousCurves().size());
    }

    /**
     * Test method for {@link math.geom2d.circulinear.buffer.MiterJoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)}.
     */
    public void testCreateJoin_TwoLineSegmentsLeft() {
        JoinFactory jf = new MiterJoinFactory();

        LineSegment2D line1 = new LineSegment2D(new Point2D(10, 30), new Point2D(60, 30));
        LineSegment2D line2 = new LineSegment2D(new Point2D(60, 30), new Point2D(60, 80));

        ICurve2D join = jf.createJoin(line1, line2, 20);

        assertTrue(join instanceof Polyline2D);
        Polyline2D poly = (Polyline2D) join;

        assertEquals(3, poly.vertexNumber());
        Point2D vertex = poly.vertex(1);
        assertTrue(vertex.distance(new Point2D(80, 10)) < IShape2D.ACCURACY);
    }

    /**
     * Test method for {@link math.geom2d.circulinear.buffer.MiterJoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)}.
     */
    public void testCreateJoin_TwoLineSegmentsRight() {
        JoinFactory jf = new MiterJoinFactory();

        LineSegment2D line1 = new LineSegment2D(new Point2D(10, 80), new Point2D(60, 80));
        LineSegment2D line2 = new LineSegment2D(new Point2D(60, 80), new Point2D(60, 30));

        ICurve2D join = jf.createJoin(line1, line2, 20);

        assertTrue(join instanceof Polyline2D);
        Polyline2D poly = (Polyline2D) join;

        assertEquals(2, poly.vertexNumber());
    }

}
