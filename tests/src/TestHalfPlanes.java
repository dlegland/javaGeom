// TestHalfPlanes.java
// a simple java file for console program


// Imports
import math.geom2d.*;
import math.geom2d.line.Ray2D;
import math.geom2d.line.StraightLine2D;
import math.geom2d.line.LinearShape2D;
import math.geom2d.polygon.HalfPlane2D;
import math.geom2d.polygon.SimplePolygon2D;

/**
 * class TestHalfPlanes.
 */
public class TestHalfPlanes{


	// ===================================================================
	// main method

	public final static void main(String arg[]){

		Point2D p1 = new Point2D(-1, 1);
		Point2D p2 = new Point2D(1, 2);
		LinearShape2D line1 = new StraightLine2D(p2, p1);
		line1 = new Ray2D(-10, -10, 30, 20);
		
		HalfPlane2D plane = new HalfPlane2D(line1);

		Box2D window = new Box2D(-100, 100, -100, 100);
		Shape2D shape1 = plane.clip(window);
		System.out.println("number of vertices : " + 
				((SimplePolygon2D)shape1).getVertexNumber());
		
		for(Point2D point : ((SimplePolygon2D)shape1).getVertices())
			System.out.println("x=" + point.getX() + " " + point.getY());		
	}
}