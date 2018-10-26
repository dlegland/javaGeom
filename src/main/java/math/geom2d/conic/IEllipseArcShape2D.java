/**
 * 
 */
package math.geom2d.conic;

import math.geom2d.AffineTransform2D;
import math.geom2d.domain.ISmoothOrientedCurve2D;

/**
 * An interface to gather CircleArc2D and EllipseArc2D.
 * 
 * @author dlegland
 *
 */
public interface IEllipseArcShape2D extends ISmoothOrientedCurve2D {

    @Override
    public IEllipseArcShape2D reverse();

    @Override
    public IEllipseArcShape2D subCurve(double t0, double t1);

    @Override
    public IEllipseArcShape2D transform(AffineTransform2D trans);
}
