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
 * @author dlegland
 *
 */
public class ColinearPointsException extends RuntimeException {

	protected Point2D p1;
	protected Point2D p2;
	protected Point2D p3;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ColinearPointsException(Point2D p1, Point2D p2, Point2D p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Point2D getP1() {
		return p1;
	}
	
	public Point2D getP2() {
		return p2;
	}
	
	public Point2D getP3() {
		return p3;
	}
}
