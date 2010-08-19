/**
 * File: 	DegeneratedLine2DException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 août 2010
 */
package math.geom2d.line;


/**
 * @author dlegland
 * @since 0.9.0
 */
public class DegeneratedLine2DException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected LinearShape2D line;
	
	/**
	 * @param arg0
	 */
	public DegeneratedLine2DException(LinearShape2D line) {
		this.line = line;
	}

	public LinearShape2D getLine() {
		return line;
	}
}
