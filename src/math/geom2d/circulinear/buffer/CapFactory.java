/**
 * File: 	CapFactory.java
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


/**
 * Generates a cap at the end of an open curve.
 * @author dlegland
 *
 */
public interface CapFactory {

	public CirculinearContinuousCurve2D createCap(Point2D center, 
			Vector2D direction, double dist);
	
	public CirculinearContinuousCurve2D createCap(Point2D p1, Point2D p2);
}
