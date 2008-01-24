/* File Rotation2D.java 
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
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.*;

/**
 * Rotate a shape around a point with a given angle.
 */
public class Rotation2D extends AbstractAffineTransform2D implements Motion2D{


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
	protected double theta;
	
		
	// ===================================================================
	// constructors

	public Rotation2D(double theta){
		this(0, 0, theta);
	}
	
	public Rotation2D(Point2D center, double theta){
		this(center.getX(), center.getY(), theta);
	}
	
	public Rotation2D(double xc, double yc, double theta){
		x0 = xc;
		y0 = yc;
		this.theta = theta;
	}

	// ===================================================================
	// rotation management
	
	public void setCenter(Point2D point){
		this.x0 = point.getX();
		this.y0 = point.getY();
	}
	
	public Point2D getCenter(){
		return new Point2D(x0, y0);
	}
	
	public void setAngle(double theta){
		this.theta = Angle2D.formatAngle(theta);
	}

	public double getAngle(){
		return theta;
	}
	
	// ===================================================================
	// methods inherited from interfaces

	public boolean isMotion(){
		return true;
	}	

	public double[] getCoefficients(){
		double[] tab = new double[6];
		double sit = Math.sin(theta);
		double cot = Math.cos(theta);
		tab[0] = cot;
		tab[1] = -sit;
		tab[2] = x0 - x0*cot + y0*sit;
		tab[3] = sit;
		tab[4] = cot;
		tab[5] = y0 - x0*sit - y0*cot;
		return tab;
	}

	public double[][] getAffineMatrix(){
		double sit = Math.sin(theta);
		double cot = Math.cos(theta);
		return new double[][]{
				new double[]{cot, -sit, x0 - x0*cot + y0*sit}, 
				new double[]{sit, cot, y0 - x0*sit - y0*cot}
		};
	}

	public Rotation2D getInverseTransform(){
		return new Rotation2D(x0, y0, -theta);
	}

	public boolean isDirect() {
		return true;
	}

	public boolean isIdentity(){
		return Math.abs(Math.cos(theta)-1)<Shape2D.ACCURACY;
	}
	
	public double getScalingFactor() {
		return 1;
	}
	
	public LinearRotation2D getLinearPart(){
		return new LinearRotation2D(theta);
	}
	
	public Translation2D getTranslationPart(){
		double sit = Math.sin(theta);
		double cot = Math.cos(theta);
		return new Translation2D(x0 - x0*cot + y0*sit, y0 - x0*sit - y0*cot);
	}

	/**
	 * Returns a new Rotation2D, Translation2D, or GenericAffineTransform2D
	 * depending on the parameter transform.
	 */
	@Override
	public AffineTransform2D compose(AffineTransform2D transfo){
		if(transfo instanceof Rotation2D){
			// extract rotation
			Rotation2D rot = (Rotation2D) transfo;
			double angle = Angle2D.formatAngle(this.theta+rot.theta);
			
			// Check case of same center: new rotation has the same center, 
			// and its angle is the sum of angles of the 2 rotations.
			Point2D center = this.getCenter();
			if(Point2D.getDistance(center, rot.getCenter())<Shape2D.ACCURACY)
				return new Rotation2D(center, angle);
			
			// Check the case of rotations with different centers, but with
			// sum of angles close to 0 => return new Translation2D.
			if(Math.min(angle, Math.PI*2-angle)<Shape2D.ACCURACY){
				center = rot.getCenter();
				Point2D point = this.transform(center);
				Vector2D vect = new Vector2D(center, point);
				return new Translation2D(vect);
			}

			// Two rotations with different centers => compute center of the
			// new rotation
			Point2D c1 = rot.getCenter();
			Point2D c2 = this.getCenter();
			Point2D c1t = rot.getInverseTransform().transform(c1);
			Point2D c2t = this.transform(c2);
			StraightLine2D line1 = StraightLine2D.createMedian2D(c1, c1t);
			StraightLine2D line2 = StraightLine2D.createMedian2D(c2, c2t);
			center = StraightLine2D.getIntersection(line1, line2);
			return new Rotation2D(center, angle);
		}
		
		// case of other transforms
		return super.compose(transfo);
	}
}