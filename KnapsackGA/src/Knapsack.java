
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class Knapsack {

    private int PopulationSize = 100;
    private int numGenerations = 1000;
    private double mutationRate = 0.01;
    private double crossoverRate = 0.90;

    private int capacity = 0;
    private int numItems = 0;
    private int totalValue = 0;
    private double penalty = 0;
    private double offset = 0;

    private boolean optimalKnown = false;
    private boolean[] optimal;
    private String optSolStr = "";
    private int optSolSize = 0;
    private int optSolVal = 0;
    private double optSolFitness = 0;
    private int genOfBestFound = 0;
    private double maxFitnessSoFar = 0;

    private ArrayList<Integer> values = new ArrayList<Integer>();
    private ArrayList<Integer> sizes = new ArrayList<Integer>();

    //User-selected parameters
    private int selectionChoice = 0;
    private int crossoverChoice = 0;
    private int mutationChoice = 0;
    private double kValue;
    private int numGensMutate;
    private int numGensCrossover;

    private Random randomizer = new Random();

    private boolean[][] population;
    private double[] vectorFitness;
    private int[] vectorSizes;

    public Knapsack() {
        Scanner inputReader = new Scanner(System.in);
        int config;

        System.out.print("Configuration (0 = Default, 1 = Configure): ");
        config = inputReader.nextInt();

        if (config == 1) {
            System.out.print("Choose selection algorithm (0 = roulette, 1 = tournament): ");
            selectionChoice = inputReader.nextInt();

            if (selectionChoice == 1) {
                System.out.print("Choose k-value for tournament selection (0,60-0,85): ");
                kValue = inputReader.nextDouble();
            }

            System.out.print("Choose crossover algorithm (0 = n-slice, 1 = uniform): ");
            crossoverChoice = inputReader.nextInt();
            if (crossoverChoice == 0) {
                System.out.print("Choose n-value for n-slice: ");
                numGensCrossover = inputReader.nextInt();
            }

            System.out.print("Choose mutation algorithm (0 = n-point, 1 = invert): ");
            mutationChoice = inputReader.nextInt();
            if (mutationChoice == 0) {
                System.out.print("Choose number of points: ");
                numGensMutate = inputReader.nextInt();
            }

            System.out.print("Input population size (50-300): ");
            PopulationSize = inputReader.nextInt();

            System.out.print("Input number of generations: ");
            numGenerations = inputReader.nextInt();

            System.out.print("Input mutation rate (0,005-0,01): ");
            mutationRate = inputReader.nextDouble();

            System.out.print("Input crossover rate (0,80-0,95): ");
            crossoverRate = inputReader.nextDouble();
        }
        int notFound;
        String prefix;
        System.out.print("Enter dataset prefix: ");

        File source = new File(System.getProperty("java.class.path"));
        String binDirectory = source.getAbsoluteFile().getParentFile().getParentFile().toString() + File.separator + "Datasets" + File.separator;
        do {
            notFound = 0;
            prefix = inputReader.next();

            File fileChecker = new File(binDirectory + prefix + "_c.txt");
            if (fileChecker.exists()) {
                notFound++;
            }
            fileChecker = new File(binDirectory + prefix + "_p.txt");
            if (fileChecker.exists()) {
                notFound++;
            }
            fileChecker = new File(binDirectory + prefix + "_w.txt");
            if (fileChecker.exists()) {
                notFound++;
            }
            if (notFound < 3) {
                System.out.print("\nComplete dataset could not be found.\nTry again: ");
            }
        } while (notFound < 3);

        File fileChecker = new File(binDirectory + prefix + "_s.txt");
        if (fileChecker.exists()) {
            optimalKnown = true;
        }
        try {
            System.out.println("\nGetting dataset " + prefix + "...");
            System.out.println("Getting " + prefix + "_c.txt");
            Scanner cScanner = new Scanner(new File(binDirectory + prefix + "_c.txt"));
            capacity = cScanner.nextInt();
            cScanner.close();

            System.out.println("Getting " + prefix + "_w.txt");
            Scanner wScanner = new Scanner(new File(binDirectory + prefix + "_w.txt"));
            while (wScanner.hasNextInt()) {
                numItems++;
                int nextSize = wScanner.nextInt();
                sizes.add(nextSize);
            }
            wScanner.close();

            System.out.println("Getting " + prefix + "_p.txt");
            Scanner pScanner = new Scanner(new File(binDirectory + prefix + "_p.txt"));
            while (pScanner.hasNextInt()) {
                int nextVal = pScanner.nextInt();
                values.add(nextVal);
                totalValue += nextVal;
            }
            pScanner.close();

            //Determine over-capacity penalties.
            //penalty per-unit-over-capacity is equal to the highest value per unit size ratio of any package
            //offset penalty is one third the total value of all packages.
            double temp;
            for (int i = 0; i < numItems; i++) {
                offset += values.get(i);
                temp = ((double) values.get(i)) / sizes.get(i);
                if (temp > penalty) {
                    penalty = temp;
                }
            }
            offset *= .3;

            //Get Optimal Selection
            if (optimalKnown) {
                System.out.println("Getting " + prefix + "_s.txt");
                Scanner sScanner = new Scanner(new File(binDirectory + prefix + "_s.txt"));
                optimal = new boolean[numItems];
                for (int i = 0; i < numItems && sScanner.hasNextInt(); i++) {
                    if (sScanner.nextInt() == 0) {
                        optimal[i] = false;
                        optSolStr += "0";
                    } else {
                        optimal[i] = true;
                        optSolStr += "1";
                        optSolSize += sizes.get(i);
                        optSolVal += values.get(i);
                    }
                }
                optSolFitness = fitness(optimal, optSolSize);
                sScanner.close();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
        System.out.println(" item # |  value |   size |");
        for (int i = 0; i < numItems; i++) {
            System.out.printf("%7d |%7d |%7d |\n", i, values.get(i), sizes.get(i));
        }
        System.out.println("Capacity: " + capacity);
        System.out.println("Number of Items: " + numItems);
        System.out.println("Total Value: " + totalValue);
        System.out.println();
        inputReader.close();

        population = new boolean[PopulationSize][numItems];
        vectorFitness = new double[PopulationSize];
        vectorSizes = new int[PopulationSize];
    }

    public void GenerateInitialPopulation() {
        for (int i = 0; i < PopulationSize; i++) {
            //bit strings as a boolean array
            for (int j = 0; j < numItems; j++) {
                population[i][j] = randomizer.nextBoolean();
                if (population[i][j] == true) {
                    vectorSizes[i] += sizes.get(j);
                }
            }
            //fitness
            vectorFitness[i] = fitness(population[i], vectorSizes[i]);
        }
    }

    public void evolve() {
        for (int generation = 0; generation < numGenerations; generation++) {
            int[] selectedParents;
            boolean[][] childPool;

            printBestFitness(generation);

            //Selection
            if (selectionChoice == 0) {
                selectedParents = rouletteSelection();
            } else {
                selectedParents = tournamentSelection();
            }

            //Crossover            
            if (crossoverChoice == 0) {
                childPool = nSliceCrossover(selectedParents);
            } else {
                childPool = uniformCrossover(selectedParents);
            }

            //Mutate
            for (int i = 0; i < PopulationSize; i++) {
                if (randomizer.nextDouble() < mutationRate) {
                    if (mutationChoice == 0) {
                        childPool[i] = nPointMutation(childPool[i]);
                    } else {
                        childPool[i] = invertMutation(childPool[i]);
                    }
                }
            }

            childPool = elitism(childPool, generation);

            //Elitism: copy best previous solutions into child pool.
            //make the new children parents, and reevaluate their size and fitness
            population = childPool;
            for (int i = 0; i < PopulationSize; i++) {
                vectorSizes[i] = 0;
                for (int j = 0; j < numItems; j++) {
                    if (population[i][j] == true) {
                        vectorSizes[i] += sizes.get(j);
                    }
                }
                vectorFitness[i] = fitness(population[i], vectorSizes[i]);
            }
        }
    }

    private boolean[][] elitism(boolean[][] childPool, int generation) {
        int best = 0;
        int almostBest = 1;
        int pos1, pos2;
        for (int i = 1; i < PopulationSize; i++) {
            if (vectorFitness[i] > vectorFitness[best]) {
                almostBest = best;
                best = i;
            } else if (vectorFitness[i] > vectorFitness[almostBest]) {
                almostBest = i;
            }
        }

        //Check for a new best solution.
        if (vectorFitness[best] > maxFitnessSoFar) {
            maxFitnessSoFar = vectorFitness[best];
            genOfBestFound = generation + 1;
        }

        //Arbitrarily discard the first two child chromosomes in favor of the elite chromosomes,
        //and copy over their size and fitness.
        do {
            pos1 = randomizer.nextInt(PopulationSize);
            pos2 = randomizer.nextInt(PopulationSize);
        } while (pos1 == pos2);

        childPool[pos1] = population[best];
        childPool[pos2] = population[almostBest];

        vectorFitness[pos1] = vectorFitness[best];
        vectorFitness[pos2] = vectorFitness[almostBest];

        vectorSizes[pos1] = vectorSizes[best];
        vectorSizes[pos2] = vectorSizes[almostBest];

        return childPool;
    }

    private double fitness(boolean[] c, int s) {
        //Get the chromosome's value
        int runningValue = 0;
        for (int i = 0; i < numItems; i++) {
            if (c[i] == true) {
                runningValue += values.get(i);
            }
        }
        if (s > capacity) {
            double returnMe = runningValue - ((s - capacity) * penalty + offset);
            // keep fitness above zero, to avoid problems with fractional-fitness
            if (returnMe < 0.1) {
                return 0.1;
            } else {
                return returnMe;
            }
        } else {
            return runningValue;
        }
    }

    private int[] rouletteSelection() {
        double totalFitness = 0;
        for (int i = 0; i < PopulationSize; i++) {
            totalFitness += vectorFitness[i];
        }

        //pick the parents based on their percent fitness
        int[] selectedIndices = new int[PopulationSize];
        for (int i = 0; i < PopulationSize; i++) {
            //pick a spot on the roulette (from 0 to 1), and subtract the fractional fitness
            //until we find a roulette-selected parent. 
            double theSpot = randomizer.nextDouble();
            int j = 0;
            while (j < PopulationSize && theSpot > 0) {
                theSpot -= vectorFitness[j] / totalFitness;
                j++;
            }
            selectedIndices[i] = j - 1;
        }
        return selectedIndices;
    }

    private int[] tournamentSelection() {
        int[] selectedIndices = new int[PopulationSize];
        for (int i = 0; i < PopulationSize; i++) {
            int ind1 = randomizer.nextInt(PopulationSize);
            int ind2 = randomizer.nextInt(PopulationSize);

            //pick out the stronger chromosome
            int stronger;
            if (vectorFitness[ind1] < vectorFitness[ind2]) {
                stronger = 0;
            } else if (vectorFitness[ind1] > vectorFitness[ind2]) {
                stronger = 1;
            } else //equal
            {
                stronger = randomizer.nextInt(2);
            }

            double r = randomizer.nextDouble();
            if (r < kValue) //select the stronger chromosome
            {
                if (stronger == 1) {
                    selectedIndices[i] = ind1;
                } else {
                    selectedIndices[i] = ind2;
                }
            } else //select the weaker chromosome
             if (stronger == 1) {
                    selectedIndices[i] = ind2;
                } else {
                    selectedIndices[i] = ind1;
                }
        }
        return selectedIndices;
    }

    /**
     * nSliceCrossover performs the the N-Slice Crossover on a set of
     * chromosomes, given the indices of the selected parents, and the number of
     * slice points to use. Crossovers are performed sequentially, but because
     * selection is random, this does not have any effect on the randomness of
     * the whole process.
     *
     * @param c the set of chromosomes to crossover
     * @param pI the indices of the chromosomes to crossover
     * @param n the number of slice points to use
     * @param xProb the probability that crossover will occur
     * @return children the set of child chromosomes created in the crossover
     */
    private boolean[][] nSliceCrossover(int[] selectedParents) {
        boolean[][] children = new boolean[PopulationSize][numItems];
        boolean[] par1;
        boolean[] par2;
        boolean[] chi1;
        boolean[] chi2;

        for (int i = 0; i < PopulationSize; i += 2) {
            par1 = population[selectedParents[i]];
            chi1 = children[i];
            if (i < PopulationSize - 1) //deal with an odd-numbered pool-size
            {
                par2 = population[selectedParents[i + 1]];
                chi2 = children[i + 1];
            } else {
                par2 = population[selectedParents[0]];
                chi2 = children[0]; //overwrite the first child
            }

            if (randomizer.nextDouble() < crossoverRate) {
                //Get numGensCrossover unique, random, sorted indices to slice. (Each slice-number represents the point after
                //chromosome index numGensCrossover and before numGensCrossover+1.) Stop numGensCrossover from exceeding numItems-1, its max value.

                TreeSet<Integer> indices = new TreeSet<Integer>();
                int iNeeded;
                if (numGensCrossover < numItems) {
                    iNeeded = numGensCrossover;
                } else {
                    iNeeded = numItems - 1;
                }

                for (int j = 0; j < iNeeded; j++) {
                    int guess = randomizer.nextInt(numItems - 1);
                    while (indices.contains(guess)) {
                        guess = (guess + 1) % (numItems - 1);
                    }
                    indices.add(guess);
                }

                boolean takeFromPar1 = true;
                int j = 0;
                for (Integer currSlice : indices) {
                    while (j <= currSlice) {
                        if (takeFromPar1) {
                            chi1[j] = par1[j];
                            chi2[j] = par2[j];
                        } else {
                            chi1[j] = par2[j];
                            chi2[j] = par1[j];
                        }
                        j++;
                    }
                    takeFromPar1 = !takeFromPar1;
                }
                while (j < numItems) {
                    if (takeFromPar1) {
                        chi1[j] = par1[j];
                        chi2[j] = par2[j];
                    } else {
                        chi1[j] = par2[j];
                        chi2[j] = par1[j];
                    }
                    j++;
                }
            } else //skip this particular crossover
            {
                chi1 = par1;
                chi2 = par2;
            }
        }
        return children;
    }

    /**
     * uniformCrossover performs a Uniform Crossover on a set of chromosomes,
     * given the indices of the selected parents. Crossovers are performed
     * sequentially, but because selection is random, this does not have any
     * effect on the randomness of the whole process.
     *
     * @param population the set of chromosomes to crossover
     * @param selectedParents the indices of the chromosomes to crossover
     * @param crossoverRate the probability that crossover will occur
     * @return children the set of child chromosomes created in the crossover
     */
    private boolean[][] uniformCrossover(int[] selectedParents) {
        boolean[][] children = new boolean[PopulationSize][numItems];
        for (int i = 0; i < PopulationSize; i += 2) {
            if (randomizer.nextDouble() < crossoverRate) {
                for (int j = 0; j < numItems; j++) {
                    if (randomizer.nextBoolean()) {
                        children[i][j] = population[selectedParents[i]][j];
                        if (i < PopulationSize - 1) {
                            children[i + 1][j] = population[selectedParents[i + 1]][j];
                        } else {
                            children[0][j] = population[selectedParents[0]][j];
                        }
                    } else if (i < PopulationSize - 1) {
                        children[i][j] = population[selectedParents[i + 1]][j];
                        children[i + 1][j] = population[selectedParents[i]][j];
                    } else {
                        children[i][j] = population[selectedParents[0]][j];
                        children[0][j] = population[selectedParents[i]][j];
                    }
                }
            } else {
                for (int j = 0; j < numItems; j++) {
                    children[i][j] = population[selectedParents[i]][j];
                    if (i < PopulationSize - 1) {
                        children[i + 1][j] = population[selectedParents[i + 1]][j];
                    } else {
                        children[0][j] = population[selectedParents[0]][j];
                    }
                }
            }
        }
        return children;
    }

    /**
     * nPointMutation mutates the given chromosome by inverting numGensMutate
     * random bits.
     *
     * @param c the chromosome to mutate
     * @param numGensMutate the number of bits to invert
     * @return c the mutated chromosome
     */
    private boolean[] nPointMutation(boolean[] c) {
        int spot;
        //invert at these indices
        for (int i = 0; i < numGensMutate; i++) {
            spot = randomizer.nextInt(numItems);
            c[spot] = !c[spot];
        }
        return c;
    }

    /**
     * invertMutation mutates the given chromosome by inverting the entire bit
     * string.
     *
     * @param c the chromosome to mutate
     * @return c the mutated chromosome
     */
    private boolean[] invertMutation(boolean[] c) {
        boolean[] newChrom = new boolean[numItems];
        for (int i = 0; i < numItems; i++) {
            newChrom[i] = !c[i];
        }
        return newChrom;
    }

    public void printResults() {
        int bestFit = 0;
        for (int i = 1; i < PopulationSize; i++) {
            if (vectorFitness[i] > vectorFitness[bestFit] && vectorSizes[i] <= capacity) {
                bestFit = i;
            }
        }

        //Generate data to print.
        String bestSolStr = "";
        int bestSolVal = 0;
        boolean sameAsOptimal = true;
        for (int i = 0; i < numItems; i++) {
            if (population[bestFit][i] == true) {
                bestSolStr += "1";
                bestSolVal += values.get(i);
            } else {
                bestSolStr += "0";
            }

            if (!optimalKnown || population[bestFit][i] != optimal[i]) {
                sameAsOptimal = false;
            }
        }

        if (optimalKnown) {
            System.out.println("\nOptimal Solution: " + optSolStr);
            System.out.println("Optimal Fitness: " + fitness(optimal, optSolSize));
            System.out.println("Optimal Size out of Capacity: " + optSolSize + "/" + capacity);
            System.out.println("Optimal Value: " + optSolVal);
        }

        System.out.println("\nFittest Solution: " + bestSolStr);
        System.out.print("Fitness: " + vectorFitness[bestFit]);
        if (optimalKnown) {
            double percent = (vectorFitness[bestFit] / optSolFitness) * 100;
            System.out.printf(" (%,.2f%% of optimal)\n", percent);
        } else {
            System.out.println();
        }
        System.out.println("Size out of Capacity: " + vectorSizes[bestFit] + "/" + capacity);
        System.out.println("Value: " + bestSolVal);
        System.out.println("Found on generation " + genOfBestFound + ".");

        if (sameAsOptimal) {
            System.out.println("==Found the optimal!==");
        }
    }

    private void printBestFitness(int generation) {
        if ((generation + 1) % (numGenerations / 10) == 0) {
            System.out.println("Generation " + (generation + 1) + "...");
            if (optimalKnown) {
                double percent = (vectorFitness[0] / optSolFitness) * 100;
                System.out.printf("Best Fitness: %,.2f%% of known optimal.\n", percent);
            } else {
                System.out.println("Best Fitness: " + maxFitnessSoFar);
            }
        }
    }
}
