/* File IAffineTransform2D.java 
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
 * Created 29 nov. 07
 */

package math.geom2d.transform;

/**
 * Represents any transform which can be expressed via a 3x3 matrix with the
 * last row equal to [0 0 1].
 * The storage or the computation of the matrix coefficients is left to the
 * implementation.<p>
 * Specialized interfaces are {@link Isometry2D} and {@link Motion2D}.
 * @author dlegland
 */
public interface AffineTransform2D extends Bijection2D{


	/**
	 * return coefficients of the transform. Result is an array of 6 double.
	 */
	public abstract double[] getCoefficients();
	
	/**
	 * return matrix of the transform coefficients. Result is a 2x3 array of double.
	 */
	public abstract double[][] getAffineMatrix();
	
	/**
	 * Return the composition of this affine transform with the given affine
	 * transform. If transforms are represented by matrices <code>this</code>
	 * and <code>that</code>, the result can be represented by matrix 
	 * <code>this*that</code>.
	 * @param transform the transform to compose with
	 * @return the composition of this transform with the given transform
	 */
	public abstract AffineTransform2D compose(AffineTransform2D transform);
	
	// Some methods to identify the type of transform
	
	
	public abstract boolean isSimilarity();
	
	/**
	 * Check if the transform is a motion, i.e. a compound of translations and
	 * rotation. Motion preserves area and orientation (directed or undirected)
	 * of shapes unchanged.
	 * @return true in case of motion.
	 */
	public abstract boolean isMotion();

	/**
	 * Check if the transform is an isometry, i.e. a compound of translation,
	 * rotation and reflection. Isometries preserves area of shapes
	 * unchanged, but can change orientation (directed or undirected).
	 * @return true in case of isometry.
	 */
	public abstract boolean isIsometry();
	
	/**
	 * Check if the transform is direct, i.e. it preserves the orientation
	 * of transformed shapes.
	 * @return true if transform is direct.
	 */
	public abstract boolean isDirect();
	
	/**
	 * Check if the transform is equivalent to the identity transform.
	 * @return true if the transform is identity
	 */
	public abstract boolean isIdentity();
}
