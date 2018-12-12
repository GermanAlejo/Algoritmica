import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Individual {

    public ArrayList<Integer> tour; // Stores the list of city paths
    private int cost; // The cost of the tour

    public Individual() {
        tour = new ArrayList<Integer>();
    }

    public void calculateCost() {
        
        int tam = tour.size()-1;
        int start = tour.get(0);
        
        for(int i=0;i<tam;i++){
            cost += TSP.matrix[tour.get(i)][tour.get(i+1)];
        }
        
        cost += TSP.matrix[tour.get(tam-1)][start];
        
    }

    public void generateRandomTour(int numCities) {
        
        for(int i=0;i<numCities;i++){
            tour.add(i);
        }
        Collections.shuffle(tour);
        
        calculateCost();
    }

    public void addCityToTour(int city) {
        tour.add(city);
    }

    public int getCity(int index) {
        return tour.get(index);
    }

    public void setCost(int c) {
        cost = c;
    }

    public int getCost() {
        return cost;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Individual (" + cost + "): ");
        for (int i : tour) {
            build.append(i + " ");
        }
        //build.append("\n");
        return build.toString();
    }
}