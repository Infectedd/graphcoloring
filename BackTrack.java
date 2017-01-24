package graphcol;

import static graphcol.Main.*;
import java.util.Arrays;

public class BackTrack {
    private static double checkAmount = 0;

    private static int maxNumber = 0;
    private static int maxNumberrev = 0;

    //private static int currentNode = 1;

    private static int currentBT = 2;
    private static int currentBTrev = 2;

    private static boolean lastBT = false;
    private static boolean lastBTrev = false;

    private static int[] degreeSort;

    public static void setMaxNumber(int numberOfColoringsWantToHaveChecked) {
        maxNumber = numberOfColoringsWantToHaveChecked - 1;
        maxNumberrev = numberOfColoringsWantToHaveChecked - 1;
    }

    public static int run(int numberOfColoringsWantToHaveChecked/*, boolean rev*/) {
        setMaxNumber(numberOfColoringsWantToHaveChecked);
        //double power = Math.pow(numberOfColoringsWantToHaveChecked,graph.verticesNumber);
        //double checksPerHour = 100E09;
        //System.out.println("Checking the number of possibilities "+numberOfColoringsWantToHaveChecked+" to the power of "+graph.verticesNumber+": "+power);
        //System.out.println("The estimated number of hours to calculate this is: "+power/checksPerHour);

        boolean loop = false;

        /*if (rev) {
            testSorting();
            while (!loop) {

            /*boolean DEBUG = true;
            if (DEBUG) {
                System.out.print("Checking the following colors: ");
                for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
                    System.out.print(node[x].currentColorBT+" ");
                }
            }
            System.out.println();*//*
                if (countingBTrev()) {
                    loop = true;
                    if (checkrev()) {
                        return 1;
                    } else if (!checkrev()) {
                        return -1;
                    }
                    //System.out.println(check());
                }
            }
        }
        else if (!rev) {*/
            while (!loop) {
                if (countingBT()) {
                    loop = true;
                    if (check()) {
                        return 1;
                    } else if (!check()) {
                        return -1;
                    }
                }
            }
       // }
        return 0;
    }

    public static boolean check() {
        boolean DEBUG = true;
        if (DEBUG) {
            //System.out.print("Checking the following colors: ");
            for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
                //System.out.print(node[x].currentColorBT+" ");
            }
        }

        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            for ( int y = 0 ; y < node[x].edgeCount ; y++ ) {
                checkAmount++;
                //if((checkAmount % 100000000) == 0) System.out.println(checkAmount);
                if (node[x].currentColorBT == node[node[x].connectedNodes[y]].currentColorBT) {
                    //if (DEBUG) System.out.println(" Nope, that was not working.");
                    //if (DEBUG) System.out.println(node[x].currentColorBT +"-" +node[node[x].connectedNodes[y]].currentColorBT);
                    //if (DEBUG) System.out.println(x + "=" + node[x].connectedNodes[y]);
                    return false;
                }
            }
        }
        //if (DEBUG) System.out.println(" This coloring was working!");
        return true;
    }

    /*public static boolean counting () { // brute force method
        if ( node[currentNode].currentColorBT == maxNumber ) {
            if (currentNode!=graph.verticesNumber) {
                node[currentNode].currentColorBT = 0;
                currentNode++;
                counting();
            }
        }
        else {
            node[currentNode].currentColorBT++;
            if ( check() == false ) { // check if this is a valid coloring
                return false;
            }
            else {
                graph.chromaticNumber = maxNumber+1;
                return true;
            }
        }
        return false;
    }*/

    public static boolean countingBT () { // backtrack loop method
        if (lastBT) { // last track was true, so we continue to the next node
            //System.out.println("TRUE lastBT");
            if (currentBT != graph.verticesNumber) { // is the node not the last node?
                currentBT++; // then we continue to the next node
                //System.out.println("currentBT:" + currentBT);
                track(currentBT);
            } else {
                //System.out.println("SOLUTION FOUND, CHECKING AGAIN:");
                //System.out.println(check());
                return true;
            }
        }
        else {
            //System.out.println("FALSE lastBT");
            if (node[currentBT].currentColorBT != maxNumber) {
                node[currentBT].currentColorBT++;
                track(currentBT);
            } else {
                node[currentBT].currentColorBT = 0;
                currentBT--;
                if (currentBT <= maxNumber+1) {
                    if (checkEnd()) {
                        return true;
                    }
                    //System.out.println("OK WE CAN STOP NOW!");
                    //return true;
                }
                //track(currentBT);
            }
        }
        return false;
    }

        /*if (!lastBT) {
            if ( node[currentBT].currentColorBT == maxNumber ) {
                node[currentBT].currentColorBT = 0;
                currentBT++;
            }
        }
        if ( node[currentBT].currentColorBT == maxNumber ) {
            if (currentBT!=graph.verticesNumber) {
                node[currentBT].currentColorBT = 0;
                currentBT++;
                counting();
            }
        }
        else {
            node[currentBT].currentColorBT++;
            if ( check() == false ) { // check if this is a valid coloring
                return false;
            }
            else {
                graph.chromaticNumber = maxNumber+1;
                return true;
            }
        }
        return false;*/

    public static void track(int nodeN) { // returns true if the coloring can become valid
        for (int x = 0 ; x < node[nodeN].edgeCount ; x++) {
            if (node[nodeN].connectedNodes[x] < nodeN) {
                if (checkIfConnected(nodeN,node[nodeN].connectedNodes[x])) {
                    lastBT = false;
                    return;
                }
            }
        }
        lastBT = true;
    }

    public static boolean checkIfConnected(int node1, int node2) {
        if (node[node1].currentColorBT == node[node2].currentColorBT) {
            return true;
        }
        else return false;
    }

    public static boolean checkEnd() {
        for (int i = 1 ; i <= maxNumber+1 ; i++) {
            if (node[i].currentColorBT > i-1) {
                return true;
            }
        }
        return false;
    }

    public static void printResult() {
        //System.out.print("Checking the following colors: ");
        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            //System.out.print(node[x].currentColorBT + " ");
        }
            //System.out.println();
        //System.out.print("Checking the following colors (REV): ");
        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            //System.out.print(node[degreeSort[x]].currentColorBTrev + " ");
        }
        //System.out.println();
    }

    public static void testSorting() {
        //int[] countries = { "France", "Spain", ... };
        DegreeSort comparator = new DegreeSort();
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, 1, graph.verticesNumber+1, comparator);
        degreeSort = new int[graph.verticesNumber+1];
        for (int i = 1; i <= graph.verticesNumber ; i++) {
            degreeSort[i] = indexes[i];
            //System.out.print(i + ":" + degreeSort[i] + "-" + node[degreeSort[i]].edgeCount + " ");
        }
    }


    public static boolean countingBTrev () { // backtrack loop method
        if (lastBTrev) { // last track was true, so we continue to the next node
            //System.out.println("TRUE lastBT");
            if (currentBTrev != graph.verticesNumber) { // is the node not the last node?
                currentBTrev++; // then we continue to the next node
                //System.out.println("currentBT:" + currentBTrev);
                trackrev(currentBTrev);
            } else {
                //System.out.println("SOLUTION FOUND, CHECKING AGAIN:");
                //System.out.println(check());
                return true;
            }
        }
        //if (!lastBT || (lastBT && currentBT == graph.verticesNumber)) { // if last track was false or last track was the last node, we raise the current track
        else {
            //System.out.println("FALSE lastBT");
            //System.out.println("currentBT:" + currentBTrev);
            if (node[degreeSort[currentBTrev]].currentColorBTrev != maxNumberrev) {
                node[degreeSort[currentBTrev]].currentColorBTrev++;
                trackrev(currentBTrev);
            } else {
                node[degreeSort[currentBTrev]].currentColorBTrev = 0;
                currentBTrev--;
                //System.out.println("currentBT:" + currentBTrev);
                int temp = maxNumberrev+1;
                //System.out.println("maxNumberrev+1:" + temp);
                //if (currentBTrev <= maxNumberrev+1) {
                if (currentBTrev == 1){
                    //if (checkEndrev()) {
                        return true;
                    //}
                    //checkrev();
                    //System.out.println("OK WE CAN STOP NOW!");
                    //return true;
                }
                //track(currentBT);
            }
        }
        return false;
    }

    public static void trackrev(int nodeN) { // returns true if the coloring can become valid
        for (int x = 0 ; x < node[degreeSort[nodeN]].edgeCount ; x++) {
            if (node[node[degreeSort[nodeN]].connectedNodes[x]].edgeCount >= node[degreeSort[nodeN]].edgeCount) {
                if (checkIfConnectedrev(degreeSort[nodeN],node[degreeSort[nodeN]].connectedNodes[x])) {
                    lastBTrev = false;
                    return;
                }
            }
        }
        lastBTrev = true;
    }

    public static boolean checkIfConnectedrev(int node1, int node2) {
        if (node[node1].currentColorBTrev == node[node2].currentColorBTrev) {
            return true;
        }
        else return false;
    }

    public static boolean checkrev() {
        /*boolean DEBUG = true;
        if (DEBUG) {
            System.out.print("Checking the following colors: ");
            for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
                System.out.print(node[x].currentColorBTrev+" ");
            }
        }*/

        for ( int x = 1 ; x <= graph.verticesNumber ; x++ ) {
            for ( int y = 0 ; y < node[x].edgeCount ; y++ ) {
                checkAmount++;
                //if((checkAmount % 100000000) == 0) System.out.println(checkAmount);
                if (node[x].currentColorBTrev == node[node[x].connectedNodes[y]].currentColorBTrev) {
                    //if (DEBUG) System.out.println(" Nope, that was not working.");
                    //if (DEBUG) System.out.println(node[x].currentColorBTrev +"-" +node[node[x].connectedNodes[y]].currentColorBTrev);
                    //if (DEBUG) System.out.println(x + "=" + node[x].connectedNodes[y]);
                    return false;
                }
            }
        }
        //if (DEBUG) System.out.println(" This coloring was working!");
        return true;
    }

    public static boolean checkEndrev() {
        for (int i = 1 ; i <= maxNumberrev+1 ; i++) {
            if (node[degreeSort[i]].currentColorBTrev > i-1) {
                return true;
            }
        }
        return false;
    }

}
