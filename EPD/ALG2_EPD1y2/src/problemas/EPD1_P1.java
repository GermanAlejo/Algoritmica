/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemas;

import static java.lang.Math.*;
import java.util.Arrays;

/**
 *
 * @author geral
 */
public class EPD1_P1 {

    /**
     * @param args the command line arguments
     */
    public static int iter = 0;

    public static void main(String[] args) {

        int[][] neightboursMatrix;
        int[] solIni = {1, 2, 3, 4, 5, 6, 7}, finalSol;
        int temp = 100;
        int minTemp = 1;
        int finalCost;
        double coolingRatio = 0.9;

        //first fill the matrix
        neightboursMatrix = setMatrix();

        //then call the algorithm funtion
        finalSol = simulatedAnnealing(neightboursMatrix, solIni, temp, minTemp, coolingRatio);

        //now calculate the result cost and print it
        finalCost = costFunc(finalSol);
        System.out.println("The Solution is: " + finalCost + "\nArray: " + finalSol.toString());

    }

    public static int[] simulatedAnnealing(int[][] neightboursMatrix, int[] solIni, int temp, int minTemp, double coolingRatio) {

        int[] sol = Arrays.copyOf(solIni, solIni.length);//inicial solution
        int[] bestSol = Arrays.copyOf(sol, sol.length);//the best solution
        int[] neighbour;//the neighbour to the solution
        boolean encontrado = false;
        double diference;//diference of cost between neighbours
        double prob;//probability to jump

        while (temp > minTemp) {

            encontrado = false;
            iter++;
            System.out.println("Estamos en la iteracion: " + iter);
            while (!encontrado && !completedMatrix(neightboursMatrix)) {

                neighbour = generateRandomNeighbours(sol, neightboursMatrix);

                //if the diference is positive means the neighbour have better cost
                diference = costFunc(neighbour) - costFunc(sol);
                prob = jumpProbability(temp, diference);

                if (diference > 0 || Math.random() <= prob) {
                    sol = Arrays.copyOf(neighbour, neighbour.length);
                    encontrado = true;

                    resetMatrix(neightboursMatrix);
                    diference = costFunc(sol) - costFunc(bestSol);
                    if (diference > 0) {
                        bestSol = sol;
                    }
                }

                //check if the new sol has better cost than the better solution we found
            }
            temp *= coolingRatio;
        }

        return bestSol;
    }

    //this method set all values in the bidimensnal matrix to 0
    public static int[][] setMatrix() {
        int[][] neighboursMatrix = new int[7][7];
        int n = neighboursMatrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighboursMatrix[i][j] = 0;
            }
        }
        return neighboursMatrix;
    }

    public static int[] generateRandomNeighbours(int[] sol, int[][] neighboursMatrix) {

        int neighbour[] = null;
        int a, b, x, y;
        do {
            x = (int) (Math.random() * (7 - 1) + 1);//random row number between 1 and 6
            y = (int) (Math.random() * (8 - (x + 1)) + (x + 1));//column number between 2 and 7
            a = x - 1;
            b = y - 1;
        } while (neighboursMatrix[a][b] == 1);

        neighboursMatrix[a][b] = 1;
        neighbour = setNeighbour(x, y, sol);

        return neighbour;
    }

    //this method changes the value of the x position in the array with the y position
    public static int[] setNeighbour(int x, int y, int[] neighbour) {

        int[] res = Arrays.copyOf(neighbour, neighbour.length);
        int cont = 0, encontrados = 0;
        int tam = res.length;
        while (cont < tam && encontrados < 2) {

            if (res[cont] == x) {
                res[cont] = y;
                encontrados++;
            } else if (res[cont] == y) {
                res[cont] = x;
                encontrados++;
            }

            cont++;

        }
        return res;
    }

    /*
    this method returns a prob to jump to another neighour
     */
    public static double jumpProbability(double temp, double diferencia) {
        double res = Math.exp(diferencia / temp);
        return res;
    }

    //returns the cost of the neighbour as parameter
    public static int costFunc(int[] neighbour) {

        //System.out.println("He petado en la iteracion: " + iter);
        int cost = neighbour[0] + neighbour[1] + neighbour[2];

        return cost;
    }

    public static boolean completedMatrix(int[][] neighboursMatrix) {

        boolean full = true;
        int i = 0, j = 0;
        int tam = neighboursMatrix.length;

        while (i < tam - 1 && full) {
            while (j < tam - 1 && full) {
                if (neighboursMatrix[i][j] == 0) {
                    full = false;
                }
            }
        }

        return full;
    }
    
    public static void resetMatrix(int [][] neighboursMatrix){
        
        int tam = neighboursMatrix.length;
        
        for(int i=0;i<tam;i++){
            for(int j=0;j<tam;j++){
                neighboursMatrix[i][j]=0;
            }
        }
        
    }

}
