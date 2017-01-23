package graphcol;

import static graphcol.Main.*;

public class BronKerbosch {

    public static boolean bkPivotRun;
    //some array initialisation for the B-K algorithm
    static int timeIndex = 0;
    public static int maxClique() {
        int[] np = new int[graph.verticesNumber]; //n for "new"
        for (int i = 0; i < graph.verticesNumber; i++) {
            np[i] = i + 1;
        }
        int[] nr = new int[0];

        int[] pivots = new int[np.length];
        System.arraycopy(np, 0, pivots, 0, np.length);
        sort(pivots);

        //System.out.print("pivots = [ ");
        for (int j = 0; j < pivots.length; j++) {
            //System.out.print(pivots[j] + " ");
        }
        //System.out.print("]\n");

        int cliq = 0;

        for (int i = 0; i < pivots.length && node[pivots[i]].edgeCount > 1; i++) {
            bkPivotRun = false;
            //System.out.println("new run with pivot = " + pivots[i]);
            int ncliq = bronKerbosch(np, nr, pivots[i]).length;
            if (ncliq > cliq)
                cliq = ncliq;
        }
        return cliq;
    }

    // method containing a variation the B-K algorithm for computing the maximum clique, from which the size is equal to the lowerbound of the graph.
    public static int[] bronKerbosch(int[] p, int[] r, int pivot) {

        int pivotVtx; // pivot vertex with the highest degree

        //System.out.print("p = [ ");
        for (int j = 0; j < p.length; j++) {
            //System.out.print(p[j] + " ");
        }
        //System.out.print("]\n");

        //System.out.print("r = [ ");
        for (int j = 0; j < r.length; j++) {
            //System.out.print(r[j] + " ");
        }
        //System.out.print("]\n");
        //System.out.println("==================");

        // whenever both []p and []x are empty, r contains a maximal clique.
        if (p.length == 0)
            //System.out.println("New maximal clique with size " + r.length + " found!");
        if (p.length == 0 && r.length > graph.maxClique.length) {
            graph.maxClique = new int[r.length];
            System.arraycopy(r, 0, graph.maxClique, 0, graph.maxClique.length);
            //System.out.println(r.length + " is a new maximum clique! (at least for now) --------------------------------------------------------");
            Main.setLowerBound(r.length);
            return graph.maxClique;
        }


        // selecting the pivot vertex.

        if (bkPivotRun) {
            if (p.length > 0)
                pivotVtx = p[0];
            else
                return graph.maxClique;
            for (int i = 0; i < p.length; i++) {
                //System.out.println(node[pivotVtx].connectedNodes.length);
                if (node[p[i]].connectedNodes.length > node[pivotVtx].connectedNodes.length)
                    pivotVtx = p[i];
            }
        } else {
            pivotVtx = pivot;
            bkPivotRun = true;
        }
        //System.out.println("Pivot = " + pivotVtx);
        int[] pivotArr = {pivotVtx};
        //System.out.println(pivotArr[0]);

        bronKerbosch(inters(p, node[pivotVtx].connectedNodes), union(r, pivotArr), pivot);

        return graph.maxClique;
    }

    //method that returns the union (U) of two arrays
    public static int[] union(int[] a1, int[] a2) {

        int p1 = 0; //pointers for the arrays
        int p2 = 0;
        int[] a = new int[0];

        while (p1 < a1.length || p2 < a2.length) {

            if (p2 >= a2.length) {
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a1[p1];
                a = na;
                p1++;
                //System.out.println("p1 updated, it is now " + p1);
            } else if (p1 >= a1.length) {
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a2[p2];
                a = na;
                p2++;
                //System.out.println("p2 updated, it is now " + p2);
            }

            // when the entry in a1 is smaller than the one in a2
            else if (a1[p1] < a2[p2]) {
                //System.out.println("adding p1 to the sum");
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a1[p1];
                a = na;
                p1++;
                //System.out.println("p1 updated, it is now " + p1);
            }

            // when the entry in a2 is smaller than the one in a1
            else if (a2[p2] < a1[p1]) {
                //System.out.println("adding p2 to the sum");
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a2[p2];
                a = na;
                p2++;
                //System.out.println("p2 updated, it is now " + p2);
            }

            // when both entries are the same
            else if (a1[p1] == a2[p2]) {
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a1[p1];
                a = na;
                p1++;
                p2++;
                //System.out.println("both p1 and p2 updated they are now " + p1 + " and " + p2);
            }
        }

        return a;
    }

    //method that returns the intersection of two arrays
    public static int[] inters(int[] a1, int[] a2) {
        int p1 = 0; //pointers for the arrays
        int p2 = 0;
        int[] a = new int[0];

        while (p1 < a1.length && p2 < a2.length) {
            //System.out.println("While loop initiated");
            // when both entries are the same
            if (a1[p1] == a2[p2]) {
                int[] na = new int[a.length + 1];
                System.arraycopy(a, 0, na, 0, a.length);
                na[na.length - 1] = a1[p1];
                a = na;
                p1++;
                p2++;
            }

            // when the entry in a1 is smaller than the one in a2
            else if (a1[p1] < a2[p2]) {
                p1++;
            }

            // when the entry in a2 is smaller than the one in a1
            else if (a2[p2] < a1[p1]) {
                p2++;
            }

        }
        return a;
    }

    //method that subtracts one array from another and returns the result
    public static int[] minus(int[] Oa1, int[] a2) {

        int[] a1 = new int[Oa1.length];
        System.arraycopy(Oa1, 0, a1, 0, Oa1.length);
        int nLength = a1.length;
        //System.out.println("Initial length = " + nLength);
        for (int i = 0; i < a2.length; i++) {
            int p1 = 0;
            for (p1 = p1; p1 < nLength; p1 = p1) {
                //System.out.println("a1[p1] = " + a1[p1]);
                //System.out.println("a2[i] = " + a2[i]);
                if (a1[p1] == a2[i]) {
                    //System.out.println("if statement");
                    nLength--;
                    for (int j = p1; j < nLength; j++) {
                        a1[j] = a1[j + 1];
                    }
                    //System.out.println("new length = " + nLength);
                } else {
                    p1++;
                }
            }
            //System.out.println("broke out of p1 for-loop");
        }

        int[] a = new int[nLength];
        System.arraycopy(a1, 0, a, 0, nLength);

        return a;
    }

    public static void sort(int a[]) {
        sort(a, 0, a.length);
    }

    private static void sort(int a[], int l, int n) {
        if(n <= 1)
            return;
        swap(a, l+n-1, l+(int)(Math.random()*n));
        int i = l, k = l, p = l+n;
        while(i < p) {
            if(node[a[i]].edgeCount > node[a[l+n-1]].edgeCount)
                swap(a, i++, k++);
            else if(node[a[i]].edgeCount == node[a[l+n-1]].edgeCount)
                swap(a, i, --p);
            else
                i++;
        }
        int m = Math.min(p-k, l+n-p+1);
        for(int j = 0; j < m-1; j++)
            swap(a, k+j, l+n-m+1+j);
        sort(a, l, k-l);
        sort(a, k+1, l+n-k-1);
    }

    public static void swap(int a[], int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}
