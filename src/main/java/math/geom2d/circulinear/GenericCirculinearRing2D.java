/**
 * File: 	GenericCirculinearRing2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 mai 09
 */
package math.geom2d.circulinear;

import java.awt.Graphics2D;
import java.util.Collection;

import math.geom2d.circulinear.buffer.BufferCalculator;
import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.IContinuousOrientedCurve2D;
import math.geom2d.transform.AffineTransform2D;
import math.geom2d.transform.CircleInversion2D;

/**
 * A basic implementation of a CirculinearRing2D, which is assumed to be always bounded and closed.
 * 
 * @author dlegland
 *
 */
public final class GenericCirculinearRing2D extends PolyCirculinearCurve2D<ICirculinearElement2D> implements ICirculinearRing2D {
    private static final long serialVersionUID = 1L;

    // TODO: parameterize with curve type ?

    /**
     * Static factory for creating a new GenericCirculinearRing2D from an array of curves.
     * 
     * @since 0.8.1
     */
    public static GenericCirculinearRing2D create(ICirculinearElement2D... curves) {
        return new GenericCirculinearRing2D(curves);
    }

    // ===================================================================
    // constructors

    public GenericCirculinearRing2D() {
        super();
        this.closed = true;
    }

    public GenericCirculinearRing2D(int size) {
        super(size);
        this.closed = true;
    }

    public GenericCirculinearRing2D(ICirculinearElement2D... curves) {
        super(curves);
        this.closed = true;
    }

    public GenericCirculinearRing2D(Collection<? extends ICirculinearElement2D> curves) {
        super(curves);
        this.closed = true;
    }

    // ===================================================================
    // methods specific to GenericCirculinearRing2D

    @Override
    public ICirculinearRing2D parallel(double dist) {
        BufferCalculator bc = BufferCalculator.getDefaultInstance();

        return new GenericCirculinearRing2D(bc.createContinuousParallel(this, dist).smoothPieces());
    }

    @Override
    public Collection<? extends GenericCirculinearRing2D> continuousCurves() {
        return wrapCurve(this);
    }

    @Override
    public GenericCirculinearRing2D transform(CircleInversion2D inv) {
        // Allocate array for result
        GenericCirculinearRing2D result = new GenericCirculinearRing2D(curves.size());

        // add each transformed element
        for (ICirculinearElement2D element : curves)
            result.add(element.transform(inv));
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Boundary2D#fill(java.awt.Graphics2D)
     */
    @Override
    public void fill(Graphics2D g2) {
        g2.fill(this.getGeneralPath());
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.domain.Boundary2D#domain()
     */
    @Override
    public ICirculinearDomain2D domain() {
        return new GenericCirculinearDomain2D(this);
    }

    @Override
    public GenericCirculinearRing2D reverse() {
        int n = curves.size();
        // create array of reversed curves
        ICirculinearElement2D[] curves2 = new ICirculinearElement2D[n];

        // reverse each curve
        for (int i = 0; i < n; i++)
            curves2[i] = curves.get(n - 1 - i).reverse();

        // create the reversed final curve
        return new GenericCirculinearRing2D(curves2);
    }

    @Override
    public BoundaryPolyCurve2D<IContinuousOrientedCurve2D> transform(AffineTransform2D trans) {
        // number of curves
        int n = this.size();

        // create result curve
        BoundaryPolyCurve2D<IContinuousOrientedCurve2D> result = new BoundaryPolyCurve2D<>(n);

        // add each curve after class cast
        for (IContinuousOrientedCurve2D curve : curves)
            result.add(curve.transform(trans));
        return result;
    }

}
