/* file : ContinuousBoundary2D.java
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
 * Created on 25 d�c. 2006
 *
 */

package math.geom2d.domain;

import math.geom2d.Box2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.curve.IContinuousCurve2D;

/**
 * Defines a part of the boundary of a planar domain. A ContinuousBoundary2D is a continuous, oriented and non self-intersecting curve.
 * 
 * @author dlegland
 */
public interface IContinuousOrientedCurve2D extends IContinuousCurve2D, IOrientedCurve2D {

    @Override
    public abstract IContinuousOrientedCurve2D reverse();

    @Override
    public abstract IContinuousOrientedCurve2D subCurve(double t0, double t1);

    @Override
    public abstract IContinuousOrientedCurve2D transform(AffineTransform2D trans);

    @Override
    public abstract ICurveSet2D<? extends IContinuousOrientedCurve2D> clip(Box2D box);
}
