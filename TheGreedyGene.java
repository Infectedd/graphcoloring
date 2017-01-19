package graphcol;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static graphcol.Main.*;

public class TheGreedyGene {

    public final static boolean POPULATION_DEBUG = false;
    public final static boolean FITNESS_DEBUG = false;
    public final static boolean CROSSOVER_DEBUG = false;

    public final static boolean RUN_JUST_ONCE = false;

    public final static int POP_SIZE = 10;
    public final static double START_WITH_GIVEN_ORDER = 0.1;
    public final static double KILL = 0.4;
    public final static int MUTATION_RATE = 1;

    public static boolean cont = true;
    public static int generationCount = 0;

    public static Individual[] population;

    public static ArrayList<Integer> allVertices;

    public static void run(int[] startingOrder){
        timeGA = System.currentTimeMillis();
        initialize();

        generateInitialPopulation(startingOrder);
        if(POPULATION_DEBUG) printPopulation();

        calculateFitnessAll();
        if(FITNESS_DEBUG) printFitness();

        HeapSort.sort(population);

        while(cont){
            generationCount++;
            kill();

            for(int i = 0; i < POP_SIZE; i++){
                if(population[i] == null){
                    int parent1 = selectParent();
                    int parent2 = selectParent();

                    boolean tryAgain = true;
                    while(tryAgain){
                        if(Arrays.equals(population[parent1].ordering,population[parent2].ordering)){
                            parent2 = selectParent();
                        } else tryAgain = false;

                    }

                    population[i] = crossOver(parent1, parent2);
                    if(CROSSOVER_DEBUG) System.out.print(" to create individual " + i);
                    mutate(population[i]);
                }
            }
            if(CROSSOVER_DEBUG) {
                System.out.print("\n");
                printPopulation();
            }
            if(RUN_JUST_ONCE) cont = false;

            calculateFitnessAll();

            if(generationCount%10000 == 0 && VERBOSE) System.out.println("  Generation " + generationCount);

        }
    }

    public static void initialize(){
        allVertices = new ArrayList<Integer>(graph.verticesNumber);
        for(int i = 1; i <= graph.verticesNumber; i++){
            allVertices.add(i);
        }
        population = new Individual[POP_SIZE];

        for(int i = 0; i<POP_SIZE; i++){
            population[i]= new Individual();
        }
    }

    public static void generateInitialPopulation(int[] startingOrder){
    //public static void generateInitialPopulation() {
        for (int i = 0; i < POP_SIZE; i++) {
            if(i < POP_SIZE*START_WITH_GIVEN_ORDER){
                population[i].ordering = startingOrder;
            }
            else{
                Collections.shuffle(allVertices);

                Integer[] tempArray = allVertices.toArray(new Integer[0]);

                for (int j = 0; j < graph.verticesNumber; j++) {
                    population[i].ordering[j] = tempArray[j];
                }
            }
        }
    }

    public static void printPopulation(){
        System.out.println("\nPopulation: ");

        for(int i = 0; i < POP_SIZE; i++){
            System.out.print("(" + i + ")");
            if(population[i] != null){
                System.out.print(" ");
                for(int j = 0; j < graph.verticesNumber; j++){
                    System.out.print(population[i].ordering[j] + " ");
                }
                System.out.print("\n");
            }
            else System.out.println(" Dead");
        }
    }

    public static void calculateFitnessAll(){
        for(int i = 0; i < POP_SIZE; i++){
            calculateFitness(i);
        }
    }

    public static void calculateFitness(int individual){
        int largestCol = 0;

        for(int i=1; i<graph.emptyNodes.length;i++){
            node[graph.emptyNodes[i]].currentColor=1;
            largestCol = 1;
        }

        for(int i = 0; i < graph.verticesNumber; i++){
            int workingNode = population[individual].ordering[i];
            int color = DSat.greedy(workingNode);
            if(color > largestCol){
                largestCol = color;
            }
        }
        if(largestCol < graph.upperBound){
            Main.setUpperBound(largestCol);
            if(VERBOSE) System.out.println(" Upper bound set by GA in " + (System.currentTimeMillis() - timeGA) + "ms");
            if(graph.chromaticNumber != -1) cont = false;
        }
        clearColoring();
        population[individual].fitness = -(largestCol);
    }

    public static void printFitness(){
        System.out.println("\nFitness: ");

        for(int i = 0; i < POP_SIZE; i++){
            if(population[i] != null) System.out.print(population[i].fitness + " ");
            else System.out.print(" Dead ");
        }
    }

    public static void kill(){
        for(int i = 1; i <= POP_SIZE*KILL; i++){
            population[POP_SIZE - i] = null;
        }
    }

    public static int selectParent(){
        double chance = Math.random();
        int p1 = (int) (chance*(POP_SIZE*KILL));

        chance = Math.random();
        int p2 = (int) (chance*(POP_SIZE*KILL));

        if(population[p1].getFitness() > population[p2].getFitness()){
            return p1;
        }
        else{
            return p2;
        }
    }

    public static Individual crossOver(int p1, int p2){
        double chance = Math.random();
        int borderGene = (int) (chance*graph.verticesNumber);

        chance = Math.random();
        boolean before;

        if(chance > 0.5) before = true;
        else before = false;

        Individual child = new Individual();

        boolean[] assigned = new boolean[graph.verticesNumber+1];

        if(before){ //yes, I could have simply flipped the arrays, applied the method, and then re-flipped it - but I only thought of it too late
            for (int i = 0; i < graph.verticesNumber; i++) {
                if(i <= borderGene && !assigned[population[p1].ordering[i]]){
                    child.ordering[i]=population[p1].ordering[i];
                    assigned[child.ordering[i]]=true;
                } else {
                    boolean cont = true;
                    for (int j = 0; j < graph.verticesNumber && cont; j++) {
                        for (int k = 1; k < graph.verticesNumber-borderGene && cont; k++) {
                            if(population[p2].ordering[j]==population[p1].ordering[k+borderGene] && !assigned[population[p2].ordering[j]]){
                                child.ordering[i] = population[p2].ordering[j];
                                assigned[child.ordering[i]] = true;
                                cont = false;
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = (graph.verticesNumber - 1); i>=0; i--){
                if(i >= borderGene && !assigned[population[p1].ordering[i]]){
                    child.ordering[i]=population[p1].ordering[i];
                    assigned[child.ordering[i]]=true;
                } else {
                    boolean cont = true;
                    for (int j = 0; j < graph.verticesNumber && cont; j++) {
                        for (int k = 0; k < graph.verticesNumber && cont; k++) {
                            if(population[p2].ordering[j]==population[p1].ordering[k] && !assigned[population[p2].ordering[j]]){
                                child.ordering[i] = population[p2].ordering[j];
                                assigned[child.ordering[i]] = true;
                                cont = false;
                            }
                        }
                    }
                }
            }
        }

        if(CROSSOVER_DEBUG) {
            System.out.print("\nCombining parents " + p1 + " and " + p2 + " with border gene " + borderGene + " and before is " + before);
            //if(before) System.out.print(" before");
            //else System.out.print(" after");
        }

        return child;
    }

    public static Individual mutate(Individual ind){
        double chance = Math.random();
        if(chance < MUTATION_RATE){
            chance = Math.random();
            int pos1 = (int) (chance*graph.verticesNumber);

            chance=Math.random();
            int pos2 = (int) (chance*graph.verticesNumber);

            do{
                if(pos1 == pos2){
                    chance = Math.random();
                    pos2 = (int) (chance*graph.verticesNumber);
                }
            } while (pos1 == pos2);

            int temp;

            temp = ind.ordering[pos1];
            ind.ordering[pos1] = ind.ordering[pos2];
            ind.ordering[pos2] = temp;
        }

        return ind;
    }
}
