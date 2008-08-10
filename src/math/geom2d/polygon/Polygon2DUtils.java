/**
 * 
 */
package math.geom2d.polygon;

import math.geom2d.domain.BoundaryPolyCurve2D;
import math.geom2d.domain.BoundarySet2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.domain.GenericDomain2D;
import math.geom2d.domain.SmoothOrientedCurve2D;
import math.geom2d.line.ClosedPolyline2D;
import math.geom2d.line.Polyline2DUtils;

/**
 * @author dlegland
 *
 */
public abstract class Polygon2DUtils {

	public final static Domain2D createBuffer(Polygon2D polygon, double d){
		BoundarySet2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>> result = 
			new BoundarySet2D<BoundaryPolyCurve2D<SmoothOrientedCurve2D>>();
		
		for(ClosedPolyline2D polyline : polygon.getBoundary())
			result.addCurve(Polyline2DUtils.createClosedParallel(polyline,d));
		
		return new GenericDomain2D(result);
	}
}
