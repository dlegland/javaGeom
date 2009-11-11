/**
 * 
 */

package math.geom2d;

/**
 * @author dlegland
 */
public class UnboundedShapeException extends RuntimeException {

	private Shape2D shape;
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @deprecated: use constructor with a shape instead (0.8.1)
     */
    @Deprecated
    public UnboundedShapeException() {
    	this.shape = null;
    }
    
    public UnboundedShapeException(Shape2D shape) {
    	this.shape = shape;
    }

    public Shape2D getShape() {
    	return shape;
    }
}
