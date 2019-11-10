package nl.ru.s1047178;

/**
 * This application is an assignment for the course Algorithm and Data Structures
 * at Radboud University (Nijmegen). The assignment is the first practical assignment
 * for the course and involves creating an algorithm according the problem specification
 * found below.
 *
 * Briefing:
 * The municipal administration of Nijmegen wants to keep the city clean and neat. In
 * order to do this, they want to place garbage bins on as many street intersections as
 * possible. This means the garbage collectors have to make more stops on their daily
 * routes, something they are not happy about. After discussion between the city and the
 * garbage collectors a compromise has been reached: the city will place as many garbage
 * bins as possible, but if two intersections are adjacent to each other at most one of them
 * will have a garbage bin.
 * Both parties are happy with this agreement, and the city has already ordered the
 * bins, but placing them in the city turns out to be a much harder problem than they
 * anticipated. They have asked you - an expert in Algorithms and Data Structures - to
 * find out if it is possible to place all the bins in the city.
 *
 * Input:
 * The local government of Nijmegen gives you (via stdin) the following data:
 * • The first line contains integers n, m and k, (1 ≤ n ≤ 200, 1 ≤ m ≤ 100, 1 ≤ k ≤ 20),
 * which denote the number of streets in Nijmegen, the number of intersections in
 * Nijmegen and the number of bins which are already ordered. It is given that on
 * every intersection a maximum of 4 streets meet.
 * • Then n lines, each containing two integers a and b, indices of intersections that
 * are connected by a street (and thus adjacent). Intersections are numbered 1 to m.
 * Moreover, a and b are always distinct (so an intersection is never adjacent to itself)
 * and no two streets can connect the same two intersections.
 *
 * Output:
 * Your output (via stdout) should be a string: “possible” if the city can place k bins,
 * “impossible” if it can not.
 *
 * Example 1:
 * input:
 * 8 7 4
 * 1 2
 * 1 4
 * 2 3
 * 2 5
 * 4 5
 * 5 6
 * 5 7
 * 6 7
 * output:
 * impossible
 *
 * Example 2:
 * 9 8 3
 * 1 2
 * 1 4
 * 2 3
 * 2 5
 * 4 5
 * 5 6
 * 5 7
 * 6 8
 * 7 8
 * output:
 * possible
 *
 * author: Shelby Hendrickx and Lars van de Werff
 */
class GarbageCollection {

    /**
     * Number of streets in Nijmegen.
     * Assuming graph G = (V,E), where E is the edge set, streets
     * represents the size of E.
     */
    private int streets;

    /**
     * Number of intersections in Nijmegen.
     * Assuming graph G = (V,E), where V is the vertex set, intersections
     * represents the size of V.
     */
    private int intersections;

    /**
     * Number of ordered garbage bins.
     */
    private int bins;

    GarbageCollection(int n, int m, int k) {
        this.streets = n;
        this.intersections = m;
        this.bins = k;
    }

    private static class Node {
        // intersection index
        int index;

        // neighbouring intersections
        Node[] neighbours;

        Node(int i) {
            this.index = i;
            this.neighbours = new Node[4];  // max 4 streets per intersection
        }
    }

    // TODO implement algorithm
    // The idea is to find the largest subset S of graph G(V,E) where no two vertices
    // in the set S represent an edge in G and then check if the size of S
    // is larger than k. Thus, S is maximal if there exists no subset of S
    // in G that abides the aforementioned rule.
    boolean isPossible() {
        int[][] maximalIndependentSets = getMaximalIndependentSets();
        int maxNumberOfBins = getSizeOfMaximumIndependentSet(maximalIndependentSets);
        return maxNumberOfBins >= bins;
        //return false;
    }

    // maybe not optimal solution but will work
    private int[][] getMaximalIndependentSets() {
        return null;
    }

    // check which maximal independent set is the largest and return the size
    private int getSizeOfMaximumIndependentSet(int[][] maximalIndependentSets) {
        return 0;
    }

}
