/**
 * 
 */

package math.geom2d.exception;

import math.geom2d.transform.ITransform2D;

/**
 * Exception thrown when trying to compute an inverse transform of a transform that does not allows this feature.
 * 
 * @author dlegland
 */
public class NonInvertibleTransform2DException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ITransform2D transform;

    public NonInvertibleTransform2DException() {
        this.transform = null;
    }

    public NonInvertibleTransform2DException(ITransform2D transform) {
        this.transform = transform;
    }

    public ITransform2D getTransform() {
        return transform;
    }
}
