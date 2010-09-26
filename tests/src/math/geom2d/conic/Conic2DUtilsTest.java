package math.geom2d.conic;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Angle2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.line.StraightLine2D;

public class Conic2DUtilsTest extends TestCase {

	/**
	 * Constructor for Circle2DTest.
	 * @param arg0
	 */
	public Conic2DUtilsTest(String arg0) {
		super(arg0);
	}
	
	public void testTransformCenteredEllipse(){
		double[] coefs = {.01, 0, .05};
		AffineTransform2D rot30 = AffineTransform2D.createRotation(Math.PI/3);
		double[] transformed = Conic2DUtils.transformCentered(coefs, rot30);
		
		double[] back = Conic2DUtils.transformCentered(transformed,
				AffineTransform2D.createRotation(-Math.PI/3));
		for(int i=0; i<3; i++)
			assertEquals(coefs[i], back[i], 1e-12);
	}
	
	public void testTransformEllipse(){
		Ellipse2D ell0 		= new Ellipse2D(0, 0, 40, 30, 0);
		double[] coefs 		= ell0.getConicCoefficients();

		double[] coefs2;
		Conic2D conic;
		
		double eps = Shape2D.ACCURACY;
		
		// Ellipse transformed by an identity
		AffineTransform2D id 	= new AffineTransform2D();
		coefs2 	= Conic2DUtils.transform(coefs, id);
		conic 	= Conic2DUtils.reduceConic(coefs2);
		assertTrue(ell0.almostEquals(conic, eps));

		// Ellipse transformed by a translation
		AffineTransform2D tra 	= AffineTransform2D.createTranslation(10, 20);
		Ellipse2D ellTra 		= new Ellipse2D(10, 20, 40, 30, 0);
		coefs2 	= Conic2DUtils.transform(coefs, tra);
		conic 	= Conic2DUtils.reduceConic(coefs2);
		assertTrue(ellTra.almostEquals(conic, eps));

		// Ellipse transformed by a rotation
		AffineTransform2D rot 	= AffineTransform2D.createRotation(Math.PI/6);
		Ellipse2D ellRot 		= new Ellipse2D(0, 0, 40, 30, Math.PI/6);
		coefs2 	= Conic2DUtils.transform(coefs, rot);
		conic 	= Conic2DUtils.reduceConic(coefs2);
		assertTrue(ellRot.almostEquals(conic, eps));

		// Ellipse transformed by a rotation then by a translation
		//AffineTransform2D RT 	= tra.compose(rot);
		AffineTransform2D RT 	= rot.chain(tra);
		Ellipse2D ellRotTra		= new Ellipse2D(10, 20, 40, 30, Math.PI/6);
		coefs2 	= Conic2DUtils.transform(coefs, RT);
		conic 	= Conic2DUtils.reduceConic(coefs2);
		assertTrue(ellRotTra.almostEquals(conic, eps));
		
		// Ellipse transformed by a scaling
		AffineTransform2D sca 	= AffineTransform2D.createScaling(3, 2);
		Ellipse2D ellSca		= new Ellipse2D(0, 0, 120, 60, 0);
		coefs2 	= Conic2DUtils.transform(coefs, sca);
		conic 	= Conic2DUtils.reduceConic(coefs2);
		assertTrue(ellSca.almostEquals(conic, eps));
	}
	
	public void testReduceConic_HorizontalCenteredParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		// Base equaltion is a*y^2=x -> a*y2 -x = 0
		coefs = new double[]{0, 0, 1, -1, 0, 0};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 0, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 1, eps);
		assertEquals(coefs[3], -1, eps);
		assertEquals(coefs[4], 0, eps);
		assertEquals(coefs[5], 0, eps);
		
		double a2 = Math.sqrt(2)/2.;
		coefs = new double[]{.5, -1, .5, -a2, -a2, 0};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
	}
	
	public void testReduceConic_HorizontalParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		Point2D vertex = new Point2D(2, 3);
		Parabola2D parabola = new Parabola2D(vertex, 1, 3*Math.PI/2);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(Angle2D.formatAngle(parabola.getAngle()), 
				3*Math.PI/2, eps);
		assertEquals(parabola.getParameter(), 1, eps);	
	
		parabola = new Parabola2D(vertex, 3, 3*Math.PI/2);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(Angle2D.formatAngle(parabola.getAngle()), 
				3*Math.PI/2, eps);
		assertEquals(parabola.getParameter(), 3, eps);
	}	
		
	public void testReduceConic_VerticalCenteredParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		coefs = new double[]{1, 0, 0, 0, -1, 0};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 1, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);
		
		coefs = new double[]{5, 0, 0, 0, -1, 0};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 5, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);
	}
	
	public void testReduceConic_VerticalParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		Point2D vertex = new Point2D(2, 3);
		Parabola2D parabola = new Parabola2D(vertex, 1, 0);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(parabola.getAngle(), 0, eps);
		assertEquals(parabola.getParameter(), 1, eps);			

		
		parabola = new Parabola2D(vertex, 3, 0);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(Angle2D.formatAngle(parabola.getAngle()), 
				0, eps);
		assertEquals(parabola.getParameter(), 3, eps);
	}	
		
	public void testReduceConic_DiagonalCenteredParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		Point2D vertex = new Point2D(0,0);
		double theta = 11*Math.PI/6;
		Parabola2D parabola = new Parabola2D(vertex, 1, theta);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(parabola.getAngle(), theta, eps);
		assertEquals(parabola.getParameter(), 1, eps);			

		theta = Math.PI/3;
		parabola = new Parabola2D(vertex, 1, theta);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(parabola.getAngle(), theta, eps);
		assertEquals(parabola.getParameter(), 1, eps);
	}
	
	public void testReduceConic_RotatedParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		Point2D vertex = new Point2D(2, 0);
		double theta = Math.PI/6;
		vertex = vertex.transform(AffineTransform2D.createRotation(theta));
		Parabola2D parabola = new Parabola2D(vertex, 1, theta-Math.PI/2);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertTrue(parabola.getVertex().almostEquals(vertex, eps));
		assertEquals(parabola.getAngle(), theta, eps);
		assertEquals(parabola.getParameter(), 1, eps);			
	}
	
	public void testReduceConic_DiagonalParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		Point2D vertex = new Point2D(2, 3);
		double theta = 11*Math.PI/6;
		Parabola2D parabola = new Parabola2D(vertex, 1, theta);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(parabola.getAngle(), theta, eps);
		assertEquals(parabola.getParameter(), 1, eps);			

		theta = Math.PI/3;
		parabola = new Parabola2D(vertex, 1, theta);
		coefs = parabola.getConicCoefficients();
		
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		parabola = (Parabola2D) conic;
		assertEquals(parabola.getVertex(), vertex);
		assertEquals(parabola.getAngle(), theta, eps);
		assertEquals(parabola.getParameter(), 1, eps);
	}
	
	public void testReduceConicEllipse(){
		double[] coefs;
		double[] coefs0;
		Conic2D conic;
		double a, b, xc, yc, theta;
		double eps = 1e-10;
		Ellipse2D ellipse;
		
		for(double t=0; t<Math.PI/2; t+=Math.PI/16){
			ellipse = new Ellipse2D(0, 0, 2, 1, t);
			coefs0 = ellipse.getConicCoefficients();
			conic = Conic2DUtils.reduceConic(coefs0);
		}
		
		a = 4; b = 2;
		double delta = a*a*b*b;
		coefs0 = new double[]{b*b/delta, 0, a*a/delta, 0, 0, -1};
		conic = Conic2DUtils.reduceConic(coefs0);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefs0[0], eps);
		assertEquals(coefs[1], coefs0[1], eps);
		assertEquals(coefs[2], coefs0[2], eps);
		assertEquals(coefs[3], coefs0[3], eps);
		assertEquals(coefs[4], coefs0[4], eps);
		assertEquals(coefs[5], coefs0[5], eps);
		
		
		a = 4; b = 2; xc=3; yc=5; theta=0;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		double[] coefsTranslated = ellipse.getConicCoefficients();
		conic = Conic2DUtils.reduceConic(coefsTranslated);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsTranslated[0], eps);
		assertEquals(coefs[1], coefsTranslated[1], eps);
		assertEquals(coefs[2], coefsTranslated[2], eps);
		assertEquals(coefs[3], coefsTranslated[3], eps);
		assertEquals(coefs[4], coefsTranslated[4], eps);
		assertEquals(coefs[5], coefsTranslated[5], eps);

		a = 4; b = 2; xc=0; yc=0; theta=Math.PI/3;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		double[] coefsRotated = ellipse.getConicCoefficients();
		conic = Conic2DUtils.reduceConic(coefsRotated);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsRotated[0], eps);
		assertEquals(coefs[1], coefsRotated[1], eps);
		assertEquals(coefs[2], coefsRotated[2], eps);
		assertEquals(coefs[3], coefsRotated[3], eps);
		assertEquals(coefs[4], coefsRotated[4], eps);
		assertEquals(coefs[5], coefsRotated[5], eps);

		a = 4; b = 2; xc=3; yc=5; theta=Math.PI/3;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		double[] coefsRotTrans = ellipse.getConicCoefficients();
		
		Ellipse2D rotated = ellipse.transform(AffineTransform2D.createRotation(-theta));
		double[] coefsRTR = rotated.getConicCoefficients();
		conic = Conic2DUtils.reduceConic(coefsRTR);
		
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsRTR[0], eps);
		assertEquals(coefs[1], coefsRTR[1], eps);
		assertEquals(coefs[2], coefsRTR[2], eps);
		assertEquals(coefs[3], coefsRTR[3], eps);
		assertEquals(coefs[4], coefsRTR[4], eps);
		assertEquals(coefs[5], coefsRTR[5], eps);
		
	
		conic = Conic2DUtils.reduceConic(coefsRotTrans);
		
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsRotTrans[0], eps);
		assertEquals(coefs[1], coefsRotTrans[1], eps);
		assertEquals(coefs[2], coefsRotTrans[2], eps);
		assertEquals(coefs[3], coefsRotTrans[3], eps);
		assertEquals(coefs[4], coefsRotTrans[4], eps);
		assertEquals(coefs[5], coefsRotTrans[5], eps);
	}

	public void testReduceConicStraightLine(){
		double[] coefs;
		Conic2D conic;
		StraightLine2D line;
		
		double eps = Shape2D.ACCURACY;
		Point2D origin = new Point2D(0, 0);
		
		// Vertical line
		coefs = new double[]{0, 0, 0, 1, 0, -1};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicStraightLine2D);
		line = (StraightLine2D) conic;
		assertEquals(line.getDistance(origin), 1, eps);
		assertEquals(line.getHorizontalAngle(), 3*Math.PI/2, eps);

		// Horizontal line
		coefs = new double[]{0, 0, 0, 0, 1, -1};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicStraightLine2D);
		line = (StraightLine2D) conic;
		assertEquals(line.getDistance(origin), 1, eps);
		assertEquals(line.getHorizontalAngle(), 0, eps);
		
		// Diagonal line
		coefs = new double[]{0, 0, 0, -1, 1, -1};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicStraightLine2D);
		line = (StraightLine2D) conic;
		assertEquals(line.getDistance(origin), Math.sqrt(2)/2, eps);
		assertEquals(line.getHorizontalAngle(), Math.PI/4, eps);
		
		// line with slope 1/2 and going through (0, 2.5)
		coefs = new double[]{0, 0, 0, -1, 2, -5};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicStraightLine2D);
		line = (StraightLine2D) conic;
		assertEquals(line.getDistance(origin), Math.sqrt(5), eps);
		assertEquals(line.getHorizontalAngle(), Math.atan(.5), eps);	
	}
	
	public void testReduceConicTwoStraightLines(){
		double[] coefs;
		Conic2D conic;
		Conic2DUtils.ConicTwoLines2D lines;
		
		double eps = Shape2D.ACCURACY;
		Point2D origin = new Point2D(0, 0);
		
		// Horizontal lines
		coefs = new double[]{0, 0, 1, 0, 0, -1};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicTwoLines2D);
		assertEquals(conic.getConicType(), Conic2D.Type.TWO_LINES);
		lines = (Conic2DUtils.ConicTwoLines2D) conic;
		assertEquals(lines.getDistance(origin), 1, eps);
		
		// Horizontal lines (different from 1)
		coefs = new double[]{0, 0, 1, 0, 0, -4};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicTwoLines2D);
		assertEquals(conic.getConicType(), Conic2D.Type.TWO_LINES);
		lines = (Conic2DUtils.ConicTwoLines2D) conic;
		assertEquals(lines.getDistance(origin), 2, eps);
		
		// Vertical lines
		coefs = new double[]{1, 0, 0, 0, 0, -1};
		conic = Conic2DUtils.reduceConic(coefs);
		assertTrue(conic instanceof Conic2DUtils.ConicTwoLines2D);
		assertEquals(conic.getConicType(), Conic2D.Type.TWO_LINES);
		lines = (Conic2DUtils.ConicTwoLines2D) conic;
		assertEquals(lines.getDistance(origin), 1, eps);
		
	}
	
	public void testTransformConicTwoStraightLines(){
		double[] coefs, coefs2;
		Conic2D conic;
		Conic2DUtils.ConicTwoLines2D lines;
		
		double eps = Shape2D.ACCURACY;
		Point2D origin = new Point2D(0, 0);
		
		// Horizontal lines
		coefs = new double[]{0, 0, 1, 0, 0, -1};
		
		Point2D center = new Point2D(20, 30);
		
		// Translation
		AffineTransform2D tra = AffineTransform2D.createTranslation(20, 30);
		coefs2 = Conic2DUtils.transform(coefs, tra);		
		conic = Conic2DUtils.reduceConic(coefs2);
		assertTrue(conic instanceof Conic2DUtils.ConicTwoLines2D);
		assertEquals(conic.getConicType(), Conic2D.Type.TWO_LINES);
		lines = (Conic2DUtils.ConicTwoLines2D) conic;
		assertEquals(lines.getDistance(center), 1, eps);
				
		// Rotation
		AffineTransform2D rot = AffineTransform2D.createRotation(Math.PI/6);
		coefs2 = Conic2DUtils.transform(coefs, rot);		
		conic = Conic2DUtils.reduceConic(coefs2);
		assertTrue(conic instanceof Conic2DUtils.ConicTwoLines2D);
		assertEquals(conic.getConicType(), Conic2D.Type.TWO_LINES);
		lines = (Conic2DUtils.ConicTwoLines2D) conic;
		assertEquals(lines.getDistance(origin), 1, eps);
	}
}
