/* file : DrawClippedPolygonDemo.java
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
 * Created on 19 avr. 2007
 *
 */

package math.geom2d.polygon;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.domain.Boundary2D;
import math.geom2d.polygon.SimplePolygon2D;


public class CheckPolygon2D_Clip extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	SimplePolygon2D polygon = null;
	SimplePolygon2D polygon2 = null;
	Box2D box = null;
	
	public CheckPolygon2D_Clip() {
		super();
		
		Point2D p1 = new Point2D(100, 30);
		Point2D p2 = new Point2D(200, 30);
		Point2D p3 = new Point2D(200, 170);
		Point2D p4 = new Point2D(100, 170);
		polygon = new SimplePolygon2D(new Point2D[]{p1, p2, p3, p4});
		
		box = new Box2D(50, 250, 50, 150);
		
		polygon2 = new SimplePolygon2D(new Point2D[]{
				new Point2D(220, 120),
				new Point2D(280, 120),
				new Point2D(280, 180),
				new Point2D(220, 180)
		});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setColor(Color.BLUE);
		box.draw(g2);

		
		Boundary2D boundary = polygon.boundary();

		// Draw initial polygon
		g2.setColor(Color.YELLOW);
		polygon.fill(g2);
		g2.setColor(Color.BLUE);
		boundary.draw(g2);

		// draw clipped polygon
		g2.setColor(Color.CYAN);
		polygon.clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		boundary.clip(box).draw(g2);
		
		boundary = polygon2.boundary();
		
		// Draw initial polygon
		g2.setColor(Color.YELLOW);
		g2.setStroke(new BasicStroke());
		polygon.fill(g2);	
		g2.setColor(Color.BLUE);
		boundary.draw(g2);

		// draw clipped polygon
		g2.setColor(Color.CYAN);
		polygon.clip(box).fill(g2);
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2.0f));
		boundary.clip(box).draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a clipped polygon");
		
		JPanel panel = new CheckPolygon2D_Clip();
		JFrame frame = new JFrame("Clip Polygon demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
