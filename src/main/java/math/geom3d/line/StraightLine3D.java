/**
 * 
 */

package math.geom3d.line;

import java.util.ArrayList;
import java.util.Collection;

import math.geom3d.Box3D;
import math.geom3d.Point3D;
import math.geom3d.IShape3D;
import math.geom3d.Vector3D;
import math.geom3d.curve.IContinuousCurve3D;
import math.geom3d.curve.ICurve3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class StraightLine3D implements IContinuousCurve3D {

    // ===================================================================
    // Class variables

    protected double x0 = 0;
    protected double y0 = 0;
    protected double z0 = 0;
    protected double dx = 1;
    protected double dy = 0;
    protected double dz = 0;

    // ===================================================================
    // Constructors

    public StraightLine3D() {
    }

    public StraightLine3D(Point3D origin, Vector3D direction) {
        this.x0 = origin.getX();
        this.y0 = origin.getY();
        this.z0 = origin.getZ();
        this.dx = direction.getX();
        this.dy = direction.getY();
        this.dz = direction.getZ();
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

    public StraightLine3D(double x0, double y0, double z0, double dx, double dy, double dz) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    // ===================================================================
    // methods specific to StraightLine3D

    public Point3D origin() {
        return new Point3D(x0, y0, z0);
    }

    public Vector3D direction() {
        return new Vector3D(dx, dy, dz);
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
     * @see math.geom3d.Shape3D#clip(math.geom3d.Box3D)
     */
    public IShape3D clip(Box3D box) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#contains(math.geom3d.Point3D)
     */
    public boolean contains(Point3D point) {
        return this.distance(point) < IShape3D.ACCURACY;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isBounded() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getBoundingBox()
     */
    public Box3D boundingBox() {
        Vector3D v = this.direction();

        // line parallel to (Ox) axis
        if (Math.hypot(v.getY(), v.getZ()) < IShape3D.ACCURACY)
            return new Box3D(x0, x0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // line parallel to (Oy) axis
        if (Math.hypot(v.getX(), v.getZ()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, y0, y0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // line parallel to (Oz) axis
        if (Math.hypot(v.getX(), v.getY()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, z0, z0);

        return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
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
    public StraightLine3D transform(AffineTransform3D trans) {
        return new StraightLine3D(origin().transform(trans), direction().transform(trans));
    }

    public Point3D firstPoint() {
        return new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Point3D lastPoint() {
        return new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Point3D point(double t) {
        return new Point3D(x0 + t * dx, y0 + t * dy, z0 + t * dz);
    }

    public double position(Point3D point) {
        return project(point);
    }

    public StraightLine3D reverseCurve() {
        return new StraightLine3D(origin(), direction().opposite());
    }

    /**
     * Returns an empty array of Point3D.
     */
    public Collection<Point3D> singularPoints() {
        return new ArrayList<Point3D>(0);
    }

    public ICurve3D subCurve(double t0, double t1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns -INFINITY;
     */
    public double getT0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns +INFINITY;
     */
    public double getT1() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Compute the position of the orthogonal projection of the given point on this line.
     */
    public double project(Point3D point) {
        Vector3D vl = this.direction();
        Vector3D vp = new Vector3D(this.origin(), point);
        return Vector3D.dotProduct(vl, vp) / vl.normSq();
    }

    public Collection<StraightLine3D> continuousCurves() {
        ArrayList<StraightLine3D> array = new ArrayList<StraightLine3D>(1);
        array.add(this);
        return array;
    }
}
