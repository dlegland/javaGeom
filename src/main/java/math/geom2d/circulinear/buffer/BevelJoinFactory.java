/**
 * File: 	BevelJoinFactory.java
 * Project: javageom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 5 janv. 2011
 */
package math.geom2d.circulinear.buffer;

import math.geom2d.Point2D;
import math.geom2d.circulinear.ICirculinearElement2D;
import math.geom2d.line.LineSegment2D;

/**
 * @author dlegland
 *
 */
public class BevelJoinFactory implements IJoinFactory {

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.circulinear.buffer.JoinFactory#createJoin(math.geom2d.circulinear.CirculinearElement2D, math.geom2d.circulinear.CirculinearElement2D, double)
     */
    @Override
    public LineSegment2D createJoin(ICirculinearElement2D curve1, ICirculinearElement2D curve2, double dist) {
        Point2D p1 = curve1.lastPoint();
        Point2D p2 = curve2.firstPoint();
        return new LineSegment2D(p1, p2);
    }
}
