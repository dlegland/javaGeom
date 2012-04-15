/**
 * File: 	CirculinearCurveSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import math.geom2d.Box2D;
import math.geom2d.curve.CurveSet2D;


/**
 * A specialization of CurveSet2D that accepts only instances of
 * CirculinearCurve2D.
 * @author dlegland
 *
 */
public interface CirculinearCurveSet2D<T extends CirculinearCurve2D>
extends CurveSet2D<T>, CirculinearCurve2D {
 
    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

	public CirculinearCurveSet2D<? extends CirculinearCurve2D> clip(Box2D box);

	public CirculinearCurveSet2D<? extends CirculinearCurve2D> subCurve(
			double t0, double t1);
	
	public CirculinearCurveSet2D<? extends CirculinearCurve2D> reverse();
}
