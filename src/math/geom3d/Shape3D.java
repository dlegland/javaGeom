/* file : Shape3D.java
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
 * Created on 27 nov. 2005
 *
 */
package math.geom3d;

/**
 * @author dlegland
 */
public interface Shape3D {
	
	public final static double ACCURACY = 1e-12;

	public final static Shape3D EMPTY_SET = new EmptySet3D();
	
	
	/**
	 * get the distance of the shape to the given point, or the distance of point
	 * to the frontier of the shape in the case of a plain shape.
	 */
	public abstract double getDistance(Point3D p);
	
	public abstract boolean contains(Point3D point);
	
	public abstract Box3D getBoundingBox();
	
	public abstract Shape3D clip(Box3D box);

	public abstract Shape3D transform(AffineTransform3D trans);
	
	
	/**
	 * 
	 */
	public class EmptySet3D implements Shape3D{
		
		protected EmptySet3D(){
		}
		
		/**
		 * return positive infinity.
		 */
		public double getDistance(Point3D p){
			return Double.POSITIVE_INFINITY;
		}
		
		public boolean contains(Point3D point){
			return false;
		}
		
		public Box3D getBoundingBox(){
			return new Box3D(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		}
		
		public Shape3D clip(Box3D box){
			return this;
		}

		public Shape3D transform(AffineTransform3D trans){
			return this;
		}

	}
}

