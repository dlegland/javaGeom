/**
 * File: 	DomainSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d.domain;

import math.geom2d.ShapeSet2D;


/**
 * @author dlegland
 *
 */
public interface DomainSet2D<T extends Domain2D> 
extends ShapeSet2D<T>, Domain2D {

}
