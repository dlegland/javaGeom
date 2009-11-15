/**
 * File: 	CirculinearCurve2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * <p>
 * Circulinear curve are composed of linear and/or circular elements. 
 * Linear elements are line segments, straight lines, rays... 
 * Circular elements are circles and circle arcs.</p>
 * <p>
 * Circulinear curves provide a convenient way to store result of geometric
 * operation like buffer computation. Moreover, the set of circulinear curves
 * is stable with respect to circle inversion.</p>
 * @author dlegland
 *
 */
public interface CirculinearCurve2D extends CirculinearShape2D, Curve2D {

	/**
	 * @return the length of the curve
	 */
	public double getLength();
	
	/**
	 * @return the length from the beginning to the position given by pos
	 */
	public double getLength(double pos);
	
	/**
	 * @return the position located at distance 'length' from the origin
	 */
	public double getPosition(double length);
	
	
	/**
	 * Creates a new curve, formed by the points with parameterization:
	 * <code> p(t) = c(t) + d*n(t)/|n(t)|</code>, with p(t) being a point of
	 * the original curve, n(t) the normal of the curve, and |n| being the
	 * norm of n.<br>
	 * In the case of a continuous curve formed by several smooth circulinear
	 * elements, the parallels of contiguous elements are joined by a circle
	 * arc.
	 * @param d the distance between the original curve and he parallel curve.
	 * @return the parallel curve
	 */
	public CirculinearCurve2D getParallel(double d);
	
    // ===================================================================
    // redefines declaration of some parent interfaces

	public CirculinearCurve2D transform(CircleInversion2D inv);
	
    /**
     * Returns the collection of continuous circulinear curves which
     * constitute this curve.
     * 
     * @return a collection of continuous circulinear curves.
     */
    public Collection<? extends CirculinearContinuousCurve2D> getContinuousCurves();

    public CurveSet2D<? extends CirculinearCurve2D> clip(Box2D box);
    public CirculinearCurve2D getSubCurve(double t0, double t1);
	public CirculinearCurve2D getReverseCurve();
}
