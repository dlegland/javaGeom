/**
 * File: 	CircularShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 mai 09
 */
package math.geom2d.conic;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.ICirculinearElement2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.domain.ISmoothOrientedCurve2D;

/**
 * Tagging interface for grouping Circle2D and CircleArc2D.
 * 
 * @author dlegland
 *
 */
public interface ICircularShape2D extends ICirculinearElement2D, ISmoothOrientedCurve2D {

    // ===================================================================
    // method specific to CircularShape2D

    /**
     * Returns the circle that contains this shape.
     */
    public Circle2D supportingCircle();

    /**
     * Returns true if the orthogonal projection of the point <code>p</code> on the supporting circle of this curve belongs to this curve.
     * 
     * @param p
     *            a point in the plane
     * @return true if the projection of p on the supporting circle also belongs to this curve
     */
    public boolean containsProjection(Point2D p);

    // ===================================================================
    // methods inherited from Shape2D and Curve2D

    public ICurveSet2D<? extends ICircularShape2D> clip(Box2D box);

    public ICircularShape2D subCurve(double t0, double t1);

    public ICircularShape2D reverse();
}
