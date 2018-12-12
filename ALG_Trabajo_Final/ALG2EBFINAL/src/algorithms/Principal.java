/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.List;
import java.util.Random;

/**
 *
 * @author geral
 */
public class Principal {

    public static void main(String[] args) {

        int temp = 100;//initial temperature
        int minTemp = 1;//minimal temperature
        double finalCost;//variable to storage the final solution´s fitness
        double coolingRatio = 0.9;//this variable controls the reduction of the temperature
        List<Player> finalSol;

        SA sa = new SA();
        //call the SA method executing the algorithm
        finalSol = sa.simulatedAnnealing(temp, minTemp, coolingRatio);
        
        
        
        
    }
}
