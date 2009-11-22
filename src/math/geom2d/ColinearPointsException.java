/**
 * File: 	ColinearPointsException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 nov. 09
 */
package math.geom2d;


/**
 * @deprecated replaced by ColinearPoints2DException (0.9.0)
 * @author dlegland
 */
@Deprecated
public class ColinearPointsException extends ColinearPoints2DException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ColinearPointsException(Point2D p1, Point2D p2, Point2D p3) {
		super(p1, p2, p3);
	}
}
