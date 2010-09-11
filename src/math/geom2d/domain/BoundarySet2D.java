/**
 * File: 	BoundarySet2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 sept. 2010
 */
package math.geom2d.domain;

import java.util.Collection;


/**
 * Temporary class used for facilitating migration to CurveArray2D.
 * 
 * @author dlegland
 * @deprecated use CurveArray2D instead
 */
@Deprecated
public class BoundarySet2D<T extends Contour2D> extends ContourArray2D<T> {

	/**
	 * 
	 */
	public BoundarySet2D() {
	}

	/**
	 * @param size
	 */
	public BoundarySet2D(int size) {
		super(size);
	}

	/**
	 * @param curves
	 */
	public BoundarySet2D(T[] curves) {
		super(curves);
	}

	/**
	 * @param curves
	 */
	public BoundarySet2D(Collection<? extends T> curves) {
		super(curves);
	}

	/**
	 * @param curve
	 */
	public BoundarySet2D(T curve) {
		super(curve);
	}

}
