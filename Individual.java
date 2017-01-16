package graphcol;

import static graphcol.Main.graph;

public class Individual {
    double fitness;
    int[] ordering;

    public Individual(){
        this.fitness = 0;
        this.ordering = new int[graph.verticesNumber];
    }

    public double getFitness(){
        return fitness;
    }
}
