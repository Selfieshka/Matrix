import Matrix.*;

public class Main {

    public static void main(String[] args) {
//        setMatrixTest();
//        multiplyTest();
//        rangTest();
//        transposeTest();
        solveTest();
//        powTest();
//        inverseTest();
    }

    public static void setMatrixTest() {
        Matrix matrixA = new Matrix();

        matrixA.printMatrix();
    }

    public static void multiplyTest() {
        Matrix matrixA = new Matrix(3, 1, new double[][]{
                {1.8},
                {4.1},
                {7.565},
        });

        Matrix matrixB = new Matrix(3, 3, new double[][]{
                {97898.07, 874.4515, 98.445},
                {24521.89, 87545.54, 98.12},
                {545155.67, 8744.55, 910.551}
        });

        matrixA.printMatrix();
        matrixB.printMatrix();

        try {
            Matrix.multiplyMatrix(matrixA, matrixB).printMatrix();
        } catch (NullPointerException e) {

        }
    }

    public static void rangTest() {
        Matrix matrixA = new Matrix(4, 4, new double[][]{
                {21, 40, -13, -48},
                {19, -18, 20, 38},
                {-65, 65 ,-65 ,65},
                {-38, 36, -40, -76}
        });

        Matrix matrixB = new Matrix(3, 4, new double[][]{
                {1, 3, 3, 4},
                {0, 0, 1, 2},
                {2, 6, 1, -2}
        });
        matrixA.printMatrix();

        System.out.printf("Ранг матрицы: %d\n", matrixA.rangMatrix());
        System.out.printf("Ранг матрицы: %d\n", matrixB.rangMatrix());
    }

    public static void transposeTest() {
        Matrix matrixA = new Matrix(4, 3, new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        });

        matrixA.transposeMatrix().printMatrix();
    }

    public static void solveTest() {
        Matrix matrixA = new Matrix(5, 5, new double[][]{
                {1, 2, 1, 2, 2},
                {3, 3, 2, -1, 2},
                {9, 10, 5, -1,-2},
                {1, 2, -1, -1, 2},
                {8, 9 , 4, 0, 2}
        });

        Matrix matrixB = new Matrix(5, 1, new double[][]{
                {3},
                {0},
                {0},
                {-3},
                {-3}
        });

        matrixA.printMatrix();
        matrixB.printMatrix();

        try {
            matrixA.solve(matrixB).printMatrix();
        } catch (NullPointerException e) {

        }
    }

    public static void powTest() {
        Matrix matrixA = new Matrix(3, 3, new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        });

        matrixA.pow().printMatrix();
    }

    public static void inverseTest() {
        Matrix matrixA = new Matrix(3, 3, new double[][]{
                {2, 3, -1},
                {0, 1, 1},
                {1, -1, 2}
        });

        matrixA.inverse().printMatrix();
    }
}
