/**
 * File: 	GenericCirculinearDomain2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.util.Collection;

import math.geom2d.domain.GenericDomain2D;
import math.geom2d.transform.CircleInversion2D;


/**
 * A specialization of GenericDomain2D, whose boundary is constrained to be
 * circulinear.
 * @author dlegland
 *
 */
public class GenericCirculinearDomain2D extends GenericDomain2D 
implements CirculinearDomain2D {

    // ===================================================================
    // Static factories
	
	public static GenericCirculinearDomain2D create(CirculinearBoundary2D boundary) {
		return new GenericCirculinearDomain2D(boundary);
	}
	
    // ===================================================================
    // constructors

	public GenericCirculinearDomain2D(CirculinearBoundary2D boundary) {
		super(boundary);
	}
	
	@Override
	public CirculinearBoundary2D boundary() {
		return (CirculinearBoundary2D) boundary;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#contours()
	 */
	public Collection<? extends CirculinearContour2D> contours() {
		return ((CirculinearBoundary2D) this.boundary).continuousCurves();
	}

	@Override
    public CirculinearDomain2D complement() {
        return new GenericCirculinearDomain2D(
        		(CirculinearBoundary2D) boundary.reverse());
    }

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
	 */
	public CirculinearDomain2D buffer(double dist) {
		
		CirculinearBoundary2D newBoundary = 
			((CirculinearBoundary2D) this.boundary).parallel(dist);
		return new GenericCirculinearDomain2D(
				CirculinearContourArray2D.create(
						CirculinearCurves2D.splitIntersectingContours(
								newBoundary.continuousCurves())));
	}

	/* (non-Javadoc)
	 * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
	 */
	public CirculinearDomain2D transform(CircleInversion2D inv) {
		// class cast
		CirculinearBoundary2D boundary2 = (CirculinearBoundary2D) boundary;
		
		// transform and reverse
		boundary2 = boundary2.transform(inv).reverse();
		
		// create the result domain
		return new GenericCirculinearDomain2D(boundary2);
	}
	
	// ===================================================================
	// methods overriding the Object class

    @Override
    public String toString() {
    	return "GenericCirculinearDomain2D(boundary=" + boundary + ")";
    }

}
