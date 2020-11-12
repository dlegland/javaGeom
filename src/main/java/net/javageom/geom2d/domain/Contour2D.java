/* file : Contour2D.java
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
 * Created on 1 avr. 2007
 *
 */

package net.javageom.geom2d.domain;

import net.javageom.geom2d.AffineTransform2D;

/**
 * A continuous oriented curve which delimits a connected planar domain.
 * A contour can be closed (like ellipse, circle, closed polyline...) or open
 * (parabola, straight line...).
 * @author dlegland
 * @since 0.9.0
 */
public interface Contour2D extends Boundary2D, ContinuousOrientedCurve2D {

	/**
	 * Computes the reversed contour. 
	 */
    public abstract Contour2D reverse();

	/**
	 * Computes the transformed contour. 
	 */
    public abstract Contour2D transform(AffineTransform2D trans);
}
