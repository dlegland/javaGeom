/* file : DrawClippedReuleauxDemo.java
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

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.Boundary2D;
import math.geom2d.curve.BoundaryPolyCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveUtil;


public class DrawClippedReuleauxDemo extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Boundary2D curve = null;
	
	public DrawClippedReuleauxDemo() {
		super();
		
		double x0 = 100;
		double y0 = 150;
		double r  = 100;
		double d  = r*Math.sqrt(3)/2;
		
		CircleArc2D arc1 = new CircleArc2D(x0, y0, r, 4*Math.PI/3, Math.PI/3);
		CircleArc2D arc2 = new CircleArc2D(x0-r/2, y0-d, r, 0, Math.PI/3);
		CircleArc2D arc3 = new CircleArc2D(x0+r/2, y0-d, r, 2*Math.PI/3, Math.PI/3);
		
		BoundaryPolyCurve2D<CircleArc2D> set = new BoundaryPolyCurve2D<CircleArc2D>();
		set.addCurve(arc1);
		set.addCurve(arc2);
		set.addCurve(arc3);
		
		curve = set;
		System.out.println(curve);
		
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		Box2D box = new Box2D(60, 140, 30, 180);
		g2.setColor(Color.CYAN);
		g2.fill(box.getAsRectangle());
		
		g2.setColor(Color.YELLOW);
		g2.fill(curve);
		
		g2.setColor(Color.BLUE);
		g2.draw(curve);
		
		Curve2D clipped = CurveUtil.clipBoundary(curve, box);
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(2));
		g2.draw(clipped);
	}

	public final static void main(String[] args){
		System.out.println("should draw a Reuleaux triangle");
		
		JPanel panel = new DrawClippedReuleauxDemo();
		JFrame frame = new JFrame("Draw Reuleaux demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
