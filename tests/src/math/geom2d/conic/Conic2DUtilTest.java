package math.geom2d.conic;

import math.geom2d.transform.AffineTransform2D;
import junit.framework.TestCase;

public class Conic2DUtilTest extends TestCase {

	/**
	 * Constructor for Circle2DTest.
	 * @param arg0
	 */
	public Conic2DUtilTest(String arg0) {
		super(arg0);
	}
	
	public void testTransformCenteredEllipse(){
		double[] coefs = {.01, 0, .05};
		AffineTransform2D rot30 = AffineTransform2D.createRotation(Math.PI/3);
		double[] transformed = Conic2DUtil.transformCentered(coefs, rot30);
		
		double[] back = Conic2DUtil.transformCentered(transformed,
				AffineTransform2D.createRotation(-Math.PI/3));
		for(int i=0; i<3; i++)
			assertEquals(coefs[i], back[i], 1e-12);
	}
	
	public void testReduceConicParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		coefs = new double[]{1, 0, 0, 0, -1, 0};
		conic = Conic2DUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 1, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);
		
		coefs = new double[]{5, 0, 0, 0, -1, 0};
		conic = Conic2DUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 5, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);

		
		coefs = new double[]{0, 0, 1, -1, 0, 0};
		conic = Conic2DUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], 0, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 1, eps);
		assertEquals(coefs[3], 1, eps);
		assertEquals(coefs[4], 0, eps);
		assertEquals(coefs[5], 0, eps);
		
		double a2 = Math.sqrt(2)/2.;
		coefs = new double[]{.5, -1, .5, -a2, -a2, 0};
		conic = Conic2DUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
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
			conic = Conic2DUtil.reduceConic(coefs0);
		}
		
		a = 4; b = 2;
		double delta = a*a*b*b;
		coefs0 = new double[]{b*b/delta, 0, a*a/delta, 0, 0, -1};
		conic = Conic2DUtil.reduceConic(coefs0);
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
		conic = Conic2DUtil.reduceConic(coefsTranslated);
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
		conic = Conic2DUtil.reduceConic(coefsRotated);
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
		conic = Conic2DUtil.reduceConic(coefsRTR);
		
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsRTR[0], eps);
		assertEquals(coefs[1], coefsRTR[1], eps);
		assertEquals(coefs[2], coefsRTR[2], eps);
		assertEquals(coefs[3], coefsRTR[3], eps);
		assertEquals(coefs[4], coefsRTR[4], eps);
		assertEquals(coefs[5], coefsRTR[5], eps);
		
	
		conic = Conic2DUtil.reduceConic(coefsRotTrans);
		
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getConicCoefficients();
		assertEquals(coefs[0], coefsRotTrans[0], eps);
		assertEquals(coefs[1], coefsRotTrans[1], eps);
		assertEquals(coefs[2], coefsRotTrans[2], eps);
		assertEquals(coefs[3], coefsRotTrans[3], eps);
		assertEquals(coefs[4], coefsRotTrans[4], eps);
		assertEquals(coefs[5], coefsRotTrans[5], eps);
	}
}
