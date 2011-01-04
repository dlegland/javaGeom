/**
 * File: 	CircularJoinFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import static java.lang.Math.PI;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.conic.CircleArc2D;


/**
 * @author dlegland
 *
 */
public class CircularJoinFactory implements JoinFactory {

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.buffer.JoinFactory#createJoin(math.geom2d.Point2D, math.geom2d.Vector2D, math.geom2d.Vector2D, double)
	 */
	public CirculinearContinuousCurve2D createJoin(Point2D center,
			Vector2D direction1, Vector2D direction2, double dist) {
		// compute angles
		double startAngle, endAngle;
		if(dist > 0) {
			startAngle = direction1.getAngle() - PI/2;
			endAngle = direction2.getAngle() - PI/2;
		} else {
			startAngle = direction1.getAngle() + PI/2;
			endAngle = direction2.getAngle() + PI/2;
		}
		
		// format angles to stay between 0 and 2*PI
		startAngle = Angle2D.formatAngle(startAngle);
		endAngle = Angle2D.formatAngle(endAngle);
		
		// compute angle difference, in absolute value
		double diffAngle = endAngle-startAngle;
		diffAngle = Math.min(diffAngle, 2*PI-diffAngle);
		
		// If the angle difference is too small, we consider the two curves
		// touch at their extremities
		if(Math.abs(diffAngle)<1e-10)
			return CircleArc2D.create(center, Math.abs(dist), startAngle, 0);
		
		// otherwise add a circle arc to the polycurve
		return CircleArc2D.create(
				center, Math.abs(dist), startAngle, endAngle, dist>0);
	}

	/**
	 * Creates a join between the parallels of two curves at the specified
	 * distance.
	 * The first point of curve2 is assumed to be the last point of curve1.
	 */
	public CirculinearContinuousCurve2D createJoin(
			CirculinearElement2D curve1, 
			CirculinearElement2D curve2, double dist) {
		
		// center of circle arc
		Point2D center = curve2.getFirstPoint();

		// compute tangents to each portion
		Vector2D direction1 = curve1.getTangent(curve1.getT1());
		Vector2D direction2 = curve2.getTangent(curve2.getT0());

		return createJoin(center, direction1, direction2, dist);
	}
}
