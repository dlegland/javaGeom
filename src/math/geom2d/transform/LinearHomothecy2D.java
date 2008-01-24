/* File Homothetie2D.java 
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

package math.geom2d.transform;

/**
 * Defines a homothetic transform centered on the origin.
 */
public class LinearHomothecy2D extends Homothecy2D implements LinearTransform2D {

	public LinearHomothecy2D(double factor){
		super(factor);
	}
	
	@Override
	public double[] getCoefficients(){
		return new double[]{k, 0, 0, 0, k, 0};
	}

	@Override
	public double[][] getAffineMatrix(){
		return new double[][]{
				new double[]{k, 0, 0}, 
				new double[]{0, k, y0}
		};
	}

	@Override
	public LinearHomothecy2D getInverseTransform(){
		return new LinearHomothecy2D(1/k);
	}	
}
