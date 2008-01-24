/* File PointSymmetry2D.java 
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
package math.geom2d.transform;

// Imports
import math.geom2d.Point2D;

/**
 * Transform each point with its symmetric with respect to a point.
 * This actually an Homothecy with factor -1.
 */
public class PointSymmetry2D extends Homothecy2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
		
	// ===================================================================
	// constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 7804398965364080940L;

	public PointSymmetry2D(Point2D point){
		this(point.getX(), point.getY());
	}
	
	public PointSymmetry2D(double xp, double yp){
		super(xp, yp, -1);
	}
	
	// ===================================================================
	// accessors

	// ===================================================================
	// modifiers


	// ===================================================================
	// general methods
}