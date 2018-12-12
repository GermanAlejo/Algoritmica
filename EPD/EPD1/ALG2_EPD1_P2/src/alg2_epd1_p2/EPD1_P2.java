/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg2_epd1_p2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author geral
 */
public class EPD1_P2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double[][] afinityMatrix = new double[4][4];
        int[][] costMatrix = new int[4][4];

        int costLimit = 350;
        int solIni[] = {1, 2, 1, 4};
        int temp = 100;
        int minTemp = 1;
        double coolingRatio = 0.9;
        
        fillMatrix(afinityMatrix,costMatrix);
        
        int[] finalSol = simulatedAnnealing(afinityMatrix,costMatrix,solIni, temp, minTemp, coolingRatio,costLimit);
        int finalCost = solCost(finalSol,costMatrix);
        double finalAfinity = afinity(finalSol,afinityMatrix);
        
        System.out.println("The shitty solution is: " + Arrays.toString(finalSol) + " ,which shitty cost is: " + finalCost + " ,and which "
                + "shitty afinity is: " + finalAfinity);
        
    }

    public static int[] simulatedAnnealing(double[][] afinityMatrix,int[][] costMatrix,int []solIni,int temp,int minTemp, double coolingRatio,int costLimit) {
        
        int[] sol = solIni;
        int[] bestSol = sol;
        int[] aux;
        int cost;
        double diference;
        List solList;
        int cont = 0, tam;
        boolean encontrado;
        double prob;
        
        
        
        while (temp > minTemp) {

            solList = generateRandomNeighbors(sol);
            tam = solList.size();
            encontrado = false;
            aux = (int[]) solList.get(0);//
            while (!encontrado && cont < tam) {
               // diference = solCost(aux,costMatrix) - solCost(sol,costMatrix);
               diference = afinity(aux,afinityMatrix)-afinity(sol,afinityMatrix);
               cost = solCost(aux,costMatrix);
                prob = jumpProbability(temp, diference);
                if (diference > 0 || Math.random() <= prob && cost<=costLimit) {
                    sol = aux;
                    encontrado = true;
                }
                diference = solCost(sol,costMatrix) - solCost(bestSol,costMatrix);
                if (diference > 0) {
                    bestSol = sol;
                }
                cont++;
            }
            
            cont = 0;
            
            temp *= coolingRatio;
        }

        return bestSol;
    }
    
    public static int solCost(int[] sol,int[][] costMatrix){
        
        int cost=0;
        int pos;
        
        for(int i=0;i<sol.length;i++){
            pos = sol[i];
            cost+=costMatrix[i][pos-1];
        }
        return cost;
    }
    
    public static double afinity(int[] sol,double [][] afinityMatrix){
        double afinity=0.0;
        int pos;
        
        for(int i=0;i<sol.length;i++){
            pos = sol[i];
            afinity+=afinityMatrix[i][pos-1];
        }
        
        return afinity;
    }

    public static List generateRandomNeighbors(int[] sol) {
        int tam = sol.length;
        int[] aux;
        List solList = new ArrayList();

        for (int i = 0; i < tam; i++) {
            aux = Arrays.copyOf(sol, tam);
            aux[i] = (int) (Math.random()*(5 - 1) + 1);
            solList.add(aux);
        }

        Collections.shuffle(solList);

        return solList;
    }

    public static void fillMatrix(double[][] afinityMatrix, int[][] costMatrix) {

        Random r = new Random();
        int tamA = afinityMatrix.length;
        int tamC = costMatrix.length;

        for (int i = 0; i < tamA; i++) {
            for (int j = 0; j < tamA; j++) {
                afinityMatrix[i][j] = r.nextDouble();
            }
        }

        for (int i = 0; i < tamC; i++) {
            for (int j = 0; j < tamC; j++) {
                costMatrix[i][j] = (r.nextInt(101 - 1) + 1);
            }
        }
    }

    /*
    this method returns a prob to jump to another neighour
     */
    public static double jumpProbability(double temp, double diferencia) {
        double res = Math.exp(diferencia / temp);
        return res;
    }

}
