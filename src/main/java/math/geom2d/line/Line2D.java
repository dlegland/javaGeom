/* File Line2D.java 
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

import java.io.Serializable;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.circulinear.ICirculinearElement2D;
import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.AbstractSmoothCurve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.Curves2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * Line object defined from 2 points. This object keep points reference in memory, and recomputes properties directly from points.
 * <p>
 * Example :
 * <p>
 * <code>
 * // Create an Edge2D<br>
 * Line2D line = new Line2D(new Point2D(0, 0), new Point2D(1, 2));<br>
 * // Change direction of line, by changing second point :<br>
 * line.setPoint2(new Point2D(4, 5));<br>
 * // Change position and direction of the line, by changing first point. <br>
 * // 'line' is now the edge (2,3)-(4,5)<br>
 * line.setPoint1(new Point2D(2, 3));<br>
 * </code>
 * <p>
 * <p>
 * This class may be slower than Edge2D or StraightLine2D, because parameters are updated each time a computation is made, causing lot of additional processing.
 */
public class Line2D extends AbstractSmoothCurve2D implements ILinearElement2D {
    private static final long serialVersionUID = 1L;

    /**
     * The origin point.
     */
    private final Point2D p1;

    /**
     * The destination point.
     */
    private final Point2D p2;

    // ===================================================================
    // constructors

    /**
     * Checks if two line intersect. Uses the {@link math.geom2d.Point2D#ccw(Point2D, Point2D, Point2D) Point2D.ccw} method, which is based on Sedgewick algorithm.
     * 
     * @param line1
     *            a Line2D object
     * @param line2
     *            a Line2D object
     * @return true if the 2 lines intersect
     */
    public static boolean intersects(Line2D line1, Line2D line2) {
        Point2D e1p1 = line1.firstPoint();
        Point2D e1p2 = line1.lastPoint();
        Point2D e2p1 = line2.firstPoint();
        Point2D e2p2 = line2.lastPoint();

        boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1) * Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
        boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1) * Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
        return b1 && b2;
    }

    // ===================================================================
    // constructors

    /** Define a new Line2D with two extremities. */
    public Line2D(Point2D point1, Point2D point2) {
        this.p1 = point1;
        this.p2 = point2;
    }

    /** Define a new Line2D with two extremities. */
    public Line2D(double x1, double y1, double x2, double y2) {
        p1 = new Point2D(x1, y1);
        p2 = new Point2D(x2, y2);
    }

    // ===================================================================
    // Methods specific to Line2D

    /**
     * Return the first point of the edge. It corresponds to getPoint(0).
     * 
     * @return the first point.
     */
    public Point2D getPoint1() {
        return p1;
    }

    /**
     * Return the last point of the edge. It corresponds to getPoint(1).
     * 
     * @return the last point.
     */
    public Point2D getPoint2() {
        return p2;
    }

    public double x1() {
        return p1.x();
    }

    public double y1() {
        return p1.y();
    }

    public double x2() {
        return p2.x();
    }

    public double y2() {
        return p2.y();
    }

    /**
     * Return the opposite vertex of the edge.
     * 
     * @param point
     *            : one of the vertices of the edge
     * @return the other vertex
     */
    public Point2D getOtherPoint(Point2D point) {
        if (point.equals(p1))
            return p2;
        if (point.equals(p2))
            return p1;
        return null;
    }

    // ===================================================================
    // methods implementing the LinearShape2D interface

    public boolean isColinear(ILinearShape2D line) {
        return new LineSegment2D(p1, p2).isColinear(line);
    }

    /**
     * Test if the this object is parallel to the given one. This method is overloaded to update parameters before computation.
     */
    public boolean isParallel(ILinearShape2D line) {
        return new LineSegment2D(p1, p2).isParallel(line);
    }

    @Override
    public boolean containsProjection(Point2D point) {
        double pos = new LineSegment2D(p1, p2).project(point);
        return pos > (0 - IShape2D.ACCURACY) && pos < (1 + IShape2D.ACCURACY);
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

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

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#parallel(double)
     */
    @Override
    public Line2D parallel(double d) {
        double x0 = x1();
        double y0 = y1();
        double dx = x2() - x0;
        double dy = y2() - y0;
        double d2 = d / Math.hypot(dx, dy);
        return new Line2D(x0 + dy * d2, y0 - dx * d2, x0 + dx + dy * d2, y0 + dy - dx * d2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#length()
     */
    @Override
    public double length() {
        return p1.distance(p2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#length(double)
     */
    @Override
    public double length(double pos) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        return pos * Math.hypot(dx, dy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#position(double)
     */
    @Override
    public double position(double length) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        return length / Math.hypot(dx, dy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    @Override
    public ICirculinearElement2D transform(CircleInversion2D inv) {
        // Extract inversion parameters
        Point2D center = inv.center();
        double r = inv.radius();

        // compute distance of line to inversion center
        Point2D po = new StraightLine2D(this).projectedPoint(center);
        double d = this.distance(po);

        // Degenerate case of a line passing through the center.
        // returns the line itself.
        if (Math.abs(d) < IShape2D.ACCURACY) {
            Point2D p1 = this.firstPoint().transform(inv);
            Point2D p2 = this.lastPoint().transform(inv);
            return new LineSegment2D(p1, p2);
        }

        // angle from center to line
        double angle = Angle2DUtil.horizontalAngle(center, po);

        // center of transformed circle
        double r2 = r * r / d / 2;
        Point2D c2 = Point2D.createPolar(center, r2, angle);

        // choose direction of circle arc
        boolean direct = !this.isInside(center);

        // compute angle between center of transformed circle and end points
        double theta1 = Angle2DUtil.horizontalAngle(c2, p1);
        double theta2 = Angle2DUtil.horizontalAngle(c2, p2);

        // create the new circle arc
        return new CircleArc2D(c2, r2, theta1, theta2, direct);
    }

    // ===================================================================
    // methods implementing the LinearShape2D interface

    /*
     * (non-Javadoc)
     */
    public double[][] parametric() {
        return new LineSegment2D(p1, p2).parametric();
    }

    public double[] cartesianEquation() {
        return new LineSegment2D(p1, p2).cartesianEquation();
    }

    public double[] polarCoefficients() {
        return new LineSegment2D(p1, p2).polarCoefficients();
    }

    public double[] polarCoefficientsSigned() {
        return new LineSegment2D(p1, p2).polarCoefficientsSigned();
    }

    // ===================================================================
    // methods implementing the LinearShape2D interface

    @Override
    public double horizontalAngle() {
        return new LineSegment2D(p1, p2).horizontalAngle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.line.LinearShape2D#intersection(math.geom2d.line.LinearShape2D)
     */
    @Override
    public Point2D intersection(ILinearShape2D line) {
        return new LineSegment2D(p1, p2).intersection(line);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.line.LinearShape2D#origin()
     */
    @Override
    public Point2D origin() {
        return p1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.line.LinearShape2D#supportingLine()
     */
    @Override
    public StraightLine2D supportingLine() {
        return new StraightLine2D(p1, p2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.line.LinearShape2D#direction()
     */
    @Override
    public Vector2D direction() {
        return new Vector2D(p1, p2);
    }

    // ===================================================================
    // methods implementing the OrientedCurve2D interface

    @Override
    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    @Override
    public double signedDistance(double x, double y) {
        return new LineSegment2D(p1, p2).signedDistance(x, y);
    }

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.curve.ContinuousCurve2D#smoothPieces()
     */
    @Override
    public Collection<? extends Line2D> smoothPieces() {
        return wrapCurve(this);
    }

    /**
     * Returns false.
     * 
     * @see math.geom2d.curve.IContinuousCurve2D#isClosed()
     */
    @Override
    public boolean isClosed() {
        return false;
    }

    // ===================================================================
    // methods implementing the Shape2D interface

    /**
     * Returns the distance of the point <code>p</code> to this edge.
     */
    @Override
    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }

    /**
     * Returns the distance of the point (x, y) to this edge.
     */
    @Override
    public double distance(double x, double y) {
        // project the point on the support line
        StraightLine2D support = new StraightLine2D(p1, p2);
        Point2D proj = support.projectedPoint(x, y);

        // if this line contains the projection, return orthogonal distance
        if (contains(proj))
            return proj.distance(x, y);

        // return distance to closest extremity
        double d1 = Math.hypot(p1.x() - x, p1.y() - y);
        double d2 = Math.hypot(p2.x() - x, p2.y() - y);
        return Math.min(d1, d2);
    }

    /**
     * Creates a straight line parallel to this object, and passing through the given point.
     * 
     * @param point
     *            the point to go through
     * @return the parallel through the point
     */
    public StraightLine2D parallel(Point2D point) {
        return new LineSegment2D(p1, p2).parallel(point);
    }

    /**
     * Creates a straight line perpendicular to this object, and passing through the given point.
     * 
     * @param point
     *            the point to go through
     * @return the perpendicular through point
     */
    public StraightLine2D perpendicular(Point2D point) {
        return new LineSegment2D(p1, p2).perpendicular(point);
    }

    /**
     * Clips the line object by a box. The result is an instance of CurveSet2D, which contains only instances of LineArc2D. If the line object is not clipped, the result is an instance of CurveSet2D which contains 0 curves.
     */
    @Override
    public ICurveSet2D<? extends Line2D> clip(Box2D box) {
        // Clip the curve
        ICurveSet2D<? extends ICurve2D> set = Curves2D.clipCurve(this, box);

        // Stores the result in appropriate structure
        CurveArray2D<Line2D> result = new CurveArray2D<>(set.size());

        // convert the result
        for (ICurve2D curve : set.curves()) {
            if (curve instanceof Line2D)
                result.add((Line2D) curve);
        }
        return result;
    }

    /**
     * Returns the bounding box of the Line2D.
     */
    @Override
    public Box2D boundingBox() {
        return new Box2D(p1, p2);
    }

    // ===================================================================
    // methods inherited from SmoothCurve2D interface

    @Override
    public Vector2D tangent(double t) {
        return new Vector2D(p1, p2);
    }

    /**
     * Returns 0 as every linear shape.
     */
    @Override
    public double curvature(double t) {
        return 0.0;
    }

    // ===================================================================
    // methods inherited from OrientedCurve2D interface

    @Override
    public double windingAngle(Point2D point) {
        return new LineSegment2D(p1, p2).windingAngle(point);
    }

    @Override
    public boolean isInside(Point2D point) {
        return new LineSegment2D(p1, p2).signedDistance(point) < 0;
    }

    // ===================================================================
    // methods inherited from Curve2D interface

    /**
     * Returns 0.
     */
    @Override
    public double t0() {
        return 0.0;
    }

    /**
     * Returns 1.
     */
    @Override
    public double t1() {
        return 1.0;
    }

    @Override
    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        double x = p1.x() * (1 - t) + p2.x() * t;
        double y = p1.y() * (1 - t) + p2.y() * t;
        return new Point2D(x, y);
    }

    /**
     * Get the first point of the curve.
     * 
     * @return the first point of the curve
     */
    @Override
    public Point2D firstPoint() {
        return p1;
    }

    /**
     * Get the last point of the curve.
     * 
     * @return the last point of the curve.
     */
    @Override
    public Point2D lastPoint() {
        return p2;
    }

    /**
     * Returns the position of the point on the line. If point belongs to the line, this position is defined by the ratio:
     * <p>
     * <code> t = (xp - x0)/dx <\code>, or equivalently :
     * <p>
     * <code> t = (yp - y0)/dy <\code>.
     * <p>
     * If point does not belong to edge, return Double.NaN. The current implementation uses the direction with the biggest derivative, in order to avoid divisions by zero.
     */
    @Override
    public double position(Point2D point) {
        return new LineSegment2D(p1, p2).position(point);
    }

    @Override
    public double project(Point2D point) {
        return new LineSegment2D(p1, p2).project(point);
    }

    /**
     * Returns the Line2D object which starts at <code>point2</code> and ends at <code>point1</code>.
     */
    @Override
    public Line2D reverse() {
        return new Line2D(p2, p1);
    }

    @Override
    public Collection<? extends Line2D> continuousCurves() {
        return wrapCurve(this);
    }

    /**
     * Returns a new Line2D, which is the portion of the line delimited by parameters t0 and t1.
     */
    @Override
    public Line2D subCurve(double t0, double t1) {
        if (t0 > t1)
            return null;
        t0 = Math.max(t0, t0());
        t1 = Math.min(t1, t1());
        return new Line2D(this.point(t0), this.point(t1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.curve.Curve2D#intersections(math.geom2d.line.LinearShape2D)
     */
    @Override
    public Collection<Point2D> intersections(ILinearShape2D line) {
        return new LineSegment2D(p1, p2).intersections(line);
    }

    // ===================================================================
    // methods inherited from Shape2D interface

    @Override
    public Line2D transform(AffineTransform2D trans) {
        return new Line2D(p1.transform(trans), p2.transform(trans));
    }

    // ===================================================================
    // methods inherited from Shape interface

    /**
     * Returns true if the point (x, y) lies on the line, with precision given by Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(double x, double y) {
        return new LineSegment2D(p1, p2).contains(x, y);
    }

    /**
     * Returns true if the point p lies on the line, with precision given by Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true
     */
    @Override
    public boolean isBounded() {
        return true;
    }

    /**
     * Returns false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    public java.awt.geom.GeneralPath getGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        path.moveTo((float) p1.x(), (float) p1.y());
        path.lineTo((float) p2.x(), (float) p2.y());
        return path;
    }

    @Override
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        path.lineTo((float) p2.x(), (float) p2.y());
        return path;
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

        // check class
        if (!(obj instanceof Line2D))
            return false;

        // cast class, and compare members
        Line2D edge = (Line2D) obj;
        return p1.almostEquals(edge.p1, eps) && p2.almostEquals(edge.p2, eps);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Line2D other = (Line2D) obj;
        if (p1 == null) {
            if (other.p1 != null)
                return false;
        } else if (!p1.equals(other.p1))
            return false;
        if (p2 == null) {
            if (other.p2 != null)
                return false;
        } else if (!p2.equals(other.p2))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + p1.hashCode();
        hash = hash * 31 + p2.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "Line2D [p1=" + p1 + ", p2=" + p2 + "]";
    }
}
