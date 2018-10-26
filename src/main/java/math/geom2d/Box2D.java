/* File Box2D.java 
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
 * 
 * Created on 05 mar. 2007
 */

// package

package math.geom2d;

// Imports
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.IBoundary2D;
import math.geom2d.exception.UnboundedBox2DException;
import math.geom2d.line.AbstractLine2D;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.line.LineArc2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.point.Point2D;
import math.geom2d.polygon.IPolygon2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * This class defines bounds of a shape. It stores limits in each direction: <code>x</code> and <code>y</code>. It also provides methods for clipping others shapes, depending on their type.
 */
public final class Box2D implements IGeometricObject2D, Serializable {
    private static final long serialVersionUID = 1L;

    public final static Box2D ZERO_BOX = new Box2D(0, 0, 0, 0);

    /**
     * The box corresponding to the unit square, with bounds [0 1] in each direction
     * 
     * @since 0.9.1
     */
    public final static Box2D UNIT_SQUARE_BOX = new Box2D(0, 1, 0, 1);

    /**
     * The box corresponding to the the whole plane, with infinite bounds in each direction.
     * 
     * @since 0.9.1
     */
    public final static Box2D INFINITE_BOX = new Box2D(NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY);

    // ===================================================================
    // class variables

    private final double xmin;
    private final double xmax;
    private final double ymin;
    private final double ymax;

    /**
     * Main constructor, given bounds for x coord, then bounds for y coord.
     */
    public Box2D(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    /**
     * Constructor from 2 points, giving extreme coordinates of the box.
     */
    public Box2D(Point2D p1, Point2D p2) {
        double x1 = p1.x();
        double y1 = p1.y();
        double x2 = p2.x();
        double y2 = p2.y();
        this.xmin = Math.min(x1, x2);
        this.xmax = Math.max(x1, x2);
        this.ymin = Math.min(y1, y2);
        this.ymax = Math.max(y1, y2);
    }

    /** Constructor from a point, a width and an height */
    public Box2D(Point2D point, double w, double h) {
        this(point.x(), point.x() + w, point.y(), point.y() + h);
    }

    // ===================================================================
    // accessors to Box2D fields

    public double getMinX() {
        return xmin;
    }

    public double getMinY() {
        return ymin;
    }

    public double getMaxX() {
        return xmax;
    }

    public double getMaxY() {
        return ymax;
    }

    public double getWidth() {
        return xmax - xmin;
    }

    public double getHeight() {
        return ymax - ymin;
    }

    /** Returns true if all bounds are finite. */
    public boolean isBounded() {
        if (isInfinite(xmin))
            return false;
        if (isInfinite(ymin))
            return false;
        if (isInfinite(xmax))
            return false;
        if (isInfinite(ymax))
            return false;
        return true;
    }

    // ===================================================================
    // tests of inclusion

    /**
     * Checks if this box contains the given point.
     */
    public boolean contains(Point2D point) {
        double x = point.x();
        double y = point.y();
        if (x < xmin)
            return false;
        if (y < ymin)
            return false;
        if (x > xmax)
            return false;
        if (y > ymax)
            return false;
        return true;
    }

    /**
     * Checks if this box contains the point defined by the given coordinates.
     */
    public boolean contains(double x, double y) {
        if (x < xmin)
            return false;
        if (y < ymin)
            return false;
        if (x > xmax)
            return false;
        if (y > ymax)
            return false;
        return true;
    }

    /**
     * Tests if the specified Shape is totally contained in this Box2D. Note that the test is performed on the bounding box of the shape, then for rotated rectangles, this method can return false with a shape totally contained in the rectangle. The problem does not exist for horizontal rectangle, since edges of rectangle and bounding box are parallel.
     */
    public boolean containsBounds(IShape2D shape) {
        if (!shape.isBounded())
            return false;
        for (Point2D point : shape.boundingBox().vertices())
            if (!contains(point))
                return false;

        return true;
    }

    // ===================================================================
    // information on the boundary

    /**
     * Returns a set of straight of lines defining half-planes, that all contain the box. If the box is bounded, the number of straight lines is 4, otherwise it can be less.
     * 
     * @return a set of straight lines
     */
    public Collection<StraightLine2D> clippingLines() {
        ArrayList<StraightLine2D> lines = new ArrayList<>(4);

        if (isFinite(ymin))
            lines.add(new StraightLine2D(0, ymin, 1, 0));
        if (isFinite(xmax))
            lines.add(new StraightLine2D(xmax, 0, 0, 1));
        if (isFinite(ymax))
            lines.add(new StraightLine2D(0, ymax, -1, 0));
        if (isFinite(xmin))
            lines.add(new StraightLine2D(xmin, 0, 0, -1));
        return lines;
    }

    /**
     * Returns the set of linear shapes that constitutes the boundary of this box.
     */
    public Collection<ILinearShape2D> edges() {
        ArrayList<ILinearShape2D> edges = new ArrayList<>(4);

        if (isBounded()) {
            edges.add(new LineSegment2D(xmin, ymin, xmax, ymin));
            edges.add(new LineSegment2D(xmax, ymin, xmax, ymax));
            edges.add(new LineSegment2D(xmax, ymax, xmin, ymax));
            edges.add(new LineSegment2D(xmin, ymax, xmin, ymin));
            return edges;
        }

        if (!isInfinite(ymin)) {
            if (isInfinite(xmin) && isInfinite(xmax))
                edges.add(new StraightLine2D(0, ymin, 1, 0));
            else if (!isInfinite(xmin) && !isInfinite(xmax))
                edges.add(new LineSegment2D(xmin, ymin, xmax, ymin));
            else
                edges.add(new LineArc2D(0, ymin, 1, 0, xmin, xmax));
        }

        if (!isInfinite(xmax)) {
            if (isInfinite(ymin) && isInfinite(ymax))
                edges.add(new StraightLine2D(xmax, 0, 0, 1));
            else if (!isInfinite(ymin) && !isInfinite(ymax))
                edges.add(new LineSegment2D(xmax, ymin, xmax, ymax));
            else
                edges.add(new LineArc2D(xmax, 0, 0, 1, ymin, ymax));
        }

        if (!isInfinite(ymax)) {
            if (isInfinite(xmin) && isInfinite(xmax))
                edges.add(new StraightLine2D(0, ymax, 1, 0));
            else if (!isInfinite(xmin) && !isInfinite(xmax))
                edges.add(new LineSegment2D(xmax, ymax, xmin, ymax));
            else
                edges.add(new LineArc2D(0, ymin, 1, 0, xmin, xmax).reverse());
        }

        if (!isInfinite(xmin)) {
            if (isInfinite(ymin) && isInfinite(ymax))
                edges.add(new StraightLine2D(xmin, 0, 0, -1));
            else if (!isInfinite(ymin) && !isInfinite(ymax))
                edges.add(new LineSegment2D(xmin, ymax, xmin, ymin));
            else
                edges.add(new LineArc2D(xmin, 0, 0, 1, ymin, ymax).reverse());
        }

        return edges;
    }

    /**
     * Returns the boundary of this box. The boundary can be bounded, in the case of a bounded box. It is unbounded if at least one bound of the box is infinite. If both x bounds or both y-bounds are infinite, the boundary is constituted from 2 straight lines.
     * 
     * @return the box boundary
     */
    public IBoundary2D boundary() {

        // First case of totally bounded box
        if (isBounded()) {
            Point2D pts[] = new Point2D[4];
            pts[0] = new Point2D(xmin, ymin);
            pts[1] = new Point2D(xmax, ymin);
            pts[2] = new Point2D(xmax, ymax);
            pts[3] = new Point2D(xmin, ymax);
            return new LinearRing2D(pts);
        }

        // extract boolean info on "boundedness" in each direction
        boolean bx0 = !isInfinite(xmin);
        boolean bx1 = !isInfinite(xmax);
        boolean by0 = !isInfinite(ymin);
        boolean by1 = !isInfinite(ymax);

        // case of boxes unbounded in both x directions
        if (!bx0 && !bx1) {
            if (!by0 && !by1)
                return new ContourArray2D<StraightLine2D>();
            if (by0 && !by1)
                return new StraightLine2D(0, ymin, 1, 0);
            if (!by0 && by1)
                return new StraightLine2D(0, ymax, -1, 0);
            return new ContourArray2D<>(new StraightLine2D[] { new StraightLine2D(0, ymin, 1, 0), new StraightLine2D(0, ymax, -1, 0) });
        }

        // case of boxes unbounded in both y directions
        if (!by0 && !by1) {
            if (!bx0 && !bx1)
                return new ContourArray2D<StraightLine2D>();
            if (bx0 && !bx1)
                return new StraightLine2D(xmin, 0, 0, -1);
            if (!bx0 && bx1)
                return new StraightLine2D(xmax, 0, 0, 1);
            return new ContourArray2D<>(new StraightLine2D[] { new StraightLine2D(xmin, 0, 0, -1), new StraightLine2D(xmax, 0, 0, 1) });
        }

        // "corner boxes"

        if (bx0 && by0) // lower left corner
            return new BoundaryPolyCurve2D<>(new LineArc2D[] { new LineArc2D(xmin, ymin, 0, -1, NEGATIVE_INFINITY, 0), new LineArc2D(xmin, ymin, 1, 0, 0, POSITIVE_INFINITY) });

        if (bx1 && by0) // lower right corner
            return new BoundaryPolyCurve2D<>(new LineArc2D[] { new LineArc2D(xmax, ymin, 1, 0, NEGATIVE_INFINITY, 0), new LineArc2D(xmax, ymin, 0, 1, 0, POSITIVE_INFINITY) });

        if (bx1 && by1) // upper right corner
            return new BoundaryPolyCurve2D<>(new LineArc2D[] { new LineArc2D(xmax, ymax, 0, 1, NEGATIVE_INFINITY, 0), new LineArc2D(xmax, ymax, -1, 0, 0, POSITIVE_INFINITY) });

        if (bx0 && by1) // upper left corner
            return new BoundaryPolyCurve2D<>(new LineArc2D[] { new LineArc2D(xmin, ymax, -1, 0, NEGATIVE_INFINITY, 0), new LineArc2D(xmin, ymax, 0, -1, 0, POSITIVE_INFINITY) });

        // Remains only 4 cases: boxes unbounded in only one direction

        if (bx0)
            return new BoundaryPolyCurve2D<>(new AbstractLine2D[] { new LineArc2D(xmin, ymax, -1, 0, NEGATIVE_INFINITY, 0), new LineSegment2D(xmin, ymax, xmin, ymin), new LineArc2D(xmin, ymin, 1, 0, 0, POSITIVE_INFINITY) });

        if (bx1)
            return new BoundaryPolyCurve2D<>(new AbstractLine2D[] { new LineArc2D(xmax, ymin, 1, 0, NEGATIVE_INFINITY, 0), new LineSegment2D(xmax, ymin, xmax, ymax), new LineArc2D(xmax, ymax, -1, 0, 0, POSITIVE_INFINITY) });

        if (by0)
            return new BoundaryPolyCurve2D<>(new AbstractLine2D[] { new LineArc2D(xmin, ymin, 0, -1, NEGATIVE_INFINITY, 0), new LineSegment2D(xmin, ymin, xmax, ymin), new LineArc2D(xmax, ymin, 0, 1, 0, POSITIVE_INFINITY) });

        if (by1)
            return new BoundaryPolyCurve2D<>(new AbstractLine2D[] { new LineArc2D(xmax, ymax, 0, 1, NEGATIVE_INFINITY, 0), new LineSegment2D(xmax, ymax, xmin, ymax), new LineArc2D(xmin, ymax, 0, -1, 0, POSITIVE_INFINITY) });

        return null;
    }

    public Collection<Point2D> vertices() {
        ArrayList<Point2D> points = new ArrayList<>(4);
        boolean bx0 = isFinite(xmin);
        boolean bx1 = isFinite(xmax);
        boolean by0 = isFinite(ymin);
        boolean by1 = isFinite(ymax);
        if (bx0 && by0)
            points.add(new Point2D(xmin, ymin));
        if (bx1 && by0)
            points.add(new Point2D(xmax, ymin));
        if (bx0 && by1)
            points.add(new Point2D(xmin, ymax));
        if (bx1 && by1)
            points.add(new Point2D(xmax, ymax));
        return points;
    }

    private final static boolean isFinite(double value) {
        if (isInfinite(value))
            return false;
        if (isNaN(value))
            return false;
        return true;
    }

    /** Returns the number of vertices of the box. */
    public int vertexNumber() {
        return this.vertices().size();
    }

    // ===================================================================
    // combination of box with other boxes

    /**
     * Returns the Box2D which contains both this box and the specified box.
     * 
     * @param box
     *            the bounding box to include
     * @return a new Box2D
     */
    public Box2D union(Box2D box) {
        double newxmin = Math.min(this.xmin, box.xmin);
        double newxmax = Math.max(this.xmax, box.xmax);
        double newymin = Math.min(this.ymin, box.ymin);
        double newymax = Math.max(this.ymax, box.ymax);
        return new Box2D(newxmin, newxmax, newymin, newymax);
    }

    /**
     * Returns the Box2D which is contained both by this box and by the specified box.
     * 
     * @param box
     *            the bounding box to include
     * @return a new Box2D
     */
    public Box2D intersection(Box2D box) {
        double newxmin = Math.max(this.xmin, box.xmin);
        double newxmax = Math.min(this.xmax, box.xmax);
        double newymin = Math.max(this.ymin, box.ymin);
        double newymax = Math.min(this.ymax, box.ymax);
        return new Box2D(newxmin, newxmax, newymin, newymax);
    }

    /**
     * Changes the bounds of this box to also include bounds of the argument.
     * 
     * @param box
     *            the bounding box to include
     * @return this
     */
    public Box2D merge(Box2D box) {
        double newxmin = Math.min(this.xmin, box.xmin);
        double newxmax = Math.max(this.xmax, box.xmax);
        double newymin = Math.min(this.ymin, box.ymin);
        double newymax = Math.max(this.ymax, box.ymax);
        return new Box2D(newxmin, newxmax, newymin, newymax);
    }

    /**
     * Clip this bounding box such that after clipping, it is totally contained in the given box.
     * 
     * @return the clipped box
     */
    public Box2D clip(Box2D box) {
        double newxmin = Math.max(this.xmin, box.xmin);
        double newxmax = Math.min(this.xmax, box.xmax);
        double newymin = Math.max(this.ymin, box.ymin);
        double newymax = Math.min(this.ymax, box.ymax);
        return new Box2D(newxmin, newxmax, newymin, newymax);
    }

    /**
     * Returns the new box created by an affine transform of this box. If the box is unbounded, return an infinite box in all directions.
     */
    public Box2D transform(AffineTransform2D trans) {
        // special case of unbounded box
        if (!this.isBounded())
            return Box2D.INFINITE_BOX;

        // initialize with extreme values
        double newxmin = POSITIVE_INFINITY;
        double newxmax = NEGATIVE_INFINITY;
        double newymin = POSITIVE_INFINITY;
        double newymax = NEGATIVE_INFINITY;

        // update bounds with coordinates of transformed box vertices
        for (Point2D point : this.vertices()) {
            point = point.transform(trans);
            newxmin = Math.min(newxmin, point.x());
            newymin = Math.min(newymin, point.y());
            newxmax = Math.max(newxmax, point.x());
            newymax = Math.max(newymax, point.y());
        }

        // create the resulting box
        return new Box2D(newxmin, newxmax, newymin, newymax);
    }

    /**
     * Converts to a rectangle.
     * 
     * @return an instance of Polygon2D
     */
    public IPolygon2D asRectangle() {
        return Polygons2D.createRectangle(xmin, ymin, xmax, ymax);
    }

    /**
     * Draws the boundary of the box on the specified graphics.
     * 
     * @param g2
     *            the instance of graphics to draw in.
     * @throws UnboundedBox2DException
     *             if the box is unbounded
     */
    public void draw(Graphics2D g2) {
        if (!isBounded())
            throw new UnboundedBox2DException(this);
        this.boundary().draw(g2);
    }

    /**
     * Fills the content of the box on the specified graphics.
     * 
     * @param g2
     *            the instance of graphics to draw in.
     * @throws UnboundedBox2DException
     *             if the box is unbounded
     */
    public void fill(Graphics2D g2) {
        if (!isBounded())
            throw new UnboundedBox2DException(this);
        this.boundary().fill(g2);
    }

    /**
     * Tests if boxes are the same. Two boxes are the same if they have the same bounds, up to the specified threshold value.
     */
    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        // check class, and cast type
        if (!(obj instanceof Box2D))
            return false;
        Box2D box = (Box2D) obj;

        if (Math.abs(this.xmin - box.xmin) > eps)
            return false;
        if (Math.abs(this.xmax - box.xmax) > eps)
            return false;
        if (Math.abs(this.ymin - box.ymin) > eps)
            return false;
        if (Math.abs(this.ymax - box.ymax) > eps)
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
        Box2D other = (Box2D) obj;
        if (Double.doubleToLongBits(xmax) != Double.doubleToLongBits(other.xmax))
            return false;
        if (Double.doubleToLongBits(xmin) != Double.doubleToLongBits(other.xmin))
            return false;
        if (Double.doubleToLongBits(ymax) != Double.doubleToLongBits(other.ymax))
            return false;
        if (Double.doubleToLongBits(ymin) != Double.doubleToLongBits(other.ymin))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Box2D [xmin=" + xmin + ", xmax=" + xmax + ", ymin=" + ymin + ", ymax=" + ymax + "]";
    }
}