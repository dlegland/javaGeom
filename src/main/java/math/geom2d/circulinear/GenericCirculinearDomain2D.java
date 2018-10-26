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
 * A specialization of GenericDomain2D, whose boundary is constrained to be circulinear.
 * 
 * @author dlegland
 *
 */
public class GenericCirculinearDomain2D extends GenericDomain2D implements ICirculinearDomain2D {
    private static final long serialVersionUID = 1L;

    public static GenericCirculinearDomain2D create(ICirculinearBoundary2D boundary) {
        return new GenericCirculinearDomain2D(boundary);
    }

    // ===================================================================
    // constructors

    public GenericCirculinearDomain2D(ICirculinearBoundary2D boundary) {
        super(boundary);
    }

    @Override
    public ICirculinearBoundary2D boundary() {
        return (ICirculinearBoundary2D) boundary;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Domain2D#contours()
     */
    @Override
    public Collection<? extends ICirculinearContour2D> contours() {
        return ((ICirculinearBoundary2D) this.boundary).continuousCurves();
    }

    @Override
    public ICirculinearDomain2D complement() {
        return new GenericCirculinearDomain2D((ICirculinearBoundary2D) boundary.reverse());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearShape2D#buffer(double)
     */
    @Override
    public ICirculinearDomain2D buffer(double dist) {

        ICirculinearBoundary2D newBoundary = ((ICirculinearBoundary2D) this.boundary).parallel(dist);
        return new GenericCirculinearDomain2D(CirculinearContourArray2D.create(CirculinearCurves2D.splitIntersectingContours(newBoundary.continuousCurves()).toArray(new ICirculinearContour2D[0])));
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.CirculinearDomain2D#transform(math.geom2d.transform.CircleInversion2D)
     */
    @Override
    public ICirculinearDomain2D transform(CircleInversion2D inv) {
        // class cast
        ICirculinearBoundary2D boundary2 = (ICirculinearBoundary2D) boundary;

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
