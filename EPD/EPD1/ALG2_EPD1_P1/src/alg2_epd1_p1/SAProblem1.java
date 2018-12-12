/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg2_epd1_p1;

import java.util.*;

/**
 *
 * @author manuel
 */
public class SAProblem1 {

    public static final Integer LENGTH = 7;
    public static final Integer QUALITY = 3;
    public static final Integer MAXITER = 999;

    public static void main(String[] args) {
        // generate matrix and vector variables
        Integer[][] swapMatrix = new Integer[LENGTH - 1][LENGTH - 1];
        resetMatrix(swapMatrix);
        Integer[] material = new Integer[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            material[i] = i + 1;
        }
        shuffleArray(material);
        Integer[] bestMaterial = new Integer[LENGTH];
        bestMaterial = copyArray(material);

        double T = 1000;
        double Tf = 1;
        System.out.println("Initial material is: " + Arrays.toString(material));
        int maxInsulation = calculateMaterial(material, swapMatrix, bestMaterial, T, Tf);
        System.out.println("Max insulation value is: " + maxInsulation);

    }

    public static int calculateMaterial(Integer[] material, Integer[][] swapMatrix, Integer[] bestMaterial, double T, double Tf) {
        Integer[] sol = copyArray(material);
        bestMaterial = copyArray(material);
        Integer valueOfSol = function(sol);
        Integer valueOfBestMaterial = function(bestMaterial);

        Integer iter = 0;
        boolean neighborAccepted = false;
        // outer loop, T controls this loop
        while (T > Tf && iter < MAXITER) {
            System.out.println("\n");

            System.out.println("Currently we are in: " + Arrays.toString(sol) + " with value of: " + valueOfSol);
            neighborAccepted = false;
            // inner loop: neighbors will be generated until one is accepted or until we run out of neighbors (running out of neighbors=all in matrix is 1)
            while (!neighborAccepted && !isMatrixFull(swapMatrix)) {
                // a single move
                Integer[] neighbor = generateNeighborSolution(sol, swapMatrix);
                Integer neighborValue = function(neighbor);

                String neighString = Arrays.toString(neighbor); // only used for debugging
                /* delta = f(s') - f(s)
                delta > 0 => neighbor is better: accept
                delta < 0 => neighbor is worse: accept is Pa says so
                 */
                System.out.println("Neighbor: " + neighString + ", value: " + neighborValue);
                Integer delta = neighborValue - valueOfSol;

                Random r = new Random();
                double ran = r.nextDouble();
                double p = probability(delta, T);
                System.out.println("Probability: p=" + p + ", ran=" + ran + ", delta=" + delta);

                if (delta > 0 || p > ran) {
                    System.out.println("Neighbor " + neighString + " got accepted");
                    sol = copyArray(neighbor);
                    valueOfSol = function(sol);
                    neighborAccepted = true;
                    // the matrix will be reset so it can be used with the new neighbor
                    resetMatrix(swapMatrix);
                    if (valueOfSol > valueOfBestMaterial) {
                        bestMaterial = copyArray(sol);
                        valueOfBestMaterial = function(bestMaterial);
                        System.out.println("New best found: " + neighString + " value of: " + valueOfBestMaterial);
                    }
                } else {
                    System.out.println("Neighbor not accepted");
                }
            }
            // cool the temp
            T = decreaseTemp(T);
            System.out.println("Temperature: " + T);
            iter++;
        }
        System.out.println("Total iterations: " + iter);
        System.out.println("Best material is " + Arrays.toString(bestMaterial));
        return valueOfBestMaterial;
    }

    public static double decreaseTemp(double T) {
        return T * 0.9;
    }

    public static Integer[] generateNeighborSolution(Integer[] material, Integer[][] swapMatrix) {
        /*To find a neighbor, find a random position in the swap matrix and swap the indexes in the material
        vector. Then, tag the swap matrix to prevent jumping back to where we came from. If the current neighbor doesnt get accepted,
        the tag will remain, if it does, all the tags will be reset
        Number of possible neighbors is ((LENGTH-1)*(LENGTH-1)/2) + (LENGTH/2)*/

        Random r = new Random();
        Integer i = r.nextInt(LENGTH - 1);
        Integer j = r.nextInt(LENGTH - 1);

        while (swapMatrix[i][j] == 1 || i==j) {
            i = r.nextInt(LENGTH - 1);
            j = r.nextInt(LENGTH - 1);
        }

        swapMatrix[i][j] = 1;
        swapMatrix[i][j] = 1;
        System.out.println("Swapped " + material[i] + " and " + material[j]);
        swap(material, i, j);

        return material;
    }

    public static Integer function(Integer[] mat) {
        // f(material) = mat[0]+mat[1]+mat[2]
        return mat[0] + mat[1] + mat[2];
    }

    public static double probability(double delta, double temp) {
        //Pa (δ, T) = e^δ/T 
        double exponent = delta / temp;
        return Math.pow(Math.E, exponent);
    }

    public static Integer[] copyArray(Integer[] v) {
        Integer[] vec = new Integer[LENGTH];
        for (Integer i = 0; i < LENGTH; i++) {
            vec[i] = v[i];
        }
        return vec;
    }

    private static void swap(Integer[] material, Integer i, Integer j) {
        Integer aux = material[i];
        material[i] = material[j];
        material[j] = aux;
    }

    private static void resetMatrix(Integer[][] swapMatrix) {
        for (int i = 0; i < LENGTH - 1; i++) {
            for (int j = 0; j < LENGTH - 1; j++) {
                swapMatrix[i][j] = 0;
            }
        }
    }

    private static boolean isMatrixFull(Integer[][] swapMatrix) {
        boolean isFull = true;
        int i = 0, j = 0;
        while (isFull && i < LENGTH - 1) {
            while (isFull && j < LENGTH - 1) {
                isFull = (swapMatrix[i][j] != 0);
                j++;
            }
            i++;
        }
        return isFull;
    }

    static void shuffleArray(Integer[] ar) {
        Random r = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);
            swap(ar, i, index);
        }
    }
}
