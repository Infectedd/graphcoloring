package graphcol;

import java.io.*;

class ColEdge
{
    int u;
    int v;
}

class Node {
    int bestColor;
    int currentColor;
    int saturationValue;
    int connectedNodes[]; // this array will contain the numbers of every connected node
    int edgeCount = 0; // by default a node has 0 edges
    boolean connectedColors[];
}

class Graph {
    int initialUpperBound;
    int upperBound;
    int lowerBound = 0; // when there are no nodes, the lowerbound is 0
    int chromaticNumber = -1; // -1 means no chromaticNumber yet
    int verticesNumber = 0; // assuming the graph is empty
    int edgesNumber = 0; // assuming the graph is empty
    int emptyNodesCount = 0; // assuming there are no empty nodes
    int emptyNodes[] = null; // assuming there are no empty nodes
    int maxCliqueSize = -1; // no clique size found at the initialization
    int[] maxClique = {};
}

class Cycle {
    int[] vertices;
    int size;
    boolean centralVertex;
}

public class Main {

    public final static boolean DEBUG = false;

    public final static String COMMENT = "//";

    //! e will contain the edges of the graph
    public static ColEdge e[];

    //! node will contain the vertices of the graph
    public static Node node[];

    //! create object graph
    public static Graph graph;

    public static long timeGlobal;
    public static long timeDSat;
    public static long timeBK;
    public static long timeGA;

    public final static boolean VERBOSE = false;

    public static void main(String[] args) {

        timeGlobal = System.currentTimeMillis();

        //BEGIN PREPARING TO READ FILE
        if( args.length < 1 ) {
            System.out.println("Error! No filename specified.");
            System.exit(0);
        }

        graph = new Graph();
        boolean seen[] = null;
        //! n is the number of vertices in the graph
        int n = -1;
        //! m is the number of edges in the graph
        int m = -1;

        String file = args[0];

        //END PREPARING TO READ FILE

        //BEGIN READING FILE

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String record = new String();

            //! The first few lines of the file are allowed to be comments, staring with a // symbol.
            //! These comments are only allowed at the top of the file.

            //! -----------------------------------------
            while ((record = br.readLine()) != null)
            {
                if( record.startsWith("//") ){
                    System.out.println(record);
                    continue;
                }
                break; // Saw a line that did not start with a comment -- time to start reading the data in!
            }

            if( record.startsWith("VERTICES = ") )
            {
                n = Integer.parseInt( record.substring(11) );
                if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
            }

            seen = new boolean[n+1];

            node = new Node[n+1];
            for (int x=1; x<=n; x++) { // create every node object
                node[x] = new Node();
                node[x].connectedNodes = new int[n+1];
            }

            record = br.readLine();

            if( record.startsWith("EDGES = ") )
            {
                m = Integer.parseInt( record.substring(8) );
                if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
            }

            e = new ColEdge[m];

            graph.edgesNumber = m; // set the edges in the graph object
            graph.verticesNumber = n; // set the vertices in the graph object

            for( int d=0; d<m; d++)
            {
                if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
                record = br.readLine();
                String data[] = record.split(" ");
                if( data.length != 2 )
                {
                    System.out.println("Error! Malformed edge line: "+record);
                    System.exit(0);
                }
                e[d] = new ColEdge();

                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                node[e[d].u].edgeCount++;
                node[e[d].v].edgeCount++;

                seen[ e[d].u ] = true;
                seen[ e[d].v ] = true;

                if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);

            }

            String surplus = br.readLine();
            if( surplus != null )
            {
                if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
            }

        }
        catch (IOException ex)
        {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " + file);
            System.exit(0);
        }

        //END READING FILE

        //BEGIN INITIALIZING EMPTY NODES

        int[] emptyNodesTemp = new int[n+1];

        for( int x=1; x<=n; x++ )
        {
            if( seen[x] == false )
            {
                //if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
                emptyNodesTemp[graph.emptyNodesCount] = x;
                graph.emptyNodesCount++;
            }
        }

        graph.emptyNodes = new int[graph.emptyNodesCount]; // set the correct size of the emptyNodes array

        for ( int x=0; x < graph.emptyNodesCount ; x++) { // TBD: this for statement can be implemented into the above if statement as an if statement
            graph.emptyNodes[x] = emptyNodesTemp[x]; // emptyNodes is the exact array with empty nodes numbers
            if(DEBUG) System.out.println(COMMENT + " This node is empty: "+graph.emptyNodes[x]);
        }

        if(DEBUG) System.out.println(COMMENT + " Total number of empty nodes: "+graph.emptyNodesCount);

        //END INITIALIZING EMPTY NODES

        //BEGIN CHECKING EDGE COUNT

        int totalEdgesCheck = 0;
        for ( int x=1; x <= n ; x++) {
            totalEdgesCheck = totalEdgesCheck + node[x].edgeCount;
            if(DEBUG) System.out.println(COMMENT + " The node "+x+" has "+node[x].edgeCount+" edges");
        }

        if ( totalEdgesCheck/2 == graph.edgesNumber) {
            if(DEBUG) System.out.println(COMMENT + " The total number of edges is correctly counted: "+totalEdgesCheck/2);
        }
        else { if(DEBUG) System.out.println(COMMENT + " WARNING! Something went wrong, the total amount of edges should be: "+graph.edgesNumber+" yet they are: "+totalEdgesCheck/2); }

        //END CHECKING EDGE COUNT

        //BEGIN FILLING CONNECTEDNODE ARRAYS

        for ( int x = 0 ; x < graph.edgesNumber ; x++ ) {
            node[e[x].u].connectedNodes[e[x].v] = e[x].v;
            node[e[x].v].connectedNodes[e[x].u] = e[x].u;
        }

        for ( int x = 1 ; x <= graph.verticesNumber ; x++) {
            int countedElements = 0;
            int tempArray[] = new int[graph.verticesNumber];
            for ( int y = 0 ; y <= graph.verticesNumber ; y++) {
                if ( node[x].connectedNodes[y] > 0 ) {
                    tempArray[countedElements] = node[x].connectedNodes[y];
                    countedElements++;
                }
            }
            node[x].connectedNodes = new int[countedElements];
            node[x].edgeCount = countedElements;
            for ( int z = 0 ; z < countedElements ; z++ ) {
                node[x].connectedNodes[z] = tempArray[z];
            }
        }

        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            if (node[x].connectedNodes.length != node[x].edgeCount) System.out.println("WARNING: Something went wrong with the connectedNodes array of node: "+x);
        }

        //END FILLING CONNECTEDNODE ARRAYS

        //BEGIN SETTING INITIAL BOUNDS

        if ( graph.edgesNumber > 0 && graph.verticesNumber > 1 ) setLowerBound(2); // if there's at least 1 edge and at least 2 nodes, the lowerbound is at least 2
        if ( graph.edgesNumber == 0 && graph.verticesNumber > 0 ) setLowerBound(1); // if there's no edges and at least 1 node, the lowerbound is 1
        int highestDegree = 0;
        for ( int x=1 ; x <= graph.verticesNumber ; x++) {
            if ( node[x].edgeCount > highestDegree ) highestDegree = node[x].edgeCount ;
        }

        graph.initialUpperBound = highestDegree + 1;
        setUpperBound(graph.initialUpperBound);

        if(DEBUG) System.out.println(COMMENT + " The lowerbound is currently "+graph.lowerBound);
        if(DEBUG) System.out.println(COMMENT + " The upperbound is currently "+graph.upperBound);

        //END SETTING INITIAL BOUNDS

        //BEGIN INITIALIZING CONNECTEDCOLORS

        for(int i=1; i<=graph.verticesNumber; i++){ //initializes the connectedColors array for each vertex
            node[i].connectedColors = new boolean[graph.upperBound + 1];
            for(int j=1; j<graph.upperBound; j++){
                node[i].connectedColors[j] = false;
            }
        }

        //END INITIALIZING CONNECTEDCOLORS

        //BEGIN PRINTING ALL EMPTY NODES
        if (DEBUG) {
            System.out.println(COMMENT + " The following nodes are empty: ");
            for ( int x = 0 ; x < graph.emptyNodes.length ; x++ ) {
                System.out.printf(graph.emptyNodes[x]+", ");
            }
            System.out.println(".");
        }

        //END PRINTING ALL EMPTY NODES

        if(VERBOSE) System.out.println(" Initialization time: " + (System.currentTimeMillis() - timeGlobal) + "ms");

        Thread ubThread = new Thread(new UpperBoundThread());
        ubThread.start();

        Thread lbThread = new Thread(new LowerBoundThread());
        lbThread.start();

        /*timeDSat = System.currentTimeMillis();

        int[] dSatResult = DSat.run();

        System.out.println(" DSat time: " + (System.currentTimeMillis() - timeDSat) + "ms");

        clearColoring();
        int dSatUpperBound = dSatResult[0];
        setUpperBound(dSatUpperBound);

        int[] dSatOrder = new int[graph.verticesNumber];

        for(int i = 0; i < graph.verticesNumber; i++){
            dSatOrder[i] = dSatResult[i+1];
        }

        clearColoring();

        graph.maxCliqueSize = BronKerbosch.maxClique();

        //System.out.println("Start time: " + timeGlobal);

        //System.out.println(" B-K time: " + (System.currentTimeMillis() - timeGlobal) + "ms");

        clearColoring();

        TheGreedyGene.run(dSatOrder);*/
    }

    public static int getVtcs() {
        return graph.verticesNumber;
    }

    public static int getEdges() {
        return graph.edgesNumber;
    }

    //returns true when the two input nodes are connected, returns false if they aren't.
    public static boolean areConnected(int node1, int node2) {
        for (int i = 0; i < node[node1].connectedNodes.length; i++) {
            if (node[node1].connectedNodes[i] == node2)
                return true;
        }
        return false;
    }

    public static void setUpperBound(int bound){
        graph.upperBound = bound;
        System.out.println("NEW BEST UPPER BOUND = " + bound);
        checkBounds();
    }

    public static void setLowerBound(int bound){
        graph.lowerBound = bound;
        System.out.println("NEW BEST LOWER BOUND = " + bound);
        checkBounds();
    }

    public static void setChromaticNumber(int number){
        graph.chromaticNumber = number;
        System.out.println("CHROMATIC NUMBER = " + graph.chromaticNumber);
        System.out.println(" Total time: " + (System.currentTimeMillis() - timeGlobal) + "ms");
        System.exit(0);
    }

    public static void checkBounds(){
        if(graph.lowerBound == graph.upperBound){
            setChromaticNumber(graph.upperBound);
        }
    }

    public static void clearColoring(){
        for(int i = 1; i <= graph.verticesNumber; i++ ) {
            node[i].currentColor = 0;
            node[i].connectedColors = new boolean[node[i].connectedColors.length];
        }
    }

}