/* File InvertedRay2D.java 
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

package math.geom2d.line;

import java.awt.geom.GeneralPath;

import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.exception.UnboundedShape2DException;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

// Imports

/**
 * Inverted ray is defined from an origin and a direction vector. It is composed of all points satisfying the parametric equation:
 * <p>
 * <code>x(t) = x0+t*dx<code><br>
 * <code>y(t) = y0+t*dy<code>
 * </p>
 * with <code>t<code> comprised between -INFINITY and 0. This is complementary class to Ray2D.
 */
public final class InvertedRay2D extends AbstractLine2D {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new Ray2D, originating from <code>point1<\code>, and going in the direction of <code>point2<\code>.
     */
    public InvertedRay2D(Point2D point1, Point2D point2) {
        super(point1, point2, false);
    }

    /**
     * Creates a new Ray2D, originating from point <code>point<\code>, and going in the direction specified by <code>vector<\code>.
     */
    public InvertedRay2D(Point2D point, Vector2D vector) {
        super(point, vector, false);
    }

    /**
     * Creates a new Ray2D, originating from point <code>point<\code>, and going in the direction specified by <code>angle<\code> (in radians).
     */
    public InvertedRay2D(Point2D point, double angle) {
        this(point, new Vector2D(Math.cos(angle), Math.sin(angle)));
    }

    /**
     * Creates a new Ray2D, originating from point <code>(x1,y1)<\code>, and going in the direction defined by vector <code>(dx, dy)<\code>.
     */
    public InvertedRay2D(double x1, double y1, double dx, double dy) {
        super(new Point2D(x1, y1), new Vector2D(dx, dy), false);
    }

    /**
     * Define a new Ray, with same characteristics as given object.
     */
    public InvertedRay2D(ILinearShape2D line) {
        this(line.origin(), line.direction());
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns another instance of InvertedRay2D, parallel to this one, and located at the given distance.
     * 
     * @see math.geom2d.circulinear.ICirculinearCurve2D#parallel(double)
     */
    @Override
    public InvertedRay2D parallel(double d) {
        double dd = Math.hypot(dx(), dy());
        return new InvertedRay2D(x() + dy() * d / dd, y() - dx() * d / dd, dx(), dy());
    }

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /** Throws an infiniteShapeException */
    @Override
    public GeneralPath appendPath(GeneralPath path) {
        throw new UnboundedShape2DException(this);
    }

    /** Throws an infiniteShapeException */
    public java.awt.geom.GeneralPath getGeneralPath() {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Point2D point(double t) {
        double mt = Math.min(t, 0);
        return new Point2D(x() + mt * dx(), y() + mt * dy());
    }

    /**
     * Returns Negative infinity.
     */
    @Override
    public double t0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns 0.
     */
    @Override
    public double t1() {
        return 0;
    }

    /**
     * Reverses this curve, and return the result as an instance of Ray2D.
     * 
     * @see Ray2D#reverse()
     */
    @Override
    public Ray2D reverse() {
        return new Ray2D(x(), y(), -dx(), -dy());
    }

    // ===================================================================
    // methods implementing the Shape2D interface

    /** Always returns false, because n inverted ray is not bounded. */
    @Override
    public boolean isBounded() {
        return false;
    }

    @Override
    public boolean contains(double x, double y) {
        if (!this.supportContains(x, y))
            return false;
        double t = this.positionOnLine(x, y);
        return t < IShape2D.ACCURACY;
    }

    @Override
    public Box2D boundingBox() {
        double t = Double.NEGATIVE_INFINITY;
        Point2D p0 = new Point2D(x(), y());
        Point2D p1 = new Point2D(t * dx(), t * dy());
        return new Box2D(p0, p1);
    }

    @Override
    public InvertedRay2D transform(AffineTransform2D trans) {
        double[] tab = trans.coefficients();
        double x1 = x() * tab[0] + y() * tab[1] + tab[2];
        double y1 = x() * tab[3] + y() * tab[4] + tab[5];
        return new InvertedRay2D(x1, y1, dx() * tab[0] + dy() * tab[1], dx() * tab[3] + dy() * tab[4]);
    }

    // ===================================================================
    // methods implementing the Shape interface

    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
     */
    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof InvertedRay2D))
            return false;
        InvertedRay2D ray = (InvertedRay2D) obj;
        if (Math.abs(x() - ray.x()) > eps)
            return false;
        if (Math.abs(y() - ray.y()) > eps)
            return false;
        if (Math.abs(dx() - ray.dx()) > eps)
            return false;
        if (Math.abs(dy() - ray.dy()) > eps)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return new String("InvertedRay2D(" + x() + "," + y() + "," + dx() + "," + dy() + ")");
    }
}