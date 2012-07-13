/* file : CheckClipBoundary2D.java
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
import math.geom2d.conic.Parabola2D;
import math.geom2d.curve.Curve2D;


public class CheckClipBoundary2D extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Curve2D curve = null;
	
	public CheckClipBoundary2D() {
		super();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		Box2D box = new Box2D(50, 350, 50, 350);
		Parabola2D parabola = new Parabola2D(200, 200, -.01, 0);
		
		
		// fill the domain
		g2.setColor(Color.CYAN);
		Boundary2D boundary = Boundaries2D.clipBoundary(parabola, box);
		new GenericDomain2D(boundary).fill(g2);
		
		// draw the domain
		g2.setColor(Color.BLUE);
		parabola.clip(box).draw(g2);
		
		// draw the boundary
		g2.setColor(Color.BLACK);
		box.boundary().draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("draw a clipped boundary");
		
		JPanel panel = new CheckClipBoundary2D();
		JFrame frame = new JFrame("Draw a clipped boundary");
		frame.setContentPane(panel);
		frame.setSize(500, 400);
		frame.setVisible(true);
		
	}
}
