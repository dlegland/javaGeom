/**
 * File: 	ConvexHull2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 18 janv. 09
 */
package math.geom2d.polygon.convhull;

import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;


/**
 * Generic interface for classes that allow computing the convex hull of a
 * set of points.
 * @author dlegland
 *
 */
public interface ConvexHull2D {

	/**
	 * Computes the convex hull of the given collection of points.
	 * @param points a set of points
	 * @return the convex polygon corresponding to the convex hull
	 */
    public abstract Polygon2D convexHull(Collection<? extends Point2D> points);
}
