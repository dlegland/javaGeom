/* file : Boundary2D.java
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
 * Created on 25 déc. 2006
 *
 */
package math.geom2d.domain;

import java.util.Collection;

import math.geom2d.transform.AffineTransform2D;

/**
 * A Boundary2D is the curve which defines the contour of a domain in the
 * plane. It is compound of one or several non-intersecting and oriented 
 * curves. 
 * @author dlegland
 */
public interface Boundary2D extends OrientedCurve2D {

	/**
	 * Return the different continuous curves composing the boundary
	 */
	public abstract Collection<ContinuousBoundary2D> getBoundaryCurves();
		
	public abstract Boundary2D getReverseCurve();

	public abstract Boundary2D transform(AffineTransform2D trans);
}
