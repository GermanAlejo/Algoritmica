/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;



/**
 *
 * @author GermánAlejo
 */
public class Particle {

    private Location pBest;//best solution of the particle
    private Location vector;//current solution of the particle
            
    private int velocity;//particle velocity, determines how much the location is increased
    
    private int pBestFitness;//fitness of the best solution
    private int fitnessValue;//fitness of current solution

    public Particle() {
        super();
    }

    public Particle(Location pBest, Location vector, int velocity, int pBestFitness, int fitnessValue) {
        this.pBest = pBest;
        this.vector = vector;
        this.velocity = velocity;
        this.pBestFitness = pBestFitness;
        this.fitnessValue = fitnessValue;
    }
    
    

    public Particle(int fitnessValue, Location pBest) {
        super();
        this.fitnessValue = fitnessValue;
        this.pBest = pBest;
    }

    public Location getpBest() {
        return pBest;
    }

    
    //cambiar por un copyof
    public void setpBest(Location pBest) {
        this.pBest = pBest;
    }

    public Location getVector() {
        return vector;
    }

    //cambiar por un copyof
    public void setVector(Location vector) {
        this.vector = vector;
    }

    //returns the best fitness of the particle
    public int getpBestFitness() {
        return pBestFitness;
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public int getVelocity() {
        return velocity;
    }
    
    public void setBoth(Location v){
        this.pBest=v;
        this.vector=v;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void setpBestFitness(int pBestFitness) {
        this.pBestFitness = pBestFitness;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
    
    @Override
    public String toString () {
        return  " cost:" + this.fitnessValue + " Location: " + pBest.toString();
    }

}
