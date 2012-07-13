/* file : CheckPolygon2DCreateBuffer.java
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

/**
 * Compute buffer (set of points located less than a given distance) from the
 * polygon, for simple polygon, and polygon with hole.
 * @author dlegland
 *
 */
public class CheckPolygon2DCreateBuffer extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	Polygon2D polygon = null;
	Polygon2D polygon1 = null;
	Polygon2D polygon2 = null;
	
	public CheckPolygon2DCreateBuffer() {
		super();
		
		Point2D[] points1 = new Point2D[]{
				new Point2D(50, 50),
				new Point2D(100, 50),
				new Point2D(100, 100),
				new Point2D(150, 100),
				new Point2D(50, 200) };

		Point2D[] points2 = new Point2D[]{
				new Point2D(200, 50), 
				new Point2D(350, 50), 
				new Point2D(350, 250), 
				new Point2D(250, 250), 
				new Point2D(200, 200)};
		
		Point2D[] points3 = new Point2D[]{
				new Point2D(250, 100), 
				new Point2D(250, 150), 
				new Point2D(300, 150), 
				new Point2D(300, 100) };

		polygon1 = new SimplePolygon2D(points1);
		polygon2 = new SimplePolygon2D(points2);
		
		polygon = new MultiPolygon2D(new LinearRing2D[]{
				new LinearRing2D(points1),
				new LinearRing2D(points2),
				new LinearRing2D(points3) });
		
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
		JPanel panel = new CheckPolygon2DCreateBuffer();
		JFrame frame = new JFrame("Create Polygon buffer");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}
