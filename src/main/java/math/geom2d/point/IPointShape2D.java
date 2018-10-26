/**
 * File: 	PointShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 6 f�vr. 09
 */
package math.geom2d.point;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.circulinear.ICirculinearShape2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Interface for shapes composed of a finite set of points. Single points should also implements this interface. Implementations of this interface can contains duplicate points.
 * 
 * @author dlegland
 *
 */
public interface IPointShape2D extends ICirculinearShape2D, Iterable<Point2D> {

    /**
     * Returns the points in the shape as a collection.
     * 
     * @return the collection of points
     */
    Collection<Point2D> points();

    /**
     * Returns the number of points in the set.
     * 
     * @return the number of points
     */
    int size();

    /**
     * Transforms the point shape by an affine transform. The result is an instance of PointShape2D.
     */
    @Override
    IPointShape2D transform(AffineTransform2D trans);

    /**
     * When a PointShape2D is clipped, the result is still a PointShape2D.
     */
    @Override
    IPointShape2D clip(Box2D box);
}
