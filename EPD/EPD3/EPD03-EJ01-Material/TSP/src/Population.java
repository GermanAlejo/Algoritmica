import java.util.ArrayList;
import java.util.Random;

public class Population {

    public ArrayList<Individual> individuals = new ArrayList<Individual>();
    private int numCities;
    private ArrayList<Individual> sorted;

    public Population(int numCities) {
        this.numCities = numCities;
    }

    public void initializePopulationRandomly(int numIndividuals) {

        Individual in;
        
        for(int i=0;i<numIndividuals;i++){
            in = new Individual();//first create the individual
            in.generateRandomTour(numCities);//then generate a random tour of cities
            individuals.add(in);//finally add the individual to the list
        }
        
    }

    public Population evolve() {

    }

    public Individual mutate(Individual ind) {

    }

    public Individual crossover(Individual p1, Individual p2) {
        Random r = new Random();
        Individual child = new Individual();
        
        int start = r.nextInt(numCities);
        int end = r.nextInt(numCities);
        
        
        
        if(Math.random() < TSP.CLONE_RATE){
            if(p1.getCost() > p2.getCost())
                return p1;
            else
                return p2;
        }
        return child;
    }

    public Individual getBestIndividual(ArrayList<Individual> pop) {

        Individual best=pop.get(0);
        for(int i=0;i<pop.size();i++){
            if(pop.get(i).getCost()>best.getCost())
                best = pop.get(i);
        }
        return best;
    }

    public Individual selectParentViaTournament() {
        Random rand = new Random();
        // Select individuals randomly from the population
        // WITH a bias towards selecting high fitness individuals
        if (rand.nextDouble() < TSP.ELITE_PARENT_RATE) {
            int numElite = (int) (TSP.ELITE_PARENT_RATE * TSP.POPULATION_SIZE);
            return sorted.get(rand.nextInt(numElite));
        }

        // Otherwise select a parent from the general population with a uniform distribution
        ArrayList<Individual> tournamentPopulation = new ArrayList<Individual>();
        for (int i = 0; i < TSP.TOURNAMENT_SIZE; i++) {
            int randIndex = (int) (Math.random() * sorted.size());
            tournamentPopulation.add(sorted.get(randIndex));
        }
        return getBestIndividual(tournamentPopulation);
    }

    public Individual getBestIndividualInPop() {
        if (sorted != null) {
            return sorted.get(0);
        }
        return getBestIndividual(this.individuals);
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        for (Individual ind : individuals) {
            build.append(ind.toString() + "\n");
        }
        return build.toString();
    }
}