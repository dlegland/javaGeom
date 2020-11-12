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
 * Created on 30 d�c. 2003
 */
package net.javageom.geom2d.circulinear;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Legland
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for math.geom2d.circulinear");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(CirculinearContourArray2DTest.class));
		suite.addTest(new TestSuite(CirculinearCurves2DTest.class));
		suite.addTest(new TestSuite(CirculinearCurveArray2DTest.class));
		suite.addTest(new TestSuite(GenericCirculinearDomain2DTest.class));
		suite.addTest(new TestSuite(GenericCirculinearRing2DTest.class));
		suite.addTest(new TestSuite(PolyCirculinearCurve2DTest.class));
        //$JUnit-END$
		return suite;
	}
}
