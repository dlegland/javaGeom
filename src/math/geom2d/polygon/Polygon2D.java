/* File AbstractPolygon2D.java 
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
import java.util.*;

import math.geom2d.Point2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Represent any class made of a finite set of simply connected edges. 
 * This include simple polygons, multiple polygons, or more specialized
 * shapes like rectangles, squares...
 */
public interface Polygon2D extends Domain2D{


	/** Return the vertices (singular points) of the polygon*/
	public abstract Collection<Point2D> getVertices();
	
	/** Return the number of vertices of the polygon*/
	public abstract int getVerticesNumber();
	
	/** Return the edges as line segments of the polygon*/
	public abstract Collection<LineSegment2D> getEdges();

	// ===================================================================
	// general methods

	/** 
	 * Return the new Polygon created by an affine transform of this polygon.
	 */
	public abstract Polygon2D transform(AffineTransform2D trans);

}