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

import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.AffineTransform2D;

/**
 * A single continuous oriented curve, which defines the boundary of a planar domain. The boundary curve is composed of several continuous and oriented curves linked together to form a continuous curve. The resulting boundary curve is either a closed curve, or an infinite curve at both ends.
 * 
 * @author dlegland
 */
public class BoundaryPolyCurve2D<T extends IContinuousOrientedCurve2D> extends PolyOrientedCurve2D<T> implements IContour2D {
    private static final long serialVersionUID = 1L;

    /**
     * Static factory for creating a new BoundaryPolyCurve2D from an array of curves.
     * 
     * @since 0.8.1
     */
    @SafeVarargs
    public static <T extends IContinuousOrientedCurve2D> BoundaryPolyCurve2D<T> create(T... curves) {
        return new BoundaryPolyCurve2D<>(curves);
    }

    // ===================================================================
    // Constructors

    /**
     * Creates an empty BoundaryPolyCurve2D.
     */
    public BoundaryPolyCurve2D() {
        super();
    }

    /**
     * Creates a BoundaryPolyCurve2D by reserving space for n curves.
     * 
     * @param n
     *            the number of curves to store
     */
    public BoundaryPolyCurve2D(int n) {
        super(n);
    }

    /**
     * Creates a BoundaryPolyCurve2D from the specified set of curves.
     */
    @SafeVarargs
    public BoundaryPolyCurve2D(T... curves) {
        super(curves);
    }

    /**
     * Creates a BoundaryPolyCurve2D from the specified set of curves.
     */
    public BoundaryPolyCurve2D(Collection<? extends T> curves) {
        super(curves);
    }

    // ===================================================================
    // Methods overriding CurveSet2D methods

    /**
     * Overrides the isClosed() in the following way: return true if all curves are bounded. If at least one curve is unbounded, return false.
     */
    @Override
    public boolean isClosed() {
        for (T curve : curves) {
            if (!curve.isBounded())
                return false;
        }
        return true;
    }

    // ===================================================================
    // Methods implementing Boundary2D interface

    @Override
    public Collection<BoundaryPolyCurve2D<T>> continuousCurves() {
        return wrapCurve(this);
    }

    @Override
    public IDomain2D domain() {
        return new GenericDomain2D(this);
    }

    @Override
    public void fill(Graphics2D g2) {
        g2.fill(this.getGeneralPath());
    }

    // ===================================================================
    // Methods implementing OrientedCurve2D interface

    @Override
    public BoundaryPolyCurve2D<? extends IContinuousOrientedCurve2D> reverse() {
        IContinuousOrientedCurve2D[] curves2 = new IContinuousOrientedCurve2D[curves.size()];
        int n = curves.size();
        for (int i = 0; i < n; i++)
            curves2[i] = curves.get(n - 1 - i).reverse();
        return new BoundaryPolyCurve2D<>(curves2);
    }

    @Override
    public BoundaryPolyCurve2D<IContinuousOrientedCurve2D> transform(AffineTransform2D trans) {
        // create result curve
        BoundaryPolyCurve2D<IContinuousOrientedCurve2D> result = new BoundaryPolyCurve2D<>(curves.size());

        // reverse each curve and add it to result
        for (IContinuousOrientedCurve2D curve : curves)
            result.add(curve.transform(trans));
        return result;
    }
}
