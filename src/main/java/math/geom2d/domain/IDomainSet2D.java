/**
 * File: 	DomainSet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d.domain;

import math.geom2d.IShapeSet2D;

/**
 * General interface for a set of domains, that is itself a domain.
 * 
 * @author dlegland
 *
 */
public interface IDomainSet2D<T extends IDomain2D> extends IShapeSet2D<T>, IDomain2D {
    // nothing
}
