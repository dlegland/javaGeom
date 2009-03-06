/* File LineObject2D.java 
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

package math.geom2d.line;

import math.geom2d.Point2D;

// Imports

/**
 * Line object defined from 2 points. This object keep points reference in
 * memory, and recomputes properties directly from points. LineObject2D is
 * mutable.
 * <p>
 * Example :
 * <p>
 * <code>
 * // Create an Edge2D<br>
 * LineObject2D line = new LineObject2D(new Point2D(0, 0), new Point2D(1, 2));<br>
 * // Change direction of line, by changing second point :<br>
 * line.setPoint2(new Point2D(4, 5));<br>
 * // Change position and direction of the line, by changing first point. <br>
 * // 'line' is now the edge (2,3)-(4,5)<br>
 * line.setPoint1(new Point2D(2, 3));<br>
 * </code>
 * <p>
 * <p>
 * This class is maybe slower than Edge2D or StraightLine2D, because parameters
 * are updated each time a computation is made, causing lot of additional
 * processing.
 * @deprecated use Line2D instead
 */
@Deprecated
public class LineObject2D extends Line2D implements Cloneable {


    // ===================================================================
    // constructors

    /** Define a new LineObject2D with two extremities. */
    public LineObject2D(Point2D point1, Point2D point2) {
        super(point1, point2);
    }

    /** Define a new LineObject2D with two extremities. */
    public LineObject2D(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    // ===================================================================
    // Methods specific to LineObject2D


    @Override
    public Line2D clone() {
        return new Line2D(p1.clone(), p2.clone());
    }
}