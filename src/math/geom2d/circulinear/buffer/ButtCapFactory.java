/**
 * File: 	ButtCapFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.line.LineSegment2D;


/**
 * @author dlegland
 *
 */
public class ButtCapFactory implements CapFactory {

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Vector2D, double)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D center,
			Vector2D direction, double dist) {
		double theta = direction.angle();
		Point2D p1 = Point2D.createPolar(center, dist/2, theta-Math.PI/2);
		Point2D p2 = Point2D.createPolar(center, dist/2, theta+Math.PI/2);
		return new LineSegment2D(p1, p2);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Point2D)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D p1, Point2D p2) {
		return new LineSegment2D(p1, p2);
	}

}
