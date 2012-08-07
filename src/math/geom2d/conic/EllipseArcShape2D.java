/**
 * 
 */
package math.geom2d.conic;

import math.geom2d.AffineTransform2D;
import math.geom2d.domain.SmoothOrientedCurve2D;

/**
 * An interface to gather CircleArc2D and EllipseArc2D. 
 * @author dlegland
 *
 */
public interface EllipseArcShape2D extends SmoothOrientedCurve2D {
	
	public EllipseArcShape2D reverse();
	public EllipseArcShape2D subCurve(double t0, double t1);
	
	public EllipseArcShape2D transform(AffineTransform2D trans);
}
