/* File SmoothOrientedCurve2D.java 
 *
 * Project : javaGeom
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
 * Created 24 janv. 08
 */

package math.geom2d.domain;

import math.geom2d.Box2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * Interface for smooth and oriented curves. The aim of this interface is mainly to specify refinement of method declarations.
 */
public interface ISmoothOrientedCurve2D extends ISmoothCurve2D, IContinuousOrientedCurve2D {

    @Override
    public abstract ISmoothOrientedCurve2D reverse();

    @Override
    public abstract ISmoothOrientedCurve2D subCurve(double t0, double t1);

    @Override
    public abstract ICurveSet2D<? extends ISmoothOrientedCurve2D> clip(Box2D box);

    @Override
    public abstract ISmoothOrientedCurve2D transform(AffineTransform2D trans);
}
