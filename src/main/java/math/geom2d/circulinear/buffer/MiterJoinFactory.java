/**
 * File: 	MiterJoinFactory.java
 * Project: javageom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import static java.lang.Math.PI;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.polygon.Polyline2D;


/**
 * @author dlegland
 *
 */
public class MiterJoinFactory implements JoinFactory {

	private double minDenom = 1e-100;
	
	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.JoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)
	 */
	public CirculinearContinuousCurve2D createJoin(CirculinearElement2D curve1,
			CirculinearElement2D curve2, double dist) {
		
		// extremity of each curve
		Point2D pc1 = curve1.lastPoint();
		Point2D pc2 = curve2.firstPoint();
		
		// Compute tangent angle of each curve 
		Vector2D vect1 = curve1.tangent(curve1.t1());
		Vector2D vect2 = curve2.tangent(curve2.t0());
		double theta1 = vect1.angle();
		double theta2 = vect2.angle();
//		System.out.println(Math.toDegrees(theta1) + " " + Math.toDegrees(theta2));
		
//		// compute center point
		Point2D center = Point2D.midPoint(pc1, pc2);
		
		// Extremities of parallels
		Point2D p1 = Point2D.createPolar(center, dist, theta1 - PI / 2);
		Point2D p2 = Point2D.createPolar(center, dist, theta2 - PI / 2);
		
		double dtheta = Angle2D.formatAngle(theta2 - theta1);
		if (dtheta > PI)
			dtheta = dtheta - 2*PI;
		
		double denom = Math.cos(dtheta / 2);
		
		denom = Math.max(denom, this.minDenom);
		
		double hypot = dist / denom;
		double angle;
		if (dtheta > 0 ^ dist < 0) {
			// Creates a right-angle corner between the two parallels
			angle = theta1 - Math.PI / 2 + dtheta / 2;
			Point2D pt = Point2D.createPolar(center, hypot, angle);
			return new Polyline2D(new Point2D[]{p1, pt, p2});
			
		} else {
			// return a direct connection between extremities
			return new Polyline2D(new Point2D[]{pc1, pc2});		
		}
			
	}
}
