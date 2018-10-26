/**
 * 
 */

package math.geom2d.line;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.circulinear.ICirculinearElement2D;
import math.geom2d.curve.ICurveSet2D;

/**
 * A continuous linear shape, like a straight line, a line segment or a ray.
 * 
 * @author dlegland
 */
public interface ILinearElement2D extends ICirculinearElement2D, ILinearShape2D {

    @Override
    public ILinearElement2D transform(AffineTransform2D trans);

    @Override
    public ILinearElement2D subCurve(double y0, double t1);

    @Override
    public ICurveSet2D<? extends ILinearElement2D> clip(Box2D box);
}
