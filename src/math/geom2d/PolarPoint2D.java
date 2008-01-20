/* File PolarPoint2D.java 
 *
 * Project : Java Geometry Library
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

// package
package math.geom2d;

// Imports

/**
 * Define a point with two polar coordinates : rho and theta. Rho is the distance to the 
 * origin, and theta is the angle with horizontal, in radians, counted Counter-Clockwise.
 * Internal storage is the same as 'cartesian' point, this class is just a convenient 
 * way to create a point when angle and distance are known.
 * @deprectated use Point2D.createPolar() instead
 */
@Deprecated
public class PolarPoint2D extends Point2D{


	// ===================================================================
	// constants

	private static final long serialVersionUID = 1L;


	// ===================================================================
	// class variables
	
	
	// ===================================================================
	// constructors
	

	/** construct a new PolarPoint2D at position (0,0). */
	public PolarPoint2D(){
		super(0, 0);
	}
	
	/** constructor with given position. Rho is the distance to the origin
	 * and THETA is angle with horizontal of the ray emanating from origin
	 * and containing the point
	 */
	public PolarPoint2D(double rho, double theta){
		this(0, 0, rho, theta);
	}
	
	/**
	 * Constructor from a java awt.geom PolarPoint2D, included for compatibility.
	 */
	public PolarPoint2D(java.awt.geom.Point2D point){
		this(point.getX(), point.getY(), 0, 0);
	}
	
	/**
	 * Constructor from a java awt.geom PolarPoint2D, included for compatibility.
	 */
	public PolarPoint2D(java.awt.geom.Point2D point, Vector2D polar){
		this(point.getX(), point.getY(), polar.getNorm(), polar.getAngle());
	}
	
	/**
	 * Construct a new Point at distance rho from given point, and making an
	 * angle theta with horizontal.
	 */
	public PolarPoint2D(java.awt.geom.Point2D point, double rho, double theta){
		this(point.getX(), point.getY(), rho, theta);
	}
	
	/**
	 * Construct a new Point at distance rho from given point, and making an
	 * angle theta with horizontal.
	 */
	public PolarPoint2D(double xref, double yref, double rho, double theta){
		super(xref+rho*Math.cos(theta), yref+rho*Math.sin(theta));
	}
	
	// ===================================================================
	// static functions

	// ===================================================================
	// accessors



	// ===================================================================
	// modifiers


	// ===================================================================
	// general methods

}