/* File Similarity2D.java 
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

package math.geom2d;

/**
 * A similarity is a transformation which conserves lengths ratios and 
 * angles (not necessarily oriented). 
 * @author dlegland
 */
public interface Similarity2D extends AffineTransform2D, Bijection2D {

	/**
	 * Returns the scaling factor of the transform. The scaling factor is
	 * positive for direct transforms.
	 * @return the scaling factor of the transform.
	 */
	public abstract double getScalingFactor();
	
	/**
	 * 
	 * @return the linear part of the transform.
	 */
	public abstract LinearTransform2D getLinearPart();
	
	/**
	 * 
	 * @return the translation part of the transform.
	 */
	public abstract Translation2D getTranslationPart();
}
