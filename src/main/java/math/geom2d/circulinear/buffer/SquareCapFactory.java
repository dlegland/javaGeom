/**
 * File: 	SquareCapFactory.java
 * Project: javageom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 Aug. 2011
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.polygon.Polyline2D;


/**
 * @author dlegland
 *
 */
public class SquareCapFactory implements CapFactory {

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Vector2D, double)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D center,
			Vector2D direction, double dist) {
		double theta = direction.angle();
		return createCap(center, theta, dist);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Point2D)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D p1, Point2D p2) {
		Point2D center = Point2D.midPoint(p1, p2);
		double dist = Point2D.distance(p1, p2)/2;
		double theta = Angle2D.horizontalAngle(p1, p2) - Math.PI/2;
		return createCap(center, theta, dist);
	}

	/**
	 * Portion of code shared by the two public methods.
	 */
	private Polyline2D createCap(Point2D center, double theta, double dist) {
		Point2D p1 = Point2D.createPolar(center, dist, theta-Math.PI/2);
		Point2D p4 = Point2D.createPolar(center, dist, theta+Math.PI/2);
		Point2D p2 = Point2D.createPolar(p1, dist, theta);
		Point2D p3 = Point2D.createPolar(p4, dist, theta);
		return new Polyline2D(new Point2D[]{p1, p2, p3, p4});
	}
}
