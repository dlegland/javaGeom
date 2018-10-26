/* file : LineArc2D.java
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
 * Created on 24 d�c. 2005
 *
 */

package math.geom2d.line;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Point2D;
import math.geom2d.domain.ISmoothOrientedCurve2D;
import math.geom2d.exception.UnboundedShape2DException;
import math.utils.EqualUtils;

/**
 * LineArc2D is a generic class to represent edges, straight lines, and rays. It is defined like other linear shapes: origin point, and direction vector. Moreover, two internal variables t0 and t1 define the limit of the object (with t0<t1).
 * <ul>
 * <li>t0=0 and t1=1: this is an edge.</li>
 * <li>t0=-inf and t1=inf: this is a straight line.</li>
 * <li>t0=0 and t1=inf: this is a ray.</li>
 * <li>t0=-inf and 0: this is an inverted ray.</li>
 * </ul>
 * 
 * @author dlegland
 */
public class LineArc2D extends AbstractLine2D implements ISmoothOrientedCurve2D {
    private static final long serialVersionUID = 1L;

    /**
     * Static factory for creating a new LineArc2D
     * 
     * @since 0.8.1
     */
    public static LineArc2D create(Point2D p1, Point2D p2, double t0, double t1) {
        return new LineArc2D(p1, p2, t0, t1);
    }

    // ===================================================================
    // class variables

    /** Lower bound of this arc parameterization */
    private final double t0;

    /** Upper bound of this arc parameterization */
    private final double t1;

    // ===================================================================
    // Constructors

    /**
     * @param point1
     *            the point located at t=0
     * @param point2
     *            the point located at t=1
     * @param t0
     *            the lower bound of line arc parameterization
     * @param t1
     *            the upper bound of line arc parameterization
     */
    public LineArc2D(Point2D point1, Point2D point2, double t0, double t1) {
        this(point1.x(), point1.y(), point2.x() - point1.x(), point2.y() - point1.y(), t0, t1);
    }

    /**
     * Construct a line arc contained in the same straight line as first argument, with bounds of arc given by t0 and t1
     * 
     * @param line
     *            an object defining the supporting line
     * @param t0
     *            the lower bound of line arc parameterization
     * @param t1
     *            the upper bound of line arc parameterization
     */
    public LineArc2D(ILinearShape2D line, double t0, double t1) {
        super(line.origin(), line.direction());
        this.t0 = t0;
        this.t1 = t1;
    }

    /**
     * Construct a line arc by the parameters of the supporting line and two positions on the line.
     * 
     * @param x1
     *            the x-coordinate of the first point
     * @param y1
     *            the y-coordinate of the first point
     * @param dx
     *            the x-coordinate of the direction vector
     * @param dy
     *            the y-coordinate of the direction vector
     * @param t0
     *            the starting position of the arc
     * @param t1
     *            the ending position of the arc
     */
    public LineArc2D(double x1, double y1, double dx, double dy, double t0, double t1) {
        super(x1, y1, dx, dy);
        this.t0 = t0;
        this.t1 = t1;
    }

    // ===================================================================
    // methods specific to LineArc2D

    /**
     * Returns the length of the line arc.
     */
    @Override
    public double length() {
        if (this.isBounded())
            return firstPoint().distance(lastPoint());
        else
            return Double.POSITIVE_INFINITY;
    }

    public double x1() {
        if (t0 != Double.NEGATIVE_INFINITY)
            return x0() + t0 * dx();
        else
            return Double.NEGATIVE_INFINITY;
    }

    public double y1() {
        if (t0 != Double.NEGATIVE_INFINITY)
            return y0() + t0 * dy();
        else
            return Double.NEGATIVE_INFINITY;
    }

    public double x2() {
        if (t1 != Double.POSITIVE_INFINITY)
            return x0() + t1 * dx();
        else
            return Double.POSITIVE_INFINITY;
    }

    public double y2() {
        if (t1 != Double.POSITIVE_INFINITY)
            return y0() + t1 * dy();
        else
            return Double.POSITIVE_INFINITY;
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#parallel(double)
     */
    @Override
    public LineArc2D parallel(double d) {
        double d2 = d / Math.hypot(dx(), dy());
        return new LineArc2D(x0() + dy() * d2, y0() - dx() * d2, dx(), dy(), t0, t1);
    }

    // ===================================================================
    // methods of Curve2D interface

    /**
     * Returns the parameter of the first point of the line arc, arbitrarily set to 0.
     */
    @Override
    public double t0() {
        return t0;
    }

    /**
     * Returns the parameter of the last point of the line arc, arbitrarily set to 1.
     */
    @Override
    public double t1() {
        return t1;
    }

    @Override
    public Point2D point(double t) {
        if (t < t0)
            t = t0;
        if (t > t1)
            t = t1;

        if (Double.isInfinite(t))
            throw new UnboundedShape2DException(this);
        else
            return new Point2D(x0() + dx() * t, y0() + dy() * t);
    }

    /**
     * Returns the first point of the edge. In the case of a line, or a ray starting from -infinity, throws an UnboundedShape2DException.
     * 
     * @return the last point of the arc
     */
    @Override
    public Point2D firstPoint() {
        if (!Double.isInfinite(t0))
            return new Point2D(x0() + t0 * dx(), y0() + t0 * dy());
        else
            throw new UnboundedShape2DException(this);
    }

    /**
     * Returns the last point of the edge. In the case of a line, or a ray ending at infinity, throws an UnboundedShape2DException.
     * 
     * @return the last point of the arc
     */
    @Override
    public Point2D lastPoint() {
        if (!Double.isInfinite(t1))
            return new Point2D(x0() + t1 * dx(), y0() + t1 * dy());
        else
            throw new UnboundedShape2DException(this);
    }

    @Override
    public Collection<Point2D> singularPoints() {
        ArrayList<Point2D> list = new ArrayList<>(2);
        if (t0 != Double.NEGATIVE_INFINITY)
            list.add(this.firstPoint());
        if (t1 != Double.POSITIVE_INFINITY)
            list.add(this.lastPoint());
        return list;
    }

    @Override
    public boolean isSingular(double pos) {
        if (Math.abs(pos - t0) < IShape2D.ACCURACY)
            return true;
        if (Math.abs(pos - t1) < IShape2D.ACCURACY)
            return true;
        return false;
    }

    @Override
    public Collection<? extends LineArc2D> continuousCurves() {
        return wrapCurve(this);
    }

    /**
     * Returns the line arc which have the same trace, but has the inverse parameterization.
     */
    @Override
    public LineArc2D reverse() {
        return new LineArc2D(x0(), y0(), -dx(), -dy(), -t1, -t0);
    }

    /**
     * Returns a new LineArc2D, which is the portion of this LineArc2D delimited by parameters t0 and t1.
     */
    @Override
    public LineArc2D subCurve(double t0, double t1) {
        t0 = Math.max(t0, this.t0());
        t1 = Math.min(t1, this.t1());
        return new LineArc2D(this, t0, t1);
    }

    // ===================================================================
    // methods of Shape2D interface

    /**
     * Returns true if both t0 and t1 are different from infinity.
     */
    @Override
    public boolean isBounded() {
        return t0 != Double.NEGATIVE_INFINITY && t1 != Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the bounding box of this line arc.
     */
    @Override
    public Box2D boundingBox() {
        return new Box2D(x0() + t0 * dx(), x0() + t1 * dx(), y0() + t0 * dy(), y0() + t1 * dy());
    }

    // ===================================================================
    // methods of Shape interface

    @Override
    public boolean contains(Point2D pt) {
        return contains(pt.x(), pt.y());
    }

    @Override
    public boolean contains(double xp, double yp) {
        if (!super.supportContains(xp, yp))
            return false;

        // compute position on the line
        double t = positionOnLine(xp, yp);

        if (t - t0 < -ACCURACY)
            return false;
        if (t - t1 > ACCURACY)
            return false;

        return true;
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        if (!this.isBounded())
            throw new UnboundedShape2DException(this);
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        path.moveTo((float) (x0() + t0 * dx()), (float) (y0() + t0 * dy()));
        path.lineTo((float) (x0() + t1 * dx()), (float) (y0() + t1 * dy()));
        return path;
    }

    /**
     * Appends a line to the current path. If t0 or t1 is infinite, throws a new UnboundedShape2DException.
     * 
     * @param path
     *            the path to modify
     * @return the modified path
     */
    @Override
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        if (!this.isBounded())
            throw new UnboundedShape2DException(this);
        if (t0 == Double.NEGATIVE_INFINITY)
            return path;
        if (t1 == Double.POSITIVE_INFINITY)
            return path;
        path.lineTo((float) x1(), (float) y1());
        path.lineTo((float) x2(), (float) y2());
        return path;
    }

    @Override
    public LineArc2D transform(AffineTransform2D trans) {
        double[] tab = trans.coefficients();
        double x1 = x0() * tab[0] + y0() * tab[1] + tab[2];
        double y1 = x0() * tab[3] + y0() * tab[4] + tab[5];
        return new LineArc2D(x1, y1, dx() * tab[0] + dy() * tab[1], dx() * tab[3] + dy() * tab[4], t0, t1);
    }

    @Override
    public String toString() {
        return new String("LineArc2D(" + x0() + "," + y0() + "," + dx() + "," + dy() + "," + t0 + "," + t1 + ")");
    }

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

        if (!(obj instanceof LineArc2D))
            return false;
        LineArc2D that = (LineArc2D) obj;

        // Compare each field
        if (!almostEquals(this.x0(), that.x0(), eps))
            return false;
        if (!almostEquals(this.y0(), that.y0(), eps))
            return false;
        if (!almostEquals(this.dx(), that.dx(), eps))
            return false;
        if (!almostEquals(this.dy(), that.dy(), eps))
            return false;
        if (!almostEquals(this.t0, that.t0, eps))
            return false;
        if (!almostEquals(this.t1, that.t1, eps))
            return false;

        return true;
    }

    /**
     * Compares two double values with a given accuracy, with correct result for infinite values. Undefined results for NaNs.
     */
    private static boolean almostEquals(double d1, double d2, double eps) {
        if (d1 == Double.POSITIVE_INFINITY && d2 == Double.POSITIVE_INFINITY)
            return true;
        if (d1 == Double.NEGATIVE_INFINITY && d2 == Double.NEGATIVE_INFINITY)
            return true;

        return Math.abs(d1 - d2) < eps;
    }

    // ===================================================================
    // methods of Object interface

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LineArc2D))
            return false;
        LineArc2D that = (LineArc2D) obj;

        // Compare each field
        if (!EqualUtils.areEqual(this.x0(), that.x0()))
            return false;
        if (!EqualUtils.areEqual(this.y0(), that.y0()))
            return false;
        if (!EqualUtils.areEqual(this.dx(), that.dx()))
            return false;
        if (!EqualUtils.areEqual(this.dy(), that.dy()))
            return false;
        if (!EqualUtils.areEqual(this.t0, that.t0))
            return false;
        if (!EqualUtils.areEqual(this.t1, that.t1))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + Double.valueOf(this.x0()).hashCode();
        hash = hash * 31 + Double.valueOf(this.y0()).hashCode();
        hash = hash * 31 + Double.valueOf(this.dx()).hashCode();
        hash = hash * 31 + Double.valueOf(this.dy()).hashCode();
        hash = hash * 31 + Double.valueOf(this.t0).hashCode();
        hash = hash * 31 + Double.valueOf(this.t1).hashCode();
        return hash;
    }

}
