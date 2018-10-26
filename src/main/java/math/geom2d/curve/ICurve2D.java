/* File Curve2D.java 
 *
 * Project : Java Geometry Library
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

// package

package math.geom2d.curve;

// Imports
import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.IShape2D;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * <p>
 * Interface for piecewise smooth curves, like polylines, conics, straight lines, line segments...
 * </p>
 * <p>
 * Several interfaces exist to use more explicit type of curves: {@link IContinuousCurve2D ContinuousCurve2D} for finite or infinite continuous curves, {@link ISmoothCurve2D SmoothCurve2D} for curves that admit a derivative (and hence a tangent, a curvature...) at each point, {@link math.geom2d.domain.IOrientedCurve2D OrientedCurve2D} that are used to define the {@link math.geom2d.domain.IBoundary2D boundary} of a {@link math.geom2d.domain.IDomain2D domain}...
 * </p>
 * <p>
 * Points on curves are identified using curve parameterization. This parameterization is left to the implementation.
 */
public interface ICurve2D extends IShape2D {
    /**
     * Get value of parameter t for the first point of the curve. It can be -Infinity, in this case the piece of curve is not bounded.
     */
    double t0();

    /**
     * Get value of parameter t for the last point of the curve. It can be +Infinity, in this case the piece of curve is not bounded.
     */
    double t1();

    /**
     * Returns the point located at the given position on the curve. If the parameter lies outside the definition range, the parameter corresponding to the closest bound is used instead. This method can be used to draw an approximated outline of a curve, by selecting multiple values for t and drawing lines between them.
     */
    Point2D point(double t);

    /**
     * Returns the first point of the curve. It must returns the same result as <code>point(t0())</code>.
     * 
     * @see #t0()
     * @see #point(double)
     * @return the first point of the curve
     */
    Point2D firstPoint();

    /**
     * Returns the last point of the curve. It must returns the same result as <code>this.point(this.t1())</code>.
     * 
     * @see #t1()
     * @see #point(double)
     * @return the last point of the curve.
     */
    Point2D lastPoint();

    /**
     * Returns a set of singular points, i. e. which do not locally admit derivative.
     * 
     * @see #vertices()
     * @return a collection of Point2D.
     */
    Collection<Point2D> singularPoints();

    /**
     * Returns the set of vertices for this curve. Vertices can be either singular points, or extremities.
     * 
     * @see #singularPoints()
     * @return a collection of Point2D.
     */
    Collection<Point2D> vertices();

    /**
     * Checks if a point is singular.
     * 
     * @param pos
     *            the position of the point on the curve
     * @return true if the point at this location is singular
     */
    boolean isSingular(double pos);

    /**
     * Computes the position of the point on the curve. If the point does not belong to the curve, return Double.NaN. It is complementary to the <code>point(double)</code> method.
     * 
     * @param point
     *            a point belonging to the curve
     * @return the position of the point on the curve
     * @see #point(double)
     */
    double position(Point2D point);

    /**
     * Returns the position of the closest orthogonal projection of the point on the curve, or of the closest singular point. This function should always returns a valid value.
     * 
     * @param point
     *            a point to project
     * @return the position of the closest orthogonal projection
     */
    double project(Point2D point);

    /**
     * Returns the intersection points of the curve with the specified line. The length of the result array is the number of intersection points.
     */
    Collection<Point2D> intersections(ILinearShape2D line);

    /**
     * Returns the curve with same trace on the plane with parameterization in reverse order.
     */
    ICurve2D reverse();

    /**
     * Returns the collection of continuous curves which constitute this curve.
     * 
     * @return a collection of continuous curves.
     */
    Collection<? extends IContinuousCurve2D> continuousCurves();

    /**
     * Returns a portion of the original curve, delimited by two positions on the curve.
     * 
     * @param t0
     *            position of the start of the sub-curve
     * @param t1
     *            position of the end of the sub-curve
     * @return the portion of original curve comprised between t0 and t1.
     */
    ICurve2D subCurve(double t0, double t1);

    /**
     * Transforms the curve by an affine transform. The result is an instance of Curve2D.
     */
    @Override
    ICurve2D transform(AffineTransform2D trans);

    /**
     * When a curve is clipped, the result is a set of curves.
     */
    @Override
    ICurveSet2D<? extends ICurve2D> clip(Box2D box);

    /**
     * @since 0.7.1
     * @return the shape corresponding to this curve
     */
    java.awt.Shape asAwtShape();

    /**
     * Draws the curve on the given Graphics2D object.
     * 
     * @param g2
     *            the graphics to draw the curve in
     * @since 0.6.3
     */
    @Override
    void draw(Graphics2D g2);
}