package graphcol;

import static graphcol.Main.graph;
import static graphcol.Main.node;

public class BronKerbosch {
    //some array initialisation for the B-K algorithm
    public static int maxClique() {
        int[] np = new int[graph.verticesNumber]; //n for "new"
        for (int i = 0; i < graph.verticesNumber; i++) {
            np[i] = i + 1;
        }
        int[] nr = new int[0];
        int[] nx = new int[0];

        return bronKerbosch(np, nr, nx).length;


    }

    // method containing a variation the B-K algorithm for computing the maximum clique, from which the size is equal to the lowerbound of the graph.
    public static int[] bronKerbosch(int[] p, int[] r, int[] x) {
        int pivotVtx; // pivot vertex with the highest degree

		/*
		System.out.print("p = [ ");
		for (int j = 0; j < p.length; j++) {
			System.out.print(p[j] + " ");
		}
		System.out.print("]\n");

		System.out.print("r = [ ");
		for (int j = 0; j < r.length; j++) {
			System.out.print(r[j] + " ");
		}
		System.out.print("]\n");
		System.out.print("x = [ ");
		for (int j = 0; j < x.length; j++) {
			System.out.print(x[j] + " ");
		}
		System.out.print("]\n");
		*/

        //r - possibleClique
        //p - neighborVertices
        //x - alreadyUsed

        // whenever both []p and []x are empty, r contains a maximal clique.
        if (p.length == 0 && x.length == 0)
            //System.out.println("New maximal clique with size " + r.length + " found!");
        if (p.length == 0 && x.length == 0 && r.length > graph.maxClique.length) {
            graph.maxClique = new int[r.length];
            System.arraycopy(r, 0, graph.maxClique, 0, graph.maxClique.length);
            if(graph.lowerBound < r.length) Main.setLowerBound(r.length);
            System.out.println(r.length + " is a new maximum clique! (at least for now)");
            return graph.maxClique;
        }

        // selecting the pivot vertex.
        if (p.length > 0)
            pivotVtx = p[0];
        else
            return graph.maxClique;
        for (int i = 0; i < p.length; i++) {
            //System.out.println(node[pivotVtx].connectedNodes.length);
            if (node[p[i]].connectedNodes.length >= node[pivotVtx].connectedNodes.length)
                pivotVtx = p[i];
        }
        //System.out.println("Pivot = " + pivotVtx);

        int[] pivotArr = {pivotVtx};
        //System.out.println(pivotArr[0]);

        for (int i = 0; i < minus(p, node[pivotVtx].connectedNodes).length; i++) { //some problem with the for-loop!
            bronKerbosch(inters(p, node[pivotVtx].connectedNodes), union(r, pivotArr), inters(x, node[i+1].connectedNodes));
            p = minus(p, pivotArr);
			/*
			System.out.println("removed " + pivotArr[0] + " from p");
			System.out.print("p = [ ");
			for (int j = 0; j < p.length; j++) {
				System.out.print(p[j] + " ");
			}
			System.out.print("] was changed\n");
			*/
            x = union(x, pivotArr);
        }

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
}
