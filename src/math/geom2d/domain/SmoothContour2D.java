/**
 * File: 	SmoothBoundary2D.java
 * Project: javaGeom-circulinear
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 juil. 09
 */
package math.geom2d.domain;

import math.geom2d.AffineTransform2D;


/**
 * Tagging interface to represent in unified way smooth curves that are
 * also contours.
 * @author dlegland
 *
 */
public interface SmoothContour2D
extends SmoothOrientedCurve2D, Contour2D {

    // ===================================================================
    // redefines declaration of some interfaces

	/**
	 * Transforms the contour, and returns an instance of SmoothContour2D.
	 */
	SmoothContour2D transform(AffineTransform2D trans);

	/**
	 * Reverses the contour, and returns an instance of SmoothContour2D.
	 */
	SmoothContour2D reverse();
}
