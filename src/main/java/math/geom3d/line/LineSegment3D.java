/**
 * 
 */

package math.geom3d.line;

import java.util.ArrayList;
import java.util.Collection;

import math.geom3d.Box3D;
import math.geom3d.IGeometricObject3D;
import math.geom3d.IShape3D;
import math.geom3d.curve.ICurve3D;
import math.geom3d.point.Point3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class LineSegment3D extends AbstractLine3D {
    private static final long serialVersionUID = 1L;

    private final Point3D point2;

    // ===================================================================
    // constructors

    public LineSegment3D(Point3D p1, Point3D p2) {
        super(p1, p2, true);
        this.point2 = p2;
    }

    // ===================================================================
    // methods implementing the Curve3D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getContinuousCurves()
     */
    @Override
    public Collection<LineSegment3D> continuousCurves() {
        ArrayList<LineSegment3D> array = new ArrayList<>(1);
        array.add(this);
        return array;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getFirstPoint()
     */
    @Override
    public Point3D firstPoint() {
        return origin();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getLastPoint()
     */
    @Override
    public Point3D lastPoint() {
        return point2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getPoint(double)
     */
    @Override
    public Point3D point(double t) {
        t = Math.max(Math.min(t, 1), 0);
        return new Point3D(x() + dx() * t, y() + dy() * t, z() + dz() * t);
    }

    /**
     * If point does not project on the line segment, return Double.NaN.
     * 
     * @see math.geom3d.curve.ICurve3D#position(math.geom3d.point.Point3D)
     */
    @Override
    public double position(Point3D point) {
        double t = this.supportingLine().position(point);
        if (t > 1)
            return Double.NaN;
        if (t < 0)
            return Double.NaN;
        return t;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getReverseCurve()
     */
    @Override
    public ICurve3D reverseCurve() {
        return new StraightLine3D(lastPoint(), firstPoint());
    }

    /**
     * Returns the2 end points.
     * 
     * @see math.geom3d.curve.ICurve3D#singularPoints()
     */
    @Override
    public Collection<Point3D> singularPoints() {
        ArrayList<Point3D> points = new ArrayList<>(2);
        points.add(firstPoint());
        points.add(lastPoint());
        return points;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#getSubCurve(double, double)
     */
    @Override
    public LineSegment3D subCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
        return new LineSegment3D(point(t0), point(t1));
    }

    /**
     * Return 0, by definition of LineSegment.
     * 
     * @see math.geom3d.curve.ICurve3D#t0()
     */
    @Override
    public double t0() {
        return 0;
    }

    /**
     * Return 1, by definition of LineSegment.
     * 
     * @see math.geom3d.curve.ICurve3D#t1()
     */
    @Override
    public double t1() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#project(math.geom3d.Point3D)
     */
    @Override
    public double project(Point3D point) {
        double t = supportingLine().project(point);
        return Math.min(Math.max(t, 0), 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.curve.Curve3D#transform(math.geom3d.transform.AffineTransform3D)
     */
    @Override
    public LineSegment3D transform(AffineTransform3D trans) {
        return new LineSegment3D(firstPoint().transform(trans), lastPoint().transform(trans));
    }

    // ===================================================================
    // methods implementing the Shape3D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#contains(math.geom3d.Point3D)
     */
    @Override
    public boolean contains(Point3D point) {
        StraightLine3D line = this.supportingLine();
        if (!line.contains(point))
            return false;
        double t = line.position(point);
        if (t < -IShape3D.ACCURACY)
            return false;
        if (t > 1 + IShape3D.ACCURACY)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getBoundingBox()
     */
    @Override
    public Box3D boundingBox() {
        return new Box3D(firstPoint(), lastPoint());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
    @Override
    public double distance(Point3D point) {
        double t = this.project(point);
        return point(t).distance(point);
    }

    /**
     * Returns true, as a LineSegment3D is always bounded.
     * 
     * @see math.geom3d.IShape3D#isBounded()
     */
    @Override
    public boolean isBounded() {
        return true;
    }

    /**
     * Returns false, as a LineSegment3D is never empty.
     * 
     * @see math.geom3d.IShape3D#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean almostEquals(IGeometricObject3D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof LineSegment3D))
            return false;
        LineSegment3D edge = (LineSegment3D) obj;

        Point3D p1 = firstPoint();
        Point3D p2 = lastPoint();
        Point3D ep1 = edge.firstPoint();
        Point3D ep2 = edge.lastPoint();
        return p1.almostEquals(ep1, eps) && p2.almostEquals(ep2, eps);
    }

    @Override
    public int hashCode() {
        Point3D point1 = firstPoint();
        final int prime = 31;
        int result = (point1 == null) ? 0 : point1.hashCode();
        result = prime * result + ((point2 == null) ? 0 : point2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        LineSegment3D other = (LineSegment3D) obj;

        Point3D point1 = firstPoint();
        Point3D opoint1 = other.firstPoint();
        if (point1 == null) {
            if (opoint1 != null)
                return false;
        } else if (!point1.equals(opoint1))
            return false;
        if (point2 == null) {
            if (other.point2 != null)
                return false;
        } else if (!point2.equals(other.point2))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new String("LineSegment3D[" + firstPoint() + "-" + lastPoint() + "]");
    }
}
