// TestLines.java
// a simple java file for console program


// Imports
import math.geom2d.*;
import math.geom2d.line.StraightLine2D;

/**
 * class TestLines.
 */
public class TestLines{


	// ===================================================================
	// main method

	public final static void main(String arg[]){

		double theta;
		
		Point2D p1 = new Point2D(-1, 0);
		Point2D p2 = new Point2D(1, 0);
		
		StraightLine2D line1 = new StraightLine2D(p1, p2);

		theta = line1.horizontalAngle();
		System.out.println("angle : " + theta);

		Point2D p3 = new Point2D(0, -1);
		Point2D p4 = new Point2D(0, 1);
		
		StraightLine2D line2 = new StraightLine2D(p3, p4);

		theta = line2.horizontalAngle();
		System.out.println("angle : " + theta);
		
		Point2D p0 = line1.intersection(line2);
		System.out.println("point intersection : " + p0.getX() + " " + p0.getY());

		
		System.out.println(line1.contains(0, 0));
		System.out.println(line1.contains(0, 1e-14));
		
		p1 = new Point2D(-1, 0);
		p2 = new Point2D(0,  1);
		
		line1 = new StraightLine2D(p1, p2);

		theta = line1.horizontalAngle();
		System.out.println("angle : " + theta);

		p3 = new Point2D(5, 0);
		p4 = new Point2D(4, 1);
		
		line2 = new StraightLine2D(p3, p4);

		theta = line2.horizontalAngle();
		System.out.println("angle : " + theta);
		
		p0 = line1.intersection(line2);
		System.out.println("point intersection : " + p0.getX() + " " + p0.getY());

		line1 = new StraightLine2D(-1, 1, 0);
		System.out.println("angle (abc) : " + line1.horizontalAngle());

		line1 = new StraightLine2D(-1, 2, 0);
		System.out.println("angle (abc) : " + line1.horizontalAngle());

		line1 = new StraightLine2D(-1, 1, -1);
		System.out.println("angle (abc) : " + line1.horizontalAngle());

		line1 = new StraightLine2D(-1, 2, -2);
		System.out.println("angle (abc) : " + line1.horizontalAngle());
		
		System.out.println("distances --------");
		p1 = new Point2D(0, 0);
		p2 = new Point2D(1, 1);				
		line1 = new StraightLine2D(p1, p2);
		
		System.out.println("point (0,0) :" + line1.signedDistance(0,0));
		System.out.println("point (1,0) :" + line1.signedDistance(1,0));
		System.out.println("point (4,0) :" + line1.signedDistance(4, 0));
		System.out.println("point (0,4) :" + line1.signedDistance(0, 4));
		System.out.println("point (10, 0) :" + line1.signedDistance(10, 0));
	}
}