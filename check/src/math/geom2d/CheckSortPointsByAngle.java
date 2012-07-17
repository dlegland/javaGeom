/* file : CheckSortPointsByAngle.java
 * 
 * Project : javaGeom
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

package math.geom2d;

import java.awt.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.SimplePolygon2D;


public class CheckSortPointsByAngle extends JPanel{

	private static final long serialVersionUID = 7331324136801936514L;
	
	ArrayList<Point2D> points;
	
	Polygon2D polygon;
	
	private class CompareByAngle implements Comparator<Point2D>{
	    Point2D basePoint;
	    public CompareByAngle(Point2D base) {
	        this.basePoint = base;
	    }
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Point2D point1, Point2D point2) {
            double angle1 = Angle2D.horizontalAngle(basePoint, point1);
            double angle2 = Angle2D.horizontalAngle(basePoint, point2);
            
            if(angle1<angle2) return -1;
            if(angle1>angle2) return +1;
            return 0;
        }
	}
	
	public CheckSortPointsByAngle() {
		super();
		
		points = new ArrayList<Point2D>(); 
		Point2D[] array = new Point2D[]{
		        new Point2D(3,  9),
                new Point2D(11, 1),
                new Point2D(6,  8),
                new Point2D(4,  3),
                new Point2D(5,  15),
                new Point2D(8,  11),
                new Point2D(1,  6),
                new Point2D(7,  4),
                new Point2D(9,  7),
                new Point2D(14, 5),
                new Point2D(10, 13),
                new Point2D(16, 14),
                new Point2D(15, 2),
                new Point2D(13, 16),
                new Point2D(3,  12),
                new Point2D(12, 10),
		};
		for(Point2D point : array)
		    points.add(new Point2D(point.x()*10, point.y()*10));
		
		// Find point with lowest y-coord
		Point2D lowestPoint = null;
		double lowestY = Double.MAX_VALUE;
		for(Point2D point : points){
		    double y = point.y();
		    if(y<lowestY){
		        lowestPoint = point;
		        lowestY = y;
		    }
		}
		
		Comparator<Point2D> comparator = new CompareByAngle(lowestPoint);
		
		Collections.sort(points, comparator);
		
		polygon = new SimplePolygon2D(points);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLUE);
		polygon.draw(g2);
	}

	public final static void main(String[] args){
		System.out.println("should draw a clipped polygon");
		
		JPanel panel = new CheckSortPointsByAngle();
		JFrame frame = new JFrame("Clip Polygon demo");
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		
	}
}


