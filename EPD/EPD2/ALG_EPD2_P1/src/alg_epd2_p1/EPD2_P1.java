/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alg_epd2_p1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author geral
 */
public class EPD2_P1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int [][] neightboursMatrix = setMatrix();
        
        Solution solIni = new Solution(0);//inicial solution with 0 tenency an {1,2,3,4,5,6,7} as array
        int iterNum = 10;//number of iterations of the algoritm
        int maxTemp = 4;//max tendency allowed
        
        Solution finalSol = tabooList(neightboursMatrix,solIni,iterNum,maxTemp);
        int finalCost = costFunc(finalSol.getSol());
        
        System.out.println("The final solution is: " + finalSol.toString() + " with cost is: " + finalCost);
        
        
        
    }

    //main method with the taboo algorithm
    public static Solution tabooList(int[][] neightboursMatrix, Solution solIni, int iterNum,int maxTemp) {

        Solution sol = solIni;//inicial solution
        Solution bestSol = sol;//the best solution
        Solution aux;
        List<Solution> neighborsList;
        Queue tabuList = new LinkedList();
        int cont = 0,i=0;
        int listTam;
        double diference;//diference of cost between neighbours
        

        while (cont < iterNum) {//stopping condition

            //generates the neighbors of the actual solution
            neighborsList = generateRandomNeighbours(sol, neightboursMatrix);
            listTam = neighborsList.size()-1;
            
            while (i < listTam) {

                //obtain the nex neighbour in the list
                aux = neighborsList.get(i);

                diference = costFunc(aux.getSol()) - costFunc(sol.getSol());
                if (!isTaboo(aux,tabuList) && diference > 0) {
                    sol = copySolution(aux);//asignate the new best solution
                } else if (isTaboo(aux,tabuList) && diference > 0) {
                    sol = copySolution(aux);
                    tabuList.remove(sol);
                }
                i++;
            }

            if (tabuList.contains(sol)) {
                //solution already in the taboo list
                sol.increaseTenencia();
            } else {
                //add the solution to the taboo list
                tabuList.add(sol);
            }
            
            diference = costFunc(sol.getSol()) - costFunc(bestSol.getSol());
            if(diference>0)
                bestSol=copySolution(sol);//saves the new best solution
            
            actualizeTabooList(tabuList,maxTemp);
            cont++;
        }

        return bestSol;
    }

    //returns if the solution is in the taboo list
    public static boolean isTaboo(Solution sol,Queue tabuList) {
        return tabuList.contains(sol);
    }
    
    //actualize the tenency of all elements in the list removing old elements
    public static void actualizeTabooList(Queue <Solution>tabuList,int maxTemp){
        
        Iterator it = tabuList.iterator();
        Solution aux;
        if(!tabuList.isEmpty()){
            aux = (Solution) it.next();
            if(aux.getTenencia()>maxTemp)
                tabuList.remove(aux);
        }
    }

    //set all values of the matrix to 0
    public static int[][] setMatrix() {
        int[][] neighboursMatrix = new int[7][7];
        int n = neighboursMatrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                neighboursMatrix[i][j] = 0;
            }
        }
        return neighboursMatrix;
    }

    //returns all the neighbours, is not random just returns all of them
    public static List<Solution> generateRandomNeighbours(Solution sol, int[][] neighboursMatrix) {

        resetMatrix(neighboursMatrix);//reset the matrix with all 0
        Solution neighbour;
        int a, b, x, y;
        List<Solution> neighborsList = new ArrayList<>();

        int cont = 0;
        while (cont < 20) {//put a variable instead of 20
            
            do {
                x = (int) (Math.random() * (7 - 1) + 1);//random row number between 1 and 6
                y = (int) (Math.random() * (8 - (x + 1)) + (x + 1));//column number between 2 and 7
                a = x - 1;
                b = y - 1;
            } while (neighboursMatrix[a][b] == 1);

            neighboursMatrix[a][b] = 1;
            neighbour = setNeighbour(x, y, sol);
            neighborsList.add(neighbour);
            cont++;
        }
//
        return neighborsList;

    }

    //changes the values in the solution´s array
    public static Solution setNeighbour(int x, int y, Solution neighbour) {

        Solution res = copySolution(neighbour);
        int cont = 0, encontrados = 0;
        int tam = res.getSol().length;
        int[] aux = res.getSol();
        while (cont < tam && encontrados < 2) {

            if (aux[cont] == x) {
                aux[cont] = y;
                encontrados++;
            } else if (aux[cont] == y) {
                aux[cont] = x;
                encontrados++;
            }

            cont++;

        }
        res.setSol(aux);

        return res;
    }


    public static int costFunc(int[] neighbour) {

        //System.out.println("He petado en la iteracion: " + iter);
        int cost = neighbour[0] + neighbour[1] + neighbour[2];

        return cost;
    }
    
    
    public static void resetMatrix(int[][] neighboursMatrix) {

        int tam = neighboursMatrix.length;

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                neighboursMatrix[i][j] = 0;
            }
        }

    }
    
    //this method just copy the solutions objects
    public static Solution copySolution(Solution sol){
        Solution res = new Solution();
        res.setSol(sol.getSol());
        res.setTenencia(sol.getTenencia());
        return res;
    }

}
