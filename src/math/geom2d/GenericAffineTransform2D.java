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
package math.geom2d;

// Imports

/**
 * An implementation of IAffineTransform2D which uses an internal matrix for
 * storing transform parameters. <br>
 * Behavior of this class will be similar to java.awt.geom.AffineTransform.
 */
public class GenericAffineTransform2D extends AbstractAffineTransform2D{
	
	// ===================================================================
	// constants
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// coefficients for x coordinate.
	protected double m00, m01, m02;
	
	// coefficients for y coordinate.
	protected double m10, m11, m12;
	
	// ===================================================================
	// constructors
	
	/** Main constructor */
	public GenericAffineTransform2D(){
		// init to identity matrix
		m00 = m11 = 1;
		m01 = m10 = 0;
		m02 = m12 = 0;
	}

	/** constructor by copy of an existing transform*/
	public GenericAffineTransform2D(AffineTransform2D trans){
		double[][] mat = trans.getAffineMatrix();
		this.m00 = mat[0][0];
		this.m01 = mat[0][1];
		this.m02 = mat[0][2];
		this.m10 = mat[1][0];
		this.m11 = mat[1][1];
		this.m12 = mat[1][2];
	}

	public GenericAffineTransform2D(double[] coefs){
		if(coefs.length==4){
			m00 = coefs[0];
			m01 = coefs[1];
			m10 = coefs[2];
			m11 = coefs[3];
		}else{
			m00 = coefs[0];
			m01 = coefs[1];
			m02 = coefs[2];
			m10 = coefs[3];
			m11 = coefs[4];
			m12 = coefs[5];
		}
	}
	
	public GenericAffineTransform2D(double xx, double yx, double tx, double xy, double yy, double ty){
		m00 = xx;
		m01 = yx;
		m02 = tx;
		m10 = xy;
		m11 = yy;
		m12 = ty;
	}


	public GenericAffineTransform2D(double xx, double yx, double xy, double yy){
		m00 = xx;
		m01 = yx; 
		m02 = 0;
		m10 = xy;
		m11 = yy;
		m12 = 0;
	}

	public GenericAffineTransform2D(java.awt.geom.AffineTransform trans){
		double[] tab = new double[6];
		trans.getMatrix(tab);
		m00 = tab[0];
		m01 = tab[1];
		m02 = tab[2];
		m10 = tab[3];
		m11 = tab[4];
		m12 = tab[5];
	}

	// ===================================================================
	// static methods
	

	// ===================================================================
	// accessors

//	public boolean isIdentity(){
//		if(m00!=1) return false;
//		if(m11!=1) return false;
//		if(m01!=0) return false;
//		if(m02!=0) return false;
//		if(m10!=0) return false;
//		if(m12!=0) return false;
//		return true;
//	}
//	
//	/**
//	 * Check if the transform is direct, i.e. it preserves the orientation
//	 * of transformed shapes.
//	 * @return true if transform is direct.
//	 */
//	public boolean isDirect(){
//		return m00*m11-m01*m10>0;
//	}
//
//	/**
//	 * Check if the transform is an isometry, i.e. a compound of
//	 * translation, rotation and reflection. Isometry remains area of shapes
//	 * unchanged, but can change orientation (directed or undirected).
//	 * @return true in case of isometry.
//	 */
//	public boolean isIsometry(){
//		double det = Math.sqrt(m00*m11 - m01*m10); 
//		return Math.abs(Math.abs(det)-1)<Shape2D.ACCURACY;
//	}
//
//	/**
//	 * Check if the transform is a motion, i.e. a compound of translations and
//	 * rotation. Motion remains area and orientation (directed or undireced)
//	 * of shapes unchanged.
//	 * @return true in case of motion.
//	 */
//	public boolean isMotion(){
//		double det = Math.sqrt(m00*m11 - m01*m10); 
//		return Math.abs(det-1)<Shape2D.ACCURACY;
//	}
//	
//	/**
//	 * Check if the transform is an similarity, i.e. transformation which keeps
//	 * unchanged the global shape, up to a scaling factor.
//	 * @return true in case of similarity.
//	 */
//	public boolean isSimilarity(){
//		return Math.abs(m00*m01 + m10*m11)<Shape2D.ACCURACY;		
//	}
//
	
	/**
	 * return coefficients of the transform. Result is an array of 6 double.
	 */
	public double[] getCoefficients(){
		double[] tab = {m00, m01, m02, m10, m11, m12};		
		return tab;
	}	

	public double[][] getAffineMatrix(){
		double[][] tab = new double[][]{
				new double[]{m00, m01, m02}, 
				new double[]{m10, m11, m12}
		};
		return tab;
	}

	public AffineTransform2D getInverseTransform(){
		double det = m00*m11 - m10*m01;
		// TODO: manage case of transforms with determinant=0
		return new GenericAffineTransform2D(
			m11/det, -m01/det, (m01*m12-m02*m11)/det, 
			-m10/det, m00/det, (m02*m10-m00*m12)/det);
	}
	
	public GenericAffineTransform2D compose(AffineTransform2D that){
		double[][] m2 = that.getAffineMatrix();
		double n00 = this.m00*m2[0][0] + this.m01*m2[1][0];
		double n01 = this.m00*m2[0][1] + this.m01*m2[1][1];
		double n02 = this.m00*m2[0][2] + this.m01*m2[1][2] + this.m02;
		double n10 = this.m10*m2[0][0] + this.m11*m2[1][0];
		double n11 = this.m10*m2[0][1] + this.m11*m2[1][1];
		double n12 = this.m10*m2[0][2] + this.m11*m2[1][2] + this.m12;
		return new GenericAffineTransform2D(n00, n01, n02, n10, n11, n12); 
	}
	
	// ===================================================================
	// mutators 

	public void setTransform(double n00, double n01, double n02, double n10, double n11, double n12){
		m00 = n00;
		m01 = n01;
		m02 = n02;
		m10 = n10;
		m11 = n11;
		m12 = n12;
	}
	
	public void setTransform(GenericAffineTransform2D trans){
		m00 = trans.m00;
		m01 = trans.m01;
		m02 = trans.m02;
		m10 = trans.m10;
		m11 = trans.m11;
		m12 = trans.m12;
	}
	
	public void setToIdentity(){
		m00 = m11 = 1;
		m10 = m01 = m02 = m12 = 0;
	}
	// ===================================================================
	// general methods

//	/**
//	 * Combine this transform with another AffineTransform.
//	 */
//	public void transform(GenericAffineTransform2D trans){
//		double n00 = m00*trans.m00 + m10*trans.m01;
//		double n10 = m00*trans.m10 + m10*trans.m11;
//		double n01 = m01*trans.m00 + m11*trans.m01;
//		double n11 = m01*trans.m10 + m11*trans.m11;
//		double n02 = m02*trans.m00 + m12*trans.m01 + trans.m02;
//		double n12 = m02*trans.m10 + m12*trans.m11 + trans.m12;
//		m00=n00; m01=n01; m02=n02; m10=n10; m11=n11; m12=n12; 
//	}

	/**
	 * Combine this transform with another AffineTransform.
	 */
	public void preConcatenate(GenericAffineTransform2D trans){
		double n00 = trans.m00*m00 + trans.m10*m01;
		double n10 = trans.m00*m10 + trans.m10*m11;
		double n01 = trans.m01*m00 + trans.m11*m01;
		double n11 = trans.m01*m10 + trans.m11*m11;
		double n02 = trans.m02*m00 + trans.m12*m01 + m02;
		double n12 = trans.m02*m10 + trans.m12*m11 + m12;
		m00=n00; m01=n01; m02=n02; m10=n10; m11=n11; m12=n12; 
	}


//	/**
//	 * Transform a shape
//	 * @deprecated use Shape2D.transform() method instead
//	 */
//	public Shape2D transform(Shape2D shape){
//		return shape.transform(this);
//	}

	
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
	
	public Point2D transform(java.awt.geom.Point2D src, Point2D dst){
		double coef[] = getCoefficients();
		if (dst==null)dst = new Point2D();	
		dst.setLocation(src.getX()*coef[0] + src.getY()*coef[1] + coef[2],
					  	src.getX()*coef[3] + src.getY()*coef[4] + coef[5]);
		return dst;
	}
	
}