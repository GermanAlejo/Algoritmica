/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author geral
 */
public class SA {

    //VARIABLES
    private static final int MAX_TEAM_PLAYERS = 25;
    private static final int MAX_LINEUP_PLAYERS = 11;

    //integer values for the diferents poitions in game
    private static final int GOALKEEPER = 0;
    private static final int DEFENDER = 1;
    private static final int MIDFIELDER = 2;
    private static final int STRIKER = 3;

    private static final int NUM_POSITIONS = 4;

    //number of players of each position
    private static final int NUM_GOALKEEPERS = 3;
    private static final int NUM_DEFENDERS = 7;
    private static final int NUM_MIDFIELDERS = 9;
    private static final int NUM_STRIKERS = 6;

    //number of players in the lineup of each position
    private static final int MAX_GOALKEEPERS = 1;
    private static final int MAX_DEFENDERS = 3;
    private static final int MAX_MIDFIELDERS = 4;
    private static final int MAX_STRIKERS = 3;

    private static final int MAX_ITER = 50;//max number of iterations

    //this will define the number of iterations that one season takes
    private static final int ITERATIONS_PER_SEASON = 10;

    //season beeing played right now
    private int currentSeason;

    //current iteration
    private int iter = 0;

    //lists and best solution
    private List<Player> playersList;//list containing all players
    private List<Player> bestSol;//best solution found

    Random rnd = new Random();

    public List<Player> simulatedAnnealing(int temp, int minTemp, double coolingRatio) {

        List<Player> sol;//current solution
        List<Player> neighbour;//neighbour to compare

        boolean encontrado;
        double diference;//diference of cost between neighbours
        double prob;//probability to jump

        //fill the player list with all players
        generateRandomTeam();

        System.out.println("THE TEAM IS: ");
        toString(playersList);
        System.out.println("\n");

        //first inicialites the inicial solution
        sol = generateInitialSolution();
        System.out.println("\nINITIAL SOLUTION: " + toString(sol));

        //initilizes the best solution
        bestSol = sol;

        while (temp > minTemp && iter < MAX_ITER) {

            encontrado = false;
            iter++;
            System.out.println("CURRENT ITERATION: " + iter);
            calculateSeason();
            System.out.println("\nSEASON Nº: " + currentSeason);
            
            System.out.println("UPDATE ALL PLAYERS");
            
            updatePlayers(sol);

            while (!encontrado) {//another stop condition??

                //generate a random neightbour of the solution
                neighbour = generateRandomNeighbour(sol);
                System.out.println("NEW NEIGHBOUR GENERATED");

                //if the diference is positive means the neighbour have better cost
                diference = costFunc(neighbour) - costFunc(sol);
                prob = jumpProbability(temp, diference);

                if (diference > 0 || Math.random() <= prob) {//if the neighbour is better than the current solution
                    //actualize the current solution
                    System.out.println("\nNEW POSIBLE SOLUTION FOUND");

                    sol.clear();//discard old solution
                    sol.addAll(neighbour);//save the new solution
                    toString(sol);

                    encontrado = true;

                    //resetMatrix(neightboursMatrix);
                    diference = costFunc(sol) - costFunc(bestSol);
                    if (diference > 0) {
                        System.out.println("NEW BEST SOLUTION FOUND");
                        bestSol = sol;
                    }
                }

                //check if the new sol has better cost than the better solution we found
            }

            //decrease the temperature
            temp *= coolingRatio;
        }

        return bestSol;
    }

    /*
    This method generates a new random neighbour
     */
    public List<Player> generateRandomNeighbour(List<Player> sol) {

        Player oldPlayer;
        Player newPlayer;
        List<Player> neighbour;
        int x;

        do {
            
            //random.nextInt(max - min + 1) + min
            x = rnd.nextInt(MAX_LINEUP_PLAYERS - 1);

            oldPlayer = sol.get(x);

            newPlayer = getSameTypePlayer(oldPlayer);
            
        } while (oldPlayer.getInjured()!=0 && newPlayer.getInjured()!=0);//check we do not select injured players

        neighbour = swapNeighbour(oldPlayer, newPlayer, sol);

        return neighbour;
    }

    /*
    this method return a player of the same type and position to exchange that player with
    the one passed as parameter
     */
    public Player getSameTypePlayer(Player oldPlayer) {
        Player newPlayer;
        int i = 0;
        boolean encontrado = false;

        do {

            newPlayer = playersList.get(i);

            if (newPlayer.getPosition() == oldPlayer.getPosition() && !newPlayer.equals(oldPlayer)) {
                encontrado = true;
            }

            i++;
        } while (!encontrado && i < playersList.size());

        return newPlayer;
    }

    /*
       this method returns a new solution generated by swaping
        the a player of the soluton with another random player of the same type
     */
    public List<Player> swapNeighbour(Player oldPlayer, Player newPlayer, List<Player> neighbour) {

        List<Player> res = new ArrayList<Player>();
        res.addAll(neighbour);
        res.remove(oldPlayer);
        res.add(newPlayer);

        return res;
    }

    //TODO: check prob generator
    /*
    this method returns a prob to jump to another neighour
     */
    public double jumpProbability(double temp, double diferencia) {
        double res = Math.exp(diferencia / temp);
        return res;
    }

    //returns the cost of the neighbour as parameter
    public int costFunc(List<Player> neighbour) {

        //System.out.println("He petado en la iteracion: " + iter);
        int cost = 0;//neighbour[0] + neighbour[1] + neighbour[2];
        int i;

        for (i = 0; i < neighbour.size(); i++) {
            cost += neighbour.get(i).getPlayerFitness();
        }

        cost /= i;

        return cost;
    }

    /*
    this method returns an array with the players needed of each type
     */
    public List<Player> generateInitialSolution() {

        List<Player> solution = new ArrayList<>();
        Player player;
        int i = 0;
        //contadores para portero,defensa,mediocentro,delantero
        int pNum = 0, dNum = 0, mNum = 0, dlNum = 0;
        boolean terminado = false;

        while (!terminado || i < MAX_LINEUP_PLAYERS) {

            player = playersList.get(i);

            if (player.getPosition() == DEFENDER && dNum < MAX_DEFENDERS) {
                solution.add(player);
                dNum++;
            } else if (player.getPosition() == GOALKEEPER && pNum < MAX_GOALKEEPERS) {
                solution.add(player);
                pNum++;
            } else if (player.getPosition() == MIDFIELDER && mNum < MAX_MIDFIELDERS) {
                solution.add(player);
                mNum++;
            } else if (player.getPosition() == STRIKER && dlNum < MAX_STRIKERS) {
                solution.add(player);
                dlNum++;
            }

            if (dNum == MAX_DEFENDERS && pNum == MAX_GOALKEEPERS && mNum == MAX_MIDFIELDERS && dlNum == MAX_STRIKERS) {
                terminado = true;
            }

            i++;
        }

        return solution;
    }

    /*
    this method fills the list of all the players randomly and respecting the number
    of players of each position
     */
    public void generateRandomTeam() {

        Player newPlayer;
        playersList = new ArrayList<>();//initialization of the main list

        int pos = 0;
        double[] val;
        int max;

        for (int i = 0; i < NUM_POSITIONS; i++) {

            switch (i) {
                case GOALKEEPER:
                    max = NUM_GOALKEEPERS;
                    pos = GOALKEEPER;
                    break;

                case DEFENDER:
                    max = NUM_DEFENDERS;
                    pos = DEFENDER;
                    break;

                case MIDFIELDER:
                    max = NUM_MIDFIELDERS;
                    pos = MIDFIELDER;
                    break;
                case STRIKER:
                    max = NUM_STRIKERS;
                    pos = STRIKER;
                    break;

                default:
                    max = NUM_POSITIONS;
                    break;
            }

            for (int j = 0; j < max; j++) {

                val = generateValues();

                newPlayer = new Player(val[0], val[1], val[2], 0, false, pos);

                playersList.add(newPlayer);

            }
        }
    }

    /*
    this method returns an array with the values of the player generated randomly
    this order: technique-moral-physicalState-playerFitness
     */
    public double[] generateValues() {

        double[] val = new double[3];
        int aux;

        for (int i = 0; i < val.length; i++) {
            aux = rnd.nextInt(10);
            val[i] = (double) aux;
        }

        return val;
    }

    public String toString(List<Player> playerList) {

        String cad = "\nLINEUP: ";

        for (int i = 0; i < playerList.size(); i++) {
            cad += playerList.get(i).toString();
        }

        cad += "\nWITH A TOTAL COST OF: " + costFunc(playerList);

        return cad;
    }

    /*
    this method calculates the current season we are
     */
    public void calculateSeason() {
        currentSeason = iter / ITERATIONS_PER_SEASON;
    }

    /*
    this method updates all players atributes
    */
    private void updatePlayers(List<Player> sol) {
    
        for (Player player : playersList) {
            player.actualizePlayer(sol);
        }
        
    }

}
