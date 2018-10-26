/**
 * 
 */

package math.geom2d.exception;

import math.geom2d.Box2D;

/**
 * Exception thrown when an unbounded box is involved in an operation that assumes a bounded box.
 * 
 * @see Box2D
 * @author dlegland
 */
public class UnboundedBox2DException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Box2D box;

    public UnboundedBox2DException(Box2D box) {
        this.box = box;
    }

    public Box2D getBox() {
        return box;
    }
}
