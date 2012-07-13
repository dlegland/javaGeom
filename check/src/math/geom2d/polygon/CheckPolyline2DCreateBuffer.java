/* file : CheckPolyline2DCreateBuffer.java
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
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.SimplePolygon2D;


public class CheckPolyline2DCreateBuffer extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Polygon2D polygon = null;
	Polygon2D polygon1 = null;
	Polygon2D polygon2 = null;
	
	public CheckPolyline2DCreateBuffer() {
		super();
		
		Point2D[] points1 = new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(50, 150) };
		
		polygon = new SimplePolygon2D(points1);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
	
		Domain2D buffer = Polygons2D.createBuffer(polygon, 10);
		g2.setColor(Color.CYAN);
		buffer.fill(g2);
		g2.setColor(Color.BLUE);
		buffer.boundary().draw(g2);
		
		g2.setColor(Color.BLACK);
		polygon.boundary().draw(g2);
	}

	public final static void main(String[] args){		
		JPanel panel = new CheckPolyline2DCreateBuffer();
		JFrame frame = new JFrame("Create Polyline buffer");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
