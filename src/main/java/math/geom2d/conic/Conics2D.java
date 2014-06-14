/**
 * 
 */

package math.geom2d.conic;
import static java.lang.Math.*;


import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.line.StraightLine2D;

/**
 * Generic class providing utilities for manipulating conics. Provides in
 * particular methods for reducing a conic.
 * 
 * @author dlegland
 */
public class Conics2D {

    public final static Conic2D reduceConic(double[] coefs) {
		if (coefs.length < 6) {
            System.err.println(
            		"Conic2DUtils.reduceConic: must provide 6 coefficients");
            return null;
        }
        boolean debug = false;

        // precision for tests
        double eps = Shape2D.ACCURACY;

        // Extract coefficients
        double a = coefs[0];
        double b = coefs[1];
        double c = coefs[2];
        double d = coefs[3];
        double e = coefs[4];
        double f = coefs[5];

        // Transform the generic conic into a conic symmetric with respect to
        // one of the basis axes.
        // This results in fixing the coefficient b to 0.

        // coefficients of transformed conic;
        double a1, b1, c1, d1, e1, f1;

        double theta0 = 0;
        // Check if b is zero
		if (abs(b) < eps) {
            // Simply keep the same coefficients
            a1 = a;
            b1 = b;
            c1 = c;
            d1 = d;
            e1 = e;
            f1 = f;
            theta0 = 0;
        } else {
            // determine rotation angle (between 0 and PI/2).
			if (abs(a - c) < eps)
				theta0 = PI / 4; // conic symmetric wrt diagonal
			else
				theta0 = Angle2D.formatAngle(atan2(b, a - c) / 2);

			if (debug)
				System.out.println("conic main angle: " + toDegrees(theta0));

			// computation shortcuts
			double cot = cos(theta0);
			double sit = sin(theta0);
			double co2t = cos(2 * theta0);
			double si2t = sin(2 * theta0);
			double cot2 = cot * cot;
			double sit2 = sit * sit;

			// Compute coefficients of the conic rotated around origin
			a1 = a * cot2 + b * sit * cot + c * sit2;
			b1 = si2t * (c - a) + b * co2t; // should be equal to zero
			c1 = a * sit2 - b * sit * cot + c * cot2;
			d1 = d * cot + e * sit;
			e1 = -d * sit + e * cot;
			f1 = f;
        }

        // small control on the value of b1
		if (abs(b1) > eps) {
            System.err.println(
            		"Conic2DUtils.reduceConic: " + 
            		"conic was not correctly transformed");
            return null;
        }

		// Test degenerate cases
		if (abs(a) < eps && abs(c) < eps) {
			if (abs(d) > eps || abs(e) > eps)
				return new ConicStraightLine2D(d, e, f);
			else
				return null; // TODO: throw exception ?
        }

        // Case of a parabola
		if (abs(a1) < eps) {
            // case of a1 close to 0 -> parabola parallel to horizontal axis
            if (debug)
                System.out.println("horizontal parabola");

			// Check degenerate case d=0
			if (abs(d1) < eps) {
				double delta = e1 * e1 - 4 * c1 * f1;
				if (delta >= 0) {
					// find the 2 roots
					double ys = -e1 / 2.0 / c1;
					double dist = sqrt(delta) / 2.0 / c1;
					Point2D center = new Point2D(0, ys)
							.transform(AffineTransform2D.createRotation(theta0));
					return new ConicTwoLines2D(center, dist, theta0);
				} else
					return null; // TODO: throw exception ?
            }

            // compute reduced coefficients
			double c2 = -c1 / d1;
			double e2 = -e1 / d1;
			double f2 = -f1 / d1;

            // vertex of the parabola
			double xs = -(e2 * e2 - 4 * c2 * f2) / (4 * c2);
			double ys = -e2 * .5 / c2;
            
            // create and return result
			return new Parabola2D(xs, ys, c2, theta0 - PI / 2);

		} else if (abs(c1) < eps) {
			// Case of c1 close to 0 -> parabola parallel to vertical axis
			if (debug)
				System.out.println("vertical parabola");

            // Check degenerate case d=0
			if (abs(e1) < eps) {
				double delta = d1 * d1 - 4 * a1 * f1;
				if (delta >= 0) {
					// find the 2 roots
					double xs = -d1 / 2.0 / a1;
					double dist = sqrt(delta) / 2.0 / a1;
					Point2D center = new Point2D(0, xs)
							.transform(AffineTransform2D.createRotation(theta0));
					return new ConicTwoLines2D(center, dist, theta0);
				} else
					return null; // TODO: throw exception ?
			}

			// compute reduced coefficients
			double a2 = -a1 / e1;
			double d2 = -d1 / e1;
			double f2 = -f1 / e1;

			// vertex of parabola
			double xs = -d2 * .5 / a2;
			double ys = -(d2 * d2 - 4 * a2 * f2) / (4 * a2);

			// create and return result
			return new Parabola2D(xs, ys, a2, theta0);
        }

        // Remaining cases: ellipse or hyperbola

        // compute coordinate of conic center
		Point2D center = new Point2D(-d1 / (2 * a1), -e1 / (2 * c1));
		center = center.transform(AffineTransform2D.createRotation(theta0));

		// length of semi axes
		double num = (c1 * d1 * d1 + a1 * e1 * e1 - 4 * a1 * c1 * f1)
				/ (4 * a1 * c1);
		double at = num / a1;
		double bt = num / c1;

		if (at < 0 && bt < 0) {
			System.err.println("Conic2DUtils.reduceConic(): found A<0 and C<0");
			return null;
        }

        // Case of an ellipse
		if (at > 0 && bt > 0) {
			if (debug)
				System.out.println("ellipse");
			if (at > bt)
				return new Ellipse2D(center, sqrt(at), sqrt(bt), theta0);
			else
				return new Ellipse2D(center, sqrt(bt), sqrt(at),
						Angle2D.formatAngle(theta0 + PI / 2));
        }

        // remaining case is the hyperbola

		// Case of east-west hyperbola
		if (at > 0) {
			if (debug)
				System.out.println("east-west hyperbola");
			return new Hyperbola2D(center, sqrt(at), sqrt(-bt), theta0);
		} else {
			if (debug)
				System.out.println("north-south hyperbola");
			return new Hyperbola2D(center, sqrt(bt), sqrt(-at), theta0 + PI / 2);
        }
    }

    /**
     * Transforms a conic centered around the origin, by dropping the
     * translation part of the transform. The array must be contains at least
     * 3 elements. If it contains 6 elements, the 3 remaining elements are
     * supposed to be 0, 0, and -1 in that order.
     * 
     * @param coefs an array of double with at least 3 coefficients
     * @param trans an affine transform
     * @return an array of double with as many elements as the input array
     */
    public final static double[] transformCentered(double[] coefs,
            AffineTransform2D trans) {
        // Extract transform coefficients
        double[][] mat = trans.affineMatrix();
        double a = mat[0][0];
        double b = mat[1][0];
        double c = mat[0][1];
        double d = mat[1][1];

        // Extract first conic coefficients
        double A = coefs[0];
        double B = coefs[1];
        double C = coefs[2];

        // compute matrix determinant
		double delta = a * d - b * c;
		delta = delta * delta;

		double A2 = (A * d * d + C * b * b - B * b * d) / delta;
		double B2 = (B * (a * d + b * c) - 2 * (A * c * d + C * a * b)) / delta;
		double C2 = (A * c * c + C * a * a - B * a * c) / delta;

        // return only 3 parameters if needed
        if (coefs.length==3)
            return new double[] { A2, B2, C2 };

        // Compute other coefficients
		double D = coefs[3];
		double E = coefs[4];
		double F = coefs[5];
		double D2 = D * d - E * b;
		double E2 = E * a - D * c;
        return new double[] { A2, B2, C2, D2, E2, F };
    }

    /**
     * Transforms a conic by an affine transform.
     * 
     * @param coefs an array of double with 6 coefficients
     * @param trans an affine transform
     * @return the coefficients of the transformed conic
     */
    public final static double[] transform(double[] coefs,
            AffineTransform2D trans) {
        // Extract coefficients of the inverse transform
        double[][] mat = trans.invert().affineMatrix();
        double a = mat[0][0];
        double b = mat[1][0];
        double c = mat[0][1];
        double d = mat[1][1];
        double e = mat[0][2];
        double f = mat[1][2];

        // Extract conic coefficients
        double A = coefs[0];
        double B = coefs[1];
        double C = coefs[2];
        double D = coefs[3];
        double E = coefs[4];
        double F = coefs[5];

		// Compute coefficients of the transformed conic
		double A2 = A * a * a + B * a * b + C * b * b;
		double B2 = 2 * (A * a * c + C * b * d) + B * (a * d + b * c);
		double C2 = A * c * c + B * c * d + C * d * d;
		double D2 = 2 * (A * a * e + C * b * f) + B * (a * f + b * e) + D * a + E * b;
		double E2 = 2 * (A * c * e + C * d * f) + B * (c * f + d * e) + D * c + E * d;
		double F2 = A * e * e + B * e * f + C * f * f + D * e + E * f + F;

        // Return the array of coefficients
        return new double[] { A2, B2, C2, D2, E2, F2 };
    }

    // -----------------------------------------------------------------
    // Some special conics

    static class ConicStraightLine2D extends StraightLine2D implements Conic2D {

        double[] coefs = new double[] { 0, 0, 0, 1, 0, 0 };

        public ConicStraightLine2D(StraightLine2D line) {
            super(line);
			coefs = new double[] { 0, 0, 0, dy, -dx, dx * y0 - dy * x0 };
        }

        public ConicStraightLine2D(double a, double b, double c) {
            super(StraightLine2D.createCartesian(a, b, c));
            coefs = new double[] { 0, 0, 0, a, b, c };
        }

        public double[] conicCoefficients() {
            return coefs;
        }

        public Type conicType() {
            return Conic2D.Type.STRAIGHT_LINE;
        }

        /** Return NaN. */
        public double eccentricity() {
            return Double.NaN;
        }

        @Override
        public ConicStraightLine2D reverse() {
            return new ConicStraightLine2D(super.reverse());
        }

        @Override
        public ConicStraightLine2D transform(AffineTransform2D trans) {
            return new ConicStraightLine2D(super.transform(trans));
        }
    }


    static class ConicTwoLines2D extends ContourArray2D<StraightLine2D>
            implements Conic2D {

        double xc = 0, yc = 0, d = 1, theta = 0;

        public ConicTwoLines2D(Point2D point, double d, double theta) {
            this(point.x(), point.y(), d, theta);
        }

        public ConicTwoLines2D(double xc, double yc, double d, double theta) {
            super();

            this.xc = xc;
            this.yc = yc;
            this.d = d;
            this.theta = theta;

            StraightLine2D baseLine = new StraightLine2D(
                    new Point2D(xc, yc), theta);
            this.add(baseLine.parallel(d));
            this.add(baseLine.parallel(-d).reverse());
        }

        public double[] conicCoefficients() {
            double[] coefs = { 0, 0, 1, 0, 0, -1 };
            AffineTransform2D sca = AffineTransform2D.createScaling(0, d);
            AffineTransform2D rot = AffineTransform2D.createRotation(theta);
            AffineTransform2D tra = AffineTransform2D.createTranslation(xc, yc);
            
            // AffineTransform2D trans = tra.compose(rot).compose(sca);
            AffineTransform2D trans = sca.chain(rot).chain(tra);
            return Conics2D.transform(coefs, trans);
        }

        public Type conicType() {
            return Conic2D.Type.TWO_LINES;
        }

        public double eccentricity() {
            return Double.NaN;
        }

        @Override
        public ConicTwoLines2D transform(AffineTransform2D trans) {
            Point2D center = new Point2D(xc, yc).transform(trans);
            StraightLine2D line = this.firstCurve().transform(trans);

            double dist = line.distance(center);
            double angle = line.horizontalAngle();
            return new ConicTwoLines2D(center, dist, angle);
        }

        @Override
        public ConicTwoLines2D reverse() {
            return new ConicTwoLines2D(xc, yc, -d, theta);
        }
    }

    // TODO: add CrossConic2D
}
