/**
 * 
 */

package math.geom2d;

import math.geom2d.transform.Transform2D;

/**
 * Exception thrown when trying to compute an inverse transform of a transform
 * that does not allows this feature.
 * @author dlegland
 */
public class NonInvertibleTransform2DException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected Transform2D transform;
    
    public NonInvertibleTransform2DException() {
    	this.transform = null;
    }
    
    public NonInvertibleTransform2DException(Transform2D transform) {
    	this.transform = transform;
    }
    
    public Transform2D getTransform() {
    	return transform;
    }
}
