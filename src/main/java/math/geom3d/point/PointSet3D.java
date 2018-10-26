/**
 * 
 */

package math.geom3d.point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.geom3d.Box3D;
import math.geom3d.IGeometricObject3D;
import math.geom3d.IShape3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * @author dlegland
 */
public class PointSet3D implements IShape3D, Iterable<Point3D>, Serializable {
    private static final long serialVersionUID = 1L;

    protected Collection<Point3D> points = new ArrayList<>();

    public PointSet3D() {
    }

    /**
     * Creates a new point set and allocate memory for storing the points.
     * 
     * @param n
     *            the number of points to store
     */
    public PointSet3D(int n) {
        this.points = new ArrayList<>(n);
    }

    /**
     * Instances of Point3D are directly added, other Point are converted to Point3D with the same location.
     */
    public PointSet3D(Point3D[] points) {
        for (Point3D element : points)
            this.points.add(element);
    }

    /**
     * Points must be a collection of java.awt.Point. Instances of Point3D are directly added, other Point are converted to Point3D with the same location.
     * 
     * @param points
     */
    public PointSet3D(Collection<? extends Point3D> points) {
        for (Point3D point : points) {
            this.points.add(point);
        }
    }

    /**
     * Adds a new point to the set of point. If point is not an instance of Point3D, a Point3D with same location is added instead of point.
     * 
     * @param point
     */
    public void addPoint(Point3D point) {
        this.points.add(point);
    }

    /**
     * Add a series of points
     * 
     * @param points
     *            an array of points
     */
    public void addPoints(Point3D[] points) {
        for (Point3D element : points)
            this.addPoint(element);
    }

    public void addPoints(Collection<Point3D> points) {
        this.points.addAll(points);
    }

    /**
     * Returns an iterator on the internal point collection.
     * 
     * @return the collection of points
     */
    public Iterator<Point3D> getPoints() {
        return points.iterator();
    }

    /**
     * Removes all points of the set.
     */
    public void clearPoints() {
        this.points.clear();
    }

    /**
     * Returns the number of points in the set.
     * 
     * @return the number of points
     */
    public int pointNumber() {
        return points.size();
    }

    // ===================================================================
    // methods implementing the Shape3D interface

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#clip(math.geom3d.Box3D)
     */
    @Override
    public IShape3D clip(Box3D box) {
        PointSet3D res = new PointSet3D(this.points.size());
        IShape3D clipped;
        for (Point3D point : points) {
            clipped = point.clip(box);
            if (clipped != null)
                res.addPoint(point);
        }
        return res;
    }

    @Override
    public Box3D boundingBox() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double zmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;
        double zmax = Double.MIN_VALUE;

        for (Point3D point : points) {
            xmin = Math.min(xmin, point.x());
            ymin = Math.min(ymin, point.y());
            zmin = Math.min(zmin, point.z());
            xmax = Math.max(xmax, point.x());
            ymax = Math.max(ymax, point.y());
            zmax = Math.max(zmax, point.z());
        }
        return new Box3D(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#getDistance(math.geom3d.Point3D)
     */
    @Override
    public double distance(Point3D p) {
        if (points.isEmpty())
            return Double.POSITIVE_INFINITY;
        double dist = Double.POSITIVE_INFINITY;
        for (Point3D point : points)
            dist = Math.min(dist, point.distance(p));
        return dist;
    }

    @Override
    public boolean contains(Point3D point) {
        for (Point3D p : points)
            if (point.distance(p) < IShape3D.ACCURACY)
                return true;
        return false;
    }

    @Override
    public boolean isEmpty() {
        return points.size() == 0;
    }

    @Override
    public boolean isBounded() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom3d.Shape3D#transform(math.geom3d.AffineTransform3D)
     */
    @Override
    public IShape3D transform(AffineTransform3D trans) {
        PointSet3D res = new PointSet3D();
        for (Point3D point : points)
            res.addPoint(point.transform(trans));
        return res;
    }

    // ===================================================================
    // methods implementing the Iterable interface

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Point3D> iterator() {
        return points.iterator();
    }

    @Override
    public boolean almostEquals(IGeometricObject3D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof PointSet3D))
            return false;

        PointSet3D set = (PointSet3D) obj;
        if (this.points.size() != set.points.size())
            return false;

        Iterator<Point3D> iter = set.iterator();
        for (Point3D point : points) {
            if (!point.almostEquals(iter.next(), eps))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((points == null) ? 0 : points.hashCode());
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
        PointSet3D other = (PointSet3D) obj;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        return true;
    }
}
