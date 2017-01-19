package graphcol;

import static graphcol.CycleFinder.findCycles;
import static graphcol.Main.graph;
import static graphcol.BronKerbosch.*;

public class LowerBoundThread extends Thread{
    public void run(){
        findCycles();
        graph.maxCliqueSize = BronKerbosch.maxClique();
    }
}
