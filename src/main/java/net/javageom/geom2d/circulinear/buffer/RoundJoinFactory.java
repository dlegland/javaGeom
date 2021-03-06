/**
 * File: 	CircularJoinFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package net.javageom.geom2d.circulinear.buffer;

import static java.lang.Math.PI;
import static net.javageom.geom2d.curve.Curves2D.JunctionType.*;

import net.javageom.geom2d.Angle2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.circulinear.CirculinearContinuousCurve2D;
import net.javageom.geom2d.circulinear.CirculinearElement2D;
import net.javageom.geom2d.conic.CircleArc2D;
import net.javageom.geom2d.curve.Curves2D;
import net.javageom.geom2d.line.LineSegment2D;

/**
 * @author dlegland
 *
 */
public class RoundJoinFactory implements JoinFactory {

	/**
	 * Creates a join between the parallels of two curves at the specified
	 * distance.
	 * The first point of curve2 is assumed to be the last point of curve1.
	 */
	public CirculinearContinuousCurve2D createJoin(
			CirculinearElement2D curve1, 
			CirculinearElement2D curve2, double dist) {
		
		// center of circle arc
		Point2D center = curve2.firstPoint();
		Curves2D.JunctionType junctionType = 
			Curves2D.getJunctionType(curve1, curve2);
		
		// compute tangents to each portion
		Vector2D direction1 = curve1.tangent(curve1.t1());
		Vector2D direction2 = curve2.tangent(curve2.t0());

		// angle of each edge
		double angle1 = direction1.angle();
		double angle2 = direction2.angle();
				
		// Special cases of direct join between the two parallels
		if ((dist > 0 && junctionType == REENTRANT) || (dist <= 0 && junctionType == SALIENT)) {
			Point2D p1 = Point2D.createPolar(center, dist, angle1 - PI/2);
			Point2D p2 = Point2D.createPolar(center, dist, angle2 - PI/2);
			return new LineSegment2D(p1, p2);
		}
		
		// compute angles
		double startAngle, endAngle;
		if (dist > 0) {
			startAngle = angle1 - PI/2;
			endAngle = angle2 - PI/2;
		} else {
			startAngle = angle1 + PI/2;
			endAngle = angle2 + PI/2;
		}
		
		// format angles to stay between 0 and 2*PI
		startAngle = Angle2D.formatAngle(startAngle);
		endAngle = Angle2D.formatAngle(endAngle);
		
		// If the angle difference is too small, we consider the two curves
		// touch at their extremities
		if (junctionType == FLAT)
			return new CircleArc2D(center, Math.abs(dist), startAngle, 0);
		
		// otherwise add a circle arc to the polycurve
		return new CircleArc2D(
				center, Math.abs(dist), startAngle, endAngle, dist > 0);
	}
}
