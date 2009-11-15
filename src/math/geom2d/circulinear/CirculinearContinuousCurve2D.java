/**
 * File: 	CirculinearContinuousCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A tagging interface defining a circulinear curve which is continuous.
 * @author dlegland
 *
 */
@SuppressWarnings("deprecation")
public interface CirculinearContinuousCurve2D 
extends ContinuousCirculinearCurve2D {
	//TODO: remove inheritance to deprecated interface
    // ===================================================================
    // redefines declaration of CirculinearCurve2D interfaces

	public CirculinearContinuousCurve2D getParallel(double d);
	public CirculinearContinuousCurve2D transform(CircleInversion2D inv);
	
	// ===================================================================
    // redefines declaration of some parent interfaces

	/**
     * Returns a set of circulinear elements, which are basis for circulinear
     * curves.
     */
    public abstract Collection<? extends CirculinearElement2D> getSmoothPieces();

    public CurveSet2D<? extends CirculinearContinuousCurve2D> clip(Box2D box);
	public CirculinearContinuousCurve2D getSubCurve(double t0, double t1);
	public CirculinearContinuousCurve2D getReverseCurve();
}
