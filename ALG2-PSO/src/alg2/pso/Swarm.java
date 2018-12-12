package alg2.pso;

import java.util.*;

/**
 *
 * @author manuel ridao pineda
 */
public class Swarm {

    public ArrayList<Particle> swarm;
    Random r;

    public Swarm() {
        swarm = new ArrayList<>();
        r = new Random();
        initializeParticlesRandomly();
    }

    // Initializes particle position and velocity randomly
    private void initializeParticlesRandomly() {
        for (int i = 0; i < PSO.MAX_PARTICLES; i++) {
            Particle p = new Particle();
            // Temp array to work with
            int[] x = new int[PSO.SIZE];
            for (int j = 0; j < PSO.SIZE; j++) {
                x[j] = j;
                p.velocity = r.nextInt(PSO.VMAX * 2 + 1) - PSO.VMAX;
            }
            shuffleArray(x);
            // Initialize particle object
            p.x_vector = Arrays.copyOf(x, PSO.SIZE);
            p.x_fitness = calculateFitness(p.x_vector);
            p.pBest = Arrays.copyOf(x, PSO.SIZE);
            p.p_fitness = calculateFitness(p.pBest);
            swarm.add(p);
        }
    }

    // Upgrades x_vector to pBest
    void upgradeParticle(Particle p) {
        p.pBest = Arrays.copyOf(p.x_vector, PSO.SIZE);
        p.p_fitness = p.x_fitness;
    }

    // Calculates the current fitness (x_fitness of x_vector) of the input particle
    public void updateValues(Particle p) {
        p.x_fitness = calculateFitness(p.x_vector);
    }

    // Quadratic assignment fitness: for each building, to each building, add flow * distance, and minimize this value
    public int calculateFitness(int[] solution) {
        int fitness = 0;
        for (int i = 0; i < PSO.SIZE; i++) {
            // Skip half the matrices because they are symmetric
            for (int j = i; j < PSO.SIZE; j++) {
                // Indices(i, j) are locations and measure distance, values ([i], [j]) are buildings and measure flow
                // When i==j, this will add 0*0
                fitness += PSO.dist[i][j] * PSO.flow[solution[i]][solution[j]];
            }
        }
        return fitness;
    }

    // Moves all the particles and updates their velocity
    public void moveParticles(int[] gBest) {
        for (Particle p : swarm) {
            double newVel = 0;
            for (int i = 0; i < PSO.SIZE; i++) {
                // Step 1: Calculate new velocity
                // Decomposed operation for easier debugging
                newVel += PSO.PHI1 * r.nextDouble() * (p.pBest[i] - p.x_vector[i])
                        + PSO.PHI2 * r.nextDouble() * (gBest[i] - p.x_vector[i]);
            }
            newVel = newVel + (double) (p.velocity);
            // Restrict the velocity
            newVel = (newVel > PSO.VMAX) ? PSO.VMAX : newVel;
            newVel = (newVel < -(PSO.VMAX)) ? -PSO.VMAX : newVel;
            p.velocity = (int) Math.round(newVel);

            // Step 2: calculate new position
            for (int i = 0; i < PSO.SIZE; i++) {
                /* Math.floorMod does not return negative values and will wrap the position around PSO.SIZE in case of overflow
                for example: M.fM(2-7, 6) = 1 (since -5 is out of index)*/
                int newPos = p.x_vector[i] + p.velocity;
                p.x_vector[i] = Math.floorMod(newPos, PSO.SIZE);
            }
        }
    }

    // Returns the particle with the best x_vector of this iteration. It doesnt necessarily have to be better than the previous iteration
    Particle getCurrentBestParticle() {
        Particle pBest = swarm.get(0);
        int xBestValue = Integer.MAX_VALUE;
        for (Particle p : swarm) {
            if (p.x_fitness < xBestValue) {
                pBest = p;
                xBestValue = p.p_fitness;
            }
        }
        return pBest;
    }

    // Fisher-Yates shuffle
    private void shuffleArray(int[] x) {
        for (int i = x.length - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);

            int a = x[index];
            x[index] = x[i];
            x[i] = a;
        }
    }

}
