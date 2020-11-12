/**
 * File: 	ColinearPointsException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 nov. 09
 */
package net.javageom.geom2d;


/**
 * Exception thrown when the assumption of non colinearity is not respected.
 * Methods are provided by retrieving the three incriminated points.
 * @author dlegland
 *
 */
public class ColinearPoints2DException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point2D p1;
	protected Point2D p2;
	protected Point2D p3;
	
	public ColinearPoints2DException(Point2D p1, Point2D p2, Point2D p3) {
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
