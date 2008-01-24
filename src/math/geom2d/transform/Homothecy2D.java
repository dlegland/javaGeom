/* File Homothety2D.java 
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
import math.geom2d.Shape2D;

/**
 * Defines a homothetic transform.
 */
public class Homothecy2D extends AbstractAffineTransform2D implements Similarity2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double x0;
	protected double y0;
	protected double k;
	
		
	// ===================================================================
	// constructors

	/**
	 * Defines a homothetie with factor k centered at the origin.
	 */
	public Homothecy2D(double k){
		this(0, 0, k);
	}

	public Homothecy2D(java.awt.geom.Point2D point, double k){
		this(point.getX(), point.getY(), k);
	}
	
	public Homothecy2D(double xp, double yp, double k){
		this.x0 = xp;
		this.y0 = yp;
		this.k=k;
	}
	
	// ===================================================================
	// accessors

	/** 
	 * The homothety is a motion if the dilation factor is equal to 1.
	 */
	public boolean isMotion(){
		return Math.abs(k-1)<Shape2D.ACCURACY;
	}
	
	public double[] getCoefficients(){
		double[] tab = new double[6];
		tab[0] = k;
		tab[1] = 0;
		tab[2] = x0*(1-k);
		tab[3] = 0;
		tab[4] = k;
		tab[5] = y0*(1-k);
		return tab;
	}

	public double[][] getAffineMatrix(){
		return new double[][]{
				new double[]{k, 0, x0*(1-k)}, 
				new double[]{0, k, y0*(1-k)}
		};
	}

	public Homothecy2D getInverseTransform(){
		return new Homothecy2D(x0, y0, 1/k);
	}
	
	public double getScalingFactor(){
		return k;
	}
	
	public LinearHomothecy2D getLinearPart(){
		return new LinearHomothecy2D(k);
	}
	
	public Translation2D getTranslationPart(){
		return new Translation2D(x0*(1-k), y0*(1-k));
	}


}