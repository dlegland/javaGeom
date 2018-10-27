/**
 * 
 */
package math.geom2d.spline;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import math.geom2d.Box2D;
import math.geom2d.IGeometricObject2D;
import math.geom2d.IShape2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.IContinuousCurve2D;
import math.geom2d.curve.ICurve2D;
import math.geom2d.curve.ICurveSet2D;
import math.geom2d.curve.ISmoothCurve2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.line.ILinearShape2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.point.Point2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * @author dlegland
 *
 */
public class GeneralPath2D implements ICurve2D, Serializable {
    private static final long serialVersionUID = 1L;

    // ===================================================================
    // Static variables and constants

    private enum Type {
        MOVE, LINE, QUAD, CUBIC, CLOSE;
    }

    // ===================================================================
    // class variables

    /**
     * A collection of elementary segments that can be linear, quad, conic, move or close
     */
    ArrayList<Segment> segments;

    /**
     * The last creation option of this path, used to know which action are allowed.
     */
    Type lastType = Type.CLOSE;

    // ===================================================================
    // constructors

    /**
     * Initialize an empty path.
     */
    public GeneralPath2D() {
        this.segments = new ArrayList<>();
    }

    /**
     * Copy constructor.
     */
    public GeneralPath2D(GeneralPath2D path) {

        // init local segment array
        this.segments = new ArrayList<>(path.segments.size());

        Point2D[] pts;

        // iterate on the collection of segments
        for (Segment seg : path.segments) {
            switch (seg.type()) {
            case MOVE:
                pts = seg.controlPoints();
                this.moveTo(pts[0]);
                break;

            case LINE:
                pts = seg.controlPoints();
                this.lineTo(pts[0]);
                break;

            case QUAD:
                pts = seg.controlPoints();
                this.quadTo(pts[0], pts[1]);
                break;

            case CUBIC:
                pts = seg.controlPoints();
                this.cubicTo(pts[0], pts[1], pts[2]);
                break;

            case CLOSE:
            default:
                this.closePath();
                break;
            }
        }
    }

    // ===================================================================
    // methods specific to GeneralPath2D

    public void moveTo(Point2D p) {
        this.segments.add(new MoveSegment(p));
        this.lastType = Type.MOVE;
    }

    public void lineTo(Point2D p) {
        this.segments.add(new LinearSegment(p));
        this.lastType = Type.LINE;
    }

    public void quadTo(Point2D p1, Point2D p2) {
        this.segments.add(new QuadSegment(p1, p2));
        this.lastType = Type.QUAD;
    }

    public void cubicTo(Point2D p1, Point2D p2, Point2D p3) {
        this.segments.add(new CubicSegment(p1, p2, p3));
        this.lastType = Type.CUBIC;
    }

    public void closePath() {
        // Cannot close a path more than once
        if (this.lastType == Type.CLOSE)
            return;
        this.segments.add(ClosingSegment.INSTANCE);
    }

    /**
     * Returns a collection of smooth curves corresponding to the elementary segments.
     */
    private Collection<ISmoothCurve2D> smoothCurves() {
        Point2D lastControl = null;
        Point2D lastStart = null;
        Point2D[] pts;

        int n = this.segments.size();
        ArrayList<ISmoothCurve2D> curves = new ArrayList<>(n);

        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                // update last control and initial point
                lastStart = seg.lastControl();
                lastControl = lastStart;
                break;

            case LINE:
                pts = seg.controlPoints();
                curves.add(new LineSegment2D(lastControl, pts[0]));
                lastControl = pts[0];
                break;

            case QUAD:
                pts = seg.controlPoints();
                curves.add(new QuadBezierCurve2D(lastControl, pts[0], pts[1]));
                lastControl = pts[1];
                break;

            case CUBIC:
                pts = seg.controlPoints();
                curves.add(new CubicBezierCurve2D(lastControl, pts[0], pts[1], pts[2]));
                lastControl = pts[2];
                break;

            case CLOSE:
                // connect to the last initial point
                curves.add(new LineSegment2D(lastControl, lastStart));
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        // Returns the set of curves
        return curves;
    }

    private ISmoothCurve2D segmentCurve(int index) {
        // Check index validity
        int n = this.segments.size();
        if (index > n - 2)
            throw new IllegalArgumentException("Index must be lower than segment number");

        ISmoothCurve2D curve = null;
        Point2D lastControl = null;
        Point2D lastStart = null;
        Point2D[] pts;

        for (int i = 0; i < index + 2; i++) {
            Segment seg = this.segments.get(i);
            switch (seg.type()) {
            case MOVE:
                // update last control and initial point
                lastStart = seg.lastControl();
                lastControl = lastStart;
                break;

            case LINE:
                pts = seg.controlPoints();
                curve = new LineSegment2D(lastControl, pts[0]);
                lastControl = pts[0];
                break;

            case QUAD:
                pts = seg.controlPoints();
                curve = new QuadBezierCurve2D(lastControl, pts[0], pts[1]);
                lastControl = pts[1];
                break;

            case CUBIC:
                pts = seg.controlPoints();
                curve = new CubicBezierCurve2D(lastControl, pts[0], pts[1], pts[2]);
                lastControl = pts[2];
                break;

            case CLOSE:
                // connect to the last initial point
                curve = new LineSegment2D(lastControl, lastStart);
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        // Returns the set of curves
        return curve;
    }

    // ===================================================================
    // Methods implementing the Curve2D interface

    /**
     * The first parameterization value is equal to 0.
     */
    @Override
    public double t0() {
        return 0;
    }

    /**
     * The last parameterization value is given by the number of elementary operations (moveTo, closePath, lineTo...) minus one (for the initial move).
     */
    @Override
    public double t1() {
        return segments.size() - 1;
    }

    @Override
    public Point2D point(double t) {
        // get curve segment index
        int index = (int) Math.floor(t);

        // Special case of last point
        if (index == this.segments.size() - 1 && Math.abs(t - index) < IShape2D.ACCURACY)
            return this.lastPoint();

        // extract curve segment
        ISmoothCurve2D curve = segmentCurve(index);
        if (curve == null) {
            throw new RuntimeException("Can not manage position for MOVE Path segments");
        }

        // convert from global to local coordinates
        double t0 = curve.t0();
        double t1 = curve.t1();
        double t2 = (t - index) * (t1 - t0) + t0;

        // delegate processing to sub-curve instance
        return curve.point(t2);
    }

    /**
     * Returns the first point of the curve, or null if this curve is empty.
     */
    @Override
    public Point2D firstPoint() {
        if (this.segments.isEmpty())
            return null;
        return this.segments.get(0).controlPoints()[0];
    }

    /**
     * Returns the last point of the curve, or null if this curve is empty.
     */
    @Override
    public Point2D lastPoint() {
        int n = this.segments.size();
        if (n == 0)
            return null;
        return this.segments.get(n - 1).lastControl();
    }

    @Override
    public Collection<Point2D> singularPoints() {
        // allocate memory for result
        ArrayList<Point2D> points = new ArrayList<>(this.segments.size());

        // iterate on segments, and add the last control of each segment
        for (Segment seg : this.segments) {
            Point2D p = seg.lastControl();
            if (p != null)
                points.add(p);
        }

        // return the set of singular points
        return points;
    }

    @Override
    public Collection<Point2D> vertices() {
        // allocate memory for result
        ArrayList<Point2D> vertices = new ArrayList<>(this.segments.size());

        // iterate on segments, and add the control points of each segment
        for (Segment seg : this.segments) {
            for (Point2D p : seg.controlPoints()) {
                vertices.add(p);
            }
        }

        // return the set of vertices
        return vertices;
    }

    @Override
    public boolean isSingular(double pos) {
        return Math.abs(pos - Math.round(pos)) < IShape2D.ACCURACY;
    }

    @Override
    public double position(Point2D point) {
        return this.project(point);
    }

    @Override
    public double project(Point2D point) {
        // local variables for computing position
        int index = -1;
        double pos = Double.NaN;
        double minDist = Double.MAX_VALUE;
        double dist;

        // local variables for iterating on segment curves
        Point2D lastControl = null;
        Point2D lastStart = null;
        Point2D[] pts;
        ISmoothCurve2D curve;

        int n = this.segments.size();

        for (int i = 0; i < n; i++) {
            Segment seg = segments.get(i);
            switch (seg.type()) {
            case MOVE:
                // update last control and initial point
                lastStart = seg.lastControl();
                lastControl = lastStart;
                continue;

            case LINE:
                pts = seg.controlPoints();
                curve = new LineSegment2D(lastControl, pts[0]);
                lastControl = pts[0];
                break;

            case QUAD:
                pts = seg.controlPoints();
                curve = new QuadBezierCurve2D(lastControl, pts[0], pts[1]);
                lastControl = pts[1];
                break;

            case CUBIC:
                pts = seg.controlPoints();
                curve = new CubicBezierCurve2D(lastControl, pts[0], pts[1], pts[2]);
                lastControl = pts[2];
                break;

            case CLOSE:
                // connect to the last initial point
                curve = new LineSegment2D(lastControl, lastStart);
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }

            // Compute position on local curve
            dist = curve.distance(point);
            if (dist < minDist) {
                minDist = dist;
                index = i - 1;
                pos = index + curve.position(point);
            }
        }

        // Returns the set of curves
        return pos;
    }

    @Override
    public Collection<Point2D> intersections(ILinearShape2D line) {
        // allocate array for the result
        ArrayList<Point2D> pts = new ArrayList<>();

        // Iterate on the set of curves, and compute intersections
        for (ISmoothCurve2D curve : this.smoothCurves()) {
            pts.addAll(curve.intersections(line));
        }

        // returns the full set of intersections
        return pts;
    }

    @Override
    public ICurve2D reverse() {
        ArrayList<IContinuousCurve2D> list = splitContinuousCurves();
        Collections.reverse(list);
        return new CurveArray2D<>(list);
    }

    @Override
    public Collection<? extends IContinuousCurve2D> continuousCurves() {
        return splitContinuousCurves();
    }

    private ArrayList<IContinuousCurve2D> splitContinuousCurves() {
        Point2D lastControl = null;
        Point2D lastStart = null;
        Point2D[] pts;

        int n = this.segments.size();
        ArrayList<IContinuousCurve2D> curveList = new ArrayList<>(n);

        // the current continuous curve
        PolyCurve2D<ISmoothCurve2D> curve = null;

        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                // If current curve is not null, add it the the set
                if (curve != null)
                    curveList.add(curve);
                curve = new PolyCurve2D<>();

                // update last control and initial point
                lastStart = seg.lastControl();
                lastControl = lastStart;
                break;

            case LINE:
                pts = seg.controlPoints();
                curve.add(new LineSegment2D(lastControl, pts[0]));
                lastControl = pts[0];
                break;

            case QUAD:
                pts = seg.controlPoints();
                curve.add(new QuadBezierCurve2D(lastControl, pts[0], pts[1]));
                lastControl = pts[1];
                break;

            case CUBIC:
                pts = seg.controlPoints();
                curve.add(new CubicBezierCurve2D(lastControl, pts[0], pts[1], pts[2]));
                lastControl = pts[2];
                break;

            case CLOSE:
                // connect and close to the last initial point
                curve.add(new LineSegment2D(lastControl, lastStart));
                curve.setClosed(true);

                // add current curve to the list, and reset it
                curveList.add(curve);
                curve = new PolyCurve2D<>();

                // update for next start
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        // Returns the set of continuous curves
        return curveList;
    }

    @Override
    public ICurve2D subCurve(double t0, double t1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Transform to a java Path2D object.
     */
    @Override
    public Path2D asAwtShape() {
        // creates the awt path
        Path2D.Double path = new Path2D.Double();

        // iterate on the path segments
        for (Segment seg : this.segments) {
            seg.updatePath(path);
        }

        // returns the updated path
        return path;
    }

    // ===================================================================
    // Methods implementing the Shape2D interface

    @Override
    public boolean contains(Point2D p) {
        return this.contains(p.x(), p.y());
    }

    @Override
    public boolean contains(double x, double y) {
        Point2D lastControl = null;
        Point2D lastStart = null;

        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                lastStart = seg.lastControl();
                lastControl = lastStart;
                break;

            case LINE:
            case QUAD:
            case CUBIC:
                if (seg.asCurve(lastControl, lastStart).contains(x, y))
                    return true;
                lastControl = seg.lastControl();
                break;

            case CLOSE:
                if (seg.asCurve(lastControl, lastStart).contains(x, y))
                    return true;
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        return false;
    }

    @Override
    public double distance(Point2D p) {
        return this.distance(p.x(), p.y());
    }

    @Override
    public double distance(double x, double y) {
        double minDist = Double.MAX_VALUE;
        double dist;

        Point2D lastControl = null;
        Point2D lastStart = null;

        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                lastStart = seg.lastControl();
                lastControl = lastStart;
                break;

            case LINE:
            case QUAD:
            case CUBIC:
                dist = seg.asCurve(lastControl, lastStart).distance(x, y);
                minDist = Math.min(dist, minDist);
                lastControl = seg.lastControl();
                break;

            case CLOSE:
                dist = seg.asCurve(lastControl, lastStart).distance(x, y);
                minDist = Math.min(dist, minDist);
                lastControl = lastStart;
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        return minDist;
    }

    /**
     * Returns true, as a curve composed of Bezier pieces is always bounded.
     */
    @Override
    public boolean isBounded() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.segments.size() > 0;
    }

    @Override
    public Box2D boundingBox() {
        // Initialize with extreme values
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        // coordinates of current point
        double x, y;

        // Iterate on each control point of each segment
        for (Segment seg : this.segments) {
            for (Point2D p : seg.controlPoints()) {
                // get current coordinates
                x = p.x();
                y = p.y();

                // update bounds
                xmin = Math.min(xmin, x);
                ymin = Math.min(ymin, y);
                xmax = Math.max(xmax, x);
                ymax = Math.max(ymax, y);
            }
        }

        // create a new Box2D with the bounds
        return new Box2D(xmin, xmax, ymin, ymax);
    }

    @Override
    public ICurveSet2D<? extends ICurve2D> clip(Box2D box) {
        ArrayList<IContinuousCurve2D> list = splitContinuousCurves();
        ICurve2D curve = new CurveArray2D<>(list);
        return curve.clip(box);
    }

    @Override
    public ICurve2D transform(AffineTransform2D trans) {
        GeneralPath2D path = new GeneralPath2D();
        Point2D[] pts;

        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                path.moveTo(seg.lastControl().transform(trans));
                break;

            case LINE:
                path.lineTo(seg.lastControl().transform(trans));
                break;

            case QUAD:
                pts = seg.controlPoints();
                path.quadTo(pts[0].transform(trans), pts[1].transform(trans));
                break;

            case CUBIC:
                pts = seg.controlPoints();
                path.cubicTo(pts[0].transform(trans), pts[1].transform(trans), pts[2].transform(trans));
                break;

            case CLOSE:
                path.closePath();
                break;

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        return path;
    }

    @Override
    public void draw(Graphics2D g2) {
        Path2D path = this.asAwtShape();
        g2.draw(path);
    }

    // ===================================================================
    // Methods implementing the GeometricObject2D interface

    @Override
    public boolean almostEquals(IGeometricObject2D obj, double eps) {
        // Check class of object
        if (obj == null)
            return false;
        if (!(obj instanceof GeneralPath2D))
            return false;

        // class cast
        GeneralPath2D that = (GeneralPath2D) obj;

        // Paths should have same number of segments
        if (this.segments.size() != that.segments.size())
            return false;

        Segment seg1, seg2;
        Point2D[] pts1, pts2;

        for (int i = 0; i < this.segments.size(); i++) {
            // extract each segment
            seg1 = this.segments.get(i);
            seg2 = that.segments.get(i);

            // check segments have same type
            if (seg1.type() != seg2.type())
                return false;

            // extract control points
            pts1 = seg1.controlPoints();
            pts2 = seg2.controlPoints();

            // check size of control point arrays
            if (pts1.length != pts2.length)
                throw new RuntimeException("Two path segments have type but different number of control points");

            // check identity of control points
            for (int j = 0; j < pts1.length; j++) {
                if (!pts1[j].almostEquals(pts2[j], eps))
                    return false;
            }
        }

        // if no difference was found, then the paths are almost equal
        return true;
    }

    // ===================================================================
    // Methods from the Object superclass

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lastType == null) ? 0 : lastType.hashCode());
        result = prime * result + ((segments == null) ? 0 : segments.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeneralPath2D other = (GeneralPath2D) obj;
        if (lastType != other.lastType)
            return false;
        if (segments == null) {
            if (other.segments != null)
                return false;
        } else if (!segments.equals(other.segments))
            return false;
        return true;
    }

    @Override
    public GeneralPath2D clone() {
        GeneralPath2D path = new GeneralPath2D();
        Point2D[] pts;

        // iterate on the collection of segments
        for (Segment seg : this.segments) {
            switch (seg.type()) {
            case MOVE:
                pts = seg.controlPoints();
                path.moveTo(pts[0]);
                break;

            case LINE:
                pts = seg.controlPoints();
                path.lineTo(pts[0]);
                break;

            case QUAD:
                pts = seg.controlPoints();
                path.quadTo(pts[0], pts[1]);
                break;

            case CUBIC:
                pts = seg.controlPoints();
                path.cubicTo(pts[0], pts[1], pts[2]);
                break;

            case CLOSE:
                path.closePath();

            default:
                throw new RuntimeException("Unknown Path segment type: " + seg.type());
            }
        }

        // return the new path
        return path;
    }

    // ===================================================================
    // Declaration and implementation of the path segments

    private interface Segment {
        public Type type();

        public Point2D[] controlPoints();

        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart);

        /**
         * Returns the last control point of the segment, or null if the segment type is CLOSE.
         */
        public Point2D lastControl();

        public void updatePath(Path2D path);
    }

    private static class MoveSegment implements Segment, Serializable {
        private static final long serialVersionUID = 1L;

        Point2D p;

        public MoveSegment(Point2D p) {
            this.p = p;
        }

        @Override
        public Point2D[] controlPoints() {
            return new Point2D[] { p };
        }

        @Override
        public Type type() {
            return Type.MOVE;
        }

        /**
         * Returns null.
         */
        @Override
        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart) {
            return null;
        }

        @Override
        public Point2D lastControl() {
            return p;
        }

        @Override
        public void updatePath(Path2D path) {
            path.moveTo(p.x(), p.y());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((p == null) ? 0 : p.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MoveSegment other = (MoveSegment) obj;
            if (p == null) {
                if (other.p != null)
                    return false;
            } else if (!p.equals(other.p))
                return false;
            return true;
        }

    }

    private static class LinearSegment implements Segment, Serializable {
        private static final long serialVersionUID = 1L;

        Point2D p;

        public LinearSegment(Point2D p) {
            this.p = p;
        }

        @Override
        public Point2D[] controlPoints() {
            return new Point2D[] { p };
        }

        @Override
        public Type type() {
            return Type.LINE;
        }

        @Override
        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart) {
            return new LineSegment2D(lastControl, p);
        }

        @Override
        public Point2D lastControl() {
            return p;
        }

        @Override
        public void updatePath(Path2D path) {
            path.lineTo(p.x(), p.y());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((p == null) ? 0 : p.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LinearSegment other = (LinearSegment) obj;
            if (p == null) {
                if (other.p != null)
                    return false;
            } else if (!p.equals(other.p))
                return false;
            return true;
        }
    }

    private static class QuadSegment implements Segment, Serializable {
        private static final long serialVersionUID = 1L;

        Point2D p1;
        Point2D p2;

        public QuadSegment(Point2D p1, Point2D p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public Point2D[] controlPoints() {
            return new Point2D[] { p1, p2 };
        }

        @Override
        public Type type() {
            return Type.QUAD;
        }

        @Override
        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart) {
            return new QuadBezierCurve2D(lastControl, p1, p2);
        }

        @Override
        public Point2D lastControl() {
            return p2;
        }

        @Override
        public void updatePath(Path2D path) {
            path.quadTo(p1.x(), p1.y(), p2.x(), p2.y());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
            result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            QuadSegment other = (QuadSegment) obj;
            if (p1 == null) {
                if (other.p1 != null)
                    return false;
            } else if (!p1.equals(other.p1))
                return false;
            if (p2 == null) {
                if (other.p2 != null)
                    return false;
            } else if (!p2.equals(other.p2))
                return false;
            return true;
        }
    }

    private static class CubicSegment implements Segment, Serializable {
        private static final long serialVersionUID = 1L;

        Point2D p1;
        Point2D p2;
        Point2D p3;

        public CubicSegment(Point2D p1, Point2D p2, Point2D p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        @Override
        public Point2D[] controlPoints() {
            return new Point2D[] { p1, p2, p3 };
        }

        @Override
        public Type type() {
            return Type.CUBIC;
        }

        @Override
        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart) {
            return new CubicBezierCurve2D(lastControl, p1, p2, p3);
        }

        @Override
        public Point2D lastControl() {
            return p3;
        }

        @Override
        public void updatePath(Path2D path) {
            path.curveTo(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
            result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
            result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CubicSegment other = (CubicSegment) obj;
            if (p1 == null) {
                if (other.p1 != null)
                    return false;
            } else if (!p1.equals(other.p1))
                return false;
            if (p2 == null) {
                if (other.p2 != null)
                    return false;
            } else if (!p2.equals(other.p2))
                return false;
            if (p3 == null) {
                if (other.p3 != null)
                    return false;
            } else if (!p3.equals(other.p3))
                return false;
            return true;
        }
    }

    private static class ClosingSegment implements Segment, Serializable {
        private static final long serialVersionUID = 1L;

        public static final ClosingSegment INSTANCE = new ClosingSegment();

        private ClosingSegment() {
        }

        @Override
        public Point2D[] controlPoints() {
            return new Point2D[] {};
        }

        @Override
        public Type type() {
            return Type.CLOSE;
        }

        @Override
        public ISmoothCurve2D asCurve(Point2D lastControl, Point2D lastStart) {
            return new LineSegment2D(lastControl, lastStart);
        }

        @Override
        public Point2D lastControl() {
            return null;
        }

        @Override
        public void updatePath(Path2D path) {
            path.closePath();
        }
    }
}
