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
public class Individual {
    
    private int cost;
    ArrayList<Integer> objectList;
    
    public Individual(){
        objectList = new ArrayList<>();
    }
    
    public int getObject(int i){
        return objectList.get(i);
    }
    
    public void switchGenes(int ind1, int ind2){
        int aux = ind1;
        objectList.set(ind1, ind2);
        objectList.set(ind2, aux);
    }
    
    public void inicializeRandom(int numObjects){
    
        Random rand = new Random();
        int r;
        
        //fill the list with random 1 and 0
        for(int i=0;i<numObjects;i++){
            r = rand.nextInt(2);
            objectList.add(r);
        }
        //calculate the cost of the individual
        calculateCost();
    }

    //calculates de cost of the individual by doing the diference of their plates
    public void calculateCost() {
    
        int cost1=0,cost2=0;
        
        for(int i=0;i<objectList.size();i++){
            if(objectList.get(i)==0){
                cost1+=Main.weights[i];
            }else{
                cost2+=Main.weights[i];
            }
        }
        
        cost=Math.abs(cost1-cost2);
    
    }
    
    public int getCost(){
        return cost;
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Individual (" + cost + "): ");
        for (int i : objectList) {
            build.append(i + " ");
        }
        return build.toString();
    }
    
    
}
