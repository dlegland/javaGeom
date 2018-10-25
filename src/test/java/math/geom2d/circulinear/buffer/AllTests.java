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
package math.geom2d.circulinear.buffer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Legland
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for math.geom2d.circulinear");
        // $JUnit-BEGIN$
        suite.addTest(new TestSuite(BufferCalculatorTest.class));
        suite.addTest(new TestSuite(ButtCapFactoryTest.class));
        suite.addTest(new TestSuite(BevelJoinFactoryTest.class));
        suite.addTest(new TestSuite(MiterJoinFactoryTest.class));
        suite.addTest(new TestSuite(RoundCapFactoryTest.class));
        suite.addTest(new TestSuite(RoundJoinFactoryTest.class));
        suite.addTest(new TestSuite(SquareCapFactoryTest.class));
        suite.addTest(new TestSuite(TriangleCapFactoryTest.class));
        // $JUnit-END$
        return suite;
    }
}
