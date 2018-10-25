/**
 * File: 	CirculinearShape2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 10 mai 09
 */
package math.geom2d.circulinear;

import math.geom2d.IShape2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * CirculinearShape comprises shapes which can be described from lines (or linear shapes) and circles (or circle arcs). They allow several operations: the computation of buffer (set of points located less than a given distance), and transformation using a circle inversion.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearShape2D extends IShape2D {

    /**
     * Computes the buffer of the shape, formed by the set of points located at a distance from the shape that is lower or equal to d.
     * 
     * @param dist
     *            the maximal distance between a point of the buffer and the shape
     * @return the buffer of the shape
     */
    public ICirculinearDomain2D buffer(double dist);

    /**
     * Transforms the shape by a circle inversion. The result is still an instance a CirculinearShape2D.
     * 
     * @param inv
     *            the circle inversion
     * @return the transformed shape
     */
    public ICirculinearShape2D transform(CircleInversion2D inv);
}
