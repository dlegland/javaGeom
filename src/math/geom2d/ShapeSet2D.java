/**
 * File: 	ShapeSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d;


/**
 * @author dlegland
 *
 */
public interface ShapeSet2D<T extends Shape2D> extends Shape2D, Iterable<T> {

}
