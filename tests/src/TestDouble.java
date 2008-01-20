// TestDouble.java
// a simple java file for console program


// Imports

/**
 * class TestDouble.
 */
public class TestDouble{


	// ===================================================================
	// main method

	public final static void main(String arg[]){

		double d1 = Double.NaN;
		double d2 = Double.POSITIVE_INFINITY;
		double d3 = 10.5;
		double d4 = -10.5;
		
		System.out.println(d3>d1);
		System.out.println(d4>d1);
		System.out.println(d3<d1);
		System.out.println(d4<d1);
		System.out.println(d4<d2);
	}
}