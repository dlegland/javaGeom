// Matrix.java
// a simple java file for a standard class

package math.utils;

// Imports

/**
 * class Matrix
 */
public class Matrix {

    // ===================================================================
    // constants

	/** The tolerance used for solving linear equations. Equal to 1e-14 by default.*/
	public static double tolerance = 1e-14;
	
    // ===================================================================
    // class variables

    /** The number of rows. */
    private int        nRows;
    /** The number of columns. */
    private int        nCols;

    /** The element array of the matrix. */
    private double[][] el;

    // ===================================================================
    // constructors

    /** Main constructor */

    /**
     * Constructs a new Matrix, with 1 row and 1 column, initialized to 1.
     */
    public Matrix() {
        this(1, 1);
    }

    /** Initializes a new Matrix with the given number of rows and columns. */
    public Matrix(int nbRows, int nbCols) {
        nRows = nbRows;
        nCols = nbCols;
        el = new double[nRows][nCols];
        setToIdentity();
    }

    /**
     * Constructs a new Matrix, initialized with the given coefficients.
     */
    public Matrix(double[][] coef) {
		if (coef == null) {
			nRows = 1;
			nCols = 1;
			el = new double[nRows][nCols];
			setToIdentity();
			return;
        }

		nRows = coef.length;
		nCols = coef[0].length;
		el = new double[nRows][nCols];
		for (int r = 0; r < nRows; r++)
			for (int c = 0; c < nCols; c++)
				el[r][c] = coef[r][c];
    }

    // ===================================================================
    // accessors

    /**
     * Returns the coef. row and col are between 1 and the number of rows and
     * columns.
     */
    public double getCoef(int row, int col) {
		return el[row - 1][col - 1];
    }

    /** Returns the number of rows. */
    public int getRows() {
        return nRows;
    }

    /** Returns the number of columns. */
    public int getColumns() {
        return nCols;
    }

    /**
     * Returns true if the matrix is square, i.e. the number of rows equals the
     * number of columns.
     */
    public boolean isSquare() {
		return (nCols == nRows);
    }

    // ===================================================================
    // modifiers

    /**
     * Sets the coef to the given value. row and col are between 1 and the number
     * of rows and columns.
     */
    public void setCoef(int row, int col, double coef) {
		el[row - 1][col - 1] = coef;
    }

    // ===================================================================
    // computation methods

    /**
     * Returns the result of the multiplication of the matrix with another one.
     * The content of the matrix is not modified.
     * @throws IllegalArgumentException if the size of matrices do not match
     */
    public Matrix multiplyWith(Matrix matrix) {

		// check sizes of the matrices
		if (nCols != matrix.nRows)
			throw new IllegalArgumentException("Matrix sizes do not match");
		
		double sum;
		Matrix m = new Matrix(nRows, matrix.nCols);

		for (int r = 0; r < m.nRows; r++)
			for (int c = 0; c < m.nCols; c++) {
				sum = 0;
				for (int i = 0; i < nCols; i++)
					sum += el[r][i] * matrix.el[i][c];
				m.el[r][c] = sum;
			}

        return m;
    }

    /**
     * Returns the result of the multiplication of the matrix with the given
     * vector. The content of the matrix is not modified.
     * @throws NullPointerException if input array is null
     * @throws IllegalArgumentException if size of vector and of matrix do not match
     */
    public double[] multiplyWith(double[] coefs) {

		if (coefs == null) {
			throw new NullPointerException();
		}

		// check sizes of matrix and vector
		if (coefs.length != nCols)
			throw new IllegalArgumentException("Matrix sizes do not match");

		double sum;
		double[] res = new double[nRows];

		for (int r = 0; r < nRows; r++) {
			sum = 0;
			for (int c = 0; c < nCols; c++)
				sum += el[r][c] * coefs[c];
			res[r] = sum;
		}
		return res;
    }

    /**
     * Returns the result of the multiplication of the matrix with the given
     * vector. The content of the matrix is not modified.
     * @throws NullPointerException if input vector is null
     * @throws IllegalArgumentException if size of vector and of matrix do not match
     */
    public double[] multiplyWith(double[] src, double[] res) {

		if (src == null)
			throw new NullPointerException();
		
		// check sizes of matrix and vector
		if (src.length != nCols) 
			throw new IllegalArgumentException("Matrix sizes do not match");

		if (src.length != res.length)
			res = new double[nRows];

		double sum;

		for (int r = 0; r < nRows; r++) {
			sum = 0;
			for (int c = 0; c < nCols; c++)
				sum += el[r][c] * src[c];
			res[r] = sum;
		}
		return res;
    }

    /** Transposes the matrix, changing the inner coefficients. */
    public void transpose() {
		int tmp = nCols;
		nCols = nRows;
		nRows = tmp;
		double[][] oldData = el;
		el = new double[nRows][nCols];

		for (int r = 0; r < nRows; r++)
			for (int c = 0; c < nCols; c++)
				el[r][c] = oldData[c][r];
    }

    /**
     * Returns the transposed matrix, without changing the inner coefficients
     * of the original matrix.
     */
    public Matrix getTranspose() {
        Matrix mat = new Matrix(nCols, nRows);

		for (int r = 0; r < nRows; r++)
			for (int c = 0; c < nCols; c++)
				mat.el[c][r] = el[r][c];
		return mat;
    }

    /**
     * Computes the solution of a linear system, using the Gauss-Jordan
     * algorithm. The inner coefficients of the matrix are not modified.
     * @throws NullPointerException if input vector is null
     * @throws IllegalArgumentException if size of vector and of matrix do not match
     * @throws UnsupportedOperationException if the matrix is not square
     * @throws ArithmeticException if the algorithm could not find pivot greater than tolerance
     */
    public double[] solve(double vector[]) {

		if (vector == null)
			throw new NullPointerException();
		
		if (vector.length != nRows) 
			throw new IllegalArgumentException("Matrix and vector dimensions do not match");
		
		if (nCols != nRows)
			throw new UnsupportedOperationException("Try to invert non square Matrix");
		
		double[] res = new double[vector.length];
		for (int i = 0; i < nRows; i++)
			res[i] = vector[i];

		// copy the matrix
        Matrix mat = new Matrix(el);

        int r, c; // row and column indices
        int p, r2; // pivot index, and secondary row index
        double pivot, tmp;

        // iterate on matrix lines
		for (r = 0; r < nRows; r++) {

            p = r;
            // look for the first non-null pivot
			while ((Math.abs(mat.el[p][r]) < Matrix.tolerance) && (p < nRows))
                p++;

			if (p == nRows)
				throw new ArithmeticException("Degenerated linear system");

            // swap the current line and the pivot
			for (c = 0; c < nRows; c++) {
				tmp = mat.el[r][c];
				mat.el[r][c] = mat.el[p][c];
				mat.el[p][c] = tmp;
			}

            // swap the corresponding values in the vector
            tmp = res[r];
            res[r] = res[p];
            res[p] = tmp;

            pivot = mat.el[r][r];

			// divide elements of the current line by the pivot
			for (c = r + 1; c < nRows; c++)
				mat.el[r][c] /= pivot;
			res[r] /= pivot;
			mat.el[r][r] = 1;

			// update other lines, before current line...
			for (r2 = 0; r2 < r; r2++) {
				pivot = mat.el[r2][r];
				for (c = r + 1; c < nRows; c++)
					mat.el[r2][c] -= pivot * mat.el[r][c];
				res[r2] -= pivot * res[r];
				mat.el[r2][r] = 0;
			}

            // and after current line
			for (r2 = r + 1; r2 < nRows; r2++) {
				pivot = mat.el[r2][r];
				for (c = r + 1; c < nRows; c++)
					mat.el[r2][c] -= pivot * mat.el[r][c];
				res[r2] -= pivot * res[r];
				mat.el[r2][r] = 0;
			}
		}

        return res;
    }

    // ===================================================================
    // general methods

    /**
     * Fills the matrix with zeros everywhere, except on the main diagonal,
     * filled with ones.
     */
	public void setToIdentity() {
		for (int r = 0; r < nRows; r++)
			for (int c = 0; c < nCols; c++)
				el[r][c] = 0;
		for (int i = Math.min(nRows, nCols) - 1; i >= 0; i--)
			el[i][i] = 1;
	}

    /**
     * Returns a String representation of the elements of the Matrix
     */
    @Override
	public String toString() {
		String res = new String("");
		res = res.concat("Matrix size : " + Integer.toString(nRows)
				+ " rows and " + Integer.toString(nCols) + " columns.\n");
		for (int r = 0; r < nRows; r++) {
			for (int c = 0; c < nCols; c++)
				res = res.concat(Double.toString(el[r][c])).concat(" ");
			res = res.concat(new String("\n"));
		}
		return res;
    }
}