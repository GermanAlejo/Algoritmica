package alg2.pso;

import java.util.*;

/**
 * @author manuel ridao pineda 
 * A solution is defined as a vector of integers
 * where the index are the locations and the values are the buildings. For
 * example, {3, 2, 0, 1} means building 3 at location 0, building 2 at location
 * 1, and so on.
 */
public class PSO {

    public static int[][] flow = {
        {0, 3, 1, 6, 1, 2},
        {3, 0, 1, 6, 6, 2},
        {1, 1, 0, 1, 2, 3},
        {6, 6, 1, 0, 4, 2},
        {1, 6, 2, 4, 0, 1},
        {2, 2, 3, 2, 1, 0}
    };
    
    public static int[][] dist = {
        {0, 5, 6, 1, 5, 2},
        {5, 0, 1, 4, 1, 6},
        {6, 1, 0, 2, 3, 3},
        {1, 4, 2, 0, 4, 6},
        {5, 1, 3, 4, 0, 1},
        {2, 6, 3, 6, 1, 0}
    };
    /*
    public static int[][] dist;
    public static int[][] flow;
     */
    // Problem size variables
    public static int SIZE = 6;
    public static int MAX_ITER = 10;

    // PSO variables
    public static int MAX_PARTICLES = 20;
    /*Defines the weight of the cognitive parameter. 0 means full social*/
    public static int PHI1 = 10;
    /*Defines the weight of the social parameter. 0 means full cognitive*/
    public static int PHI2 = 10;
    /*Max Velocity increase. Velocity is defined as how many steps the location will move forward or backward*/
    public static int VMAX = 4;

    // Profiling variables
    static long startTime;
    static long endTime;

    public static void main(String[] args) {
        //Parameter initialization
        // Remove comment for random matrices initialization
        /*Random r = new Random();
        dist = new int[SIZE][SIZE];
        flow = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = i; j < SIZE; j++) {
                if (i != j) {
                    int d = r.nextInt(10) + 1;
                    int f = r.nextInt(10) + 1;
                    dist[i][j] = d;
                    flow[i][j] = f;
                    dist[j][i] = d;
                    flow[j][i] = f;
                } else {
                    dist[i][j] = 0;
                    flow[i][j] = 0;
                    dist[j][i] = 0;
                    flow[j][i] = 0;
                }
            }
        }
        System.out.println("DISTANCES:");
        for (int i = 0; i < SIZE; i++) {
            System.out.println(Arrays.toString(dist[i]));
        }
        System.out.println("FLOW:");
        for (int i = 0; i < SIZE; i++) {
            System.out.println(Arrays.toString(flow[i]));
        }*/

        /*Swarm has all the algorithm logic. Initializing Swarm initializes all the particles*/
        Swarm swarm = new Swarm();
        /*Best particle ever (gBest)*/
        Particle bestParticle = swarm.swarm.get(0);
        /*Best particle in this iteration*/
        Particle topParticle = swarm.swarm.get(0);
        /*Best fitness ever (F(gBest))*/
        int best_p_fitness = Integer.MAX_VALUE;
        /*Iteration of best fitness ever (Only used for display)*/
        int bestIter = 0;

        startTime = System.nanoTime();

        int iter = 0;
        while (iter < MAX_ITER) {
            // Step 1: evaluate the particles
            for (Particle p : swarm.swarm) {
                // Calculates the new fitness of the x_vector
                swarm.updateValues(p);
                if (p.x_fitness < p.p_fitness) {
                    // Updates the pBest vector and value of this particle
                    swarm.upgradeParticle(p);
                }
                if (p.p_fitness < best_p_fitness) {
                    best_p_fitness = p.p_fitness;
                    bestParticle = new Particle(p);
                    bestIter = iter;
                    System.out.println("\tNew best found: " + p.toString());
                }
            }

            // Step 2: Display results
            // Returns the particle with the best x_fitness value (the current position)
            topParticle = new Particle(swarm.getCurrentBestParticle());
            System.out.println("Finished iteration " + iter + ", best result is " + topParticle.toString());

            // Step3: Move the particles: this method updates velocity and calculates location
            swarm.moveParticles(bestParticle.pBest);

            iter++;
        }

        endTime = System.nanoTime();

        double totalTime = ((double) (endTime - startTime)) / 1000;

        System.out.println("Best solution is " + bestParticle.toString());
        System.out.println("Found in iteration " + bestIter);
        System.out.println("Total time:" + totalTime + " microseconds");
    }
}
