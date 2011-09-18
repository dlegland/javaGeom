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

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearContourArray2D;
import math.geom2d.circulinear.CirculinearDomain2D;
import math.geom2d.line.LineSegment2D;

/**
 * Represent any class made of a finite set of simply connected edges. This
 * include simple polygons, multiple polygons, or more specialized shapes like
 * rectangles, squares...
 * The boundary of a polygon is a collection of LinearRing2D.
 */
public interface Polygon2D extends CirculinearDomain2D {

    /** Returns the vertices (singular points) of the polygon */
    public Collection<Point2D> getVertices();

    /**
     * Returns the i-th vertex of the polygon.
     * 
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D getVertex(int i);

    /**
     * Returns the number of vertices of the polygon
     * 
     * @since 0.6.3
     */
    public int getVertexNumber();

    /** Return the edges as line segments of the polygon */
    public Collection<? extends LineSegment2D> getEdges();

    /** Returns the number of edges of the polygon */
    public int getEdgeNumber();
    
    /**
     * Returns the set of rings comprising the boundary of this polygon.
     * @return the set of boundary rings.
     */
    public Collection<? extends LinearRing2D> getRings();

    public Point2D getCentroid();
    public double getArea();
    public double getSignedArea();
    
    // ===================================================================
    // methods inherited from the Domain2D interface

    public CirculinearContourArray2D<? extends LinearRing2D> 
    getBoundary();

    
    /**
     * Returns the complementary polygon.
     * 
     * @return the polygon complementary to this
     */
    public Polygon2D complement();
    
    // ===================================================================
    // methods inherited from the Shape2D interface

    /**
     * Returns the new Polygon created by an affine transform of this polygon.
     */
    public Polygon2D transform(AffineTransform2D trans);

    public Polygon2D clip(Box2D box);
}