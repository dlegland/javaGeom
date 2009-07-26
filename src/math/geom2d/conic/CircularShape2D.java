/**
 * File: 	CircularShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 mai 09
 */
package math.geom2d.conic;

import math.geom2d.circulinear.CirculinearCurve2D;


/**
 * Tagging interface for grouping Circle2D and CircleArc2D.
 * @author dlegland
 *
 */
public interface CircularShape2D extends CirculinearCurve2D {

	public Circle2D getSupportingCircle();
}
