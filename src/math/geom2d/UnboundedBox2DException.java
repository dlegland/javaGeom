/**
 * 
 */

package math.geom2d;

/**
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
