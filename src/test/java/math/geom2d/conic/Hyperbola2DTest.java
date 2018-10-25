package math.geom2d.conic;

import junit.framework.TestCase;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.IShape2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;

public class Hyperbola2DTest extends TestCase {

    public void testGetCenter() {
        Hyperbola2D hyper = new Hyperbola2D();
        assertEquals(hyper.getCenter(), new Point2D(0, 0));
    }

    public void testGetFocus1() {
        Hyperbola2D hyper = new Hyperbola2D();
        assertEquals(hyper.getFocus1(), new Point2D(Math.sqrt(2), 0));
    }

    public void testGetFocus2() {
        Hyperbola2D hyper = new Hyperbola2D();
        assertEquals(hyper.getFocus2(), new Point2D(-Math.sqrt(2), 0));
    }

    public void testReduceCentered() {
        double[] coefs = { 1. / 400., 0, -1. / 100. };
        Hyperbola2D hyp0 = Hyperbola2D.reduceCentered(coefs);
        assertTrue(hyp0.almostEquals(new Hyperbola2D(0, 0, 20, 10, 0, true), IShape2D.ACCURACY));

        double[] coefs2 = { 1. / 400., 0, -1. / 100., 0, 0, -1 };
        Hyperbola2D hyp2 = Hyperbola2D.reduceCentered(coefs2);
        assertTrue(hyp2.almostEquals(new Hyperbola2D(0, 0, 20, 10, 0, true), IShape2D.ACCURACY));

        double[] coefs3 = { 1., 0, -4., 0, 0, -400 };
        Hyperbola2D hyp3 = Hyperbola2D.reduceCentered(coefs3);
        assertTrue(hyp3.almostEquals(new Hyperbola2D(0, 0, 20, 10, 0, true), IShape2D.ACCURACY));
        // double theta = Math.PI/3;
        // double[] rotCoefs = Conic2DUtils.transformCentered(coefs,
        // AffineTransform2D.createRotation(theta));
        // Ellipse2D ellRot = Ellipse2D.reduceCentered(rotCoefs);
        // assertTrue(ellRot.equals(new Ellipse2D(0, 0, 20, 10, theta)));
    }

    public void testTransformCentered() {
        Hyperbola2D hyp0 = new Hyperbola2D(0, 0, 20, 10, 0, true);

        // Check rotation of an ellipse
        double theta = Math.PI / 3;
        AffineTransform2D rot60 = AffineTransform2D.createRotation(Math.PI / 3);
        Hyperbola2D hypRot = Hyperbola2D.transformCentered(hyp0, rot60);
        Hyperbola2D expRot = new Hyperbola2D(0, 0, 20, 10, theta, true);
        assertTrue(hypRot.almostEquals(expRot, IShape2D.ACCURACY));

        // Check scaling of an ellipse
        double sx = 2.5;
        double sy = 3;
        AffineTransform2D sca = AffineTransform2D.createScaling(sx, sy);
        Hyperbola2D hypSca = Hyperbola2D.transformCentered(hyp0, sca);
        Hyperbola2D expSca = new Hyperbola2D(0, 0, 20. * sx, 10. * sy, 0, true);
        assertTrue(hypSca.almostEquals(expSca, IShape2D.ACCURACY));

        // Check scaling and rotation
        Hyperbola2D hypBoth = Hyperbola2D.transformCentered(hypSca, rot60);
        Hyperbola2D expBoth = new Hyperbola2D(0, 0, 20. * sx, 10. * sy, theta, true);
        assertTrue(hypBoth.almostEquals(expBoth, IShape2D.ACCURACY));

    }

    public void testGetConicCoefficients() {
        double xc = 20;
        double yc = 30;
        double a1 = 1;
        double b1 = 1;
        double a2 = 20;
        double b2 = 10;
        double theta = Math.PI / 3;

        Hyperbola2D hyperbola, hyperbola2;
        IConic2D conic;

        double eps = IShape2D.ACCURACY;

        Point2D origin = new Point2D(0, 0);
        Point2D center = new Point2D(xc, yc);

        // Hyperbola centered and reduced
        hyperbola = new Hyperbola2D(origin, a1, b1, 0);
        double[] coefs = hyperbola.conicCoefficients();
        assertEquals(coefs[0], 1, 1e-14);
        assertEquals(coefs[1], 0, 1e-14);
        assertEquals(coefs[2], -1, 1e-14);
        assertEquals(coefs[3], 0, 1e-14);
        assertEquals(coefs[4], 0, 1e-14);
        assertEquals(coefs[5], -1, 1e-14);

        conic = Conics2D.reduceConic(coefs);
        assertTrue(conic.conicType() == IConic2D.Type.HYPERBOLA);
        hyperbola2 = (Hyperbola2D) conic;
        assertTrue(hyperbola2.almostEquals(hyperbola, eps));

        // Hyperbola reduced but not located at origin
        hyperbola = new Hyperbola2D(center, a1, b1, 0);
        coefs = hyperbola.conicCoefficients();
        assertEquals(coefs[0], 1, 1e-14);
        assertEquals(coefs[1], 0, 1e-14);
        assertEquals(coefs[2], -1, 1e-14);
        assertEquals(coefs[3], -40, 1e-14);
        assertEquals(coefs[4], 60, 1e-14);
        assertEquals(coefs[5], -501, 1e-14);

        conic = Conics2D.reduceConic(coefs);
        assertTrue(conic.conicType() == IConic2D.Type.HYPERBOLA);
        hyperbola2 = (Hyperbola2D) conic;
        assertTrue(hyperbola2.getCenter().almostEquals(center, eps));
        assertTrue(hyperbola2.almostEquals(hyperbola, eps));

        // hyperbola not reduced, not at origin, but oriented east-west
        hyperbola = new Hyperbola2D(center, a2, b2, 0);
        coefs = hyperbola.conicCoefficients();
        conic = Conics2D.reduceConic(coefs);
        assertTrue(conic.conicType() == IConic2D.Type.HYPERBOLA);
        hyperbola2 = (Hyperbola2D) conic;
        assertTrue(hyperbola2.getCenter().almostEquals(center, eps));
        assertTrue(hyperbola2.almostEquals(hyperbola, eps));

        // hyperbola centered, scaled, rotated
        hyperbola = new Hyperbola2D(center, a2, b2, theta);
        coefs = hyperbola.conicCoefficients();
        conic = Conics2D.reduceConic(coefs);
        assertTrue(conic.conicType() == IConic2D.Type.HYPERBOLA);
        hyperbola2 = (Hyperbola2D) conic;
        assertTrue(hyperbola2.getCenter().almostEquals(center, eps));
        assertTrue(hyperbola2.almostEquals(hyperbola, eps));
    }

    public void testClipBox() {
        Hyperbola2D hyperbola = new Hyperbola2D(0, 0, 1, 1, 0, true);
        Box2D box = new Box2D(-2, 2, -2, 2);

        // test number of intersections
        ICurveSet2D<?> clipped = hyperbola.clip(box);
        assertTrue(clipped.size() == 2);

        // test class of curve portions
        ICurve2D curve1 = clipped.firstCurve();
        assertTrue(curve1 instanceof HyperbolaBranchArc2D);
        ICurve2D curve2 = clipped.lastCurve();
        assertTrue(curve2 instanceof HyperbolaBranchArc2D);

        // extract curve arcs
        HyperbolaBranchArc2D arc1 = (HyperbolaBranchArc2D) curve1;
        HyperbolaBranchArc2D arc2 = (HyperbolaBranchArc2D) curve2;
        assertTrue(arc1.getHyperbolaBranch().getHyperbola().equals(hyperbola));
        assertTrue(arc2.getHyperbolaBranch().getHyperbola().equals(hyperbola));

        // Translate and scale the hypebola

        double x0 = 50, y0 = 50, a = 10, b = 10, theta = 0;
        boolean direct = true;
        hyperbola = new Hyperbola2D(x0, y0, a, b, theta, direct);
        box = new Box2D(x0 - 2 * a, x0 + 2 * a, y0 - 2 * b, y0 + 2 * b);

        // test number of intersections
        clipped = hyperbola.clip(box);
        assertTrue(clipped.size() == 2);

        // test class of curve portions
        curve1 = clipped.firstCurve();
        assertTrue(curve1 instanceof HyperbolaBranchArc2D);
        curve2 = clipped.lastCurve();
        assertTrue(curve2 instanceof HyperbolaBranchArc2D);

        // extract curve arcs
        arc1 = (HyperbolaBranchArc2D) curve1;
        arc2 = (HyperbolaBranchArc2D) curve2;
        assertTrue(arc1.getHyperbolaBranch().getHyperbola().equals(hyperbola));
        assertTrue(arc2.getHyperbolaBranch().getHyperbola().equals(hyperbola));
    }

}
