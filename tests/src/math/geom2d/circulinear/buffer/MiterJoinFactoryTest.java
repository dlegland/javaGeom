/**
 * 
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Polyline2D;
import junit.framework.TestCase;

/**
 * @author David
 *
 */
public class MiterJoinFactoryTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.circulinear.buffer.MiterJoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)}.
	 */
	public void testCreateJoin_TwoLineSegmentsLeft() {
		JoinFactory jf = new MiterJoinFactory();
		
		LineSegment2D line1 = new LineSegment2D(new Point2D(10, 30), new Point2D(60, 30));
		LineSegment2D line2 = new LineSegment2D(new Point2D(60, 30), new Point2D(60, 80));
		
		Curve2D join = jf.createJoin(line1, line2, 20);
		
		assertTrue(join instanceof Polyline2D);
		Polyline2D poly = (Polyline2D) join;
		
		assertEquals(3, poly.getVertexNumber());
		Point2D vertex = poly.getVertex(1);
		assertTrue(vertex.getDistance(new Point2D(80, 10)) < Shape2D.ACCURACY);
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.buffer.MiterJoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)}.
	 */
	public void testCreateJoin_TwoLineSegmentsRight() {
		JoinFactory jf = new MiterJoinFactory();
		
		LineSegment2D line1 = new LineSegment2D(new Point2D(10, 80), new Point2D(60, 80));
		LineSegment2D line2 = new LineSegment2D(new Point2D(60, 80), new Point2D(60, 30));
		
		Curve2D join = jf.createJoin(line1, line2, 20);
		
		assertTrue(join instanceof Polyline2D);
		Polyline2D poly = (Polyline2D) join;
		
		assertEquals(2, poly.getVertexNumber());
	}
	
}
