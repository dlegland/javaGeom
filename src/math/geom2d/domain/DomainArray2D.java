/**
 * File: 	DomainArray2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 17 août 10
 */
package math.geom2d.domain;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.ShapeArray2D;


/**
 * @author dlegland
 *
 */
public class DomainArray2D<T extends Domain2D> extends ShapeArray2D<T> 
implements DomainSet2D<T> {

	public static <D extends Domain2D> DomainArray2D<D> create(Collection<D> array) {
		return new DomainArray2D<D>(array);
	}
	
	public static <D extends Domain2D> DomainArray2D<D> create(D[] array) {
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
	 * @param shapes
	 */
	public DomainArray2D(Collection<T> domains) {
		super(domains);
	}

	/**
	 * @param shapes
	 */
	public DomainArray2D(T[] domains) {
    	super(domains);
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
	 * @see math.geom2d.domain.Domain2D#fill(java.awt.Graphics2D)
	 */
	public void fill(Graphics2D g2) {
		for(Domain2D domain : this)
			domain.fill(g2);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.domain.Domain2D#getBoundary()
	 */
	public Boundary2D getBoundary() {
		int n = this.shapes.size();
		ArrayList<ContinuousBoundary2D> boundaries = 
			new ArrayList<ContinuousBoundary2D> (n);
		for(Domain2D domain : this)
			boundaries.addAll(domain.getBoundary().getBoundaryCurves());
		return new BoundarySet2D<ContinuousBoundary2D>(boundaries);
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
            result.addShape(domain.transform(trans));
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

}
