/**
 * 
 */

package math.geom3d;

import java.io.Serializable;

import math.geom3d.point.Point3D;

/**
 * A 3-dimensional box, defined by its extent in each direction.
 * 
 * @author dlegland
 */
public class Box3D implements IGeometricObject3D, Serializable {
    private static final long serialVersionUID = 1L;

    private double xmin = 0;
    private double xmax = 0;
    private double ymin = 0;
    private double ymax = 0;
    private double zmin = 0;
    private double zmax = 0;

    /** Empty constructor (size and position zero) */
    public Box3D() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Main constructor, given bounds for x coord, bounds for y coord, and bounds for z coord. A check is performed to ensure first bound is lower than second bound.
     */
    public Box3D(double x0, double x1, double y0, double y1, double z0, double z1) {
        xmin = Math.min(x0, x1);
        xmax = Math.max(x0, x1);
        ymin = Math.min(y0, y1);
        ymax = Math.max(y0, y1);
        zmin = Math.min(z0, z1);
        zmax = Math.max(z0, z1);
    }

    /** Constructor from 2 points, giving extreme coordinates of the box. */
    public Box3D(Point3D p1, Point3D p2) {
        this(p1.x(), p2.x(), p1.y(), p2.y(), p1.z(), p2.z());
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
