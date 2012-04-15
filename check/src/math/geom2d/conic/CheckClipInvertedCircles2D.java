/* file : CheckClipInvertedCircles2D.java
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

package math.geom2d.conic;

import java.awt.*;

import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.Circle2D;
import math.geom2d.domain.ContourArray2D;


public class CheckClipInvertedCircles2D extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	double x0 = 150;
	double y0 = 150;
	double xb0=50, xb1=250;
	double yb0=50, yb1=250;

	double xc1=xb0, yc1=y0;
	double xc2=xb1, yc2=y0;
	double xc3=x0, 	yc3=yb1;
	double xc4=x0, 	yc4=yb0;
	double r = 50;
	
	Box2D box = null;
	Circle2D circle1;
	Circle2D circle2;
	Circle2D circle3;
	Circle2D circle4;
	
	public CheckClipInvertedCircles2D() {
		super();
		
		box = new Box2D(50, 250, 50, 250);
	
		circle1 = new Circle2D(xc1, yc1, r, false);
		circle2 = new Circle2D(xc2, yc2, r, false);
		circle3 = new Circle2D(xc3, yc3, r, false);
		circle4 = new Circle2D(xc4, yc4, r, false);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		ContourArray2D<Circle2D> boundary = new ContourArray2D<Circle2D>(
				new Circle2D[]{circle1, circle2, circle3, circle4});		
		
		g2.setColor(Color.CYAN);
		boundary.domain().clip(box).fill(g2);
		
		g2.setColor(Color.RED);
		boundary.clip(box).draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("check draw inverted circles");
		
		JPanel panel = new CheckClipInvertedCircles2D();
		JFrame frame = new JFrame("Draw inverted circles");
		panel.setPreferredSize(new Dimension(400, 400));
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
	}
}
