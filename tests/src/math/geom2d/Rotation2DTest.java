package math.geom2d;

import junit.framework.TestCase;

public class Rotation2DTest extends TestCase {

	public void testIsMotion() {
		Rotation2D rot = new Rotation2D(new Point2D(1, 2), Math.PI/3);
		assertTrue(rot.isMotion());
	}

	public void testIsDirect() {
		Rotation2D rot = new Rotation2D(new Point2D(1, 2), Math.PI/3);
		assertTrue(rot.isDirect());
	}

	public void testIsSimilarity() {
		Rotation2D rot = new Rotation2D(new Point2D(1, 2), Math.PI/3);
		assertTrue(rot.isSimilarity());
	}

	public void testIsIsometry() {
		Rotation2D rot = new Rotation2D(new Point2D(1, 2), Math.PI/3);
		assertTrue(rot.isIsometry());
	}

	public void testGetInverseTransform() {
		Rotation2D rot = new Rotation2D(new Point2D(1, 2), Math.PI/3);
		Rotation2D inv = new Rotation2D(new Point2D(1, 2), -Math.PI/3);
		assertTrue(rot.getInverseTransform().equals(inv));
	}

	public void testCompose() {
		Rotation2D rot1 = new Rotation2D(new Point2D(0, 0), Math.PI/2);
		Rotation2D rot2 = new Rotation2D(new Point2D(4, 0), -Math.PI/2);
		Translation2D trans = new Translation2D(4, 4);
		assertTrue(rot2.compose(rot1).equals(trans));
		
		Rotation2D rot3 = new Rotation2D(new Point2D(0, 0), Math.PI/4);
		Rotation2D res3 = new Rotation2D(3*Math.PI/4);
		assertTrue(rot3.compose(rot1).equals(res3));
		assertTrue(rot1.compose(rot3).equals(res3));
	}

}
