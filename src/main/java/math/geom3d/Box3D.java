/**
 * 
 */

package math.geom3d;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

import java.io.Serializable;

import math.geom3d.point.Point3D;

/**
 * A 3-dimensional box, defined by its extent in each direction.
 * 
 * @author dlegland
 */
public class Box3D implements IGeometricObject3D, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The box corresponding to the unit square, with bounds [0 1] in each direction
     * 
     * @since 0.9.1
     */
    public final static Box3D UNIT_SQUARE_BOX = new Box3D(0, 1, 0, 1, 0, 1);

    /**
     * The box corresponding to the the whole plane, with infinite bounds in each direction.
     * 
     * @since 0.9.1
     */
    public final static Box3D INFINITE_BOX = new Box3D(NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY);

    /**
     * The only box that xmin > xmax or ymin > ymax or zmin > zmax
     */
    public final static Box3D EMPTY_BOX = new Box3D(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, false);

    private final double xmin;
    private final double xmax;
    private final double ymin;
    private final double ymax;
    private final double zmin;
    private final double zmax;

    private Box3D(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax, boolean check) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.zmin = zmin;
        this.zmax = zmax;
        if (check && (xmin > xmax || ymin > ymax)) {
            throw new RuntimeException("xmin > xmax or ymin > ymax or zmin > zmax !");
        }
    }

    /**
     * Main constructor, given bounds for x coord, then bounds for y coord, then bounds for z coord.
     */
    public Box3D(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        this(xmin, xmax, ymin, ymax, zmin, zmax, true);
    }

    /**
     * Constructor from 2 points, giving extreme coordinates of the box.
     */
    public Box3D(Point3D p1, Point3D p2) {
        double x1 = p1.x();
        double y1 = p1.y();
        double z1 = p1.z();
        double x2 = p2.x();
        double y2 = p2.y();
        double z2 = p2.z();
        this.xmin = Math.min(x1, x2);
        this.xmax = Math.max(x1, x2);
        this.ymin = Math.min(y1, y2);
        this.ymax = Math.max(y1, y2);
        this.zmin = Math.min(z1, z2);
        this.zmax = Math.max(z1, z2);
    }

    /** Constructor from a point, a width and an height and a depth */
    public Box3D(Point3D point, double w, double h, double d) {
        this(point.x(), point.x() + w, point.y(), point.y() + h, point.z(), point.z() + d);
    }

    // ===================================================================
    // accessors to Box2D fields

    public double getMinX() {
        return xmin;
    }

    public double getMaxX() {
        return xmax;
    }

    public double getMinY() {
        return ymin;
    }

    public double getMaxY() {
        return ymax;
    }

    public double getMinZ() {
        return zmin;
    }

    public double getMaxZ() {
        return zmax;
    }

    /** Returns the width, i.e. the difference between the min and max x coord */
    public double getWidth() {
        return xmax - xmin;
    }

    /** Returns the height, i.e. the difference between the min and max y coord */
    public double getHeight() {
        return ymax - ymin;
    }

    /** Returns the depth, i.e. the difference between the min and max z coord */
    public double getDepth() {
        return zmax - zmin;
    }

    /**
     * Returns the Box2D which contains both this box and the specified box.
     * 
     * @param box
     *            the bounding box to include
     * @return this
     */
    public Box3D union(Box3D box) {
        double newxmin = Math.min(this.xmin, box.xmin);
        double newxmax = Math.max(this.xmax, box.xmax);
        double newymin = Math.min(this.ymin, box.ymin);
        double newymax = Math.max(this.ymax, box.ymax);
        double newzmin = Math.min(this.zmin, box.zmin);
        double newzmax = Math.max(this.zmax, box.zmax);
        return new Box3D(newxmin, newxmax, newymin, newymax, newzmin, newzmax);
    }

    /**
     * Returns the Box2D which is contained both by this box and by the specified box.
     * 
     * @param box
     *            the bounding box to include
     * @return this
     */
    public Box3D intersection(Box3D box) {
        double newxmin = Math.max(this.xmin, box.xmin);
        double newxmax = Math.min(this.xmax, box.xmax);
        double newymin = Math.max(this.ymin, box.ymin);
        double newymax = Math.min(this.ymax, box.ymax);
        double newzmin = Math.max(this.zmin, box.zmin);
        double newzmax = Math.min(this.zmax, box.zmax);
        return new Box3D(newxmin, newxmax, newymin, newymax, newzmin, newzmax);
    }

    /**
     * Tests if boxes are the same. Two boxes are the same if they have the same bounds, up to the specified threshold value.
     */
    @Override
    public boolean almostEquals(IGeometricObject3D obj, double eps) {
        if (this == obj)
            return true;

        // check class, and cast type
        if (!(obj instanceof Box3D))
            return false;
        Box3D box = (Box3D) obj;

        if (Math.abs(this.xmin - box.xmin) > eps)
            return false;
        if (Math.abs(this.xmax - box.xmax) > eps)
            return false;
        if (Math.abs(this.ymin - box.ymin) > eps)
            return false;
        if (Math.abs(this.ymax - box.ymax) > eps)
            return false;
        if (Math.abs(this.zmin - box.zmin) > eps)
            return false;
        if (Math.abs(this.zmax - box.zmax) > eps)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(xmax);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(xmin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ymax);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ymin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(zmax);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(zmin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Box3D other = (Box3D) obj;
        if (Double.doubleToLongBits(xmax) != Double.doubleToLongBits(other.xmax))
            return false;
        if (Double.doubleToLongBits(xmin) != Double.doubleToLongBits(other.xmin))
            return false;
        if (Double.doubleToLongBits(ymax) != Double.doubleToLongBits(other.ymax))
            return false;
        if (Double.doubleToLongBits(ymin) != Double.doubleToLongBits(other.ymin))
            return false;
        if (Double.doubleToLongBits(zmax) != Double.doubleToLongBits(other.zmax))
            return false;
        if (Double.doubleToLongBits(zmin) != Double.doubleToLongBits(other.zmin))
            return false;
        return true;
    }
}
