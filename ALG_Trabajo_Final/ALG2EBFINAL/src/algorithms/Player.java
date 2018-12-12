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
public class Player {
    
    //FINALS VARIABLES
    //Maximun and minimun number of seasons that a player can be injured
    private static final int MAX_INJURED = 3;
    private static final int MIN_INJURED = 1;
    
    //Probabilities of a player beeing injured each season
    private static final double INJ_PROB_PLAYED = 0.15;
    private static final double INJ_PROB_NOTPLAYED = 0.5;
   
    //these values are between 0 and 10
    private double technique;//value of the player´s technique  
    private double moral;//value of the player´s moral
    private double physicalState;////value of the player´s body state and fitness, which is not the fitness of the solution
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private double playerFitness;//fitness of the player, depend of the three previous

    private int injured;//number of seasons without playing, if set to 0 means is not injured
    
    private boolean lastSeason;//true=played in last season, false=not played
    
    private int position;//position of the player
    
    Random r = new Random();
    
    public Player() {
        this.technique = 0;
        this.moral = 0;
        this.physicalState = 0;
        this.playerFitness = 0;
        this.injured = 0;
        this.lastSeason = false;
        this.position = 0;
    }
    
    public Player(double technique, double moral, double physicalState
            , int injured, boolean lastSeason, int position) {
        this.technique = technique;
        this.moral = moral;
        this.physicalState = physicalState;
        calculatePlayerFitness();
        this.injured = injured;
        this.lastSeason = lastSeason;
        this.position = position;
    }

    public double getTechnique() {
        return technique;
    }
    
    //check if the passed value for this atribute is correct(0-10)
    public void setTechnique(double technique) {
        if(technique<10 && technique>0)
            this.technique = technique;
    }
    
    public boolean getLastSeason(){
        return lastSeason;
    }
    
    public void setLastSeason(boolean lastSeason){
        this.lastSeason = lastSeason;
    }

    public double getMoral() {
        return moral;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
    //check if the passed value for this atribute is correct(0-10)
    public void setMoral(double moral) {
        if(moral<10 && moral>0)
            this.moral = moral;
    }

    public double getPhysicalState() {
        return physicalState;
    }
    
    //check if the passed value for this atribute is correct(0-10)
    public void setPhysicalState(double physicalState) {
        if(physicalState<10 && physicalState>0)
            this.physicalState = physicalState;
    }

    public double getPlayerFitness() {
        return playerFitness;
    }

    public int getInjured() {
        return injured;
    }
    
    /*
    this method sets the player as injured for a number of
    seasons depending of the probability passed
    this probability only has to values and are declared as constants 
    int the SA class
    */
    private void setInjured(double prob){
        
        //decimal to integer(.15->15)
        int p = (int) prob*100;
        
        //generates a random integer between 0 and 100
        int aux = r.nextInt()*100;
        
        //if the number is less/equal to the probability, then is injured
        if(aux <= p){
            
            //generates a random number of seasons to be injured
                                //random.nextInt(max - min + 1) + min
            this.injured = r.nextInt(MAX_INJURED - MIN_INJURED + 1) + MIN_INJURED;
            
        }
    }

    /*
    this metod calculates and returns the fitness value of this player
    the value depends of the three variables
    the method calculates the average of the values of these three values
    */
    private void calculatePlayerFitness(){    
        this.playerFitness = (this.moral + this.physicalState + this.technique)/3;
    }
    
    /*
    This method increases the atributes of the player depending
     of the lastSeason value
    */
    public void increaseAtributes(){
        // randomValue = 0.1 + (0.5 - 0.1) * r.nextDouble();
        
        if(lastSeason){
            //last season played
            technique += r.nextDouble() * 0.2 - 0.05;//random number (0.2/-0.05)
            moral += r.nextDouble() * 0.2 - 0.05;
            physicalState += r.nextDouble() * 0.2 - 0.05;
        }else{
            //last season not played
            technique += r.nextDouble() * 0.05 - 0.2;
            moral += r.nextDouble() * 0.05 - 0.2;
            physicalState += r.nextDouble() * 0.05 - 0.2;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (Double.doubleToLongBits(this.technique) != Double.doubleToLongBits(other.technique)) {
            return false;
        }
        if (Double.doubleToLongBits(this.moral) != Double.doubleToLongBits(other.moral)) {
            return false;
        }
        if (Double.doubleToLongBits(this.physicalState) != Double.doubleToLongBits(other.physicalState)) {
            return false;
        }
        if (this.injured != other.injured) {
            return false;
        }
        if (this.lastSeason != other.lastSeason) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "Player{" + "technique=" + technique + ", moral=" + moral + ", physicalState=" + physicalState + ", playerFitness=" + playerFitness + ", injured=" + injured + ", lastSeason=" + lastSeason + ", position=" + position + '}';
        String cad ="\nPLAYER: technique=" + technique + " | moral=" + moral + " | physicalState=" + physicalState;
        cad+="\n\tFITNESS: " + playerFitness + " INJURED: " + injured + " PLAYEDLASTSEASON?: " + lastSeason + " POSITION PLAYED: " + position;
        return cad;
    }
    
    /*
    this method updates the player atributes
    */
    public void actualizePlayer(List<Player> solution){
        
        //if the player is in the list then he played
        if(solution.contains(this))
            lastSeason = true;
        else
            lastSeason = false;
        
        //now update the atributes and the injure prob
        increaseAtributes();
        
        if(lastSeason)
            setInjured(INJ_PROB_PLAYED);
        else
            setInjured(INJ_PROB_NOTPLAYED);
        
    }
    
    
}
