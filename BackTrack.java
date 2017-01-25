package graphcol;

import static graphcol.Main.*;

public class BackTrack {
    private static double checkAmount = 0; // counter for brute-force combination checks

    private static int maxNumber = 0; // the amount of colorings used, where 0 is the first color

    private static int currentBT = 2; // start the backtracking check at vertex 2

    private static boolean lastBT = false;

    public static void setMaxNumber(int numberOfColoringsWantToHaveChecked) {
        maxNumber = numberOfColoringsWantToHaveChecked - 1;
    }

    public static int run(int numberOfColoringsWantToHaveChecked) {
        setMaxNumber(numberOfColoringsWantToHaveChecked);

        boolean loop = false;

        while (!loop) {
            if (countingBT()) {
                loop = true;
                if (check()) {
                    return 1; // valid coloring found (new upper bound)
                } else if (!check()) {
                    return -1; // no valid coloring found (new lower lower bound)
                }
            }
        }
        return 0; // something went wrong, the backtracking is not finished
    }

    public static boolean check() { // will check the complete coloring of a graph for correctness. Can also be put into a loop for brute-forcing
        boolean DEBUG = false;
        if (DEBUG) {
            System.out.print("Checking the following colors: ");
            for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
                System.out.print(node[x].currentColorBT+" ");
            }
        }

        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            for ( int y = 0 ; y < node[x].edgeCount ; y++ ) {
                checkAmount++;
                if((checkAmount % 100000000) == 0) System.out.println(checkAmount); // will print the amount of combinations checked at each 100 million checks
                if (node[x].currentColorBT == node[node[x].connectedNodes[y]].currentColorBT) {
                    if (DEBUG) System.out.println(" Nope, that was not working.");
                    if (DEBUG) System.out.println(node[x].currentColorBT +"-" +node[node[x].connectedNodes[y]].currentColorBT);
                    if (DEBUG) System.out.println(x + "=" + node[x].connectedNodes[y]);
                    return false;
                }
            }
        }
        if (DEBUG) System.out.println(" This coloring was working!");
        return true;
    }

    public static boolean countingBT () { // backtrack loop method
        if (lastBT) { // last track was true, so we continue to the next node
            if (currentBT != graph.verticesNumber) { // is the node not the last node?
                currentBT++; // then we continue to the next node
                track(currentBT);
            } else {
                return true; // end the while loop
            }
        }
        else {
            if (node[currentBT].currentColorBT != maxNumber) {
                node[currentBT].currentColorBT++;
                track(currentBT);
            } else {
                node[currentBT].currentColorBT = 0;
                currentBT--;
                if (currentBT == 1) { // if the first node is going to be changed, there is no point in running any longer
                    return true; // end the while loop
                }
            }
        }
        return false; // continue the while loop
    }

    public static void track(int nodeN) { // returns true if the coloring can become valid
        for (int x = 0 ; x < node[nodeN].edgeCount ; x++) {
            if (node[nodeN].connectedNodes[x] < nodeN) { // are the vertices connected?
                if (checkIfConnected(nodeN,node[nodeN].connectedNodes[x])) { // if so, do they have the same color?
                    lastBT = false;
                    return;
                }
            }
        }
        lastBT = true;
    }

    public static boolean checkIfConnected(int node1, int node2) { // checks if two vertices have the same color
        if (node[node1].currentColorBT == node[node2].currentColorBT) {
            return true;
        }
        else return false;
    }

    public static void printResult() { // this method can print the current progress with a timer for example
        System.out.print("Checking the following colors: ");
        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            System.out.print(node[x].currentColorBT + " ");
        }
            System.out.println();
    }

}
