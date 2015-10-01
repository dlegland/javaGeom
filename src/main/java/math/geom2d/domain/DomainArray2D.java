/**
 * File: 	DomainArray2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 ao�t 10
 */
package math.geom2d.domain;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.ShapeArray2D;
import math.geom2d.UnboundedShape2DException;
import math.geom2d.polygon.*;


/**
 * An array of domains. Note that this class if different from a generic domain whose boundary
 * is a set of contours. In the latter case, the shape is itself a domain, not in the former.
 * @author dlegland
 *
 */
public class DomainArray2D<T extends Domain2D> extends ShapeArray2D<T> 
implements DomainSet2D<T> {

	/*public static <D extends Domain2D> DomainArray2D<D> create(Collection<D> array) {
		return new DomainArray2D<D>(array);
	}*/
	
	@SafeVarargs
	public static <D extends Domain2D> DomainArray2D<D> create(D... array) {
		return new DomainArray2D<D>(array);
	}
	
	/**
	 * 
	 */
	public DomainArray2D() {
	}

	/**
	 * @param n
	 */
	public DomainArray2D(int n) {
		super(n);
	}

	/**
	 * @param domains the initial set of domains that constitutes this array.
	 */
	public DomainArray2D(Collection<T> domains) {
		super(domains);
	}

	/**
	 * @param domains the initial set of domains that constitutes this array.
	 */
	@SafeVarargs
	public DomainArray2D(T... domains) {
    	super(domains);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#boundary()
	 */
	public Boundary2D boundary() {
		int n = this.shapes.size();
		ArrayList<Contour2D> boundaries = 
			new ArrayList<Contour2D> (n);
		for(Domain2D domain : this)
			boundaries.addAll(domain.boundary().continuousCurves());
		return new ContourArray2D<Contour2D>(boundaries);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#contours()
	 */
	public Collection<? extends Contour2D> contours() {
		return this.boundary().continuousCurves();
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#complement()
	 */
	public DomainSet2D<? extends Domain2D> complement() {
		int n = this.shapes.size();
		ArrayList<Domain2D> complements = new ArrayList<Domain2D> (n);
		for(Domain2D domain : this)
			complements.add(domain.complement());
		return new DomainArray2D<Domain2D>(complements);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#asPolygon(int)
	 */
	public Polygon2D asPolygon(int n) {
		// Compute number of contours
		int nContours = 0;
		for (Domain2D domain : this.shapes)
			nContours += domain.boundary().continuousCurves().size();

		// concatenate the set of linear rings
		ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(nContours);
		for (Domain2D domain : this.shapes) {
			for (Contour2D contour : domain.boundary().continuousCurves()) {
				// Check that the curve is bounded
		        if (!contour.isBounded())
		            throw new UnboundedShape2DException(this);
		        
		        // If contour is bounded, it should be closed
		        if (!contour.isClosed())
		        	throw new IllegalArgumentException("Can not transform open curve to linear ring");

		        LinearCurve2D poly = contour.asPolyline(n);
		        assert poly instanceof LinearRing2D : "expected result as a linear ring";
		        
				rings.add((LinearRing2D) poly);
			}
		}
		
		return new MultiPolygon2D(rings);
	}
	
	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#fill(java.awt.Graphics2D)
	 */
	public void fill(Graphics2D g2) {
		for(Domain2D domain : this)
			domain.fill(g2);
	}

    // ===================================================================
    // methods implementing the Shape2D interface

	/* (non-Javadoc)
	 * @see math.geom2d.Shape2D#transform(math.geom2d.AffineTransform2D)
	 */
	public DomainArray2D<? extends Domain2D> transform(AffineTransform2D trans) {
    	// Allocate array for result
		DomainArray2D<Domain2D> result = 
    		new DomainArray2D<Domain2D>(shapes.size());
        
        // add each transformed curve
        for (Domain2D domain : this)
            result.add(domain.transform(trans));
        return result;
	}

    /* (non-Javadoc)
	 * @see math.geom2d.Shape2D#clip(math.geom2d.Box2D)
	 */
	public Domain2D clip(Box2D box) {
		ArrayList<Domain2D> clippedShapes = new ArrayList<Domain2D>();
		for (T domain : this)
			clippedShapes.add(domain.clip(box));
		return new DomainArray2D<Domain2D>(clippedShapes);
	}

    @Override
    public boolean equals(Object obj) {
        // check class
        if (!(obj instanceof DomainArray2D<?>))
            return false;
        // call superclass method
        return super.equals(obj);
    }

}
