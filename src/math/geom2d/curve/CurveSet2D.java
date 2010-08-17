/* File CurveSet2D.java 
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
 */

package math.geom2d.curve;

import java.util.Collection;

import math.geom2d.AffineTransform2D;


/**
 * <p>
 * A parameterized set of curves. A curve cannot be included twice in a
 * CurveSet2D.
 * </p>
 * <p>
 * Note: this class will be transformed to an interface in a future version.
 * Use the class {@link CurveArray2D} for implementations.
 * </p>
 * 
 * @author Legland
 */
public interface CurveSet2D<T extends Curve2D> 
extends Curve2D, Iterable<T> {

    /**
     * Checks if the curve set contains the given curve.
     */
    public boolean containsCurve(Curve2D curve) ;
  
    /**
     * Returns the collection of curves
     * 
     * @return the inner collection of curves
     */
	public Collection<T> getCurves() ;

    /**
     * Returns the inner curve corresponding to the given index.
     * 
     * @param index index of the curve
     * @return the i-th inner curve
     * @since 0.6.3
     */
	public T getCurve(int index) ;

    /**
     * Returns the child curve corresponding to a given position.
     * 
     * @param t the position on the set of curves, between 0 and twice the
     *            number of curves
     * @return the curve corresponding to the position.
     * @since 0.6.3
     */
    public T getChildCurve(double t) ;

    /**
     * Returns the first curve of the collection if it exists, null otherwise.
     * 
     * @return the first curve of the collection
     */
    public T getFirstCurve() ;

    /**
     * Returns the last curve of the collection if it exists, null otherwise.
     * 
     * @return the last curve of the collection
     */
    public T getLastCurve();

    /**
     * Returns the number of curves in the collection
     * 
     * @return the number of curves in the collection
     */
    public int getCurveNumber();
    /**
     * Converts the position on the curve set, which is comprised between 0 and
     * 2*Nc-1 with Nc being the number of curves, to the position on the curve
     * which contains the position. The result is comprised between the t0 and
     * the t1 of the child curve.
     * 
     * @see #getGlobalPosition(int, double)
     * @see #getCurveIndex(double)
     * @param t the position on the curve set
     * @return the position on the subcurve
     */
    public double getLocalPosition(double t);

    /**
     * Converts a position on a curve (between t0 and t1 of the curve) to the
     * position on the curve set (between 0 and 2*Nc-1).
     * 
     * @see #getLocalPosition(double)
     * @see #getCurveIndex(double)
     * @param i the index of the curve to consider
     * @param t the position on the curve
     * @return the position on the curve set, between 0 and 2*Nc-1
     */
    public double getGlobalPosition(int i, double t);

    /**
     * Returns the index of the curve corresponding to a given position.
     * 
     * @param t the position on the set of curves, between 0 and twice the
     *            number of curves minus 1
     * @return the index of the curve which contains position t
     */
    public int getCurveIndex(double t);

    
    // ===================================================================
    // add some class casts
    
    
    public CurveSet2D<? extends Curve2D> transform(AffineTransform2D trans);
}
