/**
 * File: 	TriangleCapFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 janv. 2011
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
public class TriangleCapFactory implements CapFactory {

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Vector2D, double)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D center,
			Vector2D direction, double dist) {
		double theta = direction.angle();
		Point2D p1 = Point2D.createPolar(center, dist, theta-Math.PI/2);
		Point2D p2 = Point2D.createPolar(center, dist, theta);
		Point2D p3 = Point2D.createPolar(center, dist, theta+Math.PI/2);
		return new Polyline2D(new Point2D[]{p1, p2, p3});
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Point2D)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D p1, Point2D p2) {
		Point2D mid = Point2D.midPoint(p1, p2);
		double rho = Point2D.distance(p1, p2)/2;
		double theta = Angle2D.horizontalAngle(p1, p2) - Math.PI/2;
		Point2D pt = Point2D.createPolar(mid, rho, theta);
		return new Polyline2D(new Point2D[]{p1, pt, p2});
	}

}
