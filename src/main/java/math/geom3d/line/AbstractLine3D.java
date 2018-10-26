/* File AbstractLine2D.java 
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

package math.geom3d.line;

import java.io.Serializable;

import math.geom3d.Box3D;
import math.geom3d.IShape3D;
import math.geom3d.Vector3D;
import math.geom3d.curve.IContinuousCurve3D;
import math.geom3d.point.Point3D;
import math.geom3d.transform.AffineTransform3D;

/**
 * <p>
 * Base class for straight curves, such as straight lines, rays, or edges.
 * </p>
 * <p>
 * Internal representation of straight objects is parametric: (x0, y0) is a point in the object, and (dx, dy) is a direction vector of the line.
 * </p>
 * <p>
 * If the line is defined by two point, we can set (x0,y0) to the first point, and (dx,dy) to the vector (p1, p2).
 * </p>
 * <p>
 * Then, coordinates for a point (x,y) such as x=x0+t*dx and y=y0+t=dy, t between 0 and 1 give a point inside p1 and p2, t<0 give a point 'before' p1, and t>1 give a point 'after' p2, so it is convenient to easily manage edges, rays and straight lines.
 * <p>
 */
public abstract class AbstractLine3D implements IContinuousCurve3D, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Coordinates of starting point of the line
     */
    private final Point3D point;

    /**
     * Direction vector of the line. dx and dy should not be both zero.
     */
    private final Vector3D vector;

    protected AbstractLine3D(Point3D point, Vector3D vector, boolean canBeEmpty) {
        this.point = point;
        this.vector = vector;

        // enforce condition on direction vector
        if (!canBeEmpty && vector.norm() < IShape3D.ACCURACY) {
            throw new IllegalArgumentException("Straight lines can not have direction vector with zero norm");
        }
    }

    protected AbstractLine3D(Point3D point1, Point3D point2, boolean canBeEmpty) {
        this(point1, new Vector3D(point1, point2), canBeEmpty);
    }

    protected AbstractLine3D(double x0, double y0, double z0, double dx, double dy, double dz, boolean canBeEmpty) {
        this(new Point3D(x0, y0, z0), new Vector3D(dx, dy, dz), canBeEmpty);
    }

    /**
     * Tests if the two linear objects are parallel.
     */
    public static boolean isParallel(AbstractLine3D line1, AbstractLine3D line2) {
        return (Math.abs(line1.dx() * line2.dy() - line1.dy() * line2.dx()) < IShape3D.ACCURACY) && (Math.abs(line1.dx() * line2.dz() - line1.dz() * line2.dx()) < IShape3D.ACCURACY);
    }

    public double x() {
        return point.x();
    }

    public double y() {
        return point.y();
    }

    public double z() {
        return point.z();
    }

    public double dx() {
        return vector.x();
    }

    public double dy() {
        return vector.y();
    }

    public double dz() {
        return vector.z();
    }

    /**
     * Creates a straight line parallel to this object, and going through the given point.
     * 
     * @param point
     *            the point to go through
     * @return the parallel through the point
     */
    public StraightLine3D parallel(Point3D point) {
        return new StraightLine3D(point, vector);
    }

    /**
     * Returns the origin point of this linear shape.
     */
    public Point3D origin() {
        return point;
    }

    /**
     * Returns the direction vector of this linear shape.
     */
    public Vector3D direction() {
        return vector;
    }

    public StraightLine3D supportingLine() {
        return new StraightLine3D(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.curve.SmoothCurve2D#tangent(double)
     */
    public Vector3D tangent(double t) {
        return vector;
    }

    /**
     * Returns 0 as for every straight object.
     */
    public double curvature(double t) {
        return 0.0;
    }

    /**
     * Returns a new AbstractLine2D, which is the portion of this AbstractLine2D delimited by parameters t0 and t1. Casts the result to StraightLine2D, Ray2D or LineSegment2D when appropriate.
     */
    @Override
    public AbstractLine3D subCurve(double t0, double t1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns false, unless both dx and dy equal 0.
     */
    @Override
    public boolean isEmpty() {
        return Math.hypot(Math.hypot(dx(), dy()), dz()) < IShape3D.ACCURACY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.Shape2D#transform(AffineTransform2D)
     */
    @Override
    public abstract AbstractLine3D transform(AffineTransform3D transform);

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((point == null) ? 0 : point.hashCode());
        result = prime * result + ((vector == null) ? 0 : vector.hashCode());
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
        AbstractLine3D other = (AbstractLine3D) obj;
        if (point == null) {
            if (other.point != null)
                return false;
        } else if (!point.equals(other.point))
            return false;
        if (vector == null) {
            if (other.vector != null)
                return false;
        } else if (!vector.equals(other.vector))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AbstractLine2D [point=" + point.toString() + ", vector=" + vector.toString() + "]";
    }
}
