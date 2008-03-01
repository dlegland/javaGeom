/* file : ParabolaArc2DTest.java
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
 * Created on 6 mai 2007
 *
 */
package math.geom2d.conic;

import math.geom2d.Point2D;
import math.geom2d.line.Polyline2D;
import junit.framework.TestCase;

public class ParabolaArc2DTest extends TestCase {

	/**
	 * Constructor for Point2DTest.
	 * @param arg0
	 */
	public ParabolaArc2DTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.awtui.TestRunner.run(ParabolaArc2DTest.class);
	}

	public void testGetPoint(){
		
		// Vertical parabola
		ParabolaArc2D arc = new ParabolaArc2D(
				new Parabola2D(0, 0, 1, 0), -5, 5);		
		Point2D p0;
		
		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(2, 4));
		
		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(-1, 1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(-2, 4));


		// Horizontal parabola (pointing to the right)
		arc = new ParabolaArc2D(new Parabola2D(0, 0, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(0, 0));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(1, -1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(4, -2));

		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(1, 1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(4, 2));


		// Shifted horizontal parabola
		arc = new ParabolaArc2D(new Parabola2D(20, 10, 1, -Math.PI/2), -5, 5);	

		// origin
		p0 = arc.getPoint(0);
		assertEquals(p0, new Point2D(20, 10));

		// after origin
		p0 = arc.getPoint(1);
		assertEquals(p0, new Point2D(20+1, 10-1));
		p0 = arc.getPoint(2);
		assertEquals(p0, new Point2D(20+4, 10-2));

		// before origin
		p0 = arc.getPoint(-1);
		assertEquals(p0, new Point2D(20+1, 10+1));
		p0 = arc.getPoint(-2);
		assertEquals(p0, new Point2D(20+4, 10+2));
	}

	public void testGetAsPolyline2D(){
		Parabola2D parabola = new Parabola2D(0, 0, 1, 0);
		ParabolaArc2D parabolaArc = new ParabolaArc2D(parabola, -10, 10);
		
		Polyline2D polyline = parabolaArc.getAsPolyline(4);
		assertTrue(polyline.getPointArray().length==5);
	}
	
}
