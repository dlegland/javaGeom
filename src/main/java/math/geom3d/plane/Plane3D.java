/**
 * 
 */

package math.geom3d.plane;

import java.io.Serializable;

import math.geom2d.point.Point2D;
import math.geom3d.Box3D;
import math.geom3d.IGeometricObject3D;
import math.geom3d.IShape3D;
import math.geom3d.Vector3D;
import math.geom3d.line.StraightLine3D;
import math.geom3d.point.Point3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class Plane3D implements IShape3D, Serializable {
    private static final long serialVersionUID = 1L;

    protected Point3D point;
    protected Vector3D vector1;
    protected Vector3D vector2;

    // ===================================================================
    // static methods

    public final static Plane3D createXYPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
    }

    public final static Plane3D createXZPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
    }

    public final static Plane3D createYZPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(0, 1, 0), new Vector3D(0, 0, 1));
    }

    public Plane3D(Point3D point, Vector3D vector1, Vector3D vector2) {
        this.point = point;
        this.vector1 = vector1;
        this.vector2 = vector2;
    }

    // ===================================================================
    // methods specific to Plane3D

    public Point3D origin() {
        return point;
    }

    public Vector3D vector1() {
        return vector1;
    }

    public Vector3D vector2() {
        return vector2;
    }

    /**
     * Returns a normal vector that points towards the outside part of the plane.
     * 
     * @return the outer normal vector.
     */
    public Vector3D normal() {
        return Vector3D.crossProduct(this.vector1(), this.vector2()).opposite();
    }

    /**
     * Compute intersection of a line with this plane. Uses algorithm 1 given in: <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/"> http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/</a>.
     * 
     * @param line
     *            the line which intersects the plane
     * @return the intersection point
     */
    public Point3D lineIntersection(StraightLine3D line) {
        // the plane normal
        Vector3D n = this.normal();

        // the difference between origin of plane and origin of line
        Vector3D dp = new Vector3D(line.origin(), this.origin());

        // compute ratio of dot products,
        // see http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/
        double t = Vector3D.dotProduct(n, dp) / Vector3D.dotProduct(n, line.direction());

        return line.point(t);
    }

    public Point3D projectPoint(Point3D point) {
        StraightLine3D line = new StraightLine3D(point, this.normal());
        return this.lineIntersection(line);
    }

    public Vector3D projectVector(Vector3D vect) {
        Point3D p = point.plus(vect);
        p = this.projectPoint(p);
        return new Vector3D(point, p);
    }

    public Point3D point(double u, double v) {
        return new Point3D(point.x() + u * vector1.x() + v * vector2.x(), point.y() + u * vector1.y() + v * vector2.y(), point.z() + u * vector1.z() + v * vector2.z());
    }

    public Point2D pointPosition(Point3D point) {
        point = this.projectPoint(point);
        // TODO: complete it
        return null;
    }

    // ===================================================================
    // methods implementing Shape3D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#clip(math.geom3d.Box3D)
     */
    @Override
    public IShape3D clip(Box3D box) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#contains(math.geom3d.Point3D)
     */
    @Override
    public boolean contains(Point3D point) {
        Point3D proj = this.projectPoint(point);
        return (point.distance(proj) < IShape3D.ACCURACY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getBoundingBox()
     */
    @Override
    public Box3D boundingBox() {
        // plane parallel to XY plane
        if (Math.abs(vector1.z()) < IShape3D.ACCURACY && Math.abs(vector2.z()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, point.z(), point.z());

        // plane parallel to YZ plane
        if (Math.abs(vector1.x()) < IShape3D.ACCURACY && Math.abs(vector2.x()) < IShape3D.ACCURACY)
            return new Box3D(point.x(), point.x(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        // plane parallel to XZ plane
        if (Math.abs(vector1.y()) < IShape3D.ACCURACY && Math.abs(vector2.y()) < IShape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, point.y(), point.y(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
    @Override
    public double distance(Point3D point) {
        return point.distance(this.projectPoint(point));
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#isBounded()
     */
    @Override
    public boolean isBounded() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#transform(math.geom3d.transform.AffineTransform3D)
     */
    @Override
    public IShape3D transform(AffineTransform3D trans) {
        return new Plane3D(this.origin().transform(trans), this.vector1().transform(trans), this.vector2().transform(trans));
    }

    @Override
    public boolean almostEquals(IGeometricObject3D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof Plane3D))
            return false;
        Plane3D plane = (Plane3D) obj;

        Point3D p = plane.point;
        Vector3D v1 = plane.vector1;
        Vector3D v2 = plane.vector2;
        return point.almostEquals(p, eps) && vector1.almostEquals(v1, eps) && vector2.almostEquals(v2, eps);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((point == null) ? 0 : point.hashCode());
        result = prime * result + ((vector1 == null) ? 0 : vector1.hashCode());
        result = prime * result + ((vector2 == null) ? 0 : vector2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Plane3D other = (Plane3D) obj;
        if (point == null) {
            if (other.point != null)
                return false;
        } else if (!point.equals(other.point))
            return false;
        if (vector1 == null) {
            if (other.vector1 != null)
                return false;
        } else if (!vector1.equals(other.vector1))
            return false;
        if (vector2 == null) {
            if (other.vector2 != null)
                return false;
        } else if (!vector2.equals(other.vector2))
            return false;
        return true;
    }
}
