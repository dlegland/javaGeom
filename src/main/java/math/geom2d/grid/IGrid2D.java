/**
 * 
 */

package math.geom2d.grid;

import java.util.Collection;

import math.geom2d.Box2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.point.IPointSet2D;
import math.geom2d.point.Point2D;

/**
 * Defines a grid for snapping mouse pointer. The main purpose of a grid is to find the closest vertex to a given point. It also provides methods for accessing the collection of vertices and edges visible in a Box2D.
 * 
 * @author dlegland
 */
public interface IGrid2D {

    public Point2D getOrigin();

    public IPointSet2D getVertices(Box2D box);

    public Collection<LineSegment2D> getEdges(Box2D box);

    public Point2D getClosestVertex(Point2D point);
}
