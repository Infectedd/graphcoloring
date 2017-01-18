package graphcol;

import static graphcol.Main.*;


public class DSat {

    public static int largestColor;
    public static int[] order;

    public static int[] run(){
        largestColor = 0;

        order = new int[graph.verticesNumber+1];

        int orderIndex = 1;

        for(int i=0; i<graph.emptyNodes.length;i++){
            node[graph.emptyNodes[i]].currentColor=1;
            order[orderIndex]=graph.emptyNodes[i];
            orderIndex++;
            //System.out.println("Node "+ graph.emptyNodes[i] + " is empty and therefore received a color 1.");
            largestColor = 1;
        }

        for(int i=1; i<=graph.verticesNumber-graph.emptyNodes.length; i++){

            int currentMaxDSat = 0;
            int[] nodesWithMaxDSat = new int[graph.verticesNumber+1];
            int dSatIndex=1;

            int currentMaxD = 0;
            int[] nodesWithMaxD = new int[graph.verticesNumber+1];
            int dIndex=1;

            for (int j=1; j<=graph.verticesNumber; j++){ //finds the largest DSat
                if(node[j].currentColor==0){
                    int dSatVal = node[j].saturationValue;

                    if(dSatVal==currentMaxDSat){
                        nodesWithMaxDSat[dSatIndex]=j;
                        dSatIndex++;
                    }
                    else if(dSatVal>currentMaxDSat) {
                        currentMaxDSat=dSatVal;
                        nodesWithMaxDSat = new int[graph.verticesNumber+1];
                        dSatIndex=1;
                        nodesWithMaxDSat[dSatIndex]=j;
                        dSatIndex++;
                    }
                }
            }

            for (int j=1; j<dSatIndex; j++){
                if(nodesWithMaxDSat[j]!=0){
                    int dVal = node[nodesWithMaxDSat[j]].edgeCount;

                    if(dVal == currentMaxD){
                        nodesWithMaxD[dIndex]=nodesWithMaxDSat[j];
                        dIndex++;
                    }
                    else if(dVal>currentMaxD){
                        currentMaxD=dVal;
                        nodesWithMaxD = new int[graph.verticesNumber+1];
                        dIndex=1;
                        nodesWithMaxD[dIndex]=nodesWithMaxDSat[j];
                        dIndex++;
                    }
                }
            }

            int workingNode = nodesWithMaxD[1];

            order[orderIndex] = workingNode;
            orderIndex++;

            int color = greedy(workingNode);

            if(color > largestColor) largestColor = color;
        }
        order[0] = largestColor;
        Main.clearColoring();
        return order;
    }

    public static int greedy(int workingNode){

        for(int j=1; j<=graph.initialUpperBound; j++){
            if(!node[workingNode].connectedColors[j]){
                node[workingNode].currentColor=j;
                break;
            }
        }

        for(int j = 0; j < node[workingNode].connectedNodes.length; j++){
            int connectedNode = node[workingNode].connectedNodes[j];
            if(!node[connectedNode].connectedColors[node[workingNode].currentColor]){
                node[connectedNode].saturationValue++;
                node[connectedNode].connectedColors[node[workingNode].currentColor] = true;
            }
        }

        return node[workingNode].currentColor;
    }
}