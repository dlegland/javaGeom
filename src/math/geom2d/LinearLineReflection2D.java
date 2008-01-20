/* File LinearLineReflection2D.java 
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

package math.geom2d;

/**
 * @author dlegland
 *
 */
public class LinearLineReflection2D extends LineReflection2D implements
		LinearTransform2D {
	
	Vector2D vector = null;
	
	
	public LinearLineReflection2D(Vector2D vect){
		this.vector = vect;
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Similarity2D#getLinearPart()
	 */
	public LinearLineReflection2D getLinearPart() {
		return new LinearLineReflection2D(vector);
	}

	/* (non-Javadoc)
	 * @see math.geom2d.Similarity2D#getTranslationPart()
	 */
	public Translation2D getTranslationPart() {
		return new Translation2D(0, 0);
	}
	
	public LinearLineReflection2D getInverseTransform(){
		return new LinearLineReflection2D(vector);
	}
}
