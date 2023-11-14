package Matrix;

import java.util.Scanner;
import java.util.Arrays;

public class Matrix {
    private final int rowsCount;
    private final int colsCount;
    private double[][] array;
    private static final Scanner in = new Scanner(System.in);

    public Matrix() {
        System.out.print("""
                >>> Введите размер матрицы m*n через пробел, где:
                \tm - количество строк
                \tn - количество столбцов
                -->\s""");

        this.rowsCount = Integer.parseInt(in.next());
        this.colsCount = Integer.parseInt(in.next());
        this.array = new double[this.rowsCount][this.colsCount];

        System.out.print("""
                >>> Введите число, соответствующее желаемому способу матрицы:
                \t1. Поэлементно
                \t2. Построчно
                -->\s""");

        int flag = in.nextInt();

        if (flag == 1) {
            setMatrixByElms();
        } else if (flag == 2) {
            setMatrixByLines();
        } else {
            System.out.println("Некорректный ввод");
        }
    }

    public Matrix(int rowsCount, int colsCount) {
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.array = new double[rowsCount][colsCount];
    }

    public Matrix(int rowsCount, int colsCount, double[][] array) {
        this(rowsCount, colsCount);
        this.array = array;
    }


    private void setMatrixByElms() {
        System.out.println("\n- ПОЭЛЕМЕНТНЫЙ ВВОД МАТРИЦЫ -\n");

        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.colsCount; j++) {
                System.out.printf("> Элемент a%d%d: ", i + 1, j + 1);
                this.array[i][j] = in.nextDouble();
            }
        }

        System.out.println("\nМАТРИЦА ЗАПИСАНА\n");
    }

    private void setMatrixByLines() {
        System.out.println("\n- ПОСТРОЧНЫЙ ВВОД МАТРИЦЫ -\n");

        in.nextLine();

        for (int i = 0; i < this.rowsCount; i++) {
            System.out.printf("> Строка %d: ", i + 1);
            String[] line = in.nextLine().split(" ");

            for (int j = 0; j < this.colsCount; j++) {
                this.array[i][j] = Double.parseDouble(line[j]);
            }
        }

        System.out.println("\nМАТРИЦА ЗАПИСАНА\n");
    }

    public void printMatrix() {
        System.out.printf("Матрица [%d * %d]:\n", this.rowsCount, this.colsCount);

        int maxLength = maxLengthNum();
        String formatLine = "+" + "-".repeat(maxLength + 8);

        for (double[] line : this.array) {
            System.out.println(formatLine.repeat(this.colsCount) + "+");
            System.out.print("| ");

            for (double el : line) {
                el = (el != -0.0d) ? el : 0.0d;
                System.out.printf("%.5f", el);
                System.out.print(" ".repeat(maxLength - lengthNum(el)) + " | ");
            }

            System.out.println();
        }

        System.out.println(formatLine.repeat(this.colsCount) + "+\n");
    }

    private int lengthNum(double num) {
        int lengthNum = String.valueOf((long) num).length();

        // Если число вида -0.*****, то при отбрасывании дробной части минус уйдет
        if (num > -1 && num < 0) {
            ++lengthNum;
        }

        return lengthNum;
    }

    private int maxLengthNum() {
        int maxLength = 0;

        for (double[] line : array) {
            for (double el : line) {
                int length = lengthNum(el);
                if (length > maxLength) {
                    maxLength = length;
                }
            }
        }

        return maxLength;
    }

    public static Matrix multiplyMatrix(Matrix matrixA, Matrix matrixB) {
        // Прямое умножение
        if (matrixA.colsCount == matrixB.rowsCount) {

            return multiply(matrixA, matrixB);

            // Обратное умножение
        } else if (matrixB.colsCount == matrixA.rowsCount) {
            System.out.print("""
                    * Прямое умножение матриц невозможно, но возможно в обратном порядке.
                    Умножить? (да/yes/д/y) или (нет/no/н/n)
                    -->\s""");

            String flag = in.nextLine().toLowerCase();

            if (flag.equals("да") || flag.equals("yes")
                    || flag.equals("д") || flag.equals("y")) {
                return multiply(matrixB, matrixA);

            } else if (flag.equals("нет") || flag.equals("no")
                    || flag.equals("н") || flag.equals("n")) {
                System.out.println("* Операция умножения матриц была отменена");

            } else {
                System.out.println("* Некорректный ввод");
            }

        } else {
            System.out.println("* Умножение матриц невозможно");
        }

        return null;
    }

    private static Matrix multiply(Matrix matrixA, Matrix matrixB) {
        Matrix matrixC = new Matrix(matrixA.rowsCount, matrixB.colsCount);

        for (int i = 0; i < matrixA.rowsCount; i++) {
            for (int j = 0; j < matrixB.colsCount; j++) {
                double sum = 0;
                for (int k = 0; k < matrixA.colsCount; k++) {
                    sum += matrixA.array[i][k] * matrixB.array[k][j];
                }
                matrixC.array[i][j] = sum;
            }
        }

        return matrixC;
    }

    public int rangMatrix() {
        return nonZeroLinesCount(gaussianMethod().array);
    }

    // Метод Гаусса в два хода + приведение к единицам на главной диагонали
    private Matrix gaussianMethod() {
        Matrix tempMatrix = new Matrix(this.rowsCount, this.colsCount, this.array);

        // Прямой ход
        for (int i = 0; i < tempMatrix.rowsCount - 1; i++) {
            if (tempMatrix.array[i][i] == 0) {
                double[] line = tempMatrix.array[i];
                tempMatrix.swapLines(i);

                // Проверка на смену строки, если нет - пропустить шаг
                if (Arrays.equals(line, tempMatrix.array[i])) {
                    continue;
                }
            }

            for (int j = i + 1; j < tempMatrix.rowsCount; j++) {
                double k = -(tempMatrix.array[j][i] / tempMatrix.array[i][i]);
                for (int m = 0; m < tempMatrix.colsCount; m++) {
                    tempMatrix.array[j][m] += tempMatrix.array[i][m] * k;
                }
            }
        }

        // Обратный ход + единицы на главной диагонали
        for (int i = tempMatrix.rowsCount - 1; i > 0; i--) {
            // В обратном ходе не будем заменять "нулевую" строку
            if (tempMatrix.array[i][i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    double k1 = -(tempMatrix.array[j][i] / tempMatrix.array[i][i]);
                    double k2 = 1 / tempMatrix.array[i][i];
                    for (int m = tempMatrix.colsCount - 1; m >= 0; m--) {
                        tempMatrix.array[j][m] += tempMatrix.array[i][m] * k1;
                        tempMatrix.array[i][m] *= k2;
                    }

                }

            }
        }

        tempMatrix.roundMatrix();

        return tempMatrix;
    }

    private int nonZeroLinesCount(double[][] array) {
        int countLines = 0;

        for (double[] line : array) {
            int countZero = 0;
            for (double elem : line) {
                if (elem == 0) {
                    ++countZero;
                }
            }

            if (countZero != array[0].length) {
                ++countLines;
            }
        }

        return countLines;
    }

    // Смена "нулевой" строки
    private void swapLines(int lineNum) {
        double[] tempLine = this.array[lineNum];

        for (int i = lineNum + 1; i < this.rowsCount; i++) {
            if (this.array[i][lineNum] != 0) {
                this.array[lineNum] = this.array[i];
                this.array[i] = tempLine;
                break;
            }
        }
    }

    private double roundNum(double num) {
        final int ACCURACY = 10;
        double k = Math.pow(10, ACCURACY);
        return Math.round(num * k) / k;
    }

    private void roundMatrix() {
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.colsCount; j++) {
                this.array[i][j] = roundNum(this.array[i][j]);
            }
        }
    }

    public Matrix transposeMatrix() {
        Matrix transposeMatrix = new Matrix(this.colsCount, this.rowsCount);

        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.colsCount; j++) {
                transposeMatrix.array[j][i] = this.array[i][j];
            }
        }

        return transposeMatrix;
    }

    public Matrix solve(Matrix b) {
        if (determinant() != 0) {
            Matrix extendedMatrix = extendedMatrix().gaussianMethod();
            Matrix inverseMatrix = new Matrix(this.rowsCount, this.colsCount);

            for (int i = 0; i < inverseMatrix.rowsCount; i++) {
                for (int j = 0; j < inverseMatrix.colsCount; j++) {
                    inverseMatrix.array[i][j] = extendedMatrix.array[i][j + extendedMatrix.rowsCount];
                }
            }

            return multiply(inverseMatrix, b);

        } else {
            System.out.println("* Матрица коэффициентов при неизвестных не обратима");
            return null;
        }
    }

    private Double determinant() {

        if (this.rowsCount == this.colsCount) {
            double det = 0.0d;
            int[][] perm = permutation(this.colsCount);

            for (int i = 0; i < perm.length; i++) {
                double temp = Math.pow(-1, inversionsCount(perm[i]));
                for (int j = 0; j < this.rowsCount; j++) {
                    temp *= this.array[j][perm[i][j]];
                }
                det += temp;
            }

            return det;

        } else {
            System.out.println("* Матрица не является квадратной");
            return null;
        }
    }

    private Matrix extendedMatrix() {
        Matrix extendedMatrix = new Matrix(this.rowsCount, this.colsCount * 2);

        for (int i = 0; i < extendedMatrix.rowsCount; i++) {
            for (int j = 0; j < extendedMatrix.colsCount; j++) {
                if (j < this.colsCount) {
                    extendedMatrix.array[i][j] = this.array[i][j];
                } else {
                    if (i == (j - this.colsCount)) {
                        extendedMatrix.array[i][j] = 1;
                    } else {
                        extendedMatrix.array[i][j] = 0;
                    }
                }
            }
        }

        return extendedMatrix;
    }

    private int[][] permutation(int num) {
        int[][] perm = new int[fact(num)][num];
        int[] temp = new int[num];

        for (int i = 0; i < num; i++) {
            temp[i] = i;
        }

        int k = num - 1;
        int n = k;
        int i = 1;
        addArrayToArray(perm, temp, 0);

        while (k > 0) {
            leftShift(temp, k);
            if (temp[k] != k) {
                addArrayToArray(perm, temp, i);
                ++i;
                k = n;
            } else {
                --k;
            }
        }

        return perm;
    }

    private void leftShift(int[] array, int position) {
        int temp = array[0];
        for (int i = 0; i < position; i++) {
            array[i] = array[i + 1];
        }
        array[position] = temp;
    }

    private int inversionsCount(int[] array) {
        int count = 0;

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    ++count;
                }
            }
        }

        return count;
    }

    private void addArrayToArray(int[][] arrayA, int[] arrayB, int index) {
        for (int i = 0; i < arrayB.length; i++) {
            arrayA[index][i] = arrayB[i];
        }
    }

    private int fact(int n) {
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public Matrix pow() {
        if (this.rowsCount == this.colsCount) {
            System.out.print("""
                    >>> Введите степень, в которую нужно возвести матрицу:
                    -->\s""");
            return pow(in.nextInt());
        } else {
            System.out.println("* Матрица не является квадратной");
            return null;
        }
    }

    private Matrix pow(int degree) {
        Matrix exponentiatedMatrix = new Matrix(this.rowsCount, this.colsCount, this.array);

        for (int i = 0; i < degree - 1; i++) {
            exponentiatedMatrix = multiply(exponentiatedMatrix, this);
        }

        return exponentiatedMatrix;
    }

    public Matrix inverse() {
        Matrix inverseMatrix = new Matrix(this.rowsCount, this.colsCount);

        double det = determinant();

        if (det != 0) {
            for (int i = 0; i < this.rowsCount; i++) {
                for (int j = 0; j < this.colsCount; j++) {
                    inverseMatrix.array[i][j] = (1 / det) * Math.pow(-1, i + j) * minor(i, j);
                }
            }
            return inverseMatrix.transposeMatrix();
        } else {
            System.out.println("Обратную матрицу невозможно найти" +
                    ", так как определитель равен нулю");
            return null;
        }
    }

    private Double minor(int row, int col) {
        Matrix matrix = new Matrix(this.rowsCount - 1, this.colsCount - 1);

        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.colsCount; j++) {
                if (i < row) {
                    if (j < col) {
                        matrix.array[i][j] = this.array[i][j];
                    } else if (j > col) {
                        matrix.array[i][j - 1] = this.array[i][j];
                    }
                } else if (i > row) {
                    if (j < col) {
                        matrix.array[i - 1][j] = this.array[i][j];
                    } else if(j > col) {
                        matrix.array[i - 1][j - 1] = this.array[i][j];
                    }
                }
            }
        }
        return matrix.determinant();
    }
}
