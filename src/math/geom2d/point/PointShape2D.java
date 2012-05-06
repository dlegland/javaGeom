/**
 * File: 	PointShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 6 févr. 09
 */
package math.geom2d.point;

import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.circulinear.CirculinearShape2D;


/**
 * Interface for shapes composed of a finite set of points. Single points
 * should also implements this interface. Implementations of this interface
 * can contains duplicate points.
 * @author dlegland
 *
 */
public interface PointShape2D extends CirculinearShape2D, Iterable<Point2D> {

    /**
     * Returns the points in the shape as a collection.
     * 
     * @return the collection of points
     */
    public Collection<Point2D> points();

    /**
     * Returns the number of points in the set.
     * 
     * @return the number of points
     */
    public int size();
    
    /**
     * Transforms the point shape by an affine transform. 
     * The result is an instance of PointShape2D.
     */
    public abstract PointShape2D transform(AffineTransform2D trans);

    /**
     * When a PointShape2D is clipped, the result is still a PointShape2D.
     */
    public abstract PointShape2D clip(Box2D box);
}
