/**
 * 
 */

package math.geom2d.exception;

import math.geom2d.IShape2D;

/**
 * Exception thrown when an unbounded shape is involved in an operation that assumes a bounded shape.
 * 
 * @author dlegland
 */
public class UnboundedShape2DException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final IShape2D shape;

    public UnboundedShape2DException(IShape2D shape) {
        this.shape = shape;
    }

    public IShape2D getShape() {
        return shape;
    }
}
