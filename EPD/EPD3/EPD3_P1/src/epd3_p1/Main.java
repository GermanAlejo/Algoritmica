/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epd3_p1;

import java.util.Random;

/**
 *
 * @author geral
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    // Algorithm input
    public static final int numItems = 1000;
    public static final int[] weights = new int[numItems];
    public static final int MAX_WEIGHT = 10000;

    public static final int OPTIMAL_SOLUTION = 1;
    // Genetic parameters
    public static final double ELITE_PERCENT = 0.2;
    public static final double ELITE_PARENT_RATE = 0.1;
    public static final double MUTATION_CHANCE = 0.5;
    public static final int TOURNAMENT_POPULATION = 3;
    public static final double CLONE_CHANCE = 0.01;
    // Generic parameters
    public static final int POPULATION_SIZE = 1000;
    public static final int NUM_EVOLUTIONS = 1000;
    
    
    public static void main(String[] args) {
        
        
    }
    
}
