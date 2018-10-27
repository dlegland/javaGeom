/* file : OrientedCurve2D.java
 * 
 * Project : geometry
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
 * 
 * Created on 25 d�c. 2006
 *
 */

package math.geom2d.domain;

import math.geom2d.Box2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * An OrientedCurve2D defines an 'inside' and an 'outside'. It is typically a part of the boundary of a domain. Several OrientedCurve2D form a Contour2D, and one or several Contour2D form a Boundary2D.
 * 
 * @author dlegland
 */
public interface IOrientedCurve2D extends ICurve2D {

    /**
     * Return the angle portion that the curve turn around the given point. Result is a signed angle.
     * 
     * @param point
     *            a point of the plane
     * @return a signed angle
     */
    public abstract double windingAngle(Point2D point);

    /**
     * Returns the signed distance of the curve to the given point. The distance is positive if the point lies outside the shape, and negative if the point lies inside the shape. In both cases, absolute value of distance is equals to the distance to the border of the shape.
     * 
     * @param point
     *            a point of the plane
     * @return the signed distance to the curve
     */
    public abstract double signedDistance(Point2D point);

    /**
     * The same as distanceSigned(Point2D), but by passing 2 double as arguments.
     * 
     * @param x
     *            x-coord of a point
     * @param y
     *            y-coord of a point
     * @return the signed distance of the point (x,y) to the curve
     */
    public abstract double signedDistance(double x, double y);

    /**
     * Returns true if the point is 'inside' the domain bounded by the curve.
     * 
     * @param pt
     *            a point in the plane
     * @return true if the point is on the left side of the curve.
     */
    public abstract boolean isInside(Point2D pt);

    @Override
    public abstract IOrientedCurve2D reverse();

    // TODO: what to do with non-continuous oriented curves ?
    // public abstract OrientedCurve2D subCurve(double t0, double t1);

    @Override
    public abstract ICurveSet2D<? extends IOrientedCurve2D> clip(Box2D box);

    /**
     * Transforms the oriented curve, and returns another oriented curve. If transform is not direct, the domains bounded by the transformed curve should be complemented to have same orientation as the original domain.
     */
    @Override
    public abstract IOrientedCurve2D transform(AffineTransform2D trans);
}
