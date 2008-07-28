/* file : BoundaryPolyCurve2D.java
 * 
 * Project : geometry
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Created on 31 mars 2007
 *
 */
package math.geom2d.domain;

import java.util.*;

import math.geom2d.transform.AffineTransform2D;

/**
 * A single continuous oriented curve, which defines the boundary of a 
 * planar domain. The boundary curve is composed of several continuous and
 * oriented curves linked together to form a continuous curve.
 * @author dlegland
 */
public class BoundaryPolyCurve2D <T extends ContinuousOrientedCurve2D>
		extends PolyOrientedCurve2D<T>
		implements ContinuousBoundary2D {

	// ===================================================================
	// Constructors
	
	public BoundaryPolyCurve2D() {
		super();
	}

	public BoundaryPolyCurve2D(T[] curves) {
		super(curves);
	}
	
	public BoundaryPolyCurve2D(Collection<? extends T> curves) {
		super(curves);
	}
	

	// ===================================================================
	// Methods implementing Boundary2D interface
	
	/**
	 * return a ArrayList<ContinuousBoundary2D> containing only <code>this</code>.
	 */
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}
	
	public Domain2D getDomain() {
		return new GenericDomain2D(this);
	}


	// ===================================================================
	// Methods implementing OrientedCurve2D interface
	
	public BoundaryPolyCurve2D<? extends ContinuousOrientedCurve2D> getReverseCurve(){
		ContinuousOrientedCurve2D[] curves2 = 
			new ContinuousOrientedCurve2D[curves.size()];
		int n=curves.size();
		for(int i=0; i<n; i++)
			curves2[i] = curves.get(n-1-i).getReverseCurve();
		return new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>(curves2);
	}

	public BoundaryPolyCurve2D<ContinuousOrientedCurve2D> transform(AffineTransform2D trans) {
		BoundaryPolyCurve2D<ContinuousOrientedCurve2D> result = 
			new BoundaryPolyCurve2D<ContinuousOrientedCurve2D>();
		for(ContinuousOrientedCurve2D curve : curves)
			result.addCurve(curve.transform(trans));
		return result;
	}
}
