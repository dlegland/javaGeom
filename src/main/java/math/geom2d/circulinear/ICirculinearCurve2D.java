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
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * <p>
 * Circulinear curve are composed of linear and/or circular elements. Linear elements are line segments, straight lines, rays... Circular elements are circles and circle arcs.
 * </p>
 * <p>
 * Circulinear curves provide a convenient way to store result of geometric operation like buffer computation. Moreover, the set of circulinear curves is stable with respect to circle inversion.
 * </p>
 * 
 * @author dlegland
 *
 */
public interface ICirculinearCurve2D extends ICirculinearShape2D, ICurve2D {

    /**
     * @return the length of the curve
     */
    public double length();

    /**
     * @return the length from the beginning to the position given by pos
     */
    public double length(double pos);

    /**
     * @return the position located at a given geodesic distance from the origin
     */
    public double position(double distance);

    /**
     * Creates a new curve, formed by the points with parameterization: <code> p(t) = c(t) + d*n(t)/|n(t)|</code>, with p(t) being a point of the original curve, n(t) the normal of the curve, and |n| being the norm of n.<br>
     * In the case of a continuous curve formed by several smooth circulinear elements, the parallels of contiguous elements are joined by a circle arc.
     * 
     * @param d
     *            the distance between the original curve and he parallel curve.
     * @return the parallel curve
     */
    public ICirculinearCurve2D parallel(double d);

    // ===================================================================
    // redefines declaration of some parent interfaces

    @Override
    public ICirculinearCurve2D transform(CircleInversion2D inv);

    /**
     * Returns the collection of continuous circulinear curves which constitute this curve.
     * 
     * @return a collection of continuous circulinear curves.
     */
    @Override
    public Collection<? extends ICirculinearContinuousCurve2D> continuousCurves();

    @Override
    public ICurveSet2D<? extends ICirculinearCurve2D> clip(Box2D box);

    @Override
    public ICirculinearCurve2D subCurve(double t0, double t1);

    @Override
    public ICirculinearCurve2D reverse();
}
