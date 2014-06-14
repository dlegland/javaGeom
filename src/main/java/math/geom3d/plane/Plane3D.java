/**
 * 
 */

package math.geom3d.plane;

import math.geom2d.Point2D;
import math.geom3d.Box3D;
import math.geom3d.Point3D;
import math.geom3d.Shape3D;
import math.geom3d.Vector3D;
import math.geom3d.line.StraightLine3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class Plane3D implements Shape3D {

    // ===================================================================
    // class variables

    protected double x0  = 0;
    protected double y0  = 0;
    protected double z0  = 0;
    protected double dx1 = 1;
    protected double dy1 = 0;
    protected double dz1 = 0;
    protected double dx2 = 0;
    protected double dy2 = 1;
    protected double dz2 = 0;

    // ===================================================================
    // static methods

    public final static Plane3D createXYPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(1, 0, 0),
                new Vector3D(0, 1, 0));
    }

    public final static Plane3D createXZPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(1, 0, 0),
                new Vector3D(0, 0, 1));
    }

    public final static Plane3D createYZPlane() {
        return new Plane3D(new Point3D(0, 0, 0), new Vector3D(0, 1, 0),
                new Vector3D(0, 0, 1));
    }

    // ===================================================================
    // constructors

    public Plane3D() {
    }

    public Plane3D(Point3D point, Vector3D vector1, Vector3D vector2) {
        this.x0 = point.getX();
        this.y0 = point.getY();
        this.z0 = point.getZ();
        this.dx1 = vector1.getX();
        this.dy1 = vector1.getY();
        this.dz1 = vector1.getZ();
        this.dx2 = vector2.getX();
        this.dy2 = vector2.getY();
        this.dz2 = vector2.getZ();
    }

    // ===================================================================
    // methods specific to Plane3D

    public Point3D origin() {
        return new Point3D(x0, y0, z0);
    }

    public Vector3D vector1() {
        return new Vector3D(dx1, dy1, dz1);
    }

    public Vector3D vector2() {
        return new Vector3D(dx2, dy2, dz2);
    }

    /**
     * Returns a normal vector that points towards the outside part of the plane.
     * 
     * @return the outer normal vector.
     */
    public Vector3D normal() {
        return Vector3D.crossProduct(this.vector1(), this.vector2())
                .opposite();
    }

    /**
     * Compute intersection of a line with this plane. Uses algorithm 1 given
     * in: <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/</a>.
     * 
     * @param line the line which intersects the plane
     * @return the intersection point
     */
    public Point3D lineIntersection(StraightLine3D line) {
        // the plane normal
        Vector3D n = this.normal();

        // the difference between origin of plane and origin of line
        Vector3D dp = new Vector3D(line.origin(), this.origin());

        // compute ratio of dot products,
        // see http://local.wasp.uwa.edu.au/~pbourke/geometry/planeline/
        double t = Vector3D.dotProduct(n, dp)
                /Vector3D.dotProduct(n, line.direction());

        return line.point(t);
    }

    public Point3D projectPoint(Point3D point) {
        StraightLine3D line = new StraightLine3D(point, this.normal());
        return this.lineIntersection(line);
    }

    public Vector3D projectVector(Vector3D vect) {
        Point3D point = new Point3D(x0+vect.getX(), y0+vect.getY(), z0
                +vect.getZ());
        point = this.projectPoint(point);
        return new Vector3D(point.getX()-x0, point.getY()-y0, point.getZ()-z0);
    }

    public Point3D point(double u, double v) {
        return new Point3D(x0+u*dx1+v*dx2, y0+u*dy1+v*dy2, z0+u*dz1+v*dz2);
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
    public Shape3D clip(Box3D box) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#contains(math.geom3d.Point3D)
     */
    public boolean contains(Point3D point) {
        Point3D proj = this.projectPoint(point);
        return (point.distance(proj)<Shape3D.ACCURACY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getBoundingBox()
     */
    public Box3D boundingBox() {
        // plane parallel to XY plane
        if (Math.abs(dz1)<Shape3D.ACCURACY&&Math.abs(dz2)<Shape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY, z0, z0);

        // plane parallel to YZ plane
        if (Math.abs(dx1)<Shape3D.ACCURACY&&Math.abs(dx2)<Shape3D.ACCURACY)
            return new Box3D(x0, x0, Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY);

        // plane parallel to XZ plane
        if (Math.abs(dy1)<Shape3D.ACCURACY&&Math.abs(dy2)<Shape3D.ACCURACY)
            return new Box3D(Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY, y0, y0, Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY);

        return new Box3D(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
    public double distance(Point3D point) {
        return point.distance(this.projectPoint(point));
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#isBounded()
     */
    public boolean isBounded() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#isEmpty()
     */
    public boolean isEmpty() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#transform(math.geom3d.transform.AffineTransform3D)
     */
    public Shape3D transform(AffineTransform3D trans) {
        return new Plane3D(this.origin().transform(trans), this.vector1()
                .transform(trans), this.vector2().transform(trans));
    }

    // ===================================================================
    // methods overriding Object superclass

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Plane3D))
            return false;
        Plane3D plane = (Plane3D) obj;

        if (Math.abs(this.x0-plane.x0)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.y0-plane.y0)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.z0-plane.z0)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dx1-plane.dx1)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dy1-plane.dy1)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dz1-plane.dz1)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dx2-plane.dx2)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dy2-plane.dy2)>Shape3D.ACCURACY)
            return false;
        if (Math.abs(this.dz2-plane.dz2)>Shape3D.ACCURACY)
            return false;
        return true;
    }

}
