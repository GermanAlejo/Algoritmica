/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg2_epd1_ej1;

import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author UPO
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        boolean solIni[] = {false, false, false, false, true};
        int temp = 100;
        int minTemp = 1;
        double coolingRatio = 0.9;
        
        boolean[] finalSol = simulatedAnnealing(solIni, temp, minTemp, coolingRatio);
        int finalCost = costFunc(finalSol);
        int sol = binToDec(finalSol);
        System.out.println("The shitty solution is: " + sol + " ,which shitty cost is: " + finalCost);

    }

    //main fuction of the algorithm
    public static boolean[] simulatedAnnealing(boolean[] solIni, int temp, int minTemp,double coolingRatio) {
        boolean[] sol = solIni;
        boolean[] bestSol = sol;
        boolean[] aux;
        int diference;
        List solList;
        int cont = 0, tam;
        boolean encontrado;
        double prob;

        while (temp > minTemp) {

            solList = generateRandomNeighbors(sol);
            tam = solList.size();
            encontrado = false;
            aux = (boolean[]) solList.get(0);//
            while (!encontrado && cont < tam) {
                diference = costFunc(aux) - costFunc(sol);
                prob = jumpProbability(temp, diference);
                if (diference > 0 || Math.random() <= prob) {
                    sol = aux;
                    encontrado = true;
                }
                diference = costFunc(sol) - costFunc(bestSol);
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

    public static double jumpProbability(double temp, double diferencia) {
        double res = Math.exp(diferencia / temp);
        return res;
    }

    //returns a binary value in decimal base
    public static int binToDec(boolean bin[]) {
        int num = 0;

        for (int i = bin.length - 1; i >= 0; i--) {
            if (bin[i]) {
                num += 1 * Math.pow(2, (bin.length - i - 1));
            }
        }
        return num;
    }

    //returns a decimal value in binary base
    public static boolean[] decToBin(int num) {
        List<Boolean> binList = new ArrayList<>();
        boolean fin = false;
        double res = 2;
        int bit;

        while (num >= 1) {
            res = num % 2;
            num /= 2;
            if (res == 1) {
                binList.add(true);
            } else {
                binList.add(false);
            }
        }

        boolean binArray[] = new boolean[binList.size()];

        for (int i = 0; i < binArray.length; i++) {
            binArray[i] = binList.get(binList.size() - i - 1);
        }

        return binArray;
    }

    //returns a list of neightbors
    public static List generateRandomNeighbors(boolean[] sol) {

        int tam = sol.length, cont = tam;
        boolean[] aux;
        List solList = new ArrayList();

        while (cont > 0) {
            aux = Arrays.copyOf(sol, tam);
            if (aux[cont - 1]) {
                aux[cont - 1] = false;
            } else {
                aux[cont - 1] = true;
            }
            solList.add(aux);
            cont--;
        }

        Collections.shuffle(solList);
        return solList;
    }

    //return de cost of a neighbour
    public static int costFunc(boolean[] neighbour) {
        int n = neighbour.length;

        int decVal = binToDec(neighbour);
        return (int) (pow(decVal, 3) - (60 * pow(decVal, 2)) + (900 * decVal) + 100);
    }
}
