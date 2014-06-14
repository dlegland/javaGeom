/**
 * 
 */

package math.geom2d;

/**
 * Exception thrown when an unbounded box  is involved in an operation
 * that assumes a bounded box. 
 * @see Box2D
 * @author dlegland
 */
public class UnboundedBox2DException extends RuntimeException {

	private Box2D box;
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    public UnboundedBox2DException(Box2D box) {
    	this.box = box;
    }

    public Box2D getBox() {
    	return box;
    }
}
