package graphcol;

import java.util.Arrays;

import static graphcol.Main.VERBOSE;
import static graphcol.Main.graph;
import static graphcol.Main.node;

/**
 * Created by bvsla on 1/19/2017.
 */


public class CycleFinder {

    static void findCycles(){
        int[] vertices = new int[graph.verticesNumber];
        int index = 0;

        for (int i = 1; i <= graph.verticesNumber ; i++) {
            vertices[0]=i;
            findCycles(vertices, index);
        }
    }

    static void findCycles(int[] vertices, int index){
        if(graph.lowerBound < 3){
            int workingNode = vertices[index];

            for (int i = 0; i < node[workingNode].connectedNodes.length; i++) {
                boolean add = true;

                if(index != 0 && node[workingNode].connectedNodes[i]==vertices[index-1]) add = false;

                if(node[workingNode].connectedNodes[i]==vertices[index]) add = false;

                for (int j = 0; j < index-1; j++) {
                    if(vertices[j] == node[workingNode].connectedNodes[i]){
                        add = false;

                        if((index-j)%2 == 0){
                            if(Main.graph.lowerBound < 3){
                                Main.setLowerBound(3);
                                if(VERBOSE) System.out.println(" Odd cycle found!");
                            }
                        }
                    }
                }

                if(add){
                    vertices[index+1] = node[workingNode].connectedNodes[i];
                    findCycles(vertices, index+1);
                }
            }
        }
    }
}
