/* file : ContinuousOrientedCurveSet2D.java
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
package math.geom2d;

import java.util.Collection;

/**
 * A set of continuous and oriented curves, without assumptions on the set
 * itself.
 * @deprecated replace by CurveSet2D<? extends ContinuousOrientedCurve2D>
 * @author dlegland
 */
@Deprecated
public class ContinuousOrientedCurveSet2D<T extends ContinuousOrientedCurve2D> 
extends CurveSet2D<ContinuousOrientedCurve2D> {
//TODO: remove this class
	public ContinuousOrientedCurveSet2D() {
		super();
	}

	public ContinuousOrientedCurveSet2D(T[] curves) {
		super(curves);
	}

	public ContinuousOrientedCurveSet2D(Collection<? extends T> curves){
		super(curves);
	}
	
	
	/** 
	 * Return an instance of ContinuousOrientedCurveSet2D. 
	 */
	public ContinuousOrientedCurveSet2D<? extends ContinuousOrientedCurve2D>
			getSubCurve(double t0, double t1){
		CurveSet2D<?> set = (CurveSet2D<?>) super.getSubCurve(t0, t1);
		ContinuousOrientedCurveSet2D<ContinuousOrientedCurve2D> subCurve = 
			new ContinuousOrientedCurveSet2D<ContinuousOrientedCurve2D>();
		
		// convert to PolySmoothCurve by adding curves.
		for(Curve2D curve : set.getCurves())
			subCurve.addCurve((ContinuousOrientedCurve2D) curve);
		
		return subCurve;
	}

}
