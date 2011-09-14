/**
 * File: 	JoinFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;


/**
 * Generate a join between two consecutive parallel curves.
 * @author dlegland
 *
 */
public interface JoinFactory {

	/**
	 * Creates a join between the parallels of two curves at the specified
	 * distance.
	 * The first point of curve2 is assumed to be the last point of curve1.
	 */
	public CirculinearContinuousCurve2D createJoin(
			CirculinearElement2D previous, 
			CirculinearElement2D next, double dist);
}
