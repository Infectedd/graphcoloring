package graphcol;

import static graphcol.Main.*;

import java.util.Comparator;

/**
 * Created by Roel on 23-1-2017.
 */
public class DegreeSort implements Comparator<Integer> {
    //private final int[] array;

    /*public DegreeSort(int[] array) {
        this.array = array;
    }*/

    public Integer[] createIndexArray() {
        Integer[] indexes = new Integer[graph.verticesNumber+1];
        for (int i = 1 ; i <= graph.verticesNumber ; i++) {
            indexes[i] = i;
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
        // Autounbox from Integer to int to use as array indexes
        Integer node1O = new Integer(node[index1].edgeCount);
        Integer node2O = new Integer(node[index2].edgeCount);
        return node2O.compareTo(node1O);
    }
}
