/**
 * 
 */
package math.geom2d.conic;

import java.util.Collection;

import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.domain.SmoothContour2D;

/**
 * A common interface for Circle2D and Ellipse2D.
 * @author dlegland
 *
 */
public interface EllipseShape2D extends SmoothContour2D, Conic2D {

    // ===================================================================
    // methods specific to EllipseShape2D interface

	/**
	 * Returns center of the ellipse shape.
	 */
	public Point2D center();
    
    /**
     * Returns true if this ellipse shape is similar to a circle, i.e. has
     * same length for both semi-axes.
     */
	public boolean isCircle();
	
	/**
	 * If an ellipse shape is direct, it is the boundary of a convex domain.
	 * Otherwise, the complementary of the bounded domain is convex.
	 */
	public boolean isDirect();

	// ===================================================================
    // methods of Curve2D interface

    public EllipseShape2D reverse();

    public Collection<? extends EllipseShape2D> continuousCurves();

    // ===================================================================
    // methods of Shape2D interface

    public EllipseShape2D transform(AffineTransform2D trans);
    
}
