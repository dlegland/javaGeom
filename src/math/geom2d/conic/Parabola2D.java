/* file : Parabola2D.java
 * 
 * Project : geometry
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
 * 
 * Created on 29 janv. 2007
 *
 */
package math.geom2d.conic;

import java.util.*;

import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveSet2D;
import math.geom2d.curve.Curve2DUtil;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.domain.ContinuousBoundary2D;
import math.geom2d.domain.SmoothOrientedCurve2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.line.Polyline2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.StraightObject2D;
import math.geom2d.polygon.Rectangle2D;
import math.geom2d.transform.AffineTransform2D;

/**
 * A parabola, defined by its vertex, its orientation, and its pedal.
 * Orientation is defined as the orientation of derivative at vertex point, with
 * the second derivative pointing to the top.<p>
 * Following parametric representation is used:<p>
 * <code>x(t)=t </code>   <p>
 * <code>y(t)=a*t^2</code> <p>
 * This is a signed parameter (negative a makes the
 * parabola point to opposite side). 
 * @author dlegland
 */
public class Parabola2D implements SmoothOrientedCurve2D, Conic2D, ContinuousBoundary2D {
	/** Coordinate of the vertex*/
	protected double xv=0, yv=0;

	/** orientation of the parabola*/
	protected double theta=0;
	
	/** The parameter of the parabola. If positive, the parabola is direct. */
	protected double a=1;
	
	private boolean debug = false;
	
	/**
	 * Creates a parabola by supplying the vertex and the focus.
	 * @param vertex the vertex point of the parabola
	 * @param focus the focal point of the parabola
	 * @return the parabola with given vertex and focus
	 */
	public final static Parabola2D create(Point2D vertex, Point2D focus){
		double p = Point2D.getDistance(vertex, focus);
		double theta = Angle2D.getHorizontalAngle(vertex, focus) - Math.PI/2;
		return new Parabola2D(vertex, 1/(4*p), theta);
	}
	
	
	public Parabola2D() {
		super();
	}

	public Parabola2D(Point2D vertex, double a, double theta) {
		this(vertex.getX(), vertex.getY(), a, theta);
	}

	public Parabola2D(double xv, double yv, double a, double theta) {
		super();
		this.xv = xv;
		this.yv = yv;
		this.a = a;
		this.theta = theta;
	}

	// ==========================================================
	// methods specific to Parabola2D
	
	public Point2D getFocus() {
		double c = 1/a/4.0;
		return new Point2D(xv-c*Math.sin(theta), yv+c*Math.cos(theta));
	}
	
	public double getParameter(){
		return a;
	}
	
	public double getFocusDistance(){
		return 1.0/(4*a);
	}
	
	public Point2D getVertex(){
		return new Point2D(xv, yv);
	}

	/** 
	 * Change coordinate of the point to correspond to a standard parabola.
	 * Standard parabola s such that y=x^2 for every point of the parabola.
	 * @param point
	 * @return
	 */
	private Point2D formatPoint(java.awt.geom.Point2D point){
		Point2D p2 = new Point2D(point);
		p2 = p2.transform(AffineTransform2D.createTranslation(-xv, -yv));
		p2 = p2.transform(AffineTransform2D.createRotation(-theta));
		p2 = p2.transform(AffineTransform2D.createScaling(1, 1.0/a));
		return p2;
	}
	
	/** 
	 * Change coordinate of the line to correspond to a standard parabola.
	 * Standard parabola s such that y=x^2 for every point of the parabola.
	 * @param point
	 * @return
	 */
	private StraightObject2D formatLine(StraightObject2D line){
		line = line.transform(AffineTransform2D.createTranslation(-xv, -yv));
		line = line.transform(AffineTransform2D.createRotation(-theta));
		line = line.transform(AffineTransform2D.createScaling(1, 1.0/a));
		return line;
	}
	
	// ==========================================================
	// methods implementing the Conic2D interface

	public int getConicType() {
		return Conic2D.PARABOLA;
	}

//	public boolean isEllipse() {
//		return false;
//	}
//
//	public boolean isParabola() {
//		return true;
//	}
//
//	public boolean isHyperbola() {
//		return false;
//	}
//
//	public boolean isCircle() {
//		return false;
//	}
//
//	public boolean isStraightLine() {
//		return false;
//	}
//
//	public boolean isTwoLines() {
//		return false;
//	}
//
//	public boolean isPoint() {
//		return false;
//	}
//
//	public boolean isDegenerated() {
//		return false;
//	}

	/**
	 * @deprecated use getConicCoefficients instead
	 */
	@Deprecated
	public double[] getCartesianEquation() {
		return getConicCoefficients();
	}

	public double[] getConicCoefficients() {
		// computation shortcuts
		double cot 	= Math.cos(theta);
		double sit 	= Math.sin(theta);
		double cot2 = cot*cot;
		double sit2	= sit*sit;
		
		// Compute new coefficients after rotation of parabola located at
		// (xv,yv) by a rotation of angle theta around origin.
		return new double[]{
			a*cot2, 
			-2*a*cot*sit,
			a*sit2,
			-2*a*xv*cot - sit,
			2*a*xv*sit - cot,
			a*xv*xv + yv
		};
	}

//	public Point2D getCenter() {
//		return null;
//	}

//	public Point2D getFocus1() {
//		double c = 1/a/4.0;
//		return new Point2D(xv-c*Math.sin(theta), yv+c*Math.cos(theta));
//	}

//	/**
//	 * Always return Point2D.INFINITY_POINT.
//	 */
//	public Point2D getFocus2() {
//		return Point2D.INFINITY_POINT;
//	}

	/**
	 * return the first vector of the parabola
	 */
	public Vector2D getVector1() {
		Vector2D vect = new Vector2D(1, 0);
		return vect.transform(AffineTransform2D.createRotation(theta));
	}

	/** 
	 * return second director vector of the parabola.
	 */
	public Vector2D getVector2() {
		Vector2D vect = new Vector2D(1, 0);
		return vect.transform(AffineTransform2D.createRotation(theta+Math.PI/2));
	}

//	/**
//	 * return 0.
//	 */
//	public double getLength1() {
//		return 0;
//	}
//
//	/**
//	 * return 0.
//	 */
//	public double getLength2() {
//		return 0;
//	}

	/**
	 * Return 1, by definition for a parabola.
	 */
	public double getEccentricity() {
		return 1.0;
	}

	/**
	 * Return orientation angle of parabola. It is defined as the angle of
	 * the derivative at the vertex.
	 */
	public double getAngle() {
		return theta;
	}

	/**
	 * at the moment, return true.
	 */
	public boolean isDirect() {
		return a>0;
	}
	
	
	// ==========================================================
	// methods implementing the Boundary2D interface
	
	public Collection<ContinuousBoundary2D> getBoundaryCurves(){
		ArrayList<ContinuousBoundary2D> list = new ArrayList<ContinuousBoundary2D>(1);
		list.add(this);
		return list;
	}
	
	// ==========================================================
	// methods implementing the OrientedCurve2D interface

	public double getWindingAngle(java.awt.geom.Point2D point) {
		if(isInside(point))
			return Math.PI*2;
		else
			return 0.0;
	}

	public double getSignedDistance(java.awt.geom.Point2D p) {
		return getSignedDistance(p.getX(), p.getY());
	}

	public double getSignedDistance(double x, double y) {
		if(isInside(new Point2D(x, y)))
			return -getDistance(x, y);
		return -getDistance(x, y);
	}

	public boolean isInside(java.awt.geom.Point2D point) {
		// Process the point to be in a referentiel such that parabola is vertical
		Point2D p2 = formatPoint(point);
		
		// get coordinate of transformed point
		double x = p2.getX();
		double y = p2.getY();
		
		// check condition of parabola
		return y>x*x;
	}

	// ==========================================================
	// methods implementing the SmoothCurve2D interface
	
	public Vector2D getTangent(double t) {
		Vector2D vect = new Vector2D(1, 2.0*a*t);
		return vect.transform(AffineTransform2D.createRotation(theta));
	}

	/**
	 * returns the curvature of the ellipse.
	 */
	public double getCurvature(double t){
		double p2 = .25/a/a;
		return p2/Math.pow(t*t+p2, 3/2);		
	}
	
	
	// ==========================================================
	// methods implementing the ContinuousCurve2D interface

	/**
	 * return the polyline of the parabola arc from t=-100 to t=100.
	 */
	public Polyline2D getAsPolyline(int n){
		return new ParabolaArc2D(this, -100, 100).getAsPolyline(n);
	}

	public Collection<? extends SmoothCurve2D> getSmoothPieces() {
		ArrayList<Parabola2D> list = new ArrayList<Parabola2D>(1);
		list.add(this);
		return list;
	}

	public boolean isClosed() {
		return false;
	}


	// ==========================================================
	// methods implementing the Curve2D interface
	
	/** 
	 * Returns the parameter of the first point of the line, which is always Double.NEGATIVE_INFINITY.
	 */
	public double getT0(){
		return Double.NEGATIVE_INFINITY;
	}

	/** 
	 * Returns the parameter of the last point of the line, which is always Double.POSITIVE_INFINITY.
	 */
	public double getT1(){
		return Double.POSITIVE_INFINITY;
	}


	public Point2D getPoint(double t) {
		return getPoint(t, new Point2D());
	}

	public Point2D getPoint(double t, Point2D point) {
		if (point==null) point = new Point2D();
		point.setLocation(t, a*t*t);
		point = AffineTransform2D.createRotation(theta).transform(point);
		point = AffineTransform2D.createTranslation(xv, yv).transform(point);
		return point;
	}

	/**
	 * return the constant Point2D.INFINITY_POINT.
	 */
	public Point2D getFirstPoint() {
		return Point2D.INFINITY_POINT;
	}

	/**
	 * return the constant Point2D.INFINITY_POINT.
	 */
	public Point2D getLastPoint() {
		return Point2D.INFINITY_POINT;
	}

	public Collection<Point2D> getSingularPoints(){
		return new ArrayList<Point2D>(0);
	}
	
	/**
	 * Always returns false, as a parabola does not have any singular point.
	 */
	public boolean isSingular(double pos) {
		return false;
	}

	/**
	 * Returns position of point on the parabola. If point is not on the
	 * parabola returns the positions on its "vertical" projection
	 * (i.e. its projection parallel to the symetry axis of the parabola).
	 */
	public double getPosition(java.awt.geom.Point2D point) {
		// t parameter is x-coordinate of point
		return formatPoint(point).getX();
	}

	/**
	 * Returns position of point on the parabola. If point is not on the
	 * parabola returns the positions on its "vertical" projection
	 * (i.e. its projection parallel to the symetry axis of the parabola).
	 */
	public double project(java.awt.geom.Point2D point) {
		// t parameter is x-coordinate of point
		return formatPoint(point).getX();
	}

	public Collection<Point2D> getIntersections(StraightObject2D line) {
		// Computes the lines which corresponds to a "Unit" parabola.
		StraightObject2D line2 = this.formatLine(line);
		double dx = line2.getVector().getX();
		double dy = line2.getVector().getY();
		
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		// case of vertical or quasi-vertical line
		if(Math.abs(dx)<Shape2D.ACCURACY){
			if (debug)
				System.out.println("intersect parabola with vertical line ");
			double x = line2.getOrigin().getX();
			Point2D point = new Point2D(x, x*x);
			if(line2.contains(point))
				points.add(line.getPoint(line2.getPosition(point)));
			return points;			
		}

		// Extract formatted line parameters
		Point2D origin = line2.getOrigin();
		double x0 = origin.getX();
		double y0 = origin.getY();
		
		// Solve second order equation
		double k 	= dy/dx;		// slope of the line
		double yl 	= k*x0 - y0;
		double delta = k*k - 4*yl;
		
		// Case of a line 'below' the parabola
		if(delta<0) return points;
		
		// There are two intersections with supporting line,
		// need to check these points belong to the line.
		
		double x;
		Point2D point;
		StraightLine2D support = line2.getSupportLine();
		
		// test first intersection point
		x = (k - Math.sqrt(delta))*.5;
		point = new Point2D(x, x*x);
		if(line2.contains(support.getProjectedPoint(point)))
			points.add(line.getPoint(line2.getPosition(point)));
		
		// test second intersection point
		x = (k + Math.sqrt(delta))*.5;
		point = new Point2D(x, x*x);
		if(line2.contains(support.getProjectedPoint(point)))
			points.add(line.getPoint(line2.getPosition(point)));
		
		
		return points;
	}

	/**
	 * Returns the parabola with same vertex, direction vector in opposite
	 * direction and opposite parameter <code>p</code>.
	 */
	public Parabola2D getReverseCurve(){
		return new Parabola2D(xv, yv, -a, Angle2D.formatAngle(theta+Math.PI));
	}

	public Collection<ContinuousCurve2D> getContinuousCurves() {
		ArrayList<ContinuousCurve2D> list = new ArrayList<ContinuousCurve2D>(1);
		list.add(this);
		return list;
	}

	/**
	 * return a new ParabolaArc2D, or null if t1<t0.
	 */
	public ParabolaArc2D getSubCurve(double t0, double t1){
		if(debug)
			System.out.println("theta = " + Math.toDegrees(theta));
		if(t1<t0) return null;
		return new ParabolaArc2D(this, t0, t1);
	}

	public double getDistance(java.awt.geom.Point2D p) {
		return getDistance(p.getX(), p.getY());
	}

	public double getDistance(double x, double y) {
		// TODO Computes on polyline approximation, needs to compute on whole curve
		return new ParabolaArc2D(this, -100, 100).getDistance(x, y);
	}
	
	// methods implementing the Shape2D interface
	
	/** Always returns false, because a parabola is not bounded.*/
	public boolean isBounded(){
		return false;
	}

	/**
	 * Returns false, as a parabola is never empty.
	 */
	public boolean isEmpty(){
		return false;
	}

	/**
	 * Clip the parabola by a box. The result is an instance of
	 * CurveSet2D<ParabolaArc2D>, which 
	 * contains only instances of ParabolaArc2D. If the parabola is not
	 * clipped, the result is an instance of
	 * CurveSet2D<ParabolaArc2D> which contains 0 curves.
	 */
	public CurveSet2D<ParabolaArc2D> clip(Box2D box) {
		// Clip the curve
		CurveSet2D<SmoothCurve2D> set = Curve2DUtil.clipSmoothCurve(this, box);
		
		// Stores the result in appropriate structure
		CurveSet2D<ParabolaArc2D> result =
			new CurveSet2D<ParabolaArc2D> ();
		
		// convert the result
		for(Curve2D curve : set.getCurves()){
			if (curve instanceof ParabolaArc2D)
				result.addCurve((ParabolaArc2D) curve);
		}
		return result;
	}

	
	public Box2D getBoundingBox(){
		//TODO: manage parabolas with horizontal or vertical orientations
		return new Box2D(
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public Parabola2D transform(AffineTransform2D trans) {
		Point2D vertex = this.getVertex().transform(trans); 
		Point2D focus = this.getFocus().transform(trans);
		double a = 1/(4.0*Point2D.getDistance(vertex, focus));
		double theta = Angle2D.getHorizontalAngle(vertex, focus) - Math.PI/2;
		
		// check orientation of resulting parabola
		if(this.a<0 ^ trans.isDirect())
			// normal case
			return new Parabola2D(vertex, a, theta);
		else
			// inverted case
			return new Parabola2D(vertex, -a, theta+Math.PI);
	}

	/**
	 * Return bounding box of the shape.
	 */
	public java.awt.Rectangle getBounds(){
		return this.getBoundingBox().getAsAWTRectangle();
	}
	
	/**
	 * Return more precise bounds for the shape.
	 */
	public java.awt.geom.Rectangle2D getBounds2D(){
		return this.getBoundingBox().getAsAWTRectangle2D();
	}


	//	 methods implementing the Shape interface
	
	public boolean contains(double x, double y) {
		// Process the point to be in a basis such that parabola is vertical
		Point2D p2 = formatPoint(new Point2D(x, y));
		
		// get coordinate of transformed point
		double xp = p2.getX();
		double yp = p2.getY();
		
		// check condition of parabola
		return Math.abs(yp-xp*xp)<Shape2D.ACCURACY;
	}

	public boolean contains(java.awt.geom.Point2D point) {
		return contains(point.getX(), point.getY());
	}

	/**
	 * Returns true if one of the edges of the rectangle intersects the parabola.
	 */
	public boolean intersects(double xr, double yr, double wr, double hr) {
		for(LineSegment2D edge : new Rectangle2D(xr, yr, wr, hr).getEdges())
			if(this.getIntersections(edge).size()>0) 
				return true;
		return false;
	}

	/**
	 * Returns true if one of the edges of the rectangle intersects the parabola.
	 */
	public boolean intersects(java.awt.geom.Rectangle2D rect) {
		return intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * return false, as every curve.
	 */
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	/**
	 * return false, as every curve.
	 */
	public boolean contains(java.awt.geom.Rectangle2D arg0) {
		return false;
	}

	/** 
	 * returns the appended path of a ParabolaArc2D, with -100<t<100,
	 * transformed as a polyline with 201 points.
	 */
	public java.awt.geom.GeneralPath appendPath(java.awt.geom.GeneralPath path){
		double t0 = -100;
		double t1 = 100;
		Point2D point = getPoint(t0);
		for(double t=t0; t<=t1; t+=1){
			point = getPoint(t);
			path.lineTo((float)point.getX(), (float)point.getY());
		}
		point = getPoint(t1);
		path.lineTo((float)point.getX(), (float)point.getY());
			
		return path;
	}

	/**
	 * compute path of a ParabolaArc2D with -100<t<100.
	 */
	public java.awt.geom.GeneralPath getGeneralPath(){
		return new ParabolaArc2D(this, -100, 100).getGeneralPath();
	}
	
	/**
	 * return path iterator of a ParabolaArc2D with -100<t<100.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans) {
		return getGeneralPath().getPathIterator(trans);
	}

	/**
	 * return path iterator of a ParabolaArc2D with -100<t<100.
	 */
	public java.awt.geom.PathIterator getPathIterator(java.awt.geom.AffineTransform trans, double flatness) {
		return getGeneralPath().getPathIterator(trans, flatness);
	}
	// ====================================================================
	// Methods inherited from object interface
	
	
	public boolean equals(Object obj){
		if(!(obj instanceof Parabola2D)) return false;
		Parabola2D parabola = (Parabola2D) obj;
		
		if ((this.xv-parabola.xv)>Shape2D.ACCURACY) return false;
		if ((this.yv-parabola.yv)>Shape2D.ACCURACY) return false;
		if ((this.a-parabola.a)>Shape2D.ACCURACY) return false;
		if ((Angle2D.formatAngle(this.theta)-Angle2D.formatAngle(parabola.theta))>Shape2D.ACCURACY)
			return false;
		
		return true;
	}
	
}
