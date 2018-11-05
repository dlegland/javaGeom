/* File StraightLine2D.java 
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

package math.geom2d.line;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.circulinear.ICircleLine2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.exception.DegeneratedLine2DException;
import math.geom2d.exception.UnboundedShape2DException;
import math.geom2d.point.Point2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * Implementation of a straight line. Such a line can be constructed using two points, a point and a parallel line or straight object, or with coefficient of the Cartesian equation.
 */
public class StraightLine2D extends AbstractLine2D implements ICircleLine2D {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a vertical straight line through the given point.
     * 
     * @since 0.10.3
     */
    public static StraightLine2D createHorizontal(Point2D origin) {
        return new StraightLine2D(origin, new Vector2D(1, 0));
    }

    /**
     * Creates a vertical straight line through the given point.
     * 
     * @since 0.10.3
     */
    public static StraightLine2D createVertical(Point2D origin) {
        return new StraightLine2D(origin, new Vector2D(0, 1));
    }

    /**
     * Creates a median between 2 points.
     * 
     * @param p1
     *            one point
     * @param p2
     *            another point
     * @return the median of points p1 and p2
     * @since 0.6.3
     */
    public static StraightLine2D createMedian(Point2D p1, Point2D p2) {
        Point2D mid = Point2D.midPoint(p1, p2);
        StraightLine2D line = new StraightLine2D(p1, p2);
        return StraightLine2D.createPerpendicular(line, mid);
    }

    /**
     * Returns a new Straight line, parallel to another straight object (ray, straight line or edge), and going through the given point.
     * 
     * @since 0.6.3
     */
    public static StraightLine2D createParallel(ILinearShape2D line, Point2D point) {
        return new StraightLine2D(line, point);
    }

    /**
     * Returns a new Straight line, parallel to another straight object (ray, straight line or edge), and going through the given point.
     * 
     * @since 0.6.3
     */
    public static StraightLine2D createParallel(ILinearShape2D linear, double d) {
        StraightLine2D line = linear.supportingLine();
        double d2 = d / Math.hypot(line.dx(), line.dy());
        return new StraightLine2D(line.x() + line.dy() * d2, line.y() - line.dx() * d2, line.dx(), line.dy());
    }

    /**
     * Returns a new Straight line, perpendicular to a straight object (ray, straight line or edge), and going through the given point.
     * 
     * @since 0.6.3
     */
    public static StraightLine2D createPerpendicular(ILinearShape2D linear, Point2D point) {
        StraightLine2D line = linear.supportingLine();
        return new StraightLine2D(point, new Vector2D(-line.dy(), line.dx()));
    }

    /**
     * Returns a new Straight line, with the given coefficient of the Cartesian equation (a*x + b*y + c = 0).
     */
    public static StraightLine2D createCartesian(double a, double b, double c) {
        double d = a * a + b * b;
        double x0 = -a * c / d;
        double y0 = -b * c / d;
        double theta = Math.atan2(-a, b);
        double dx = Math.cos(theta);
        double dy = Math.sin(theta);

        return new StraightLine2D(x0, y0, dx, dy);
    }

    /**
     * Computes the intersection point of the two (infinite) lines going through p1 and p2 for the first one, and p3 and p4 for the second one. Returns null if two lines are parallel.
     */
    public static Point2D getIntersection(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        StraightLine2D line1 = new StraightLine2D(p1, p2);
        StraightLine2D line2 = new StraightLine2D(p3, p4);
        return line1.intersection(line2);
    }

    /**
     * Defines a new Straight line going through the given point, and with the specified direction vector.
     */
    public StraightLine2D(Point2D point, Vector2D direction) {
        super(point, direction, false);
    }

    /** Defines a new Straight line going through the two given points. */
    public StraightLine2D(Point2D point1, Point2D point2) {
        super(point1, point2, false);
    }

    /**
     * Defines a new Straight line going through the given point, and with the specified direction given by angle.
     */
    public StraightLine2D(Point2D point, double angle) {
        this(point.x(), point.y(), Math.cos(angle), Math.sin(angle));
    }

    /**
     * Defines a new Straight line going through the point (xp, yp) and with the direction vector given by (dx, dy).
     */
    public StraightLine2D(double x1, double y1, double dx, double dy) {
        this(new Point2D(x1, y1), new Vector2D(dx, dy));
    }

    /**
     * Copy constructor: Defines a new Straight line at the same position and with the same direction than an other straight object (line, edge or ray).
     */
    public StraightLine2D(ILinearShape2D line) {
        this(line.origin(), line.direction());
    }

    /**
     * Defines a new Straight line, parallel to another straigth object (ray, straight line or edge), and going through the given point.
     */
    public StraightLine2D(ILinearShape2D line, Point2D point) {
        this(point, line.direction());
    }

    // ===================================================================
    // methods specific to StraightLine2D

    /**
     * Returns a new Straight line, parallel to another straight object (ray, straight line or edge), and going through the given point.
     */
    @Override
    public StraightLine2D parallel(Point2D point) {
        return new StraightLine2D(point, this.direction());
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns the parallel line located at a distance d from the line. Distance is positive in the 'right' side of the line (outside of the limiting half-plane), and negative in the 'left' of the line.
     * 
     * @throws DegeneratedLine2DException
     *             if line direction vector is null
     */
    @Override
    public StraightLine2D parallel(double d) {
        double d2 = Math.hypot(this.dx(), this.dy());
        if (Math.abs(d2) < IShape2D.ACCURACY)
            throw new DegeneratedLine2DException("Can not compute parallel of degenerated line", this);
        d2 = d / d2;
        return new StraightLine2D(x() + dy() * d2, y() - dx() * d2, dx(), dy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    @Override
    public ICircleLine2D transform(CircleInversion2D inv) {
        // Extract inversion parameters
        Point2D center = inv.center();
        double r = inv.radius();

        // projection of inversion center on the line
        Point2D po = this.projectedPoint(center);
        double d = this.distance(center);

        // Degenerate case of a point belonging to the line:
        // the transform is the line itself.
        if (Math.abs(d) < IShape2D.ACCURACY) {
            return new StraightLine2D(this);
        }

        // angle from center to line
        double angle = Angle2DUtil.horizontalAngle(center, po);

        // center of transformed circle
        double r2 = r * r / d / 2;
        Point2D c2 = Point2D.createPolar(center, r2, angle);

        // choose direction of circle arc
        boolean direct = this.isInside(center);

        // return the created circle
        return new Circle2D(c2, r2, direct);
    }

    // ===================================================================
    // methods specific to Boundary2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Boundary2D#domain()
     */
    @Override
    public ICirculinearDomain2D domain() {
        return new GenericCirculinearDomain2D(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Boundary2D#fill()
     */
    @Override
    public void fill(Graphics2D g2) {
        g2.fill(this.getGeneralPath());
    }

    // ===================================================================
    // methods specific to OrientedCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.OrientedCurve2D#windingAngle(Point2D)
     */
    @Override
    public double windingAngle(Point2D point) {

        double angle1 = Angle2DUtil.horizontalAngle(-dx(), -dy());
        double angle2 = Angle2DUtil.horizontalAngle(dx(), dy());

        if (this.isInside(point)) {
            if (angle2 > angle1)
                return angle2 - angle1;
            else
                return 2 * Math.PI - angle1 + angle2;
        } else {
            if (angle2 > angle1)
                return angle2 - angle1 - 2 * Math.PI;
            else
                return angle2 - angle1;
        }
    }

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /**
     * Throws an exception when called.
     */
    @Override
    public Polyline2D asPolyline(int n) {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    /** Throws an infiniteShapeException */
    @Override
    public Point2D firstPoint() {
        throw new UnboundedShape2DException(this);
    }

    /** Throws an infiniteShapeException */
    @Override
    public Point2D lastPoint() {
        throw new UnboundedShape2DException(this);
    }

    /** Returns an empty list of points. */
    @Override
    public Collection<Point2D> singularPoints() {
        return new ArrayList<>(0);
    }

    /** Returns false, whatever the position. */
    @Override
    public boolean isSingular(double pos) {
        return false;
    }

    /**
     * Returns the parameter of the first point of the line, which is always Double.NEGATIVE_INFINITY.
     */
    @Override
    public double t0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the parameter of the last point of the line, which is always Double.POSITIVE_INFINITY.
     */
    @Override
    public double t1() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the point specified with the parametric representation of the line.
     */
    @Override
    public Point2D point(double t) {
        return new Point2D(x() + dx() * t, y() + dy() * t);
    }

    /**
     * Need to override to cast the type.
     */
    @Override
    public Collection<? extends StraightLine2D> continuousCurves() {
        ArrayList<StraightLine2D> list = new ArrayList<>(1);
        list.add(this);
        return list;
    }

    /**
     * Returns the straight line with same origin but with opposite direction vector.
     */
    @Override
    public StraightLine2D reverse() {
        return new StraightLine2D(this.x(), this.y(), -this.dx(), -this.dy());
    }

    @Override
    public GeneralPath appendPath(GeneralPath path) {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods implementing the Shape2D interface

    /** Always returns false, because a line is not bounded. */
    @Override
    public boolean isBounded() {
        return false;
    }

    /**
     * Returns the distance of the point (x, y) to this straight line.
     */
    @Override
    public double distance(double x, double y) {
        Point2D proj = super.projectedPoint(x, y);
        return proj.distance(x, y);
    }

    @Override
    public Box2D boundingBox() {
        if (Math.abs(dx()) < IShape2D.ACCURACY)
            return new Box2D(x(), x(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        if (Math.abs(dy()) < IShape2D.ACCURACY)
            return new Box2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, x(), y());

        return new Box2D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Returns the transformed line. The result is still a StraightLine2D.
     */
    @Override
    public StraightLine2D transform(AffineTransform2D trans) {
        double[] tab = trans.coefficients();
        return new StraightLine2D(x() * tab[0] + y() * tab[1] + tab[2], x() * tab[3] + y() * tab[4] + tab[5], dx() * tab[0] + dy() * tab[1], dx() * tab[3] + dy() * tab[4]);
    }

    // ===================================================================
    // methods implementing the Shape interface

    /**
     * Returns true if the point (x, y) lies on the line, with precision given by Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(double x, double y) {
        return super.supportContains(x, y);
    }

    /**
     * Returns true if the point p lies on the line, with precision given by Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(Point2D p) {
        return super.supportContains(p.x(), p.y());
    }

    @Override
    public StraightLine2D supportingLine() {
        return this;
    }

    /** Throws an infiniteShapeException */
    public java.awt.geom.GeneralPath getGeneralPath() {
        throw new UnboundedShape2DException(this);
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

        if (!(obj instanceof StraightLine2D))
            return false;
        StraightLine2D line = (StraightLine2D) obj;

        if (Math.abs(x() - line.x()) > eps)
            return false;
        if (Math.abs(y() - line.y()) > eps)
            return false;
        if (Math.abs(dx() - line.dx()) > eps)
            return false;
        if (Math.abs(dy() - line.dy()) > eps)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return new String("StraightLine2D(" + x() + "," + y() + "," + dx() + "," + dy() + ")");
    }
}