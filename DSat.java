package graphcol;

import static graphcol.Main.*;


public class DSat {

//problem - method has to return BOTH largestColor AND order - maybe put them in one array, with [0] holding largestColor?
    public static int largestColor;
    public static int[] order;

    public static int[] run(){
        largestColor = 0;

        order = new int[graph.verticesNumber+1];

        for(int i=1; i<graph.emptyNodes.length;i++){
            node[graph.emptyNodes[i]].currentColor=1;
            //System.out.println("Node "+ graph.emptyNodes[i] + " is empty and therefore received a color 1.");
            largestColor = 1;
        }

        for(int i=1; i<=graph.verticesNumber; i++){ //initializes the connectedColors array for each vertex
            node[i].connectedColors = new boolean[graph.upperBound];
            for(int j=1; j<graph.upperBound; j++){
                node[i].connectedColors[j] = false;
            }
        }

        for(int i=1; i<=graph.verticesNumber-graph.emptyNodes.length; i++){
            //System.out.println("Run " + i);
            int currentMaxDSat = 0;
            int currentMaxD = 0;
            int nodesWithMaxDSat[] = new int[graph.verticesNumber+1];
            int nodesWithMaxD[] = new int[graph.verticesNumber+1];
            int k=1;
            int l=1;
            boolean unavailableColors[] = new boolean[graph.upperBound];

            for (int j=1; j<=graph.verticesNumber; j++){ //finds the largest DSat
                if(node[j].currentColor==0){
                    int satval = node[j].saturationValue;
                    if(satval>currentMaxDSat) {
                        currentMaxDSat=satval;
                        //System.out.println(" Found a new maximum DSat: " + satval);
                    }
                }
            }

            for (int j=1; j<=graph.verticesNumber && k<=graph.verticesNumber; j++){ //adds all vertices with the largest DSat to an array
                if(node[j].currentColor==0){
                    int satval = node[j].saturationValue;
                    if(satval==currentMaxDSat){
                        nodesWithMaxDSat[k]=j;
                        k++;
                        //System.out.println("Added a new node to the list of maximum DSat nodes: " + j);
                    }
                }
            }

            for (int j=1; j<=graph.verticesNumber; j++){
                if(nodesWithMaxDSat[j]!=0){
                    int dval = node[nodesWithMaxDSat[j]].edgeCount;
                    if(dval>currentMaxD) currentMaxD=dval;
                }

            }

            for (int j=1; j<=graph.verticesNumber && l<=graph.verticesNumber; j++){
                if(nodesWithMaxDSat[j]!=0){
                    int dval = node[nodesWithMaxDSat[j]].edgeCount;
                    if(dval==currentMaxD){
                        nodesWithMaxD[l]=nodesWithMaxDSat[j];
                        //System.out.println(" Added a new node to the list of maximum D nodes: " + nodesWithMaxD[l]);
                        l++;
                    }
                }
            }

            int workingNode = nodesWithMaxD[1];

            //System.out.println(" Working node is " + workingNode);

            order[i] = workingNode;

            int color = greedy(workingNode);

            if(color > largestColor) largestColor = color;

            for(int jj=0; jj<node[workingNode].connectedNodes.length; jj++){
                int currentNeighborNode = node[workingNode].connectedNodes[jj];
                node[currentNeighborNode].connectedColors[color]=true;
                //System.out.println(" Node " + currentNeighborNode + " is now connected to color " + j);
                int counter=0;
                for(int jjj=1; jjj<graph.upperBound; jjj++){
                    if(node[currentNeighborNode].connectedColors[jjj]) counter++;
                }
                node[currentNeighborNode].saturationValue=counter;
            }
        }

        //System.out.println("The upper bound is " + largestColor + " as per the DSat version of the Greedy algorithm.");
        order[0] = largestColor;
        return order;
    }

    public static int greedy(int workingNode){
        for(int j=1; j<graph.upperBound && node[workingNode].currentColor==0; j++){
            if(!node[workingNode].connectedColors[j]){
                node[workingNode].currentColor=j;
                if (DEBUG) System.out.println(" Node " + workingNode +  " with degree " + node[workingNode].edgeCount + " and degree of saturation " + node[workingNode].saturationValue + " just received color " + j);
                return j;
            }
        }
        return 0;
    }
}

