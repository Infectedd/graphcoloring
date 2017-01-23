package graphcol;

import static graphcol.Main.*;

/**
 * Created by bvsla on 1/19/2017.
 */
public class UpperBoundThread extends Thread{
    public void run(){

        timeDSat = System.currentTimeMillis();

        int[] dSatResult = DSat.run();

        if(VERBOSE) System.out.println(" DSat time: " + (System.currentTimeMillis() - timeDSat) + "ms");

        clearColoring();
        int dSatUpperBound = dSatResult[0];
        setUpperBound(dSatUpperBound);

        int[] dSatOrder = new int[graph.verticesNumber];

        if(dSatUpperBound > 2){
            Main.setLowerBound(3);
        }

        for(int i = 0; i < graph.verticesNumber; i++){
            dSatOrder[i] = dSatResult[i+1];
        }

        clearColoring();

        TheGreedyGene.run(dSatOrder);
    }
}
