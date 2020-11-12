/**
 * 
 */

package net.javageom.geom2d.grid;

import java.util.ArrayList;
import java.util.Collection;

import net.javageom.geom2d.Box2D;
import net.javageom.geom2d.Point2D;
import net.javageom.geom2d.Shape2D;
import net.javageom.geom2d.line.LineSegment2D;
import net.javageom.geom2d.point.PointArray2D;
import net.javageom.geom2d.point.PointSet2D;

/**
 * Defines a square grid, which can have different size in each direction. The
 * grid is always parallel to the main axes.
 * 
 * @author dlegland
 */
public class SquareGrid2D implements Grid2D {

    double x0 = 0;
    double y0 = 0;

    double sx = 1;
    double sy = 1;

    public SquareGrid2D() {

    }

    public SquareGrid2D(Point2D origin) {
        this(origin.x(), origin.y(), 1, 1);
    }

    public SquareGrid2D(Point2D origin, double s) {
        this(origin.x(), origin.y(), s, s);
    }

    public SquareGrid2D(Point2D origin, double sx, double sy) {
        this(origin.x(), origin.y(), sx, sy);
    }

    public SquareGrid2D(double x0, double y0, double s) {
        this(x0, y0, s, s);
    }

    public SquareGrid2D(double s) {
        this(0, 0, s, s);
    }

    public SquareGrid2D(double sx, double sy) {
        this(0, 0, sx, sy);
    }

    public SquareGrid2D(double x0, double y0, double sx, double sy) {
        this.x0 = x0;
        this.y0 = y0;
        this.sx = sx;
        this.sy = sy;
    }

    /**
     * @deprecated grids are supposed to be immutable (0.8.0)
     */
    @Deprecated
    public void setOrigin(Point2D point) {
        this.x0 = point.x();
        this.y0 = point.y();
    }

    public Point2D getOrigin() {
        return new Point2D(x0, y0);
    }

    public double getSizeX() {
        return sx;
    }

    public double getSizeY() {
        return sy;
    }

    /**
     * @deprecated grids are supposed to be immutable (0.8.0)
     */
    @Deprecated
    public void setSize(double s) {
        sx = s;
        sy = s;
    }

    /**
     * @deprecated grids are supposed to be immutable (0.8.0)
     */
    @Deprecated
    public void setSize(double sx, double sy) {
        this.sx = sx;
        this.sy = sy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.grid.Grid2D#getClosestVertex(math.geom2d.Point2D)
     */
	public Point2D getClosestVertex(Point2D point) {
		double nx = Math.round((point.x() - x0) / sx);
		double ny = Math.round((point.y() - y0) / sy);
		return new Point2D(nx * sx + x0, ny * sy + y0);
	}

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.grid.Grid2D#getEdges(math.geom2d.Box2D)
     */
    public Collection<LineSegment2D> getEdges(Box2D box) {
        double x, y; // iterations
        double xmin, ymin, xmax, ymax; // limits
        double xi, yi; // first point in the box

        // extract bounds of the box
        xmin = box.getMinX();
        ymin = box.getMinY();
        xmax = box.getMaxX();
        ymax = box.getMaxY();

        // coordinates of first vertex in the box
		xi = Math.ceil((xmin - x0) / sx) * sx + x0;
		yi = Math.ceil((ymin - y0) / sy) * sy + y0;

    ArrayList<LineSegment2D> array = new ArrayList<LineSegment2D>();

        // add horizontal lines
		for (y = yi; y - ymax < Shape2D.ACCURACY; y += sy)
            array.add(new LineSegment2D(xmin, y, xmax, y));

        // add vertical lines
		for (x = xi; x - xmax < Shape2D.ACCURACY; x += sx)
            array.add(new LineSegment2D(x, ymin, x, ymax));

        // return the set of lines
        return array;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math.geom2d.grid.Grid2D#getVertices(math.geom2d.Box2D)
     */
    public PointSet2D getVertices(Box2D box) {
        double x, y; // iterations
        double xmin, ymin, xmax, ymax; // limits
        double xi, yi; // first point in the box

        // extract bounds of the box
        xmin = box.getMinX();
        ymin = box.getMinY();
        xmax = box.getMaxX();
        ymax = box.getMaxY();

        // coordinates of first vertex in the box
        xi = Math.ceil((xmin-x0)/sx)*sx+x0;
        yi = Math.ceil((ymin-y0)/sy)*sy+y0;

        ArrayList<Point2D> array = new ArrayList<Point2D>();

        // iterate on lines in each direction
        for (y = yi; y-ymax<Shape2D.ACCURACY; y += sy)
            for (x = xi; x-xmax<Shape2D.ACCURACY; x += sx)
                array.add(new Point2D(x, y));

        // return the set of lines
        return new PointArray2D(array);
    }
}
