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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static math.geom2d.IShape2D.ACCURACY;

import java.io.Serializable;

import math.geom2d.exception.NonInvertibleTransform2DException;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.transform.IBijection2D;

/**
 * Base class for generic affine transforms in the plane. They include rotations, translations, shears, similarities, and combinations of these. Such transformations can be constructed by using coefficients specification, or by creating specialized instances, by using static methods.
 * <p>
 */
public class AffineTransform2D implements IBijection2D, IGeometricObject2D, Serializable {
    private static final long serialVersionUID = 1L;

    public static final AffineTransform2D IDENTITY = new AffineTransform2D(1, 0, 0, 0, 1, 0);

    public static final AffineTransform2D QUADRANTROTATION_0 = new AffineTransform2D(1, 0, 0, 0, 1, 0);
    public static final AffineTransform2D QUADRANTROTATION_1 = new AffineTransform2D(0, -1, 0, 1, 0, 0);
    public static final AffineTransform2D QUADRANTROTATION_2 = new AffineTransform2D(-1, 0, 0, 0, -1, 0);
    public static final AffineTransform2D QUADRANTROTATION_3 = new AffineTransform2D(0, 1, 0, -1, 0, 0);

    // coefficients for x coordinate.
    private final double m00, m01, m02;

    // coefficients for y coordinate.
    private final double m10, m11, m12;

    // ===================================================================
    // static methods

    /**
     * Creates an affine transform defined by an array of coefficients. The input array must have either 4 or 6 coefficients.
     * 
     * @since 0.8.1
     */
    public static AffineTransform2D create(double[] coefs) {
        if (coefs.length == 4) {
            return new AffineTransform2D(coefs[0], coefs[1], 0, coefs[2], coefs[3], 0);
        } else if (coefs.length == 6) {
            return new AffineTransform2D(coefs[0], coefs[1], coefs[2], coefs[3], coefs[4], coefs[5]);
        } else {
            throw new IllegalArgumentException("Input array must have either 4 or 6 elements");
        }
    }

    /**
     * @since 0.8.1
     */
    public static AffineTransform2D create(double xx, double yx, double tx, double xy, double yy, double ty) {
        return new AffineTransform2D(xx, yx, tx, xy, yy, ty);
    }

    /**
     * Create a glide reflection, composed of a reflection by the given line, and a translation in the direction of the line by a distance given by second parameter.
     */
    public static AffineTransform2D createGlideReflection(ILinearShape2D line, double distance) {
        // get origin and vector of line
        Vector2D vector = line.direction().normalize();
        Point2D origin = line.origin();

        // extract origin and vector coordinates
        double dx = vector.x();
        double dy = vector.y();
        double x0 = origin.x();
        double y0 = origin.y();

        // compute translation parameters
        double tx = dx * distance;
        double ty = dy * distance;

        // some computation shortcuts
        double delta = dx * dx + dy * dy;
        double dx2 = dx * dx;
        double dy2 = dy * dy;
        double dxy = dx * dy;
        double dxy0 = dx * y0;
        double dyx0 = dy * x0;

        // create the affine transform with parameters of glide reflection
        return new AffineTransform2D((dx2 - dy2) / delta, 2 * dxy / delta, 2 * dy * (dyx0 - dxy0) / delta + tx, 2 * dxy / delta, (dy2 - dx2) / delta, 2 * dx * (dxy0 - dyx0) / delta + ty);
    }

    /**
     * Creates a reflection by the given line. The resulting transform is indirect.
     */
    public static AffineTransform2D createLineReflection(ILinearShape2D line) {
        // origin and direction of line
        Point2D origin = line.origin();
        Vector2D vector = line.direction();

        // extract direction vector coordinates
        double dx = vector.x();
        double dy = vector.y();
        double x0 = origin.x();
        double y0 = origin.y();

        // pre-compute some terms
        double dx2 = dx * dx;
        double dy2 = dy * dy;
        double dxy = dx * dy;
        double delta = dx2 + dy2;

        // creates the new transform
        return new AffineTransform2D((dx2 - dy2) / delta, 2 * dxy / delta, 2 * (dy2 * x0 - dxy * y0) / delta, 2 * dxy / delta, (dy2 - dx2) / delta, 2 * (dx2 * y0 - dxy * x0) / delta);
    }

    /**
     * Returns a center reflection around a point. The resulting transform is equivalent to a rotation by 180 around this point.
     * 
     * @param center
     *            the center of the reflection
     * @return an instance of AffineTransform2D representing a point reflection
     */
    public static AffineTransform2D createPointReflection(Point2D center) {
        return AffineTransform2D.createScaling(center, -1, -1);
    }

    /**
     * Creates a rotation composed of the given number of rotations by 90 degrees around the origin.
     */
    public static AffineTransform2D getQuadrantRotation(int numQuadrant) {
        int n = ((numQuadrant % 4) + 4) % 4;
        switch (n) {
        case 0:
            return QUADRANTROTATION_0;
        case 1:
            return QUADRANTROTATION_1;
        case 2:
            return QUADRANTROTATION_2;
        case 3:
            return QUADRANTROTATION_3;
        default:
            throw new RuntimeException("Error in integer rounding...");
        }
    }

    /**
     * Creates a rotation composed of the given number of rotations by 90 degrees around the given point.
     */
    public static AffineTransform2D createQuadrantRotation(Point2D center, int numQuadrant) {
        AffineTransform2D trans = getQuadrantRotation(numQuadrant);
        return trans.recenter(center.x(), center.y());
    }

    /**
     * Creates a rotation composed of the given number of rotations by 90 degrees around the point given by (x0,y0).
     */
    public static AffineTransform2D createQuadrantRotation(double x0, double y0, int numQuadrant) {
        AffineTransform2D trans = getQuadrantRotation(numQuadrant);
        return trans.recenter(x0, y0);
    }

    /**
     * Creates a rotation around the origin, with angle in radians.
     */
    public static AffineTransform2D createRotation(double angle) {
        return AffineTransform2D.createRotation(0, 0, angle);
    }

    /**
     * Creates a rotation around the specified point, with angle in radians.
     */
    public static AffineTransform2D createRotation(Point2D center, double angle) {
        return AffineTransform2D.createRotation(center.x(), center.y(), angle);
    }

    /**
     * Creates a rotation around the specified point, with angle in radians. If the angular distance of the angle with a multiple of PI/2 is lower than the threshold Shape2D.ACCURACY, the method assumes equality.
     */
    public static AffineTransform2D createRotation(double cx, double cy, double angle) {
        angle = Angle2DUtil.formatAngle(angle);

        // special processing to detect angle close to multiple of PI/2.
        int k = (int) round(angle * 2 / PI);
        if (abs(k * PI / 2 - angle) < ACCURACY) {
            return createQuadrantRotation(cx, cy, k);
        }

        // pre-compute trigonometric functions
        double cot = cos(angle);
        double sit = sin(angle);

        // init coef of the new AffineTransform.
        return new AffineTransform2D(cot, -sit, (1 - cot) * cx + sit * cy, sit, cot, (1 - cot) * cy - sit * cx);
    }

    /**
     * Creates a scaling by the given coefficients, centered on the origin.
     */
    public static AffineTransform2D createScaling(double sx, double sy) {
        return AffineTransform2D.createScaling(new Point2D(0, 0), sx, sy);
    }

    /**
     * Creates a scaling by the given coefficients, centered on the point given by (x0,y0).
     */
    public static AffineTransform2D createScaling(Point2D center, double sx, double sy) {
        return new AffineTransform2D(sx, 0, (1 - sx) * center.x(), 0, sy, (1 - sy) * center.y());
    }

    /**
     * Creates a Shear transform, using the classical Java notation.
     * 
     * @param shx
     *            shear in x-axis
     * @param shy
     *            shear in y-axis
     * @return a shear transform
     */
    public static AffineTransform2D createShear(double shx, double shy) {
        return new AffineTransform2D(1, shx, 0, shy, 1, 0);
    }

    /**
     * Return a translation by the given vector.
     */
    public static AffineTransform2D createTranslation(Vector2D vect) {
        return new AffineTransform2D(1, 0, vect.x(), 0, 1, vect.y());
    }

    /**
     * Return a translation by the given vector.
     */
    public static AffineTransform2D createTranslation(double dx, double dy) {
        return new AffineTransform2D(1, 0, dx, 0, 1, dy);
    }

    // ===================================================================
    // methods to identify transforms

    /**
     * Checks if the given transform is the identity transform.
     */
    public static boolean isIdentity(AffineTransform2D trans) {
        if (abs(trans.m00 - 1) > ACCURACY)
            return false;
        if (abs(trans.m01) > ACCURACY)
            return false;
        if (abs(trans.m02) > ACCURACY)
            return false;
        if (abs(trans.m10) > ACCURACY)
            return false;
        if (abs(trans.m11 - 1) > ACCURACY)
            return false;
        if (abs(trans.m12) > ACCURACY)
            return false;
        return true;
    }

    /**
     * Checks if the transform is direct, i.e. it preserves the orientation of transformed shapes.
     * 
     * @return true if transform is direct.
     */
    public static boolean isDirect(AffineTransform2D trans) {
        return trans.m00 * trans.m11 - trans.m01 * trans.m10 > 0;
    }

    /**
     * Checks if the transform is an isometry, i.e. a compound of translation, rotation and reflection. Isometry keeps area of shapes unchanged, but can change orientation (direct or indirect).
     * 
     * @return true in case of isometry.
     */
    public static boolean isIsometry(AffineTransform2D trans) {
        // extract matrix coefficients
        double a = trans.m00;
        double b = trans.m01;
        double c = trans.m10;
        double d = trans.m11;

        // transform vectors should be normalized
        if (abs(a * a + b * b - 1) > ACCURACY)
            return false;
        if (abs(c * c + d * d - 1) > ACCURACY)
            return false;

        // determinant must be -1 or +1
        if (abs(a * b + c * d) > ACCURACY)
            return false;

        // if all tests passed, return true;
        return true;
    }

    /**
     * Checks if the transform is a motion, i.e. a compound of translations and rotations. Motions are special case of isometries that keep orientation (directed or undirected) of shapes unchanged.
     * 
     * @return true in case of motion.
     */
    public static boolean isMotion(AffineTransform2D trans) {
        // Transform must be 1) an isometry and 2) be direct
        return isIsometry(trans) && isDirect(trans);
    }

    /**
     * Checks if the transform is an similarity, i.e. transformation which keeps unchanged the global shape, up to a scaling factor.
     * 
     * @return true in case of similarity.
     */
    public static boolean isSimilarity(AffineTransform2D trans) {
        // computation shortcuts
        double a = trans.m00;
        double b = trans.m01;
        double c = trans.m10;
        double d = trans.m11;

        // determinant
        double k2 = abs(a * d - b * c);

        // test each condition
        if (abs(a * a + b * b - k2) > ACCURACY)
            return false;
        if (abs(c * c + d * d - k2) > ACCURACY)
            return false;
        if (abs(a * a + c * c - k2) > ACCURACY)
            return false;
        if (abs(b * b + d * d - k2) > ACCURACY)
            return false;

        // if each test passed, return true
        return true;
    }

    /**
     * Creates a new Affine Transform by directly specifying the coefficients, in the order m00, m01, m02, m10, m11, m12 (different order of java.awt.geom.AffineTransform).
     */
    public AffineTransform2D(double xx, double yx, double xy, double yy) {
        this(xx, yx, 0, xy, yy, 0);
    }

    public AffineTransform2D(double xx, double yx, double tx, double xy, double yy, double ty) {
        m00 = xx;
        m01 = yx;
        m02 = tx;
        m10 = xy;
        m11 = yy;
        m12 = ty;
    }

    /**
     * Helper function that fixes the center of the transform. This function recomputes m02 and m12 from the other coefficients and the given parameters. If transform is a pure translation, the result is the identity transform.
     */
    private AffineTransform2D recenter(double x0, double y0) {
        double newm00 = m00;
        double newm01 = m01;
        double newm02 = (1 - m00) * x0 - m01 * y0;
        double newm10 = m10;
        double newm11 = m11;
        double newm12 = (1 - m11) * y0 - m10 * x0;
        return new AffineTransform2D(newm00, newm01, newm02, newm10, newm11, newm12);
    }
    // ===================================================================
    // methods specific to AffineTransform2D class

    /**
     * Returns coefficients of the transform in a linear array of 6 double.
     */
    public double[] coefficients() {
        double[] tab = { m00, m01, m02, m10, m11, m12 };
        return tab;
    }

    /**
     * Returns the 3x3 square matrix representing the transform.
     * 
     * @return the 3x3 affine transform representing the matrix
     */
    public double[][] affineMatrix() {
        double[][] tab = new double[][] { new double[] { m00, m01, m02 }, new double[] { m10, m11, m12 }, new double[] { 0, 0, 1 } };
        return tab;
    }

    /**
     * Returns the affine transform created by applying first the affine transform given by <code>that</code>, then this affine transform. This is the equivalent method of the 'concatenate' method in java.awt.geom.AffineTransform.
     * 
     * @param that
     *            the transform to apply first
     * @return the composition this * that
     * @since 0.6.3
     */
    public AffineTransform2D concatenate(AffineTransform2D that) {
        double n00 = this.m00 * that.m00 + this.m01 * that.m10;
        double n01 = this.m00 * that.m01 + this.m01 * that.m11;
        double n02 = this.m00 * that.m02 + this.m01 * that.m12 + this.m02;
        double n10 = this.m10 * that.m00 + this.m11 * that.m10;
        double n11 = this.m10 * that.m01 + this.m11 * that.m11;
        double n12 = this.m10 * that.m02 + this.m11 * that.m12 + this.m12;
        return new AffineTransform2D(n00, n01, n02, n10, n11, n12);
    }

    /**
     * Returns the affine transform created by applying first this affine transform, then the affine transform given by <code>that</code>. This the equivalent method of the 'preConcatenate' method in java.awt.geom.AffineTransform. <code><pre>
     * shape = shape.transform(T1.chain(T2).chain(T3));
     * </pre></code> is equivalent to the sequence: <code><pre>
     * shape = shape.transform(T1);
     * shape = shape.transform(T2);
     * shape = shape.transform(T3);
     * </pre></code>
     * 
     * @param that
     *            the transform to apply in a second step
     * @return the composition that * this
     * @since 0.6.3
     */
    public AffineTransform2D chain(AffineTransform2D that) {
        return new AffineTransform2D(that.m00 * this.m00 + that.m01 * this.m10, that.m00 * this.m01 + that.m01 * this.m11, that.m00 * this.m02 + that.m01 * this.m12 + that.m02, that.m10 * this.m00 + that.m11 * this.m10, that.m10 * this.m01 + that.m11 * this.m11, that.m10 * this.m02 + that.m11 * this.m12 + that.m12);
    }

    /**
     * Return the affine transform created by applying first this affine transform, then the affine transform given by <code>that</code>. This the equivalent method of the 'preConcatenate' method in java.awt.geom.AffineTransform.
     * 
     * @param that
     *            the transform to apply in a second step
     * @return the composition that * this
     * @since 0.6.3
     */
    public AffineTransform2D preConcatenate(AffineTransform2D that) {
        return this.chain(that);
    }

    // ===================================================================
    // methods testing type of transform

    /**
     * Tests if this affine transform is a similarity.
     */
    public boolean isSimilarity() {
        return AffineTransform2D.isSimilarity(this);
    }

    /**
     * Tests if this affine transform is a motion, i.e. is composed only of rotations and translations.
     */
    public boolean isMotion() {
        return AffineTransform2D.isMotion(this);
    }

    /**
     * Tests if this affine transform is an isometry, i.e. is equivalent to a compound of translations, rotations and reflections. Isometry keeps area of shapes unchanged, but can change orientation (direct or indirect).
     * 
     * @return true in case of isometry.
     */
    public boolean isIsometry() {
        return AffineTransform2D.isIsometry(this);
    }

    /**
     * Tests if this affine transform is direct, i.e. the sign of the determinant of the associated matrix is positive. Direct transforms preserve the orientation of transformed shapes.
     */
    public boolean isDirect() {
        return AffineTransform2D.isDirect(this);
    }

    /**
     * Tests is this affine transform is equal to the identity transform.
     * 
     * @return true if this transform is the identity transform
     */
    public boolean isIdentity() {
        return AffineTransform2D.isIdentity(this);
    }

    // ===================================================================
    // implementations of Bijection2D methods

    /**
     * Returns the inverse transform. If the transform is not invertible, throws a new NonInvertibleTransform2DException.
     * 
     * @since 0.6.3
     */
    @Override
    public AffineTransform2D invert() {
        double det = m00 * m11 - m10 * m01;

        if (Math.abs(det) < IShape2D.ACCURACY)
            throw new NonInvertibleTransform2DException(this);

        return new AffineTransform2D(m11 / det, -m01 / det, (m01 * m12 - m02 * m11) / det, -m10 / det, m00 / det, (m02 * m10 - m00 * m12) / det);
    }

    // ===================================================================
    // implementations of Transform2D methods

    /**
     * Computes the coordinates of the transformed point.
     */
    @Override
    public Point2D transform(Point2D p) {
        Point2D dst = new Point2D(p.x() * m00 + p.y() * m01 + m02, p.x() * m10 + p.y() * m11 + m12);
        return dst;
    }

    @Override
    public Point2D[] transform(Point2D[] src, Point2D[] dst) {
        if (dst == null)
            dst = new Point2D[src.length];

        double x, y;
        for (int i = 0; i < src.length; i++) {
            x = src[i].x();
            y = src[i].y();
            dst[i] = new Point2D(x * m00 + y * m01 + m02, x * m10 + y * m11 + m12);
        }
        return dst;
    }

    // ===================================================================
    // implements the GeometricObject2D interface

    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof AffineTransform2D))
            return false;

        double[] tab1 = this.coefficients();
        double[] tab2 = ((AffineTransform2D) obj).coefficients();

        for (int i = 0; i < 6; i++)
            if (Math.abs(tab1[i] - tab2[i]) > eps)
                return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(m00);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m01);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m02);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m10);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m11);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(m12);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AffineTransform2D other = (AffineTransform2D) obj;
        if (Double.doubleToLongBits(m00) != Double.doubleToLongBits(other.m00))
            return false;
        if (Double.doubleToLongBits(m01) != Double.doubleToLongBits(other.m01))
            return false;
        if (Double.doubleToLongBits(m02) != Double.doubleToLongBits(other.m02))
            return false;
        if (Double.doubleToLongBits(m10) != Double.doubleToLongBits(other.m10))
            return false;
        if (Double.doubleToLongBits(m11) != Double.doubleToLongBits(other.m11))
            return false;
        if (Double.doubleToLongBits(m12) != Double.doubleToLongBits(other.m12))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AffineTransform2D [m00=" + m00 + ", m01=" + m01 + ", m02=" + m02 + ", m10=" + m10 + ", m11=" + m11 + ", m12=" + m12 + "]";
    }
}