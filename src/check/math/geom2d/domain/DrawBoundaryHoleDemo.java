/* file : DrawBoundaryHoleDemo.java
 * 
 * Project : Euclide
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
 * Created on 1 avr. 2007
 *
 */

package math.geom2d.domain;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.domain.ContourArray2D;
import math.geom2d.domain.Contour2D;
import math.geom2d.polygon.LinearRing2D;


public class DrawBoundaryHoleDemo extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Curve2D curve = null;
	
	public DrawBoundaryHoleDemo() {
		super();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		// build the different curves
		Circle2D circle = new Circle2D(150, 150, 50, false);
		LinearRing2D square = new LinearRing2D(new Point2D[]{
				new Point2D(50, 50),
				new Point2D(250, 50),
				new Point2D(250, 250),
				new Point2D(50, 250) });

		// build the boundary set
		ContourArray2D<Contour2D> boundary = 
			new ContourArray2D<Contour2D>(
				new Contour2D[]{square, circle});
		
		// fill the domain
		g2.setColor(Color.CYAN);
		new GenericDomain2D(boundary).fill(g2);
		
		// draw the boundary
		g2.setColor(Color.BLACK);
		boundary.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a circle");
		
		JPanel panel = new DrawBoundaryHoleDemo();
		JFrame frame = new JFrame("Draw boundary of a domain with hole");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
