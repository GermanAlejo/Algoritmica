/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author GermánAlejo
 */
public class PSOProcess {

    public static int SWARM_SIZE = 10;
    public static int MATRIX_TAM = 6;
    public static int MAX_VELOCITY = 5;
    public static int MIN_VELOCITY = 0;
    public static int MAX_FLOW = 10;
    public static int MIN_FLOW = 0;
    public static int MAX_DISTANCE = 6;
    public static int MIN_DISTANCE = 1;
    public static int MAX_ITERATIONS = 10;
    
    public static int SOC = 10;//social parameter
    public static int COG = 10;//cognitive parameter

    //list of all particles
    List<Particle> swarm = new ArrayList<>();

    int bestIter=0;
    int gBest;//fitness of the best solution in the swarm
    Particle pBest;//best particle of the swarm
    Particle tBest;//best particle of the current iteration
    Location tBestLocation;//best location of the current best solution
    Location gBestLocation;//best location of the best solution in the swarm
    

    Random rn = new Random();
    
    private long startTime;
    private long endTime;

    private int[][] flow;//matrix with the flow of people between buildings
    private int[][] loc;//matrix with the distances between buildings

    //Calculate the fitness in function of the solution
    public int getFitnessValue(int[] solution) {

        int fitness = 0;
       

        for (int i = 0; i < MATRIX_TAM; i++) {
            for (int j = i ; j < MATRIX_TAM; j++) {
                fitness += loc[i][j] * flow[solution[i]][solution[j]];
             
            }
        }

        return fitness;
    }

    public void execute() {

        startTime = System.currentTimeMillis();
        
        generateMatrix();
        initializeSwarm();//initialize swarm randomly
        updateFitness();//calculates the new fitness of the location of each particle
        int t = 0;

        //the loop ends whith the max iterations 
        while (t < MAX_ITERATIONS) {
            
            Particle p;//= swarm.get(t);
            
            
            for(int i=0;i<SWARM_SIZE;i++){
                
                p = swarm.get(i);
                
                //update pBest when the fitness if better
                if(p.getFitnessValue() < p.getpBestFitness()){
                    p.setpBest(p.getVector());
                    p.setpBestFitness(p.getFitnessValue());
                }
                
                //update gBest
                if(p.getpBestFitness() < this.gBest){
                    gBest = p.getpBestFitness();
                    gBestLocation = p.getpBest();
                    pBest = p;
                    bestIter = t;
                    System.out.println("new best solution found: " + pBest.toString()+"\n");
                }
                
            }
            
            updateFitness();
            p = swarm.get(t);
           
            System.out.println("Iteration: " + t + " Best Solution found in current iteration: " + p.toString());
            
            //Calculate new velocities and update locations
            for (int i = 0; i < SWARM_SIZE; i++) {
                
                p=swarm.get(i);
                
                Location solution = p.getVector();
                
                double vel=0;//new velocity
                
                //calculate new velocities
                for(int j=0;j<MATRIX_TAM;j++){
                    
                    vel += COG * rn.nextDouble() * (p.getpBest().getPos()[j] - solution.getPos()[j])
                            + SOC * rn.nextDouble() *(gBestLocation.getPos()[j] - solution.getPos()[j]);
                }
                
                if(vel< 0)
                    vel = MAX_VELOCITY * -1;
                else if(vel>MAX_VELOCITY)
                    vel = MAX_VELOCITY;
                
                
                p.setVelocity((int) vel);
                swarm.set(i, p);
                //System.out.println(p.toString());
                //calculate new positions
                for(int j=0;j<MATRIX_TAM;j++){
                    int newPosition = solution.getPos()[j] + p.getVelocity();
                    
                    if(newPosition<0)
                        newPosition*=-1;
                    
                    if(newPosition>5)
                        newPosition =5;
                    
                     p.getVector().getPos()[j] = Math.floorMod(newPosition, MATRIX_TAM);
                    
                    solution.getPos()[j] = newPosition;
                    p.setVector(solution);
                }
                
            }
            

            t++;
        }
        
        endTime = System.currentTimeMillis();
        double totalTime = ((double) (endTime - startTime));
        
        System.out.println("\nBest Solution:" + pBest.toString() + " Iteration:" + bestIter);
        System.out.println("Time: " + totalTime);
        
        
    }

    public void initializeSwarm() {

        Particle p;

        
        
        //inicialize location
        for (int n = 0; n < SWARM_SIZE; n++) {

            p = new Particle();

            int vel;//this velocity will be asing to the particle
            int[] x_location = new int[MATRIX_TAM];//this initial solution will be asign to the particle
 
            Location l;

            for (int i = 0; i < MATRIX_TAM; i++) {
                
                //fills the array
                x_location[i] = i;
                
            }
            
            mixArray(x_location);//mix the array randomly
            l = new Location(x_location);
            p.setVector(l);
            
            vel = rn.nextInt(MAX_VELOCITY - MIN_VELOCITY + 1) - MIN_VELOCITY;//the velocity is generated randomly
            p.setVelocity(vel);
            p.setBoth(l);
            p.setFitnessValue(getFitnessValue(p.getVector().getPos()));//calculate fitness of current solution 
            p.setpBestFitness(p.getFitnessValue());
         
            
            swarm.add(p);
        }
        
        this.pBest=swarm.get(0);
        this.tBest=swarm.get(0);
        this.gBestLocation = pBest.getpBest();
        this.tBestLocation = tBest.getpBest();
        this.gBest = getFitnessValue(gBestLocation.getPos());
        
    }
    
    //shuffle the numbers in the array randomly
    public void mixArray(int[] v){
        Random rnd = new Random();
        for (int i = v.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = v[index];
            v[index] = v[i];
            v[i] = a;
        }
    }

    //fills both distances matrix and fow matrix randomly
    public void generateMatrix() {

        flow = new int[MATRIX_TAM][MATRIX_TAM];
        loc = new int[MATRIX_TAM][MATRIX_TAM];

        for (int i = 0; i < MATRIX_TAM; i++) {
            for (int j = 0; j < MATRIX_TAM; j++) {

                //fill the distances matrix
                if (i != j) {
                    loc[i][j] = rn.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1) + MIN_DISTANCE;
                } else {
                    loc[i][j] = 0;
                }

                //fill the flow matrix
                if (i != j) {
                    flow[i][j] = rn.nextInt(MAX_FLOW - MIN_FLOW + 1) + MIN_FLOW;
                } else {
                    flow[i][j] = 0;
                }

            }
        }

    }

    //update the fitness of all particles
    public void updateFitness() {
        Particle p;
        for (int i = 0; i < SWARM_SIZE; i++) {
            p = swarm.get(i);
            p.setFitnessValue(getFitnessValue(p.getVector().getPos()));
            p.setpBestFitness(getFitnessValue(p.getpBest().getPos()));
        }
    }

}
