/*
 * File : AllTests.java
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
 * author : Legland
 * Created on 30 déc. 2003
 */
package math.geom2d;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Legland
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for math.geom2d");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(AffineTransform2DTest.class));
		suite.addTest(new TestSuite(Angle2DTest.class));
		suite.addTest(new TestSuite(BezierCurve2DTest.class));
		suite.addTest(new TestSuite(Box2DTest.class));
		suite.addTest(new TestSuite(Circle2DTest.class));
		suite.addTest(new TestSuite(CircleArc2DTest.class));
		suite.addTest(new TestSuite(ClosedPolyline2DTest.class));
		suite.addTest(new TestSuite(CurveSet2DTest.class));
		suite.addTest(new TestSuite(LineSegment2DTest.class));
		suite.addTest(new TestSuite(Ellipse2DTest.class));
		suite.addTest(new TestSuite(EllipseArc2DTest.class));
		suite.addTest(new TestSuite(HalfPlane2DTest.class));
		suite.addTest(new TestSuite(Hyperbola2DTest.class));
		suite.addTest(new TestSuite(HyperbolaBranch2DTest.class));
		suite.addTest(new TestSuite(LineArc2DTest.class));
		suite.addTest(new TestSuite(LineObject2DTest.class));
		suite.addTest(new TestSuite(LineReflection2DTest.class));
		suite.addTest(new TestSuite(MultiPolygon2DTest.class));
		suite.addTest(new TestSuite(Parabola2DTest.class));
		suite.addTest(new TestSuite(ParabolaArc2DTest.class));
		suite.addTest(new TestSuite(Point2DTest.class));
		suite.addTest(new TestSuite(Polygon2DTest.class));
		suite.addTest(new TestSuite(Polyline2DTest.class));
		suite.addTest(new TestSuite(PolyCurve2DTest.class));
		suite.addTest(new TestSuite(PolyOrientedCurve2DTest.class));
		suite.addTest(new TestSuite(Ray2DTest.class));
		suite.addTest(new TestSuite(Rectangle2DTest.class));
		suite.addTest(new TestSuite(Scaling2DTest.class));
		suite.addTest(new TestSuite(StraightLine2DTest.class));
		suite.addTest(new TestSuite(StraightObject2DTest.class));
		suite.addTest(new TestSuite(Translation2DTest.class));
		suite.addTest(new TestSuite(Vector2DTest.class));
		//$JUnit-END$
		return suite;
	}
}
