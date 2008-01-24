/* File AffineTransform2D.java 
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

/**
 * Base class for generic affine transforms in the plane. They include
 * rotations, translations, shears, homotheties, and combinations of these. 
 * Such transformations can be constructed by using coefficients
 * specification, or by creating specialized instances, by using static
 * methods.<p>
 * To implement an affine transform, use either 
 * {@link GenericAffineTransform2D}, or a specialized class like 
 * {@link Translation2D}, {@link Rotation2D}, {@link Homothecy2D}...
 */
public abstract class AbstractAffineTransform2D implements AffineTransform2D{
	

	// ===================================================================
	// static methods
	
	/**
	 * Return a rotation around the origin, with angle in radians.
	 */
	public final static AffineTransform2D createRotation(double angle){
		return AbstractAffineTransform2D.createRotation(0, 0, angle);
	}

	/**
	 * Return a rotation around the specified point, with angle in radians.
	 */
	public final static AffineTransform2D createRotation(Point2D center, double angle){
		return AbstractAffineTransform2D.createRotation(center.getX(), center.getY(), angle);
	}

	/**
	 * Return a rotation around the specified point, with angle in radians.
	 * If the angular distance of the angle with a multiple of PI/2 is lower
	 * than the threshold Shape2D.ACCURACY, the method assumes equality.
	 */
	public final static AffineTransform2D createRotation(double cx, double cy, double angle){
		angle = Angle2D.formatAngle(angle);
		int k = (int) Math.round(angle*2/Math.PI);
		double cot=1, sit=0;
		if(Math.abs(k*Math.PI/2 - angle)<Shape2D.ACCURACY){
			assert k>=0 : "k should be positive";
			assert k<5 : "k should be between 0 and 4";
			switch(k){
			case 0:cot=1; sit=0; break;
			case 1:cot=0; sit=1; break;
			case 2:cot=-1; sit=0; break;
			case 3:cot=0; sit=-1; break;
			case 4:cot=1; sit=0; break;	
			}
		}else{
			cot = Math.cos(angle);
			sit = Math.sin(angle);
		}
		return new GenericAffineTransform2D(
				cot, -sit, (1-cot)*cx+sit*cy,
				sit,  cot, (1-cot)*cy-sit*cx);
	}

	/**
	 * Return a scaling by the given coefficients, centered on the origin.
	 */
	public final static AffineTransform2D createScaling(double sx, double sy){
		return AbstractAffineTransform2D.createScaling(new Point2D(0, 0), sx, sy);
	}

	/**
	 * Return a scaling by the given coefficients, centered on the given point.
	 */
	public final static AffineTransform2D createScaling(Point2D center, double sx, double sy){
		return new GenericAffineTransform2D(sx, 0, (1-sx)*center.getX(), 0, sy, (1-sy)*center.getY());
	}

	/**
	 * Return a translation by the given vector.
	 */
	public final static AffineTransform2D createTranslation(Vector2D vect){
		return new GenericAffineTransform2D(1, 0, vect.getDx(), 0, 1, vect.getDy());
	}
	
	/**
	 * Return a translation by the given vector.
	 */
	public final static AffineTransform2D createTranslation(double dx, double dy){
		return new GenericAffineTransform2D(1, 0, dx, 0, 1, dy);
	}

	
	// ===================================================================
	// methods to identify transforms
	
	public final static boolean isIdentity(AffineTransform2D trans){
		double[] coefs = trans.getCoefficients();
		if(Math.abs(coefs[0]-1)>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[1])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[2])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[3])>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[4]-1)>Shape2D.ACCURACY) return false;
		if(Math.abs(coefs[5])>Shape2D.ACCURACY) return false;
		return true;
	}
	
	/**
	 * Check if the transform is direct, i.e. it preserves the orientation
	 * of transformed shapes.
	 * @return true if transform is direct.
	 */
	public final static boolean isDirect(AffineTransform2D trans){
		if(trans instanceof Isometry2D)
			return ((Isometry2D)trans).isDirect();
		double[][] mat = trans.getAffineMatrix();
		return mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0] > 0;
	}

	/**
	 * Check if the transform is an isometry, i.e. a compound of
	 * translation, rotation and reflection. Isometry keeps area of shapes
	 * unchanged, but can change orientation (directed or undirected).
	 * @return true in case of isometry.
	 */
	public final static boolean isIsometry(AffineTransform2D trans){
		if(trans instanceof Isometry2D) return true;
			
		double[][] mat = trans.getAffineMatrix();
		double det = mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0]; 
		return Math.abs(Math.abs(det)-1)<Shape2D.ACCURACY;
	}

	/**
	 * Check if the transform is a motion, i.e. a compound of translations and
	 * rotation. Motion remains area and orientation (directed or undirected)
	 * of shapes unchanged.
	 * @return true in case of motion.
	 */
	public final static boolean isMotion(AffineTransform2D trans){
		if(trans instanceof Motion2D) return true;
		
		double[][] mat = trans.getAffineMatrix();
		double det = mat[0][0]*mat[1][1] - mat[0][1]*mat[1][0]; 
		return Math.abs(det-1)<Shape2D.ACCURACY;
	}
	
	/**
	 * Check if the transform is an similarity, i.e. transformation which keeps
	 * unchanged the global shape, up to a scaling factor.
	 * @return true in case of similarity.
	 */
	public final static boolean isSimilarity(AffineTransform2D trans){
		if(trans instanceof Similarity2D) return true;
		
		double[][] mat = trans.getAffineMatrix();
		return Math.abs(mat[0][0]*mat[0][1] + mat[1][0]*mat[1][1])<
			Shape2D.ACCURACY;		
	}

	// ===================================================================
	// implementations of AffineTransform2D methods
	
	public AffineTransform2D compose(AffineTransform2D that){
		double[][] m1 = this.getAffineMatrix();
		double[][] m2 = that.getAffineMatrix();
		double n00 = m1[0][0]*m2[0][0] + m1[0][1]*m2[1][0];
		double n01 = m1[0][0]*m2[0][1] + m1[0][1]*m2[1][1];
		double n02 = m1[0][0]*m2[0][2] + m1[0][1]*m2[1][2] + m1[0][2];
		double n10 = m1[1][0]*m2[0][0] + m1[1][1]*m2[1][0];
		double n11 = m1[1][0]*m2[0][1] + m1[1][1]*m2[1][1];
		double n12 = m1[1][0]*m2[0][2] + m1[1][1]*m2[1][2] + m1[1][2];
		return new GenericAffineTransform2D(n00, n01, n02, n10, n11, n12); 
	}
	
	public boolean isSimilarity(){
		return AbstractAffineTransform2D.isSimilarity(this);
	}
	
	public boolean isMotion(){
		return AbstractAffineTransform2D.isMotion(this);
	}
	
	public boolean isIsometry(){
		return AbstractAffineTransform2D.isIsometry(this);
	}
	
	public boolean isDirect(){
		return AbstractAffineTransform2D.isDirect(this);
	}
	
	public boolean isIdentity(){
		return AbstractAffineTransform2D.isIdentity(this);
	}
	
	// ===================================================================
	// implementations of Transform2D methods

	public Point2D[] transform(java.awt.geom.Point2D[] src, Point2D[] dst){
		if(dst==null)
			dst = new Point2D[src.length];
		if(dst[0]==null)
			for(int i=0; i<src.length; i++)
				dst[i]=new Point2D();
		
		double coef[] = getCoefficients();
		
		for(int i=0; i<src.length; i++)
			dst[i].setLocation(new Point2D(
				src[i].getX()*coef[0] + src[i].getY()*coef[1] + coef[2],
				src[i].getX()*coef[3] + src[i].getY()*coef[4] + coef[5]));
		return dst;
	}
	
	public Point2D transform(java.awt.geom.Point2D src) {
		return transform(src, new Point2D());
	}
	
	public Point2D transform(java.awt.geom.Point2D src, Point2D dst){
		double coef[] = getCoefficients();
		if (dst==null)dst = new Point2D();	
		dst.setLocation(src.getX()*coef[0] + src.getY()*coef[1] + coef[2],
					  	src.getX()*coef[3] + src.getY()*coef[4] + coef[5]);
		return dst;
	}	
	
	public boolean equals(Object obj){
		if(!(obj instanceof AffineTransform2D)) return false;
		
		double[] tab1 = this.getCoefficients();
		double[] tab2 = ((AffineTransform2D) obj).getCoefficients();

		for(int i=0; i<6; i++)
			if(Math.abs(tab1[i]-tab2[i])>Shape2D.ACCURACY) return false;

		return true;
	}
	
	public final static class Identity2D extends GenericAffineTransform2D implements
	LinearTransform2D, Motion2D {

		/* (non-Javadoc)
		 * @see math.geom2d.Similarity2D#getLinearPart()
		 */
		public LinearTransform2D getLinearPart() {
			return this;
		}

		/* (non-Javadoc)
		 * @see math.geom2d.Similarity2D#getScalingFactor()
		 */
		public double getScalingFactor() {
			return 1;
		}

		/* (non-Javadoc)
		 * @see math.geom2d.Similarity2D#getTranslationPart()
		 */
		public Translation2D getTranslationPart() {
			return new Translation2D(0, 0);
		}

		public Identity2D getInverseTransform(){
			return new Identity2D();
		}
		
		public boolean isSimilarity(){
			return true;
		}
		
		public boolean isMotion(){
			return true;
		}
		
		public boolean isIsometry(){
			return true;
		}
		
		public boolean isDirect(){
			return true;
		}
		
		public boolean isIdentity(){
			return true;
		}		
	}
}