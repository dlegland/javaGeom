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

import math.geom2d.domain.Domain2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A domain whose boundary is a circulinear curve.
 * @author dlegland
 *
 */
public interface CirculinearDomain2D extends CirculinearShape2D, Domain2D {

    // ===================================================================
    // redefines declaration of some parent interfaces

    public abstract CirculinearBoundary2D boundary();
    
	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#contours()
	 */
	public Collection<? extends CirculinearContour2D> contours();
    
    public CirculinearDomain2D complement();

    public CirculinearDomain2D transform(CircleInversion2D inv);
}
