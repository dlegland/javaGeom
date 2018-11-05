/* File LineSegment2D.java 
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

import math.geom2d.Angle2DUtil;
import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.Vector2D;
import math.geom2d.exception.DegeneratedLine2DException;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Line segment, defined as the set of points located between the two end points.
 */
public final class LineSegment2D extends AbstractLine2D {
    private static final long serialVersionUID = 1L;
    private final Point2D point2;

    /**
     * Returns the straight line that is the median of the edge extremities.
     */
    public static StraightLine2D getMedian(LineSegment2D edge) {
        return new StraightLine2D(edge.x() + edge.dx() * .5, edge.y() + edge.dy() * .5, -edge.dy(), edge.dx());
    }

    /**
     * Returns angle between two edges sharing one vertex.
     */
    public static double getEdgeAngle(LineSegment2D edge1, LineSegment2D edge2) {
        double x0, y0, x1, y1, x2, y2;

        Point2D e1p1 = edge1.firstPoint();
        Point2D e1p2 = edge1.lastPoint();
        Point2D e2p1 = edge2.firstPoint();
        Point2D e2p2 = edge2.lastPoint();
        if (Math.abs(e1p1.x() - e2p1.x()) < IShape2D.ACCURACY && Math.abs(e1p1.y() - e2p1.y()) < IShape2D.ACCURACY) {
            x0 = e1p1.x();
            y0 = e1p1.y();
            x1 = e1p2.x();
            y1 = e1p2.y();
            x2 = e2p2.x();
            y2 = e2p2.y();
        } else if (Math.abs(e1p2.x() - e2p1.x()) < IShape2D.ACCURACY && Math.abs(e1p2.y() - e2p1.y()) < IShape2D.ACCURACY) {
            x0 = e1p2.x();
            y0 = e1p2.y();
            x1 = e1p1.x();
            y1 = e1p1.y();
            x2 = e2p2.x();
            y2 = e2p2.y();
        } else if (Math.abs(e1p2.x() - e2p2.x()) < IShape2D.ACCURACY && Math.abs(e1p2.y() - e2p2.y()) < IShape2D.ACCURACY) {
            x0 = e1p2.x();
            y0 = e1p2.y();
            x1 = e1p1.x();
            y1 = e1p1.y();
            x2 = e2p1.x();
            y2 = e2p1.y();
        } else if (Math.abs(e1p1.x() - e2p2.x()) < IShape2D.ACCURACY && Math.abs(e1p1.y() - e2p2.y()) < IShape2D.ACCURACY) {
            x0 = e1p1.x();
            y0 = e1p1.y();
            x1 = e1p2.x();
            y1 = e1p2.y();
            x2 = e2p1.x();
            y2 = e2p1.y();
        } else {// no common vertex -> return NaN
            return Double.NaN;
        }

        return Angle2DUtil.angle(new Vector2D(x1 - x0, y1 - y0), new Vector2D(x2 - x0, y2 - y0));
    }

    /**
     * Checks if two line segment intersect. Uses the Point2D.ccw() method, which is based on Sedgewick algorithm.
     * 
     * @param edge1
     *            a line segment
     * @param edge2
     *            a line segment
     * @return true if the 2 line segments intersect
     */
    public static boolean intersects(LineSegment2D edge1, LineSegment2D edge2) {
        Point2D e1p1 = edge1.firstPoint();
        Point2D e1p2 = edge1.lastPoint();
        Point2D e2p1 = edge2.firstPoint();
        Point2D e2p2 = edge2.lastPoint();

        boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1) * Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
        boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1) * Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
        return b1 && b2;
    }

    // ===================================================================
    // constructors

    /** Defines a new Edge with two extremities. */
    public LineSegment2D(Point2D point1, Point2D point2) {
        super(point1, point2, true);
        this.point2 = point2;
    }

    /** Defines a new Edge with two extremities. */
    public LineSegment2D(double x1, double y1, double x2, double y2) {
        this(new Point2D(x1, y1), new Point2D(x2, y2));
    }

    // ===================================================================
    // Methods specific to LineSegment2D

    /**
     * Returns the opposite vertex of the edge.
     * 
     * @param point
     *            one of the vertices of the edge
     * @return the other vertex, or null if point is nor a vertex of the edge
     */
    public Point2D opposite(Point2D point) {
        if (point.equals(firstPoint())) {
            return lastPoint();
        }
        if (point.equals(lastPoint())) {
            return firstPoint();
        }
        return null;
    }

    /**
     * Returns the median of the edge, that is the locus of points located at equal distance of each vertex.
     */
    public StraightLine2D getMedian() {
        // initial point is the middle of the edge -> x = x0+.5*dx
        // direction vector is the initial direction vector rotated by pi/2.
        return new StraightLine2D(x() + dx() * .5, y() + dy() * .5, -dy(), dx());
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns the length of the line segment.
     */
    @Override
    public double length() {
        return direction().norm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearCurve2D#parallel(double)
     */
    @Override
    public LineSegment2D parallel(double d) {
        // Checks line segment has a valid length
        double d2 = Math.hypot(dx(), dy());
        if (Math.abs(d2) < IShape2D.ACCURACY) {
            throw new DegeneratedLine2DException("Can not compute parallel of degenerated edge", this);
        }

        // compute parallel line segment
        d2 = d / d2;
        return new LineSegment2D(x() + dy() * d2, y() - dx() * d2, x() + dx() + dy() * d2, y() + dy() - dx() * d2);
    }

    // ===================================================================
    // Methods implementing the OrientedCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.OrientedCurve2D#signedDistance(math.geom2d.Point2D)
     */
    @Override
    public double signedDistance(double x, double y) {
        Point2D proj = super.projectedPoint(x, y);
        if (contains(proj))
            return super.signedDistance(x, y);

        double d = this.distance(x, y);
        return super.signedDistance(x, y) > 0 ? d : -d;
    }

    // ===================================================================
    // Methods implementing the Curve2D interface

    /**
     * Returns the first point of the edge.
     * 
     * @return the first point of the edge
     */
    @Override
    public Point2D firstPoint() {
        return origin();
    }

    /**
     * Returns the last point of the edge.
     * 
     * @return the last point of the edge
     */
    @Override
    public Point2D lastPoint() {
        return point2;
    }

    /**
     * Returns the parameter of the first point of the edge, equals to 0.
     */
    @Override
    public double t0() {
        return 0.0;
    }

    /**
     * Returns the parameter of the last point of the edge, equals to 1.
     */
    @Override
    public double t1() {
        return 1.0;
    }

    @Override
    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        return new Point2D(x() + dx() * t, y() + dy() * t);
    }

    /**
     * Returns the LineSegment which start from last point of this line segment, and which ends at the fist point of this last segment.
     */
    @Override
    public LineSegment2D reverse() {
        return new LineSegment2D(lastPoint(), firstPoint());
    }

    // ===================================================================
    // Methods implementing the Shape2D interface

    /**
     * Returns true
     */
    @Override
    public boolean isBounded() {
        return true;
    }

    @Override
    public boolean contains(double xp, double yp) {
        if (!super.supportContains(xp, yp))
            return false;

        // compute position on the line
        double t = positionOnLine(xp, yp);

        if (t < -ACCURACY)
            return false;
        if (t - 1 > ACCURACY)
            return false;

        return true;
    }

    /**
     * Get the distance of the point (x, y) to this edge.
     */
    @Override
    public double distance(double x, double y) {
        // In case of line segment with same extremities, computes distance to initial point
        if (length() < IShape2D.ACCURACY) {
            return Point2D.distance(this.x(), this.y(), x, y);
        }

        // compute position on the supporting line
        StraightLine2D line = this.supportingLine();
        double t = line.positionOnLine(x, y);

        // clamp with parameterization bounds of edge
        t = Math.max(Math.min(t, 1), 0);

        // compute position of projected point on the edge
        Point2D proj = line.point(t);

        // return distance to projected point
        return proj.distance(x, y);
    }

    @Override
    public LineSegment2D transform(AffineTransform2D trans) {
        return new LineSegment2D(firstPoint().transform(trans), lastPoint().transform(trans));
    }

    /**
     * Returns the bounding box of this line segment.
     */
    @Override
    public Box2D boundingBox() {
        return new Box2D(firstPoint(), lastPoint());
    }

    // =================================
    // Methods implementing the Shape interface

    /**
     * Appends a line to the current path.
     * 
     * @param path
     *            the path to modify
     * @return the modified path
     */
    @Override
    public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path) {
        path.lineTo((float) lastPoint().x(), (float) lastPoint().y());
        return path;
    }

    /**
     * deprecated
     */
    public java.awt.geom.GeneralPath getGeneralPath() {
        java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
        path.moveTo((float) firstPoint().x(), (float) firstPoint().y());
        path.lineTo((float) lastPoint().x(), (float) lastPoint().y());
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

        if (!(obj instanceof LineSegment2D))
            return false;
        LineSegment2D edge = (LineSegment2D) obj;

        Point2D p1 = firstPoint();
        Point2D p2 = lastPoint();
        Point2D ep1 = edge.firstPoint();
        Point2D ep2 = edge.lastPoint();
        return p1.almostEquals(ep1, eps) && p2.almostEquals(ep2, eps);
    }

    @Override
    public String toString() {
        return new String("LineSegment2D[" + firstPoint() + "-" + lastPoint() + "]");
    }
}
