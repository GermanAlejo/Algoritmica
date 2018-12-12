/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pso;

import java.util.Arrays;

/**
 *
 * @author GermánAlejo
 * this class represents the solution
 */
public class Location {

    //array with the buildings positions
    int[] pos;
    
     public Location() {
        super();
    }

    
    public Location(int[] pos) {
        super();
        this.pos=pos;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
    
    public String toString(){
        return Arrays.toString(pos);
    }

    

}
