package math.geom2d.conic;

import junit.framework.TestCase;

public class ConicUtilTest extends TestCase {

	/**
	 * Constructor for Circle2DTest.
	 * @param arg0
	 */
	public ConicUtilTest(String arg0) {
		super(arg0);
	}
	
	
	public void testReduceConicParabola(){
		double[] coefs;
		Conic2D conic;
		double eps = 1e-14;
		
		coefs = new double[]{1, 0, 0, 0, -1, 0};
		conic = ConicUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], 1, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);
		
		coefs = new double[]{5, 0, 0, 0, -1, 0};
		conic = ConicUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], 5, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 0, eps);
		assertEquals(coefs[3], 0, eps);
		assertEquals(coefs[4], -1, eps);
		assertEquals(coefs[5], 0, eps);

		
		coefs = new double[]{0, 0, 1, -1, 0, 0};
		conic = ConicUtil.reduceConic(coefs);
		assertTrue(conic instanceof Parabola2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], 0, eps);
		assertEquals(coefs[1], 0, eps);
		assertEquals(coefs[2], 1, eps);
		assertEquals(coefs[3], 1, eps);
		assertEquals(coefs[4], 0, eps);
		assertEquals(coefs[5], 0, eps);
		
		double a2 = Math.sqrt(2)/2.;
		coefs = new double[]{.5, -1, .5, -a2, -a2, 0};
		conic = ConicUtil.reduceConic(coefs);
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
			coefs0 = ellipse.getCartesianEquation();
			conic = ConicUtil.reduceConic(coefs0);
		}
		
		a = 4; b = 2;
		coefs0 = new double[]{b*b, 0, a*a, 0, 0, -a*a*b*b};
		conic = ConicUtil.reduceConic(coefs0);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], coefs0[0], eps);
		assertEquals(coefs[1], coefs0[1], eps);
		assertEquals(coefs[2], coefs0[2], eps);
		assertEquals(coefs[3], coefs0[3], eps);
		assertEquals(coefs[4], coefs0[4], eps);
		assertEquals(coefs[5], coefs0[5], eps);
		
		
		a = 4; b = 2; xc=3; yc=5; theta=0;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		coefs0 = ellipse.getCartesianEquation();
		conic = ConicUtil.reduceConic(coefs0);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], coefs0[0], eps);
		assertEquals(coefs[1], coefs0[1], eps);
		assertEquals(coefs[2], coefs0[2], eps);
		assertEquals(coefs[3], coefs0[3], eps);
		assertEquals(coefs[4], coefs0[4], eps);
		assertEquals(coefs[5], coefs0[5], eps);

		a = 2; b = 1; xc=0; yc=0; theta=Math.PI/3;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		coefs0 = ellipse.getCartesianEquation();
		conic = ConicUtil.reduceConic(coefs0);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], coefs0[0], eps);
		assertEquals(coefs[1], coefs0[1], eps);
		assertEquals(coefs[2], coefs0[2], eps);
		assertEquals(coefs[3], coefs0[3], eps);
		assertEquals(coefs[4], coefs0[4], eps);
		assertEquals(coefs[5], coefs0[5], eps);

		a = 4; b = 2; xc=3; yc=5; theta=Math.PI/3;
		ellipse = new Ellipse2D(xc, yc, a, b, theta);
		coefs0 = ellipse.getCartesianEquation();
		conic = ConicUtil.reduceConic(coefs0);
		assertTrue(conic instanceof Ellipse2D);
		coefs = conic.getCartesianEquation();
		assertEquals(coefs[0], coefs0[0], eps);
		assertEquals(coefs[1], coefs0[1], eps);
		assertEquals(coefs[2], coefs0[2], eps);
		assertEquals(coefs[3], coefs0[3], eps);
		assertEquals(coefs[4], coefs0[4], eps);
		assertEquals(coefs[5], coefs0[5], eps);

	}
	
}
