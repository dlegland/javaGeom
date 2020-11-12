/**
 * 
 */
package net.javageom.geom2d.circulinear.buffer;

import junit.framework.TestCase;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.domain.Boundary2D;
import net.javageom.geom2d.domain.Domain2D;
import net.javageom.geom2d.line.LineSegment2D;
import net.javageom.geom2d.polygon.Polyline2D;

/**
 * @author dlegland
 *
 */
public class ButtCapFactoryTest extends TestCase {

	public void testGetBufferLineSegment () {

		LineSegment2D curve = new LineSegment2D(
				new Point2D(100, 100), 
				new Point2D(200, 100));
		
		BufferCalculator bc1 = new BufferCalculator(
				new BevelJoinFactory(), new ButtCapFactory());
		Domain2D domain = bc1.computeBuffer(curve, 50);

		assertTrue(domain.isBounded());
		assertFalse(domain.isEmpty());
		
		Boundary2D boundary = domain.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}
	
	public void testGetBufferPolyline () {
		Polyline2D curve = new Polyline2D(new Point2D[]{
				new Point2D(50, 50), 
				new Point2D(50, 100), 
				new Point2D(100, 100), 
				new Point2D(100, 50), 
				new Point2D(150, 100), 
				new Point2D(150, 50), 
				});

		BufferCalculator bc1 = new BufferCalculator(
				new BevelJoinFactory(), new ButtCapFactory());
		Domain2D domain = bc1.computeBuffer(curve, 20);

		assertTrue(domain.isBounded());
		assertFalse(domain.isEmpty());
		
		Boundary2D boundary = domain.boundary();
		assertEquals(1, boundary.continuousCurves().size());
	}
}
