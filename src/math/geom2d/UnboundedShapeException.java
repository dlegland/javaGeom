/**
 * 
 */

package math.geom2d;

/**
 * @deprecated replaced by UnboundedShape2DException (0.9.0)
 * @author dlegland
 */
@Deprecated
public class UnboundedShapeException extends UnboundedShape2DException {
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @deprecated: use constructor with a shape instead (0.8.1)
     */
    @Deprecated
    public UnboundedShapeException() {
    }
    
    public UnboundedShapeException(Shape2D shape) {
    	super(shape);
    }

}
