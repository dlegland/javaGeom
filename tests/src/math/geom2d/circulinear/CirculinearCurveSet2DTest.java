/**
 * File: 	CirculinearCurveSet2DTest.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 26 juil. 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.Shape2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.Boundary2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import junit.framework.TestCase;


/**
 * @author dlegland
 *
 */
public class CirculinearCurveSet2DTest extends TestCase {

	/**
	 * Test method for {@link math.geom2d.circulinear.CirculinearCurveSet2D#getLength()}.
	 */
	public void testGetLength() {
		LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
		LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);
		CirculinearCurveSet2D<LineSegment2D> set = 
			CirculinearCurveArray2D.create(new LineSegment2D[]{line1, line2});
		assertEquals(set.getLength(), 200, Shape2D.ACCURACY);
	}

	/**
	 * Test method for {@link math.geom2d.circulinear.CirculinearCurveSet2D#getBuffer(double)}.
	 */
	public void testGetBuffer() {
		// create the two line segments
		LineSegment2D line1 = new LineSegment2D(50, 100, 150, 100);
		LineSegment2D line2 = new LineSegment2D(100, 50, 100, 150);
		
		// gather line segments into a curve set
		CirculinearCurveArray2D<LineSegment2D> set = 
			new CirculinearCurveArray2D<LineSegment2D>(
					new LineSegment2D[]{line1, line2});
		
		// only one continuous boundary
		Domain2D buffer = set.getBuffer(30);
		Boundary2D boundary = buffer.getBoundary();
		assertEquals(boundary.getContinuousCurves().size(), 1);
		
		// 12 parts: 4 circle arcs, and 4*2 line segments
		Collection<? extends SmoothCurve2D> smoothCurves =
			boundary.getContinuousCurves().iterator().next().getSmoothPieces();
		assertEquals(12, smoothCurves.size());
	}

}
