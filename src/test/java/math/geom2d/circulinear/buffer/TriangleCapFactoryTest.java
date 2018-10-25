/**
 * 
 */
package math.geom2d.circulinear.buffer;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.domain.IDomain2D;
import math.geom2d.polygon.Polyline2D;

/**
 * @author dlegland
 *
 */
public class TriangleCapFactoryTest extends TestCase {

    public void testGetBufferPolyline() {
        Polyline2D curve = new Polyline2D(new Point2D[] { new Point2D(50, 50), new Point2D(50, 100), new Point2D(100, 100), new Point2D(100, 50), new Point2D(150, 100), new Point2D(150, 50), });

        BufferCalculator bc1 = new BufferCalculator(new BevelJoinFactory(), new TriangleCapFactory());
        IDomain2D domain = bc1.computeBuffer(curve, 20);

        assertTrue(domain.isBounded());
        assertFalse(domain.isEmpty());

        IBoundary2D boundary = domain.boundary();
        assertEquals(1, boundary.continuousCurves().size());
    }

}
