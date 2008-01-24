/* File LineSymmetry2D.java 
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
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.*;

/**
 * interface for projections in the plane : projections on a line, on a circle, ...
 */
public class LineReflection2D extends AbstractAffineTransform2D implements Isometry2D{


	// ===================================================================
	// constants
	

	// ===================================================================
	// class variables
	
	public StraightLine2D line;
	
	// ===================================================================
	// constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor uses Ox axis as symmetry line.
	 */
	public LineReflection2D(){
		this(new StraightLine2D(0, 0, 1, 0));
	}
	
	public LineReflection2D(StraightObject2D line){
		this.line = new StraightLine2D(line);
	}
	
	
	public StraightLine2D getReflectionLine(){
		return line;
	}
	
	public double[] getCoefficients(){
		double[] tab = new double[6];
		Vector2D vector = line.getVector();
		Point2D origin = line.getOrigin();
		double dx = vector.getDx();
		double dy = vector.getDy();
		double x0 = origin.getX();
		double y0 = origin.getY();
		double delta = dx*dx + dy*dy;
		
		tab[0] = (dx*dx - dy*dy)/delta; 
		tab[1] = 2*dx*dy/delta; 
		tab[2] = 2*dy*(dy*x0 - dx*y0)/delta; 
		tab[3] = 2*dx*dy/delta; 
		tab[4] = (dy*dy - dx*dx)/delta; 
		tab[5] = 2*dx*(dx*y0 - dy*x0)/delta; 
		return tab;
	}

	public double[][] getAffineMatrix(){
		Vector2D vector = line.getVector();
		Point2D origin = line.getOrigin();
		double dx = vector.getDx();
		double dy = vector.getDy();
		double x0 = origin.getX();
		double y0 = origin.getY();
		double delta = dx*dx + dy*dy;
		
		return new double[][]{
				new double[]{(dx*dx - dy*dy)/delta, 2*dx*dy/delta,
						2*dy*(dy*x0 - dx*y0)/delta}, 
				new double[]{2*dx*dy/delta, (dy*dy - dx*dx)/delta, 
						2*dx*(dx*y0 - dy*x0)/delta}
		};
	}

	/**
	 * Return a new line reflection with respect to the same line.
	 */
	public LineReflection2D getInverseTransform(){
		return new LineReflection2D(line);
	}

	public boolean isDirect() {
		return false;
	}

	public double getScalingFactor() {
		return -1;
	}

	
	public LinearLineReflection2D getLinearPart() {
		return new LinearLineReflection2D(line.getVector());
	}

	
	public Translation2D getTranslationPart() {
		double[][] mat = this.getAffineMatrix();
		return new Translation2D(mat[0][2], mat[1][2]);
	}
	
	/**
	 * Return a new instance of Translation2D if the case of a composition
	 * with a LineReflection2D parallel to this, a new instance of
	 * Rotation2D in the case of a LineReflection not parallel to this, and
	 * a Generic transform otherwise.
	 */
	@Override
	public AffineTransform2D compose(AffineTransform2D transfo){
		if(transfo instanceof LineReflection2D){
			// get as line reflection
			LineReflection2D ref = (LineReflection2D) transfo;
			
			// case of parallel line => composition is translation
			if(StraightObject2D.isParallel(this.line, ref.line)){
				double rho = ref.line.getSignedDistance(this.line.getOrigin());
				double theta = Angle2D.formatAngle(
						Angle2D.getHorizontalAngle(this.line)+Math.PI/2);
				Vector2D vect = Vector2D.createPolar(rho, theta);
				return new Translation2D(vect);
			}
			
			// Composition is a rotation
			Point2D center = StraightObject2D.getIntersection(this.line,
					ref.line);
			double angle = Angle2D.getAngle(this.line, ref.line);
			return new Rotation2D(center, angle);
		}
			
		return super.compose(transfo);
	}
}