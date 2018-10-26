/**
 * File: 	CirculinearDomain2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.domain.IDomain2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A domain whose boundary is a circulinear curve.
 * 
 * @author dlegland
 *
 */
public interface ICirculinearDomain2D extends ICirculinearShape2D, IDomain2D {

    // ===================================================================
    // redefines declaration of some parent interfaces

    @Override
    public abstract ICirculinearBoundary2D boundary();

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Domain2D#contours()
     */
    @Override
    public Collection<? extends ICirculinearContour2D> contours();

    @Override
    public ICirculinearDomain2D complement();

    @Override
    public ICirculinearDomain2D transform(CircleInversion2D inv);
}
