/* file : CheckJarvisMarchSedgewick.java
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

package math.geom2d.polygon.convhull;

import java.awt.*;
import javax.swing.*;

import math.geom2d.*;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.IPointSet2D;
import math.geom2d.polygon.IPolygon2D;

/**
 * Check computation of convex hull using Jarvis March, on a random set of 100 points with coordinate generated between 100 and 300.
 * 
 * @author dlegland
 *
 */
public class CheckJarvisMarch2DRandom100 extends JPanel {

    private static final long serialVersionUID = 7331324136801936514L;

    private final static int nPoints = 100;

    IPointSet2D points;
    IPolygon2D hull;

    public CheckJarvisMarch2DRandom100() {
        super();

        // Generate point coordinates
        Point2D[] array = new Point2D[nPoints];
        for (int i = 0; i < nPoints; i++)
            array[i] = new Point2D(Math.random() * 200 + 100, Math.random() * 200 + 100);
        points = new PointArray2D(array);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        for (Point2D point : points) {
            point.draw(g2, 2);
        }

        IPolygon2D hull = new JarvisMarch2D().convexHull(points.points());
        g2.setColor(Color.BLUE);
        hull.draw(g2);
    }

    public final static void main(String[] args) {
        System.out.println("Check convex hull by Jarvis march");

        JPanel panel = new CheckJarvisMarch2DRandom100();
        JFrame frame = new JFrame("Convex hull by Jarvis march");
        frame.setContentPane(panel);
        frame.setSize(500, 400);
        frame.setVisible(true);

    }
}
