/**
 * File: 	CircleLine2D.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 juil. 09
 */
package math.geom2d.circulinear;

import math.geom2d.domain.SmoothBoundary2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * Tagging interface to be able to consider in a same way circles and lines.
 * @author dlegland
 *
 */
public interface CircleLine2D extends CirculinearContour2D,
		CirculinearElement2D, SmoothBoundary2D {
	
    // ===================================================================
    // redefines declaration of some interfaces

	public CircleLine2D transform(CircleInversion2D inv);
	public CircleLine2D getReverseCurve();
}
