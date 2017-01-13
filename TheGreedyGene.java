package graphcol;

import java.util.ArrayList;
import java.util.Collections;

import static graphcol.Main.graph;
import static graphcol.Main.node;

public class TheGreedyGene {

    public final static boolean POPULATION_DEBUG = false;

    public final static int POP_SIZE = 10;
    public final static double START_WITH_GIVEN_ORDER = 0;
    public final static int MUTATION_RATE = 1;

    public static int[][] population;
    public static double[] fitness;

    public static ArrayList<Integer> allVertices;

    public static void run(int[] startingOrder){
        initialize();

        generateInitialPopulation(startingOrder);

        if(POPULATION_DEBUG) {
            System.out.println("\nPopulation generated: ");

            for(int i = 0; i < POP_SIZE; i++){
                for(int j = 0; j < graph.verticesNumber; j++){
                    System.out.print(population[i][j] + " ");
                }
                System.out.print("\n ");
            }
        }

        /*calculateFitness();
        sort();

        kill();
        selectParents();
        crossover();
        mutate();

        calculateFitness();*/
    }

    public static void initialize(){
        allVertices = new ArrayList<Integer>(graph.verticesNumber);
        for(int i = 1; i <= graph.verticesNumber; i++){
            allVertices.add(i);
        }
        population = new int[POP_SIZE][graph.verticesNumber];
        fitness = new double[POP_SIZE];
    }

    public static void generateInitialPopulation(int[] startingOrder){
        for(int i = 0; i < POP_SIZE; i++){
            if(i<POP_SIZE*START_WITH_GIVEN_ORDER){
                for(int j = 0; j < graph.verticesNumber; j++){
                    population[i][j]=startingOrder[j];
                }
            }
            else{
                Collections.shuffle(allVertices);

                Integer[] tempArray = (Integer[])allVertices.toArray(new Integer[0]);

                for(int j = 0; j < graph.verticesNumber; j++){
                    population[i][j] = tempArray[j];
                }
            }
        }
    }
}
