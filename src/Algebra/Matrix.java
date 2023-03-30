package Algebra;

import Exception.*;
import Exception.ExceptionMessage.TargetedMessage;
import General.TrueTextEncodable;
import Geometry.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Matrix implements TrueTextEncodable {
    private final Fraction[][] e;

    /**
     * Creates a new, empty Matrix
     */
    public Matrix() {
        this.e = new Fraction[0][];
    }

    /**
     * Creates a new Matrix
     * @param v the set of Vectors composing the rows of the Matrix
     */
    public Matrix(Vector @NotNull ... v) {
        this.e = new Fraction[v.length][];
        for(int i = 0; i < v.length; i++) {
            this.e[i] = v[i].getCoordinates();
        }
    }

    /**
     * Creates a new Matrix
     * @param f the elements of this Matrix
     */
    public Matrix(Fraction[] ... f) {
        this.e = new Fraction[f.length][];
        for(int i = 0; i < f.length; i++) {
            this.e[i] = new Fraction[f[i].length];
            System.arraycopy(f[i], 0, this.e[i], 0, f[i].length);
        }
    }

    /**
     * Creates a new Matrix
     * @param s the String representation of the elements of this Matrix
     */
    public Matrix(String s) {
        final BigInteger[][][] values = General.Converter.convertTo3DBigIntegerArray(s, "/", ",", ";");
        this.e = new Fraction[values.length][];
        for(int i = 0; i < values.length; i++) {
            this.e[i] = new Fraction[values[i].length];
            for(int j = 0; j < values[i].length; j++) {
                this.e[i][j] = new Fraction(values[i][j]);
            }
        }
    }

    /**
     * Creates a new square null Matrix
     * @param size the dimension of the Matrix
     */
    public Matrix(int size) {
        Matrix model = new Matrix(size, size);
        this.e = model.e;
    }

    /**
     * Creates a new null Matrix
     * @param rows the height of the Matrix
     * @param columns the width of the Matrix
     */
    public Matrix(int rows, int columns) {
        this.e = new Fraction[rows][];
        for(int i = 0; i < rows; i++) {
            this.e[i] = new Fraction[columns];
            for(int j = 0; j < columns; j++) {
                this.e[i][j] = Fraction.ZERO;
            }
        }
    }

    /**
     * Creates a new Matrix based on the following parameters
     * @param rows the number of rows
     * @param cols the number of columns
     * @param startRow the starting row index (as an input for the element function)
     * @param startCol the starting column index (as an input for the element function)
     * @param rCoefficient the linear coefficient for the given row index
     * @param cCoefficient the linear coefficient for the given column index
     * @param constant the constant sum term
     */
    public Matrix(int rows, int cols, int startRow, int startCol, Fraction rCoefficient, Fraction cCoefficient, Fraction constant) {
        final Fraction[] ROWS = new Fraction[rows], COLS = new Fraction[cols];
        final int ROW_LIMIT = startRow + rows, COL_LIMIT = startCol + cols;
        for(int i = startRow; i < ROW_LIMIT; i++) {
            ROWS[i] = rCoefficient.multiply(new Fraction(i));
        }
        for(int i = startCol; i < COL_LIMIT; i++) {
            COLS[i] = cCoefficient.multiply(new Fraction(i));
        }
        this.e = new Fraction[rows][];
        for(int i = 0; i < rows; i++) {
            this.e[i] = new Fraction[cols];
            for(int j = 0; j < cols; j++) {
                this.e[i][j] = ROWS[i].add(COLS[j]).add(constant);
            }
        }
    }

    /**
     * Adds two Matrices
     * @param addend the addend Matrix
     * @return the sum of this Matrix and the addend Matrix
     * @throws IllegalDimensionException if the addend Matrix is incompatible for addition
     */
    public Matrix add(Matrix addend) throws IllegalDimensionException {
        verifyAdditionDimensions(addend);
        final Matrix sum = new Matrix(this.e);
        for(int i = 0; i < sum.e.length; i++) {
            for(int j = 0; j < this.e[i].length; j++) {
                sum.e[i][j] = sum.e[i][j].add(addend.e[i][j]);
            }
        }
        return sum;
    }

    /**
     * Subtracts two Matrices
     * @param subtrahend the subtrahend Matrix
     * @return the difference between this Matrix and the subtrahend Matrix
     * @throws IllegalDimensionException if the subtrahend Matrix is incompatible for subtraction
     */
    public Matrix subtract(Matrix subtrahend) throws IllegalDimensionException {
        verifyAdditionDimensions(subtrahend);
        final Matrix sum = new Matrix(this.e);
        for(int i = 0; i < sum.e.length; i++) {
            for(int j = 0; j < this.e[i].length; j++) {
                sum.e[i][j] = sum.e[i][j].subtract(subtrahend.e[i][j]);
            }
        }
        return sum;
    }

    /**
     * Multiplies a Matrix by a scalar value
     * @param multiplicand the multiplicand Fraction
     * @return the scaled Matrix
     */
    public Matrix multiply(Fraction multiplicand) {
        final Matrix product = new Matrix(this.e);
        for(int i = 0; i < product.e.length; i++) {
            product.e[i] = new Fraction[this.e[i].length];
            for(int j = 0; j < product.e[i].length; j++) {
                product.e[i][j] = this.e[i][j].multiply(multiplicand);
            }
        }
        return product;
    }

    /**
     * Multiplies two Matrices
     * @param multiplicand the multiplicand Matrix
     * @return the product of the two Matrices
     * @throws IllegalDimensionException if the multiplicand Matrix is incompatible for dot-product multiplication
     */
    public Matrix multiply(Matrix multiplicand) throws IllegalDimensionException {
        verifyMultiplicationDimensions(multiplicand);
        final Matrix product = new Matrix(this.e.length, multiplicand.e.length > 0 ? multiplicand.e[0].length : 0);
        for(int i = 0; i < product.e.length; i++) {
            for(int j = 0; j < product.e[i].length; j++) {
                Fraction sum = Fraction.ZERO;
                for(int k = 0; k < multiplicand.e.length; k++) {
                    sum = sum.add(this.e[i][k].multiply(multiplicand.e[k][j]));
                }
                product.e[i][j] = sum;
            }
        }
        return product;
    }

    /**
     * Raises this Matrix to an Integer power
     * @param pow the Integer power
     * @return this ^ pow
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public Matrix pow(int pow) throws IllegalDimensionException {
        verifySquareMatrix();
        Matrix antilogarithm = identityMatrix(this.e.length);
        int powTest = Math.abs(pow);
        List<Boolean> powers = new LinkedList<>();
        while(powTest > 0) {
            powers.add(0, (powTest & 1) == 1);
            powTest >>>= 1;
        }
        for(boolean instruction : powers) {
            antilogarithm = antilogarithm.multiply(antilogarithm);
            if(instruction) {
                antilogarithm = multiply(antilogarithm);
            }
        }
        return pow < 0 ? antilogarithm.reducedRowEchelon(identityMatrix(this.e.length))[1] : antilogarithm;
    }

    /**
     * Augments another {@code Matrix} to this {@code Matrix}.
     * @param augment the augmented {@code Matrix}.
     * @return this | augment
     */
    public Matrix augment(Matrix augment) {
        if(this.e.length != augment.e.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_MATRIX_DIMENSION);
        }
        final Fraction[][] elements = new Fraction[this.e.length][];
        for(int i = 0; i < elements.length; i++) {
            final int thisLength = this.e[i].length, augmentLength = augment.e[i].length;
            elements[i] = new Fraction[thisLength + augmentLength];
            System.arraycopy(this.e[i], 0, elements[i], 0, thisLength);
            System.arraycopy(augment.e[i], 0, elements[i], thisLength, augmentLength);
        }
        return new Matrix(elements);
    }

    /**
     * Finds the row echelon form
     * @return an upper triangular Matrix derived from Gaussian elimination on this Matrix
     * @throws IllegalDimensionException if this Matrix is not rectangular
     */
    public Matrix rowEchelon() throws IllegalDimensionException {
        verifyRectangularMatrix();
        final Matrix rowEchelon = new Matrix(this.e);
        int row = 0;
        for(int i = 0; i < rowEchelon.e[0].length && row < rowEchelon.e.length; i++) {
            rowEchelon.reduceRow(row);
            boolean topIsZero = rowEchelon.e[row][i].equals(Fraction.ZERO);
            for(int j = row + 1; j < rowEchelon.e.length; j++) {
                if(! rowEchelon.e[j][i].equals(Fraction.ZERO)) {
                    rowEchelon.reduceRow(j);
                    if(topIsZero) {
                        rowEchelon.switchRows(row, j);
                        topIsZero = false;
                    } else {
                        rowEchelon.addRow(j, row, Fraction.ONE.negate());
                    }
                }
                int startI = 0, startJ = 0;
                while(rowEchelon.e[row][startI].equals(Fraction.ZERO) && startI + 1 < rowEchelon.e[row].length) {
                    startI++;
                }
                startI += rowEchelon.e[row][startI].equals(Fraction.ZERO) ? 1 : 0;
                while(rowEchelon.e[j][startJ].equals(Fraction.ZERO) && startJ + 1 < rowEchelon.e[j].length) {
                    startJ++;
                }
                startJ += rowEchelon.e[j][startJ].equals(Fraction.ZERO) ? 1 : 0;
                if(startJ < startI) {
                    rowEchelon.switchRows(row, j);
                }
            }
            if(! topIsZero) {
                row++;
            }
        }
        return rowEchelon;
    }

    /**
     * Finds the reduced row echelon form
     * @return an upper triangular Matrix derived from Gauss-Jordan elimination on this Matrix
     * @throws IllegalDimensionException if this Matrix is not rectangular
     */
    public Matrix reducedRowEchelon() throws IllegalDimensionException {
        final Matrix rowEchelon = rowEchelon();
        for(int i = rowEchelon.e.length - 1; i > 0; i--) {
            int column = 0;
            while(column + 1 < rowEchelon.e[i].length && rowEchelon.e[i][column].equals(Fraction.ZERO)) {
                column++;
            }
            for(int j = 0; j < i; j++) {
                if(! rowEchelon.e[i][column].multiply(rowEchelon.e[j][column]).equals(Fraction.ZERO)) {
                    rowEchelon.addRow(j, i, rowEchelon.e[j][column].divide(rowEchelon.e[i][column]).negate());
                }
            }
        }
        return rowEchelon;
    }

    /**
     * Concatenates another Matrix to this and performs Gauss-Jordan elimination
     * @param m the target Matrix
     * @return the new Matrix made by appending the target to this Matrix and performing Gauss-Jordan elimination
     * @throws IllegalArgumentException if the target Matrix cannot be properly appended to this Matrix or this Matrix is not rectangular
     */
    public Matrix[] reducedRowEchelon(Matrix m) throws IllegalArgumentException {
        verifyRectangularMatrix();
        if(this.e.length != m.e.length) {
            throw new IllegalArgumentException();
        }
        final Matrix rowEchelon = new Matrix(this.e), inversion = new Matrix(m.e);
        int row = 0;
        for(int i = 0; i < rowEchelon.e[0].length && row < rowEchelon.e.length; i++) {
            inversion.scaleRow(row, rowEchelon.reduceRow(row));
            boolean topIsZero = rowEchelon.e[row][i].equals(Fraction.ZERO);
            for(int j = row + 1; j < rowEchelon.e.length; j++) {
                if(! rowEchelon.e[j][i].equals(Fraction.ZERO)) {
                    inversion.scaleRow(j, rowEchelon.reduceRow(j));
                    if(topIsZero) {
                        rowEchelon.switchRows(row, j);
                        inversion.switchRows(row, j);
                        topIsZero = false;
                    } else {
                        rowEchelon.addRow(j, row, Fraction.ONE.negate());
                        inversion.addRow(j, row, Fraction.ONE.negate());
                    }
                }
                int startI = 0, startJ = 0;
                while(rowEchelon.e[row][startI].equals(Fraction.ZERO) && startI + 1 < rowEchelon.e[row].length) {
                    startI++;
                }
                startI += rowEchelon.e[row][startI].equals(Fraction.ZERO) ? 1 : 0;
                while(rowEchelon.e[j][startJ].equals(Fraction.ZERO) && startJ + 1 < rowEchelon.e[j].length) {
                    startJ++;
                }
                startJ += rowEchelon.e[j][startJ].equals(Fraction.ZERO) ? 1 : 0;
                if(startJ < startI) {
                    rowEchelon.switchRows(row, j);
                    inversion.switchRows(row, j);
                }
            }
            if(! topIsZero) {
                row++;
            }

        }
        for(int i = rowEchelon.e.length - 1; i > 0; i--) {
            int column = 0;
            while(column + 1 < rowEchelon.e[i].length && rowEchelon.e[i][column].equals(Fraction.ZERO)) {
                column++;
            }
            for(int j = i - 1; j >= 0; j--) {
                if(! rowEchelon.e[i][column].multiply(rowEchelon.e[j][column]).equals(Fraction.ZERO)) {
                    Fraction factor = rowEchelon.e[j][column].divide(rowEchelon.e[i][column]).negate();
                    rowEchelon.addRow(j, i, factor);
                    inversion.addRow(j, i, factor);
                }
            }
        }
        return new Matrix[]{rowEchelon, inversion};
    }

    /**
     * Finds the inverse of this Matrix (assuming determinant != 0)
     * @return a Matrix M such that this * M is an identity Matrix
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public Matrix inverse() throws IllegalDimensionException {
        verifySquareMatrix();
        return reducedRowEchelon(identityMatrix(this.e.length))[1];
    }

    /**
     * Finds the left inverse of a Matrix (typically non-square)
     * @return a Matrix m such that m * this = I
     */
    public Matrix leftInverse() {
        return transpose().multiply(this).inverse().multiply(transpose());
    }

    /**
     * Finds the right inverse of a Matrix (typically non-square)
     * @return a Matrix m such that this * m = I
     */
    public Matrix rightInverse() {
        return transpose().multiply(multiply(transpose()).inverse());
    }

    /**
     * Finds the characteristic Polynomial of this Matrix
     * @return the Polynomial 'p' such that for all eigenvalues 'e' of this Matrix, p(e) = 0
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public Polynomial characteristicPolynomial() throws IllegalDimensionException {
        verifySquareMatrix();
        final RationalFunction X = new RationalFunction(new Polynomial(0, 1));
        final RationalFunction[][] elements = new RationalFunction[this.e.length][];
        for(int i = 0; i < elements.length; i++) {
            elements[i] = new RationalFunction[this.e[i].length];
            for(int j = 0; j < elements[i].length; j++) {
                elements[i][j] = new RationalFunction(new Polynomial(this.e[i][j].negate()));
            }
            elements[i][i] = elements[i][i].add(X);
        }
        for(int i = 0; i < this.e.length; i++) {
            for(int j = i + 1; j < this.e.length; j++) {
                final RationalFunction scaleFactor = elements[j][i].divide(elements[i][i]);
                for(int k = i + 1; k < this.e.length; k++) {
                    elements[j][k] = elements[j][k].subtract(elements[i][k].multiply(scaleFactor));
                }
            }
        }
        RationalFunction characteristicPolynomial = new RationalFunction(new Polynomial(1));
        for(int i = 0; i < this.e.length; i++) {
            characteristicPolynomial = characteristicPolynomial.multiply(elements[i][i]);
        }
        return new Polynomial(Fraction.raiseToIntegers(characteristicPolynomial.numerator().getTerms()));
    }

    /**
     * Finds a List of all rational eigenvalues of this Matrix
     * @return a List of all rational roots of the characteristic Polynomial of this Matrix
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public List<Fraction> rationalEigenvalues() throws IllegalDimensionException {
        return characteristicPolynomial().rationalRoots();
    }

    /**
     * Finds the LU decomposition of this Matrix
     * @return the Matrices L (lower triangular) and U (upper triangular) such that L*U = this
     * @throws IllegalDimensionException if this Matrix is not rectangular
     */
    public Matrix[] LU_decomposition() {
        verifyRectangularMatrix();
        final Fraction[][] LU = new Fraction[this.e.length][];
        for(int i = 0; i < LU.length; i++) {
            LU[i] = new Fraction[this.e[i].length];
            for(int j = 0; j < LU[i].length; j++) {
                LU[i][j] = this.e[i][j];
                for(int k = 0; k < Math.min(i, j); k++) {
                    LU[i][j] = LU[i][j].subtract(LU[i][k].multiply(LU[k][j]));
                }
                if(i > j) {
                    LU[i][j] = LU[i][j].divide(LU[j][j]);
                }
            }
        }
        final int minimumDimension = Math.min(LU.length, LU.length == 0 ? 0 : LU[0].length);
        final Fraction[][][] decomposed = {new Fraction[LU.length][], new Fraction[minimumDimension][]};
        for(int i = 0; i < LU.length; i++) {
            decomposed[0][i] = new Fraction[minimumDimension];
            final boolean isRowLegal = i < minimumDimension;
            if(isRowLegal) {
                decomposed[1][i] = new Fraction[LU[i].length];
            }
            for(int j = 0; j < LU[i].length; j++) {
                if(j < minimumDimension && isRowLegal) {
                    decomposed[0][i][j] = (i == j) ? Fraction.ONE : Fraction.ZERO;
                    decomposed[1][i][j] = decomposed[0][i][j];
                }
                decomposed[i <= j ? 1 : 0][i][j] = LU[i][j];
            }
        }
        return new Matrix[]{new Matrix(decomposed[0]), new Matrix(decomposed[1])};
    }

    /**
     * Finds the LDU decomposition of this Matrix
     * @return the Matrices L (lower triangular), D (diagonal), and U (upper triangular) such that L*D*U = this
     * @throws IllegalDimensionException if this Matrix is not rectangular
     */
    public Matrix[] LDU_decomposition() {
        verifyRectangularMatrix();
        final Fraction[][] LDU = new Fraction[this.e.length][];
        for(int i = 0; i < LDU.length; i++) {
             LDU[i] = new Fraction[this.e[i].length];
             for(int j = 0; j < LDU[i].length; j++) {
                 LDU[i][j] = this.e[i][j];
                 for(int k = 0; k < Math.min(i, j); k++) {
                     LDU[i][j] = LDU[i][j].subtract(LDU[k][k].multiply(LDU[i][k]).multiply(LDU[k][j]));
                 }
                 if(i != j) {
                     LDU[i][j] = LDU[i][j].divide(LDU[Math.min(i, j)][Math.min(i, j)]);
                 }
             }
        }
        final int minimumLength = Math.min(LDU.length, LDU.length == 0 ? 0 : LDU[0].length);
        final Fraction[][][] decomposed = {new Fraction[LDU.length][], new Fraction[minimumLength][], new Fraction[minimumLength][]};
        for(int i = 0; i < LDU.length; i++) {
            decomposed[0][i] = new Fraction[minimumLength];
            final boolean rowIsLegal = i < minimumLength;
            if(rowIsLegal) {
                decomposed[1][i] = new Fraction[minimumLength];
                decomposed[2][i] = new Fraction[LDU[i].length];
            }
            for(int j = 0; j < LDU[i].length; j++) {
                final boolean columnIsLegal = j < minimumLength;
                if(columnIsLegal) {
                    decomposed[0][i][j] = i == j ? Fraction.ONE : Fraction.ZERO;
                    if(rowIsLegal) {
                        decomposed[1][i][j] = i == j ? Fraction.ONE : Fraction.ZERO;
                    }
                }
                if(rowIsLegal) {
                    decomposed[2][i][j] = i == j ? Fraction.ONE : Fraction.ZERO;
                }
                decomposed[i > j ? 0 : (i == j ? 1 : 2)][i][j] = LDU[i][j];
            }
        }
        return new Matrix[]{new Matrix(decomposed[0]), new Matrix(decomposed[1]), new Matrix(decomposed[2])};
    }

    /**
     * Finds the determinant of this Matrix
     * @return the divisibility factor of the inverse of this Matrix
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public Fraction determinant() {
        verifySquareMatrix();
        final Matrix rowEchelon = new Matrix(this.e);
        Fraction determinant = Fraction.ONE;
        int row = 0;
        for(int i = 0; i < rowEchelon.e[0].length && row < rowEchelon.e.length; i++) {
            determinant = determinant.multiply(rowEchelon.reduceRow(row));
            boolean topIsZero = rowEchelon.e[row][i].equals(Fraction.ZERO);
            for(int j = row + 1; j < rowEchelon.e.length; j++) {
                if(! rowEchelon.e[j][i].equals(Fraction.ZERO)) {
                    determinant = determinant.multiply(rowEchelon.reduceRow(j));
                    if(topIsZero) {
                        rowEchelon.switchRows(row, j);
                        determinant = determinant.negate();
                        topIsZero = false;
                    } else {
                        rowEchelon.addRow(j, row, Fraction.ONE.negate());
                    }
                }
                int startI = 0, startJ = 0;
                while(rowEchelon.e[row][startI].equals(Fraction.ZERO) && startI + 1 < rowEchelon.e[row].length) {
                    startI++;
                }
                startI += rowEchelon.e[row][startI].equals(Fraction.ZERO) ? 1 : 0;
                while(rowEchelon.e[j][startJ].equals(Fraction.ZERO) && startJ + 1 < rowEchelon.e[j].length) {
                    startJ++;
                }
                startJ += rowEchelon.e[j][startJ].equals(Fraction.ZERO) ? 1 : 0;
                if(startJ < startI) {
                    rowEchelon.switchRows(row, j);
                    determinant = determinant.negate();
                }
            }
            if(topIsZero) {
                return Fraction.ZERO;
            } else {
                row++;
            }
        }
        return determinant.inverse();
    }

    /**
     * Finds the rank of this Matrix
     * @return the number of linearly independent nonzero rows in this Matrix
     */
    public int rank() {
        final Matrix rowEchelon = rowEchelon();
        int rank = this.e.length, column = 0;
        while(rank > 0) {
            if(rowEchelon.e[rank - 1][column].equals(Fraction.ZERO)) {
                column++;
                if(column == this.e.length) {
                    column = 0;
                    rank--;
                }
            } else {
                return rank;
            }
        }
        return 0;
    }

    /**
     * Finds the trace of this Matrix
     * @return the sum of the elements along the principal diagonal
     * @throws IllegalDimensionException if this Matrix is not square
     */
    public Fraction trace() throws IllegalDimensionException {
        Fraction trace = Fraction.ZERO;
        for(int i = 0; i < this.e.length; i++) {
            if(this.e[i].length != this.e.length) {
                throw new IllegalDimensionException(IllegalDimensionException.NON_SQUARE_MATRIX);
            }
            trace = trace.add(this.e[i][i]);
        }
        return trace;
    }

    /**
     * Finds the transpose of this Matrix
     * @return the Matrix created by reflecting all elements of this Matrix across the main diagonal
     * @throws IllegalDimensionException if this Matrix is non-rectangular
     */
    public Matrix transpose() throws IllegalDimensionException {
        verifyRectangularMatrix();
        final Matrix transpose = new Matrix(this.e.length > 0 ? this.e[0].length : 0, this.e.length);
        for(int i = 0; i < this.e.length; i++) {
            for(int j = 0; j < this.e[i].length; j++) {
                transpose.e[j][i] = this.e[i][j];
            }
        }
        return transpose;
    }

    /**
     * Finds the null space of this {@code Matrix}.
     * @return an array of {@code Vectors} forming a basis for this {@code Matrix}.
     */
    public List<Vector> nullSpace() {
        Matrix reducedRowEchelon = reducedRowEchelon();
        return null;
    }

    public List<Vector> getColumns() {
        //List<Vector>
        return null;
    }

    /**
     * Finds the Schur product of two Matrices
     * @param multiplicand the multiplicand Matrix
     * @return the Matrix where each element is the product of the corresponding elements in
     * this Matrix and the multiplicand Matrix
     * @throws IllegalDimensionException if the dimensions of the multiplicand Matrix are incompatible for Schur multiplication
     */
    public Matrix schurProduct(Matrix multiplicand) throws IllegalDimensionException {
        verifyAdditionDimensions(multiplicand);
        final Matrix product = new Matrix(this.e);
        for(int i = 0; i < product.e.length; i++) {
            for(int j = 0; j < product.e[i].length; j++) {
                product.e[i][j] = product.e[i][j].multiply(multiplicand.e[i][j]);
            }
        }
        return product;
    }

    /**
     * Reduces a row so that its leading coefficient is 1
     * @param row the target row for reduction
     * @return the factor of reduction
     */
    public Fraction reduceRow(int row) {
        int column = 0, limit = this.e.length > 0 ? this.e[0].length : 0;
        while(this.e[row][column].equals(Fraction.ZERO) && column < limit - 1) {
            column++;
        }
        final Fraction factor = this.e[row][column].equals(Fraction.ZERO) ? Fraction.ZERO : this.e[row][column].inverse();
        for(int i = column; i < limit; i++) {
            this.e[row][i] = this.e[row][i].multiply(factor);
        }
        return factor;
    }

    /**
     * Adds a scaled multiple of one row of this Matrix to another row
     * @param row the target row to be changed
     * @param addend the addend row
     * @param factor the multiplicand scale of the addend row
     */
    public void addRow(int row, int addend, Fraction factor) {
        final int limit = this.e.length > 0 ? this.e[0].length : 0;
        for(int i = 0; i < limit; i++) {
            this.e[row][i] = this.e[row][i].add(this.e[addend][i].multiply(factor));
        }
    }

    /**
     * Switches two rows in this Matrix
     * @param i one row in the switch operation
     * @param j the second row in the switch operation
     */
    public void switchRows(int i, int j) {
        final Fraction[] proxy = this.e[i];
        this.e[i] = this.e[j];
        this.e[j] = proxy;
    }

    /**
     * Scales a row of this Matrix
     * @param row the target row
     * @param scale the scale factor
     */
    public void scaleRow(int row, Fraction scale) {
        for(int i = 0; i < this.e[row].length; i++) {
            this.e[row][i] = this.e[row][i].multiply(scale);
        }
    }

    /**
     * Finds an element of this Matrix
     * @param r the row number
     * @param c the column number
     * @return the element at the specified coordinates within this Matrix
     */
    public Fraction getElement(int r, int c) {
        return this.e[r][c];
    }

    /**
     * Resets an element of this Matrix
     * @param r the target row
     * @param c the target column
     * @param e the new element
     */
    public void setElement(int r, int c, Fraction e) {
        this.e[r][c] = e;
    }

    /**
     * Gets the number of rows in this <code>Matrix</code>
     * @return <code>this.e.length</code>
     */
    public int rowSize() {
        return this.e.length;
    }

    /**
     * Gets the column size for a specified row in this {@code Matrix}.
     * @param row the row index.
     * @return the column size.
     */
    public int columnSize(int row) {
        return this.e[row].length; //TODO: fix for index error
    }

    /**
     * Finds the maximum column size across this {@code Matrix}.
     * @return {@code -1} if there are no rows in this {@code Matrix}, else the
     * number of columns in the longest row.
     */
    public int maxColumnSize() {
        int columnSize = -1;
        for(Fraction[] row : this.e) {
            if(row.length > columnSize) {
                columnSize = row.length;
            }
        }
        return columnSize;
    }

    /**
     * Finds the minimal Polynomial of this Matrix
     * @return the Polynomial f of least degree such that f(m) = [0]
     * @throws IllegalDimensionException if this Matrix is non-square
     */
    public Polynomial minimalPolynomial() {
        verifySquareMatrix();
        final Fraction[][] elements = new Fraction[this.e.length][];
        for(int i = 0; i < elements.length; i++) {
            elements[i] = new Fraction[]{Fraction.ZERO};
        }
        elements[0] = new Fraction[]{Fraction.ONE};
        Matrix vector = new Matrix(elements);
        final Matrix[] vectors = new Matrix[this.e.length + 1];
        for(int i = 0; i < vectors.length; i++) {
            vectors[i] = vector;
            vector = multiply(vector);
        }
        final Fraction[][] finalMatrix = new Fraction[this.e.length][];
        for(int i = 0; i < finalMatrix.length; i++) {
            finalMatrix[i] = new Fraction[this.e.length + 1];
            for(int j = 0; j < finalMatrix[i].length; j++) {
                finalMatrix[i][j] = vectors[j].e[i][0];
            }
        }
        final Matrix solution = new Matrix(finalMatrix).reducedRowEchelon();
        int maxIndex = 0;
        boolean continueIteration = true;
        while(maxIndex < finalMatrix.length && continueIteration) {
            continueIteration = false;
            for(int j = 0; j < finalMatrix[maxIndex].length; j++) {
                if(! solution.e[maxIndex][j].equals(Fraction.ZERO)) {
                    continueIteration = true;
                    j = finalMatrix[maxIndex].length;
                }
            }
            if(continueIteration) {
                maxIndex++;
            }
        }
        final List<Fraction> termsList = new LinkedList<>();
        for(int i = 0; i < maxIndex; i++) {
            termsList.add(solution.e[i][maxIndex].negate());
        }
        termsList.add(Fraction.ONE);
        final Fraction[] terms = new Fraction[termsList.size()];
        int index = 0;
        while(termsList.size() > 0) {
            terms[index++] = termsList.remove(0);
        }
        return new Polynomial(terms);
    }

    /**
     * Determines whether a linear system is conditionally consistent.
     * @param constant the constant {@code Matrix}.
     * @return {@code true} if the algorithm does not detect an inconsistent system, else
     * {@code false} if the system can be declared inconsistent. Note that this algorithm
     * assumes that a sufficient set of row operations has already been performed on the
     * {@code Matrix} to reveal an inconsistency by way of a row of zeros in the coefficient
     * {@code Matrix} and a nonzero constant in the same row in the constant {@code Matrix}.
     * @throws IllegalDimensionException if the coefficient and constant {@code Matrices}
     * have different row dimensions.
     */
    public boolean isConditionallyConsistent(Matrix constant) {
        if(this.e.length != constant.e.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        for(int i = 0; i < constant.e.length; i++) {
            if(! constant.e[i][0].equals(Fraction.ZERO)) {
                boolean failedConsistencyCheck = true;
                final int length = this.e[i].length;
                for(int j = 0; j < length; j++) {
                    if(! this.e[i][j].equals(Fraction.ZERO)) {
                        failedConsistencyCheck = false;
                        j = length;
                    }
                }
                if(failedConsistencyCheck) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines whether this {@code Matrix} is the zero matrix.
     * @return {@code true} if all elements of this {@code Matrix} are zero, else
     * {@code false} if at least one element is nonzero.
     */
    public boolean isZero() {
        for(Fraction[] row : this.e) {
            for(Fraction element : row) {
                if(! element.equals(Fraction.ZERO)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifies that a given row and column is within the bounds of this {@code Matrix}.
     * @param row the row index.
     * @param column the column index.
     * @throws IllegalDimensionException if either the row or column index forces
     * access of an element that does not exist in this {@code Matrix}. This may
     * be due to a negative input index or one that extends outside the range of indices.
     */
    private void verifyRowIndexValidity(int row, int column) throws IllegalDimensionException {
        if(row < 0 | row >= rowSize() | column < 0 | column >= columnSize(row)) {
            throw new IllegalDimensionException(IllegalDimensionException.MATRIX_ELEMENT_OUT_OF_BOUNDS);
        }
    }

    /**
     * Checks if two {@code Matrices} cannot be added.
     * @param addend the addend Matrix
     * @return {@code true} if the row and column lengths do not match between this
     * {@code Matrix} and the comparator, else {@code false}.
     */
    private void verifyAdditionDimensions(Matrix addend) {
        if(this.e.length != addend.e.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_MATRIX_DIMENSION);
        }
        for(int i = 0; i < this.e.length; i++) {
            if(this.e[i].length != addend.e[i].length) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_MATRIX_DIMENSION);
            }
        }
    }

    /**
     * Checks if two Matrices cannot be multiplied
     * @param multiplicand the multiplicand Matrix
     * TODO: include exception thrown
     */
    private void verifyMultiplicationDimensions(Matrix multiplicand) {
        verifyRectangularMatrix();
        multiplicand.verifyRectangularMatrix();
        if((this.e.length > 0 & this.e[0].length != multiplicand.e.length)) {
            throw new IllegalDimensionException(IllegalDimensionException.MATRIX_DOT_PRODUCT_ILLEGAL);
        }
    }

    /**
     * Checks if this Matrix is not rectangular
     * TODO: include exception thrown
     */
    public void verifyRectangularMatrix() {
        if(this.e.length != 0) {
            int length = this.e[0].length;
            for(Fraction[] row : this.e) {
                if(row.length != length) {
                    throw new IllegalDimensionException(IllegalDimensionException.NON_RECTANGULAR_MATRIX);
                }
            }
        }
    }

    /**
     * Checks if this Matrix is not square
     * TODO: include exception thrown
     */
    public void verifySquareMatrix() {
        for(Fraction[] row : this.e) {
            if(row.length != this.e.length) {
                throw new IllegalDimensionException(IllegalDimensionException.NON_SQUARE_MATRIX);
            }
        }
    }

    /**
     * Provides the TrueText of this Matrix
     * @return this Matrix in a parsable format
     */
    @Override
    public String trueText() {
        final StringBuilder builder = new StringBuilder();
        String nextRow = "";
        for(Fraction[] row : this.e) {
            builder.append(nextRow);
            nextRow = ";";
            String nextElement = "";
            StringBuilder rowBuilder = new StringBuilder();
            for(Fraction e : row) {
                rowBuilder.append(nextElement).append(e.toString());
                nextElement = ",";
            }
            builder.append(rowBuilder);
        }
        return builder.toString();
    }

    /**
     * Converts this Matrix to a printable format
     * @return this Matrix as a String
     */
    @Override
    public String toString() {
        final List<Integer> elementLengths = new LinkedList<>();
        for (Fraction[] matrixRow : this.e) {
            final ListIterator<Integer> elementIterator = elementLengths.listIterator();
            for (Fraction matrixElement : matrixRow) {
                final int length = matrixElement.toString().length();
                if (elementIterator.hasNext()) {
                    final int currentLength = elementIterator.next();
                    if (currentLength < length) {
                        elementIterator.remove();
                        elementIterator.add(length);
                    }
                } else {
                    elementIterator.add(length);
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        int rowNumber = 1;
        for (Fraction[] matrixRow : this.e) {
            StringBuilder row = new StringBuilder();
            final ListIterator<Integer> elementIterator = elementLengths.listIterator();
            for (Fraction matrixElement : matrixRow) {
                String element = matrixElement.toString();
                final int length = elementIterator.next();
                while (element.length() <= length) {
                    element = element.concat(" ");
                }
                row.append(element);
            }
            builder.append(newLine).append("[").append(row).append("] ").append(rowNumber++);
            newLine = "\n";
        }
        return builder.toString();
    }

    /**
     * Prints this Matrix
     */
    public void print() {
        System.out.println(this);
    }

    // static methods

    /**
     * Creates an identity <code>Matrix</code>
     * @param size the size of the <code>Matrix</code>
     * @return a square <code>Matrix</code> with ones on the main diagonal and zeros everywhere else
     */
    public static Matrix identityMatrix(int size) {
        final Matrix identity = new Matrix(size);
        for (int i = 0; i < size; i++) {
            identity.e[i][i] = Fraction.ONE;
        }
        return identity;
    }

    // static methods

    /**
     * Gets a <code>Matrix</code> from a file
     * @param filename the name of the Matrix file
     * @return the converted <code>Matrix</code> object
     */
    public static Matrix getMatrixFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            TargetedMessage.scannerHasNextElement(scanner);
            int rows = scanner.nextInt();
            TargetedMessage.scannerHasNextElement(scanner);
            int columns = scanner.nextInt();
            Matrix matrix = new Matrix(rows, columns);
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    TargetedMessage.scannerHasNextElement(scanner);
                    matrix.setElement(i, j, new Fraction(scanner.next()));
                }
            }
            scanner.close();
            return matrix;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * Gets an adjacency matrix from a file
     * @param filename the name of the file storing the adjacency matrix
     * @return the converted <code>Matrix</code> object
     */
    public static Matrix getAdjacencyMatrixFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            ExceptionMessage.TargetedMessage.scannerHasNextElement(scanner);
            int length = scanner.nextInt();
            Matrix matrix = new Matrix(length);
            matrix.setElement(0, 0, Fraction.ZERO);
            for(int i = 1; i < length; i++) {
                matrix.setElement(i, i, Fraction.ZERO);
                for(int j = 0; j < i; j++) {
                    ExceptionMessage.TargetedMessage.scannerHasNextElement(scanner);
                    Fraction element = scanner.next().equals("0") ? Fraction.ZERO : Fraction.ONE;
                    matrix.setElement(i, j, element);
                    matrix.setElement(j, i, element);
                }
            }
            scanner.close();
            return matrix;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a coefficient and constant {@code Matrix} from a system of {@code LinearEquations}.
     * @param equations the system of {@code LinearEquations}.
     * @return the {@code Matrices} associated with the provided system.
     */
    public static Matrix[] getSystem(LinearEquation... equations) {
        Fraction[][] coefficient = new Fraction[equations.length][], constant = new Fraction[equations.length][];
        for (int i = 0; i < equations.length; i++) {
            coefficient[i] = Fraction.valueOf(equations[i].getCoefficients());
            constant[i] = new Fraction[]{new Fraction(equations[i].getConstant())};
        }
        return new Matrix[]{new Matrix(coefficient), new Matrix(constant)};
    }

    /**
     * Determines whether this {@code Matrix} system is conditionally consistent.
     * @param coefficient the coefficient {@code Matrix}.
     * @param constant the constant {@code Matrix}.
     * @throws NoRealSolutionException if there exists at least one row for which the constant
     * {@code Matrix} element is nonzero and all coefficient {@code Matrix} elements are zero.
     * Directly references the {@code isConditionallyConsistent()} method.
     */
    public static void verifyConditionalConsistency(Matrix coefficient, Matrix constant) throws NoRealSolutionException {
        if(! coefficient.isConditionallyConsistent(constant)) {
            throw new NoRealSolutionException(NoRealSolutionException.INCONSISTENT_SYSTEM_OF_EQUATIONS);
        }
    }

    /**
     * Converts an array of Matrices to a printable format
     * @param ar the array of Matrices
     * @return the array in String format
     */
    public static String arrayToString(Matrix... ar) {
        return listToString(Arrays.asList(ar));
    }

    /**
     * Prints an array of Matrices
     * @param ar the array of Matrices
     */
    public static void printArray(Matrix... ar) {
        System.out.println(arrayToString(ar));
    }

    /**
     * Converts a list of Matrices to a printable format
     * @param list the list of Matrices
     * @return the list in String format
     */
    public static String listToString(List<Matrix> list) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\t[--\n");
        String newLine = "";
        for(Matrix item : list) {
            builder.append(newLine).append(item.toString());
            newLine = "\n";
        }
        builder.append("\n\t--]");
        return builder.toString();
    }

    /**
     * Prints an array of Matrices
     * @param list the array of Matrices
     */
    public static void printList(List<Matrix> list) {
        System.out.println(listToString(list));
    }
}