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
 * Tagging interface to represent in unified way smooth curves which are
 * boundaries.
 * @author dlegland
 *
 */
public interface SmoothBoundary2D
extends SmoothOrientedCurve2D, ContinuousBoundary2D {

    // ===================================================================
    // redefines declaration of some interfaces

	SmoothBoundary2D transform(AffineTransform2D trans);
	SmoothBoundary2D getReverseCurve();
}
