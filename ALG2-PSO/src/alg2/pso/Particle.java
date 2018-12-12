package alg2.pso;

import java.util.Arrays;

/**
 *
 * @author manuel ridao pineda
 */
public class Particle {
    /* X vector, ie this particle solution vector*/
    public int[] x_vector;
    /* This particle's velocity. Velocity will determine how much to increase x_vector. Velocity cant be a vector because
    then it could duplicate buildings*/
    public int velocity;
    /*pBest vector, ie this particle best solution*/
    public int[] pBest;
    
    /*Fitness of current solution (x_vector)*/
    public int x_fitness;
    /*Fitness of best solution (pBest_vector)*/
    public int p_fitness;
    
    public Particle () {
        x_vector = new int[PSO.SIZE];        
        pBest = new int[PSO.SIZE];
        velocity = 0;
        x_fitness = 0;
        p_fitness = 0;
    }
    // Copy constructor: the logic is that (this==p) is false and this.equals(p) is true
    public Particle (Particle p) {
        x_vector = Arrays.copyOf(p.x_vector, PSO.SIZE);
        velocity = p.velocity;
        pBest = Arrays.copyOf(p.pBest, PSO.SIZE);
        x_fitness = p.x_fitness;
        p_fitness = p.p_fitness;
    }

    @Override
    public String toString () {
        return "( " + this.p_fitness + " ) : " + Arrays.toString(pBest);
    }
}
