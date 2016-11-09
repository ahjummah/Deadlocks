
import Jama.Matrix;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author jessa
 */
public class MP01 {

    public static void main(String[] args) {

        System.out.println("Menu:\n1. Deadlock Prevention \n2. Deadlock Avoidance");
        System.out.print("Enter choice: ");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        if (choice == 1) {
            System.out.println("Deadlock Prevention");

            deadlock_prevention();
        } else if (choice == 2) {
            System.out.println("Deadlock Avoidance");

            deadlock_avoidance();
        }

    }

    private static void deadlock_prevention() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of processes: ");
        int numproc = scan.nextInt();
        String[] nameprocs = new String[numproc];
        int[] time = new int[numproc];

        for (int i = 0; i < numproc; i++) {
            System.out.printf("Name for P[%d]: ", i + 1);
            nameprocs[i] = scan.next();
            System.out.printf("Time for P[%d]: ", i + 1);
            time[i] = scan.nextInt();
        }
        System.out.println("Available resources: ");
        int avail = scan.nextInt();

    }

    private static void deadlock_avoidance() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of processes: ");
        int numproc = scan.nextInt();
        System.out.print("Number of resources: ");
        int numres = scan.nextInt();

        boolean[] flag = new boolean[numproc];
        Arrays.fill(flag, Boolean.FALSE);
        String safe = "";
        double[][] resources = new double[numproc][numres];

        for (int i = 0; i < numproc; i++) {
            for (int j = 0; j < numres; j++) {
                System.out.printf("Enter resources for P[%d]: ", i + 1);
                resources[i][j] = scan.nextInt();
            }
            System.out.println("");
        }

        double[][] maxdemand = new double[numproc][numres];
        for (int i = 0; i < numproc; i++) {
            for (int j = 0; j < numres; j++) {
                System.out.printf("Enter maximum demand for P[%d]: ", i + 1);
                maxdemand[i][j] = scan.nextInt();
            }
            System.out.println("");
        }
        double[][] work = new double[1][numres];

        for (int i = 0; i < numres; i++) {
            System.out.printf("Enter Available Resources [%d]: ", i);
            work[0][i] = scan.nextInt();
        }

        System.out.println("Resources Matrix:");
        Matrix matrixres = new Matrix(resources);
        matrixres.print(5, 2);

        Matrix matrixdemand = new Matrix(maxdemand);
        System.out.println("Maximum Demand Matrix:");
        matrixdemand.print(5, 2);

        //calculate needed matrix
        Matrix needed = matrixdemand.minus(matrixres);
        System.out.println("Need Matrix:");
        needed.print(5, 2);

        Matrix matrixavail = new Matrix(work);
        System.out.println("Available Resources:");
        matrixavail.print(5, 2);

        //CALCULATION FOR DEADLOCKS
        while (Arrays.toString(flag).contains("f")) {
            for (int i = 0; i < numproc; i++) {
                if (flag[i] == false) {
                    System.out.printf("P[%d]: ", i + 1);
                    Matrix tempMatrix = needed.getMatrix(i, i, 0, needed.getColumnDimension() - 1);
                    Matrix rowResource = matrixres.getMatrix(i, i, 0, matrixres.getColumnDimension() - 1);
                    for (int j = 0; j < tempMatrix.getColumnDimension(); j += 3) {

                        if (checkSufficiency(tempMatrix, matrixavail)) {
                            matrixavail.plusEquals(rowResource);
                            flag[i] = true;
                            System.out.println("Safe");
                            safe += "-P[" + (i + 1) + "]-";
                        } else {

                            System.out.printf("Deadlock Occurred\n", i + 1);
                        }
                    }
                }
            }
        }
        System.out.println(safe);
        System.out.println("Final Work Matrix");
        matrixavail.print(5, 2);

    }

    private static boolean checkSufficiency(Matrix needMatrix, Matrix availMatrix) {

        for (int i = 0; i < needMatrix.getColumnDimension(); i++) {
            if (needMatrix.get(0, i) > availMatrix.get(0, i)) {
                return false;
            }
        }
        return true;
    }
}
