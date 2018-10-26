/* file : Circle2D.java
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
 * Created on 30 avr. 2006
 *
 */

package math.geom2d.conic;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.GenericCirculinearDomain2D;
import math.geom2d.circulinear.ICircleLine2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.circulinear.ICirculinearRing2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.curve.AbstractSmoothCurve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.Curves2DUtil;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.exception.ColinearPoints2DException;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.Point2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.CircleInversion2D;
import math.utils.EqualUtils;

/**
 * A circle in the plane, defined as the set of points located at an equal distance from the circle center. A circle is a particular ellipse, with first and second axis length equal.
 * 
 * @author dlegland
 */
public final class Circle2D extends AbstractSmoothCurve2D implements IEllipseShape2D, ICircleLine2D, ICircularShape2D, ICirculinearRing2D {
    private static final long serialVersionUID = 1L;

    public static final Circle2D UNIT_CIRCLE = new Circle2D(Point2D.ORIGIN, 1);

    /**
     * Computes the circumscribed circle of the 3 input points.
     * 
     * @return the circle that contains the three input points
     * @throws ColinearPoints2DException
     *             if the 3 points are colinear
     */
    public static Circle2D circumCircle(Point2D p1, Point2D p2, Point2D p3) {
        // Computes circum center, possibly throwing ColinearPoints2DException
        Point2D center = circumCenter(p1, p2, p3);

        // compute radius
        double radius = Point2D.distance(center, p2);

        // return the created circle
        return new Circle2D(center, radius);
    }

    /**
     * Computes the center of the circumscribed circle of the three input points.
     * 
     * @throws ColinearPoints2DException
     *             if the 3 points are colinear
     */
    public static Point2D circumCenter(Point2D p1, Point2D p2, Point2D p3) {
        if (Point2D.isColinear(p1, p2, p3))
            throw new ColinearPoints2DException(p1, p2, p3);

        // create two median lines
        StraightLine2D line12 = StraightLine2D.createMedian(p1, p2);
        StraightLine2D line23 = StraightLine2D.createMedian(p2, p3);

        // check medians are not parallel
        assert !AbstractLine2D.isParallel(line12, line23) : "If points are not colinear, medians should not be parallel";

        // Compute intersection of the medians, and circle radius
        Point2D center = AbstractLine2D.getIntersection(line12, line23);

        // return the center
        return center;
    }

    /**
     * Computes the intersections points between two circles or circular shapes.
     * 
     * @param circle1
     *            an instance of circle or circle arc
     * @param circle2
     *            an instance of circle or circle arc
     * @return a collection of 0, 1 or 2 intersection points
     */
    public static Collection<Point2D> circlesIntersections(Circle2D circle1, Circle2D circle2) {
        // extract center and radius of each circle
        Point2D center1 = circle1.center();
        Point2D center2 = circle2.center();
        double r1 = circle1.radius();
        double r2 = circle2.radius();

        double d = Point2D.distance(center1, center2);

        // case of no intersection
        if (d < abs(r1 - r2) || d > (r1 + r2))
            return new ArrayList<>(0);

        // Angle of line from center1 to center2
        double angle = Angle2DUtil.horizontalAngle(center1, center2);

        if (d == abs(r1 - r2) || d == (r1 + r2)) {
            Collection<Point2D> r = new ArrayList<>(1);
            r.add(Point2D.createPolar(center1, r1, angle));
            return r;
        }

        // position of intermediate point
        double d1 = d / 2 + (r1 * r1 - r2 * r2) / (2 * d);
        Point2D tmp = Point2D.createPolar(center1, d1, angle);

        // distance between intermediate point and each intersection
        double h = sqrt(r1 * r1 - d1 * d1);

        // create empty array
        ArrayList<Point2D> intersections = new ArrayList<>(2);

        // Add the 2 intersection points
        Point2D p1 = Point2D.createPolar(tmp, h, angle + PI / 2);
        intersections.add(p1);
        Point2D p2 = Point2D.createPolar(tmp, h, angle - PI / 2);
        intersections.add(p2);

        return intersections;
    }

    /**
     * Computes intersections of a circle with a line. Returns an array of Point2D, of size 0, 1 or 2 depending on the distance between circle and line. If there are 2 intersections points, the first one in the array is the first one on the line.
     * 
     * @return a collection of intersection points
     * @since 0.11.1
     */
    public static Collection<Point2D> lineCircleIntersections(ILinearShape2D line, ICircularShape2D circle) {
        // initialize array of points (maximum 2 intersections)
        ArrayList<Point2D> intersections = new ArrayList<>(2);

        // extract parameters of the circle
        Circle2D parent = circle.supportingCircle();
        Point2D center = parent.center();
        double radius = parent.radius();

        // Compute line perpendicular to the test line, and going through the
        // circle center
        StraightLine2D perp = StraightLine2D.createPerpendicular(line, center);

        // Compute distance between line and circle center
        Point2D inter = perp.intersection(new StraightLine2D(line));
        if (inter == null) {
            throw new RuntimeException("Could not compute intersection point when computing line-cicle intersection");
        }
        double dist = inter.distance(center);

        // if the distance is the radius of the circle, return the
        // intersection point
        if (abs(dist - radius) < IShape2D.ACCURACY) {
            if (line.containsProjection(inter) && circle.containsProjection(inter))
                intersections.add(inter);
            return intersections;
        }

        // compute angle of the line, and distance between 'inter' point and
        // each intersection point
        double angle = line.horizontalAngle();
        double d2 = sqrt(radius * radius - dist * dist);

        // Compute position and angle of intersection points
        Point2D p1 = Point2D.createPolar(inter, d2, angle + Math.PI);
        Point2D p2 = Point2D.createPolar(inter, d2, angle);

        // add points to the array only if they belong to the line
        if (line.containsProjection(p1) && circle.containsProjection(p1))
            intersections.add(p1);
        if (line.containsProjection(p2) && circle.containsProjection(p2))
            intersections.add(p2);

        // return the result
        return intersections;
    }

    /**
     * Computes the radical axis of the two circles.
     * 
     * @since 0.11.1
     * @return the radical axis of the two circles.
     * @throws IllegalArgumentException
     *             if the two circles have same center
     */
    public static StraightLine2D radicalAxis(Circle2D circle1, Circle2D circle2) {

        // extract center and radius of each circle
        double r1 = circle1.radius();
        double r2 = circle2.radius();
        Point2D p1 = circle1.center();
        Point2D p2 = circle2.center();

        // compute horizontal angle of joining line
        double angle = Angle2DUtil.horizontalAngle(p1, p2);

        // distance between centers
        double dist = p1.distance(p2);
        if (dist < IShape2D.ACCURACY) {
            throw new IllegalArgumentException("Input circles must have distinct centers");
        }

        // position of the radical axis on the joining line
        double d = (dist * dist + r1 * r1 - r2 * r2) * .5 / dist;

        // pre-compute trigonometric functions
        double cot = Math.cos(angle);
        double sit = Math.sin(angle);

        // compute parameters of the line
        double x0 = p1.x() + d * cot;
        double y0 = p1.y() + d * sit;
        double dx = -sit;
        double dy = cot;

        // update state of current line
        return new StraightLine2D(x0, y0, dx, dy);
    }

    // ===================================================================
    // Class variables

    /** Coordinate of center. */
    private final Point2D center;

    /** the radius of the circle. */
    private final double r;

    /** Directed circle or not */
    private final boolean direct;

    /** Create a new circle with specified center, radius and orientation */
    public Circle2D(Point2D center, double radius, boolean direct) {
        this.center = center;
        this.r = radius;
        this.direct = direct;
    }

    /** Create a new circle with specified point center and radius */
    public Circle2D(Point2D center, double radius) {
        this(center, radius, true);
    }

    /** Create a new circle with specified center and radius */
    public Circle2D(double xcenter, double ycenter, double radius) {
        this(xcenter, ycenter, radius, true);
    }

    /** Create a new circle with specified center, radius and orientation. */
    public Circle2D(double xcenter, double ycenter, double radius, boolean direct) {
        this(new Point2D(xcenter, ycenter), radius, direct);
    }

    // ===================================================================
    // methods specific to class Circle2D

    /**
     * Returns the radius of the circle.
     */
    public double radius() {
        return r;
    }

    /**
     * Returns the intersection points with another circle. The result is a collection with 0, 1 or 2 points.
     */
    public Collection<Point2D> intersections(Circle2D circle) {
        return Circle2D.circlesIntersections(this, circle);
    }

    // ===================================================================
    // methods implementing CircularShape2D interface

    /**
     * Returns the circle itself.
     */
    @Override
    public Circle2D supportingCircle() {
        return this;
    }

    // ===================================================================
    // Methods implementing the Ellipse2D interface

    /**
     * Returns true if circle has a direct orientation.
     */
    @Override
    public boolean isDirect() {
        return direct;
    }

    /**
     * Returns center of the circle.
     */
    @Override
    public Point2D center() {
        return center;
    }

    @Override
    public boolean isCircle() {
        return true;
    }

    /**
     * Converts this circle to an instance of Ellipse2D.
     * 
     * @return a new instance of Ellipse2D that corresponds to this circle
     */
    public Ellipse2D asEllipse() {
        return new Ellipse2D(this.center.x(), this.center.y(), this.r, this.r, 0, this.direct);
    }

    // ===================================================================
    // methods implementing the Conic2D interface

    @Override
    public Type conicType() {
        return IConic2D.Type.CIRCLE;
    }

    /**
     * Returns Cartesian equation of the circle:
     * <p>
     * <code>(x-xc)^2 + (y-yc)^2 = r^2</code>, giving:
     * <p>
     * <code>x^2 + 0*x*y + y^2 -2*xc*x -2*yc*y + xc*xc+yc*yc-r*r = 0</code>.
     */
    @Override
    public double[] conicCoefficients() {
        return new double[] { 1, 0, 1, -2 * center.x(), -2 * center.y(), center.x() * center.x() + center.y() * center.y() - r * r };
    }

    /**
     * Returns 0, which is the eccentricity of a circle by definition.
     */
    @Override
    public double eccentricity() {
        return 0;
    }

    // ===================================================================
    // Methods implementing the CirculinearCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
     */
    @Override
    public ICirculinearDomain2D buffer(double dist) {
        BufferCalculator bc = BufferCalculator.getDefaultInstance();
        return bc.computeBuffer(this, dist);
    }

    /**
     * Returns the parallel circle located at a distance d from this circle. For direct circle, distance is positive outside of the circle, and negative inside. This is the contrary for indirect circles.
     */
    @Override
    public Circle2D parallel(double d) {
        double rp = max(direct ? r + d : r - d, 0);
        return new Circle2D(center, rp, direct);
    }

    /** Returns perimeter of the circle (equal to 2*PI*radius). */
    @Override
    public double length() {
        return PI * 2 * r;
    }

    /**
     * Returns the geodesic leangth until the given position.
     * 
     * @see math.geom2d.circulinear.ICirculinearCurve2D#length(double)
     */
    @Override
    public double length(double pos) {
        return pos * this.r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#position(double)
     */
    @Override
    public double position(double length) {
        return length / this.r;
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
        Point2D c1 = this.center();

        // If circles are concentric, creates directly the new circle
        if (center.distance(c1) < IShape2D.ACCURACY) {
            double r0 = inv.radius();
            double r2 = r0 * r0 / this.r;
            return new Circle2D(center, r2, this.direct);
        }

        // line joining centers of the two circles
        StraightLine2D centersLine = new StraightLine2D(center, c1);

        // get the two intersection points with the line joining the circle centers
        Collection<Point2D> points = this.intersections(centersLine);
        if (points.size() < 2) {
            throw new RuntimeException("Intersection of circle with line through center has less than 2 points");
        }
        Iterator<Point2D> iter = points.iterator();
        Point2D p1 = iter.next();
        Point2D p2 = iter.next();

        // If the circle contains the inversion center, it transforms into a
        // straight line
        if (this.distance(center) < IShape2D.ACCURACY) {
            // choose the intersection point that is not the center
            double dist1 = center.distance(p1);
            double dist2 = center.distance(p2);
            Point2D p0 = dist1 < dist2 ? p2 : p1;

            // transform the point, and return the perpendicular
            p0 = p0.transform(inv);
            return StraightLine2D.createPerpendicular(centersLine, p0);
        }

        // For regular cases, the circle transforms into an other circle

        // transform the two extreme points of the circle,
        // resulting in a diameter of the new circle
        p1 = p1.transform(inv);
        p2 = p2.transform(inv);

        // compute center and diameter of transformed circle
        double diam = p1.distance(p2);
        c1 = Point2D.midPoint(p1, p2);

        // create the transformed circle
        boolean direct = !this.isDirect() ^ this.isInside(inv.center());
        return new Circle2D(c1, diam / 2, direct);
    }

    // ===================================================================
    // methods implementing the Boundary2D interface

    @Override
    public ICirculinearDomain2D domain() {
        return new GenericCirculinearDomain2D(this);
    }

    @Override
    public void fill(Graphics2D g2) {
        // convert ellipse to awt shape
        java.awt.geom.Ellipse2D.Double ellipse = new java.awt.geom.Ellipse2D.Double(center.x() - r, center.y() - r, 2 * r, 2 * r);

        // need to rotate by angle theta
        java.awt.geom.AffineTransform trans = java.awt.geom.AffineTransform.getRotateInstance(0, center.x(), center.y());
        Shape shape = trans.createTransformedShape(ellipse);

        // draw the awt ellipse
        g2.fill(shape);
    }

    // ===================================================================
    // methods implementing OrientedCurve2D interface

    /**
     * Return either 0, 2*PI or -2*PI, depending whether the point is located inside the interior of the ellipse or not.
     */
    @Override
    public double windingAngle(Point2D point) {
        if (this.signedDistance(point) > 0)
            return 0;
        else
            return direct ? PI * 2 : -PI * 2;
    }

    // ===================================================================
    // methods of SmoothCurve2D interface

    @Override
    public Vector2D tangent(double t) {
        if (!direct)
            t = -t;
        double cost = cos(t);
        double sint = sin(t);

        if (direct)
            return new Vector2D(-r * sint, r * cost);
        else
            return new Vector2D(r * sint, -r * cost);
    }

    /**
     * Returns the inverse of the circle radius. If the circle is indirect, the curvature is negative.
     */
    @Override
    public double curvature(double t) {
        double k = 1 / r;
        return direct ? k : -k;
    }

    // ===================================================================
    // methods of ContinuousCurve2D interface

    /**
     * Returns a set of smooth curves, which contains only the circle.
     */
    @Override
    public Collection<? extends Circle2D> smoothPieces() {
        return wrapCurve(this);
    }

    /**
     * Returns true, as an ellipse is always closed.
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.curve.ContinuousCurve2D#asPolyline(int)
     */
    @Override
    public LinearRing2D asPolyline(int n) {
        return this.asPolylineClosed(n);
    }

    // ===================================================================
    // methods of OrientedCurve2D interface

    /**
     * Test whether the point is inside the circle. The test is performed by translating the point, and re-scaling it such that its coordinates are expressed in unit circle basis.
     */
    @Override
    public boolean isInside(Point2D point) {
        double xp = (point.x() - this.center.x()) / this.r;
        double yp = (point.y() - this.center.y()) / this.r;
        return (xp * xp + yp * yp < 1) ^ !direct;
    }

    @Override
    public double signedDistance(Point2D point) {
        return signedDistance(point.x(), point.y());
    }

    @Override
    public double signedDistance(double x, double y) {
        if (direct)
            return Point2D.distance(center.x(), center.y(), x, y) - r;
        else
            return r - Point2D.distance(center.x(), center.y(), x, y);
    }

    // ===================================================================
    // methods of Curve2D interface

    /** Always returns true. */
    @Override
    public boolean isBounded() {
        return true;
    }

    /** Always returns false. */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns the parameter of the first point of the ellipse, set to 0.
     */
    @Override
    public double t0() {
        return 0;
    }

    /**
     * Returns the parameter of the last point of the ellipse, set to 2*PI.
     */
    @Override
    public double t1() {
        return 2 * PI;
    }

    /**
     * Get the position of the curve from internal parametric representation, depending on the parameter t. This parameter is between the two limits 0 and 2*Math.PI.
     */
    @Override
    public Point2D point(double t) {
        double angle = t;
        if (!direct)
            angle = -t;
        return new Point2D(center.x() + r * cos(angle), center.y() + r * sin(angle));
    }

    /**
     * Get the first point of the circle, which is the same as the last point.
     * 
     * @return the first point of the curve
     */
    @Override
    public Point2D firstPoint() {
        return new Point2D(center.x() + r, center.y());
    }

    /**
     * Get the last point of the circle, which is the same as the first point.
     * 
     * @return the last point of the curve.
     */
    @Override
    public Point2D lastPoint() {
        return new Point2D(center.x() + r, center.y());
    }

    @Override
    public double position(Point2D point) {
        double angle = Angle2DUtil.horizontalAngle(center.x(), center.y(), point.x(), point.y());
        if (direct)
            return Angle2DUtil.formatAngle(angle);
        else
            return Angle2DUtil.formatAngle(-angle);
    }

    /**
     * Computes the projection position of the point on the circle, by computing angle with horizonrtal
     */
    @Override
    public double project(Point2D point) {
        double xp = point.x() - this.center.x();
        double yp = point.y() - this.center.y();

        // compute angle
        return Angle2DUtil.horizontalAngle(xp, yp);
    }

    /**
     * Returns the circle with same center and same radius, but with the opposite orientation.
     */
    @Override
    public Circle2D reverse() {
        return new Circle2D(this.center, this.r, !this.direct);
    }

    /**
     * Returns a new CircleArc2D. t0 and t1 are position on circle.
     */
    @Override
    public CircleArc2D subCurve(double t0, double t1) {
        double startAngle, extent;
        if (this.direct) {
            startAngle = t0;
            extent = Angle2DUtil.formatAngle(t1 - t0);
        } else {
            extent = -Angle2DUtil.formatAngle(t1 - t0);
            startAngle = Angle2DUtil.formatAngle(-t0);
        }
        return new CircleArc2D(this, startAngle, extent);
    }

    @Override
    public Collection<? extends Circle2D> continuousCurves() {
        return wrapCurve(this);
    }

    // ===================================================================
    // methods of Shape2D interface

    @Override
    public double distance(Point2D point) {
        return abs(Point2D.distance(center.x(), center.y(), point.x(), point.y()) - r);
    }

    @Override
    public double distance(double x, double y) {
        return abs(Point2D.distance(center.x(), center.y(), x, y) - r);
    }

    /**
     * Computes intersections of the circle with a line. Return an array of Point2D, of size 0, 1 or 2 depending on the distance between circle and line. If there are 2 intersections points, the first one in the array is the first one on the line.
     */
    @Override
    public Collection<Point2D> intersections(ILinearShape2D line) {
        return Circle2D.lineCircleIntersections(line, this);
    }

    /**
     * Clips the circle by a box. The result is an instance of CurveSet2D, which contains only instances of CircleArc2D or Circle2D. If the circle is not clipped, the result is an instance of CurveSet2D which contains 0 curves.
     */
    @Override
    public ICurveSet2D<? extends ICircularShape2D> clip(Box2D box) {
        // Clip the curve
        ICurveSet2D<ISmoothCurve2D> set = Curves2DUtil.clipSmoothCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<ICircularShape2D> result = new CurveArray2D<>(set.size());

        // convert the result
        for (ICurve2D curve : set.curves()) {
            if (curve instanceof CircleArc2D)
                result.add((CircleArc2D) curve);
            if (curve instanceof Circle2D)
                result.add((Circle2D) curve);
        }
        return result;
    }

    // ===================================================================
    // methods of Shape interface

    /**
     * Returns true if the point p lies on the circle, with precision given by Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true.
     */
    @Override
    public boolean containsProjection(Point2D p) {
        return true;
    }

    /**
     * Returns true if the point (x, y) lies exactly on the circle.
     */
    @Override
    public boolean contains(double x, double y) {
        return abs(distance(x, y)) <= IShape2D.ACCURACY;
    }

    @Override
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        double cost, sint;

        if (direct) {
            // Counter-clockwise circle
            for (double t = .1; t < PI * 2; t += .1) {
                cost = cos(t);
                sint = sin(t);
                path.lineTo((float) (center.x() + r * cost), (float) (center.y() + r * sint));
            }
        } else {
            // Clockwise circle
            for (double t = .1; t < PI * 2; t += .1) {
                cost = cos(t);
                sint = sin(t);
                path.lineTo((float) (center.x() + r * cost), (float) (center.y() - r * sint));
            }
        }

        // line to first point
        path.lineTo((float) (center.x() + r), (float) (center.y()));

        return path;
    }

    @Override
    public void draw(Graphics2D g2) {
        java.awt.geom.Ellipse2D.Double ellipse = new java.awt.geom.Ellipse2D.Double(center.x() - r, center.y() - r, 2 * r, 2 * r);
        g2.draw(ellipse);
    }

    // ===================================================================
    // methods implementing GeometricObject2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.GeometricObject2D#almostEquals(math.geom2d.GeometricObject2D, double)
     */
    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (!(obj instanceof Circle2D))
            return false;

        Circle2D circle = (Circle2D) obj;

        if (abs(circle.center.x() - center.x()) > eps)
            return false;
        if (abs(circle.center.y() - center.y()) > eps)
            return false;
        if (abs(circle.r - r) > eps)
            return false;
        if (circle.direct != direct)
            return false;
        return true;
    }

    /**
     * Returns bounding box of the circle.
     */
    @Override
    public Box2D boundingBox() {
        return new Box2D(center.x() - r, center.x() + r, center.y() - r, center.y() + r);
    }

    /**
     * Transforms this circle by an affine transform. If the transformed shape is a circle (ellipse with equal axis lengths), returns an instance of Circle2D. The resulting ellipse is direct if this ellipse and the transform are either both direct or both indirect.
     */
    @Override
    public IEllipseShape2D transform(AffineTransform2D trans) {
        // When the transform is not a similarity, should switch to EllipseArc
        // computation
        if (!AffineTransform2D.isSimilarity(trans)) {
            return this.asEllipse().transform(trans);
        }

        // If transform is a similarity, the result is a circle
        Point2D center = this.center().transform(trans);
        Point2D p1 = this.firstPoint().transform(trans);

        boolean direct = !this.direct ^ trans.isDirect();
        Circle2D result = new Circle2D(center, center.distance(p1), direct);
        return result;
    }

    // ===================================================================
    // methods of Object interface

    @Override
    public String toString() {
        return String.format(Locale.US, "Circle2D(%7.2f,%7.2f,%7.2f,%s)", center.x(), center.y(), r, direct ? "true" : "false");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Circle2D) {
            Circle2D that = (Circle2D) obj;

            // Compare each field
            if (!EqualUtils.areEqual(this.center.x(), that.center.x()))
                return false;
            if (!EqualUtils.areEqual(this.center.y(), that.center.y()))
                return false;
            if (!EqualUtils.areEqual(this.r, that.r))
                return false;
            if (this.direct != that.direct)
                return false;

            return true;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + center().hashCode();
        hash = hash * 31 + Double.valueOf(radius()).hashCode();
        return hash;
    }
}
