/**
 * 
 */

package math.geom3d.line;

import java.util.ArrayList;
import java.util.Collection;

import math.geom3d.Box3D;
import math.geom3d.IGeometricObject3D;
import math.geom3d.IShape3D;
import math.geom3d.Vector3D;
import math.geom3d.point.Point3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class StraightLine3D extends AbstractLine3D {
    private static final long serialVersionUID = 1L;

    public StraightLine3D(Point3D p, Vector3D v) {
        super(p, v, false);
    }

    /**
     * Constructs a line passing through the 2 points.
     * 
     * @param p1
     *            the first point
     * @param p2
     *            the second point
     */
    public StraightLine3D(Point3D p1, Point3D p2) {
        this(p1, new Vector3D(p1, p2));
    }

    public StraightLine3D(AbstractLine3D l) {
        this(l.origin(), l.direction());
    }

    // /**
    // * not yet implemented
    // */
    // public StraightLine2D project(Plane3D plane) {
    // // TODO Auto-generated method stub
    // return null;
    // }

    // ===================================================================
    // methods implementing the Shape3D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#contains(math.geom3d.Point3D)
     */
    @Override
    public boolean contains(Point3D point) {
        return this.distance(point) < IShape3D.ACCURACY;
    }

    @Override
    public boolean isBounded() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getBoundingBox()
     */
    @Override
    public Box3D boundingBox() {
        Vector3D v = this.direction();

        // line parallel to (Ox) axis
        if (Math.hypot(v.y(), v.z()) < IShape3D.ACCURACY)
            return new Box3D(x(), x(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // line parallel to (Oy) axis
        if (Math.hypot(v.x(), v.z()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, y(), y(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // line parallel to (Oz) axis
        if (Math.hypot(v.x(), v.y()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, z(), z());

        return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
    @Override
    public double distance(Point3D p) {
        Vector3D vl = this.direction();
        Vector3D vp = new Vector3D(this.origin(), p);
        return Vector3D.crossProduct(vl, vp).norm() / vl.norm();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#transform(math.geom3d.AffineTransform3D)
     */
    @Override
    public StraightLine3D transform(AffineTransform3D trans) {
        return new StraightLine3D(origin().transform(trans), direction().transform(trans));
    }

    @Override
    public Point3D firstPoint() {
        return new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public Point3D lastPoint() {
        return new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public Point3D point(double t) {
        return new Point3D(origin().x() + t * direction().x(), origin().y() + t * direction().y(), origin().z() + t * direction().z());
    }

    @Override
    public double position(Point3D point) {
        return project(point);
    }

    @Override
    public StraightLine3D reverseCurve() {
        return new StraightLine3D(origin(), direction().opposite());
    }

    /**
     * Returns an empty array of Point3D.
     */
    @Override
    public Collection<Point3D> singularPoints() {
        return new ArrayList<>(0);
    }

    /**
     * Returns -INFINITY;
     */
    @Override
    public double t0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns +INFINITY;
     */
    @Override
    public double t1() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Compute the position of the orthogonal projection of the given point on this line.
     */
    @Override
    public double project(Point3D point) {
        Vector3D vl = this.direction();
        Vector3D vp = new Vector3D(this.origin(), point);
        return Vector3D.dotProduct(vl, vp) / vl.normSq();
    }

    @Override
    public Collection<StraightLine3D> continuousCurves() {
        ArrayList<StraightLine3D> array = new ArrayList<>(1);
        array.add(this);
        return array;
    }

    @Override
    public StraightLine3D supportingLine() {
        return this;
    }

    @Override
    public boolean almostEquals(IGeometricObject3D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof StraightLine3D))
            return false;
        StraightLine3D line = (StraightLine3D) obj;

        return origin().almostEquals(line.origin(), eps) && direction().almostEquals(line.direction(), eps);
    }
}
