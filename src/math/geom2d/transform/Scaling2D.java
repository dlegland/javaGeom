/* File Scaling2D.java 
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
 * 
 * Created on 02 Jan. 2007
 *
 */

// package
package math.geom2d.transform;

// Imports
import math.geom2d.Point2D;

/**
 * Scales a shape in the 2 main directions of the axis, centered at the
 * origin.
 */
public class Scaling2D extends AbstractAffineTransform2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double sx=1;
	protected double sy=1;
	protected double xc=0;
	protected double yc=0;
	
		
	// ===================================================================
	// constructors

	/**
	 * Use the same scaling factor for x and y axis.
	 */
	public Scaling2D(double scale){
		this(scale, scale);
	}
	
	/**
	 * 
	 * @param sx scaling in x direction
	 * @param sy scaling in y direction
	 */
	public Scaling2D(double sx, double sy){
		this.sx = sx;
		this.sy = sy;
	}

	/**
	 * 
	 * @param origin the invariant point of the transform
	 * @param sx scaling in x direction
	 * @param sy scaling in y direction
	 */
	public Scaling2D(Point2D origin, double sx, double sy){
		this.xc = origin.getX();
		this.yc = origin.getY();
		this.sx = sx;
		this.sy = sy;
	}
	// ===================================================================
	// accessors

	/**
	 * return false, as the scaling transform does not preserves areas.
	 */
	public boolean isMotion(){
		return false;
	}
	
	/**
	 * return true if both scaling factors have the same sign.
	 */
	public boolean isDirect(){
		return sx*sy>0;
	}
	

	public double[] getCoefficients(){
		double[] tab = new double[6];
		tab[0] = sx;
		tab[1] = 0;
		tab[2] = xc*(1-sx);
		tab[3] = 0;
		tab[4] = sy;
		tab[5] = yc*(1-sy);
		return tab;
	}
	
	public double[][] getAffineMatrix(){
		return new double[][]{
				new double[]{sx, 0, xc*(1-sx)}, 
				new double[]{0, sy, yc*(1-sy)}
		};
	}

	public Scaling2D getInverseTransform(){
		return new Scaling2D(new Point2D(xc, yc), 1/sx, 1/sy);
	}

}