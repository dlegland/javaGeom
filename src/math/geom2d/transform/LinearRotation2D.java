/* File LinearRotation2D.java 
 *
 * Project : geometry
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
 * Created 16 déc. 07
 */

package math.geom2d.transform;

import math.geom2d.Point2D;

/**
 * A rotation without translation part. Fixed point is the origin.
 * @author dlegland
 *
 */
public class LinearRotation2D extends Rotation2D implements LinearTransform2D {

	public LinearRotation2D(double angle){
		super(angle);
	}
	
	
	/**
	 * Overrides parent method to keep center to origin.
	 */
	@Override
	public void setCenter(Point2D point){
	}
	
	@Override
	public Point2D getCenter(){
		return new Point2D(0, 0);
	}
	
	@Override
	public double[] getCoefficients(){
		double sit = Math.sin(theta);
		double cot = Math.cos(theta);
		return new double[]{cot, -sit, 0, sit, cot, 0};
	}

	@Override
	public double[][] getAffineMatrix(){
		double sit = Math.sin(theta);
		double cot = Math.cos(theta);
		return new double[][]{
				new double[]{cot, -sit, 0}, 
				new double[]{sit, cot, 0}
		};
	}
	
	@Override
	public LinearRotation2D getInverseTransform(){
		return new LinearRotation2D(-theta);
	}
}
