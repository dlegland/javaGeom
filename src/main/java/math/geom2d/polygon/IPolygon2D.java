/* File Polygon2D.java 
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

package math.geom2d.polygon;

// Imports
import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.ICirculinearDomain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Represent any class made of a finite set of simply connected edges. This include simple polygons, multiple polygons, or more specialized shapes like rectangles, squares... The boundary of a polygon is a collection of LinearRing2D.
 */
public interface IPolygon2D extends ICirculinearDomain2D {

    /** Returns the vertices (singular points) of the polygon */
    public Collection<Point2D> vertices();

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i
     *            index of the vertex, between 0 and the number of vertices
     */
    public Point2D vertex(int i);

    /**
     * Sets the position of the i-th vertex
     * 
     * @param i
     *            the vertex index
     * @param point
     *            the new position of the vertex
     * @throws UnsupportedOperationException
     *             if this polygon implementation does not support vertex modification
     */
    public void setVertex(int i, Point2D point);

    /**
     * Adds a vertex as last vertex of this polygon.
     * 
     * @param point
     *            the position of the new vertex
     * @throws UnsupportedOperationException
     *             if this polygon implementation does not support vertex modification
     */
    public void addVertex(Point2D point);

    /**
     * Inserts a vertex at the specified position.
     * 
     * @param index
     *            index at which the specified vertex is to be inserted
     * @param point
     *            the position of the new vertex
     * @throws UnsupportedOperationException
     *             if this polygon implementation does not support vertex modification
     */
    public void insertVertex(int index, Point2D point);

    /**
     * Removes the vertex at the given index.
     * 
     * @param index
     *            index of the vertex to remove
     * @throws UnsupportedOperationException
     *             if this polygon implementation does not support vertex modification
     */
    public void removeVertex(int index);

    /**
     * Returns the number of vertices of the polygon
     * 
     * @since 0.6.3
     */
    public int vertexNumber();

    /**
     * Returns the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point);

    /** Return the edges as line segments of the polygon */
    public Collection<? extends LineSegment2D> edges();

    /** Returns the number of edges of the polygon */
    public int edgeNumber();

    /**
     * Returns the centroid (center of mass) of the polygon.
     */
    public Point2D centroid();

    /**
     * Returns the signed area of the polygon.
     */
    public double area();

    // ===================================================================
    // methods inherited from the Domain2D interface

    /**
     * Overrides the definition of boundary() such that the boundary of a polygon is defined as a set of LinearRing2D.
     */
    @Override
    public CirculinearContourArray2D<? extends LinearRing2D> boundary();

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Domain2D#contours()
     */
    @Override
    public Collection<? extends LinearRing2D> contours();

    /**
     * Returns the complementary polygon.
     * 
     * @return the polygon complementary to this
     */
    @Override
    public IPolygon2D complement();

    // ===================================================================
    // methods inherited from the Shape2D interface

    /**
     * Returns the new Polygon created by an affine transform of this polygon.
     */
    @Override
    public IPolygon2D transform(AffineTransform2D trans);

    @Override
    public IPolygon2D clip(Box2D box);
}