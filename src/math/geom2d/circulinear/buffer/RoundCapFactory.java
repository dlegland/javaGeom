/**
 * File: 	CircularCapFactory.java
 * Project: javageom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package math.geom2d.circulinear.buffer;


import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.conic.CircleArc2D;

import static java.lang.Math.PI;

/**
 * Generate a circular cap at the end of a curve.
 * @author dlegland
 *
 */
public class RoundCapFactory implements CapFactory {

	public RoundCapFactory() {
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.CapFactory#createCap(math.geom2d.Point2D, math.geom2d.Vector2D, double)
	 */
	public CirculinearContinuousCurve2D createCap(Point2D center,
			Vector2D direction, double dist) {
		double angle = direction.getAngle();
		double angle1 = Angle2D.formatAngle(angle - PI/2);
		double angle2 = Angle2D.formatAngle(angle + PI/2);
		return CircleArc2D.create(center, dist, angle1, angle2, true);
	}

	public CirculinearContinuousCurve2D createCap(Point2D p1, Point2D p2) {
		Point2D center = Point2D.midPoint(p1, p2);
		double radius = p1.getDistance(p2)/2;
		
		double angle1 = Angle2D.getHorizontalAngle(center, p1);
		double angle2 = Angle2D.getHorizontalAngle(center, p2);
		return CircleArc2D.create(center, radius, angle1, angle2, true);
	}

}
