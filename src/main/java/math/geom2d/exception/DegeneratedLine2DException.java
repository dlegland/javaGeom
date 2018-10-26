/**
 * File: 	DegeneratedLine2DException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 août 2010
 */
package math.geom2d.exception;

import math.geom2d.line.ILinearShape2D;

/**
 * A degenerated line, whose direction vector is undefined, had been encountered. This kind of exception can occur during polygon or polylines algorithms, when polygons have multiple vertices.
 * 
 * @author dlegland
 * @since 0.9.0
 */
public class DegeneratedLine2DException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ILinearShape2D line;

    /**
     * @param msg
     *            the error message
     * @param line
     *            the degenerated line
     */
    public DegeneratedLine2DException(String msg, ILinearShape2D line) {
        super(msg);
        this.line = line;
    }

    /**
     * @param line
     *            the degenerated line
     */
    public DegeneratedLine2DException(ILinearShape2D line) {
        super();
        this.line = line;
    }

    public ILinearShape2D getLine() {
        return line;
    }
}
