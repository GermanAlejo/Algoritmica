/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg_epd2_p1;

import java.util.Arrays;

/**
 *
 * @author geral
 */
public class Solution {
    
    private int tenencia;
    private int [] sol;
    
    public Solution(){
        this.tenencia=0;
        this.sol= new int [7];
    }

    public Solution(int tenencia) {
        this.tenencia = tenencia;
        this.sol = new int[]{1,2,3,4,5,6,7};
    }

    public Solution(int tenencia, int[] sol) {
        this.tenencia = tenencia;
        this.sol = sol;
    }

    public int getTenencia() {
        return tenencia;
    }

    public void setTenencia(int tenencia) {
        this.tenencia = tenencia;
    }
    
    public void increaseTenencia(){
        this.tenencia++;
    }

    public int[] getSol() {
        return sol;
    }

    public void setSol(int[] sol) {
        this.sol = Arrays.copyOf(sol, sol.length);
    }

    @Override
    public String toString() {
        return "Solution{" + "tenencia=" + tenencia + ", sol=" + Arrays.toString(sol) + "}";
    }
    
    
    
}
