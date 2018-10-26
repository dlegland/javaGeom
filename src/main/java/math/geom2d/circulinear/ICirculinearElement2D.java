/**
 * File: 	CirculinearElement2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.domain.ISmoothOrientedCurve2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * <p>
 * Circulinear elements are lowest level of circulinear curve: each circulinear curve can be divided into a set of circulinear elements.
 * </p>
 * <p>
 * Circulinear elements can be either linear elements (implementations of LinearShape2D), or circular elements (circle or circle arcs).
 * </p>
 * 
 * @author dlegland
 *
 */
public interface ICirculinearElement2D extends ICirculinearContinuousCurve2D, ISmoothOrientedCurve2D {
    @Override
    public ICirculinearElement2D parallel(double d);

    @Override
    public ICirculinearElement2D transform(CircleInversion2D inv);

    @Override
    public ICurveSet2D<? extends ICirculinearElement2D> clip(Box2D box);

    @Override
    public ICirculinearElement2D subCurve(double t0, double t1);

    @Override
    public ICirculinearElement2D reverse();

    /**
     * Returns true if the orthogonal projection of the point <code>p</code> on the supporting shape of this curve (either e straight line or a circle) also belongs to this curve.
     * 
     * @param p
     *            a point in the plane
     * @return true if the projection of p on the supporting curve also belongs to this curve
     */
    public boolean containsProjection(Point2D p);
}
