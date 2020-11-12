/**
 * File: 	CapFactory.java
 * Project: javageom-buffer
 * 
 * Distributed under the LGPL License.
 *
 * Created: 4 janv. 2011
 */
package net.javageom.geom2d.circulinear.buffer;

import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Vector2D;
import net.javageom.geom2d.circulinear.CirculinearContinuousCurve2D;


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
