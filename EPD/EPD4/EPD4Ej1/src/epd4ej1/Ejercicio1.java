package epd4ej1;

public class Ejercicio1 {

    public static void main(String[] args) {
        Knapsack mochila = new Knapsack();
        mochila.GenerateInitialPopulation();
        mochila.evolve();
        mochila.printResults();
    }
}
