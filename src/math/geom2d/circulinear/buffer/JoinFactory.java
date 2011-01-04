/**
 * File: 	JoinFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;


/**
 * Generate a join between two consecutive parallel curves.
 * @author dlegland
 *
 */
public interface JoinFactory {

	public CirculinearContinuousCurve2D createJoin(Point2D center, 
			Vector2D direction1, Vector2D direction2, double dist);
	
	public CirculinearContinuousCurve2D createJoin(
			CirculinearElement2D curve1, 
			CirculinearElement2D curve2, double dist);
}
