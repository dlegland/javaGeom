/**
 * 
 */

package math.geom2d.line;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.curve.CurveSet2D;

/**
 * A continuous linear shape, like a straight line, a line segment or a ray.
 * 
 * @author dlegland
 */
public interface LinearElement2D extends CirculinearElement2D, LinearShape2D {

    public LinearElement2D transform(AffineTransform2D trans);
    public LinearElement2D subCurve(double y0, double t1);
    public CurveSet2D<? extends LinearElement2D> clip(Box2D box);
}
