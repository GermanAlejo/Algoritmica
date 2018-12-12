/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epd3_p1;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author geral
 */
public class Population {

    ArrayList<Individual> individuals;
    int numObjects;
    ArrayList<Individual> sorted;

    public Population(int numObjects) {
        individuals = new ArrayList<>();
        this.numObjects = numObjects;
    }

    //inicializes the population with the individuals
    public void inicializePopulationRandomly() {

        Individual indi;
        for (int i = 0; i < Main.POPULATION_SIZE; i++) {

            indi = new Individual();
            indi.inicializeRandom(numObjects);
            individuals.add(indi);
        }
    }

    public Individual crossover(Individual i1, Individual i2) {

        Random r = new Random();
        Individual child = new Individual();
        if (r.nextDouble() < Main.CLONE_CHANCE) {

            int index1 = (int) (Math.random() * numObjects);
            int index2 = (int) (Math.random() * numObjects);
            int start = Math.min(index1, index2);
            int end = Math.max(index1, index2);

            for (int i = start; i < end; i++) {
                child.objectList.add(i1.getObject(i));
            }
            
            for (int j = 0; j < numObjects; j++) {
                if (!child.objectList.contains(i2.getObject(j))) {
                    child.objectList.add(i2.getObject(j));
                }
            }

        } else {
            if (i1.getCost() < i2.getCost()) {
                return i1;
            } else {
                return i2;
            }
        }
        
        return child;

    }

    public Individual mutate(Individual indi){
        
        int index1 = (int) (Math.random() * numObjects);
        int index2 = (int) (Math.random() * numObjects);
        
        indi.switchGenes(index1, index2);
        return indi;
    }
    
    public Population evolve(){
        
        // STEP 1: Select the best fit (elitism)
        sorted = new ArrayList<Individual>();
        Population nextGenPop = new Population(numObjects);
        int populationSpaceAvailable = individuals.size();

        for (int i = 0; i < Main.POPULATION_SIZE; i++) {
            int bestCost = Integer.MAX_VALUE;
            int bestIndex = -1;
            for (int j = 0; j < individuals.size(); j++) {
                if (individuals.get(j).getCost() < bestCost) {
                    bestCost = individuals.get(j).getCost();
                    bestIndex = j;
                }
            }
            sorted.add(individuals.get(bestIndex));
            individuals.remove(bestIndex);
        }
        
        // Keep the best individual
        nextGenPop.individuals.add(sorted.get(0));
        --populationSpaceAvailable;
        // Add "top" individuals to the next generation
        int numElite = (int) (Main.POPULATION_SIZE * Main.ELITE_PERCENT);
        for (int i = 0; i < numElite; i++) {
            if (Math.random() < Main.MUTATION_CHANCE) {
                nextGenPop.individuals.add(mutate(sorted.get(i)));
                nextGenPop.individuals.get(nextGenPop.individuals.size() - 1).calculateCost();
            } else {
                nextGenPop.individuals.add(sorted.get(i));
                nextGenPop.individuals.get(nextGenPop.individuals.size() - 1).calculateCost();
            }
            populationSpaceAvailable--;
        }
        
        return nextGenPop;
        
    }
    
    
    public Individual getBestIndividual(ArrayList<Individual> pop) {
        int minIndex = -1;
        int minCost = Integer.MAX_VALUE;

        for (int i = 0; i < pop.size(); i++) {
            if (pop.get(i).getCost() < minCost) {
                minCost = pop.get(i).getCost();
                minIndex = i;
            }
        }
        return pop.get(minIndex);
    }

}
