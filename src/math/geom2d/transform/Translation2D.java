/* File Translation2D.java 
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
import math.geom2d.Vector2D;

/**
 * Transform each point by translating it by a given vector.
 */
public class Translation2D extends AbstractAffineTransform2D implements Motion2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double dx=0;
	protected double dy=0;
	
	// ===================================================================
	// constructors

	public Translation2D(java.awt.geom.Point2D point){
		this(point.getX(), point.getY());
	}
	
	public Translation2D(Vector2D vector){
		this(vector.getDx(), vector.getDy());
	}
	
	public Translation2D(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2){
		this(p2.getX()-p1.getX(),  p2.getY()-p1.getY());
	}
	
	public Translation2D(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	

	// ===================================================================
	// general methods

	public Vector2D getVector(){
		return new Vector2D(dx, dy);
	}

	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
		
	public void setVector(Vector2D v){
		this.dx = v.getDx();
		this.dy = v.getDy();
	}
	
	
	// ===================================================================
	// interface implementations

	/**
	 * return true, by definition of a translation.
	 */
	@Override
	public boolean isMotion(){
		return true;
	}

	/**
	 * return true, by definition of a translation.
	 */
	@Override
	public boolean isDirect() {
		return true;
	}

	/**
	 * return true, by definition of a translation.
	 */
	@Override
	public boolean isSimilarity(){
		return true;
	}
	
	/**
	 * return true, by definition of a translation.
	 */
	@Override
	public boolean isIsometry(){
		return true;
	}
	
	
	// ===================================================================
	// implementation of Similarity2D methods
	
	/**
	 * return 1, by definition of a translation.
	 */
	public double getScalingFactor() {
		return 1;
	}

	public LinearTransform2D getLinearPart(){
		return new Identity2D();
	}
	
	public Translation2D getTranslationPart(){
		return new Translation2D(dx, dy);
	}

	// ===================================================================
	// implementation of AffineTransform methods
	
	public double[] getCoefficients(){
		return new double[]{1, 0, dx, 0, 1, dy};
	}	

	public double[][] getAffineMatrix(){
		return new double[][]{
				new double[]{1, 0, dx}, 
				new double[]{0, 1, dy}
		};
	}

	/**
	 * return a new Translation2D with given vector opposite to this.
	 */
	public Translation2D getInverseTransform(){
		return new Translation2D(-dx, -dy);
	}
	
	/**
	 * If the transform given as parameter is a translation, returns
	 * an instance of translation2D.
	 */
	@Override
	public AffineTransform2D compose(AffineTransform2D transfo){
		if(transfo instanceof Translation2D){
			Translation2D trans = (Translation2D) transfo;
			return new Translation2D(this.dx+trans.dx, this.dy+trans.dy);
		}
		return super.compose(transfo);
	}
}