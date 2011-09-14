package math.geom2d.circulinear.buffer;

import junit.framework.TestCase;
import math.geom2d.Point2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.Polyline2D;

public class RoundJoinFactoryTest extends TestCase {
	
	public void testGetParallels_SmallAnglePolyline() {
		Polyline2D polyline = new Polyline2D(new Point2D[]{
				new Point2D(200, 100),
				new Point2D(100, 100),
				new Point2D(180, 140) });
		double dist = 30;
		
		BufferCalculator bc = BufferCalculator.getDefaultInstance();
		Domain2D buffer = bc.computeBuffer(polyline, dist);
		
		assertEquals(1, buffer.getBoundary().getBoundaryCurves().size());
	}


}
