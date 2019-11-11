package nl.ru.s1047178;

import nl.ru.s1047178.utils.TextParser;

import java.util.*;

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
     * Maximum number of streets that meet on an intersection.
     */
    private static final int MAX_STREETS = 4;

    /**
     * Number of streets in Nijmegen.
     * Assuming graph G = (V,E), where E is the edge set, streets
     * represents the size of E.
     */
    private int streetCount;

    /**
     * Number of intersections in Nijmegen.
     * Assuming graph G = (V,E), where V is the vertex set, intersections
     * represents the size of V.
     */
    private int intersectionCount;

    /**
     * Number of ordered garbage bins.
     */
    private int binCount;

    /**
     * Intersections with their neighbours initialized.
     */
    private List<Node> intersections;

    /**
     * @param n number of streets
     * @param m number of intersections
     * @param k number of bins
     */
    GarbageCollection(int n, int m, int k) {
        this.streetCount = n;
        this.intersectionCount = m;
        this.binCount = k;

        intersections = new ArrayList<>();
        for (int i = 0; i < intersectionCount; i++) {
            intersections.add(new Node(i));
        }
    }

    private static class Node implements Comparable<Node> {
        // intersection index
        int index;

        // neighbouring intersections
        Node[] neighbors;

        Node(int i) {
            this.index = i;
            this.neighbors = new Node[MAX_STREETS];
        }

        /**
         * @return first null neighbor or -1 if all neighbors are occupied.
         */
        int findFirstEmptyNeighbor() {
            for (int i = 0; i < neighbors.length; i++)
                if (neighbors[i] == null) return i;
            return -1;
        }

        boolean isNeighborOf(Node n) {
            for (int i = 0; i < MAX_STREETS; i++) {
                if (neighbors[i] == n) return true;
            }
            return false;
        }

        int getDegree() {
            int emptySpot = findFirstEmptyNeighbor();
            return emptySpot == -1 ? 0 : emptySpot;
        }

        void printNode() {
            System.out.printf("Intersection [%d] neighbors: ", index + 1);
            int terminalNeighbor = findFirstEmptyNeighbor();
            for (int i = 0; i < terminalNeighbor || (terminalNeighbor == -1 && i < MAX_STREETS); i++) {
                System.out.printf("%d ", neighbors[i].index + 1);
            }
            System.out.println();
        }

        @Override
        public int compareTo(Node node) {
            // perhaps more efficient to use ArrayList and retrieve size than this.
            return node.findFirstEmptyNeighbor() - this.findFirstEmptyNeighbor();
        }
    }

    // The idea is to find the largest subset S of graph G(V,E) where no two vertices
    // in the set S represent an edge in G and then check if the size of S
    // is larger than k. Thus, S is maximal if there exists no subset of S
    // in G that abides the aforementioned rule.
    boolean isPossible() {
        createGraph();
        for (Node intersection : intersections) {
            intersection.printNode();
        }

        // Sort by order of minimum degree
        Collections.sort(intersections);

        return maxIndependentSet(intersections) >= binCount;
    }

    private void createGraph() {
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < streetCount; ++i) {
            int[] street = TextParser.getParsedInput(input.nextLine().split(" "));

            Node is1 = intersections.get(street[0] - 1);
            Node is2 = intersections.get(street[1] - 1);
            is1.neighbors[is1.findFirstEmptyNeighbor()] = is2;
            is2.neighbors[is2.findFirstEmptyNeighbor()] = is1;
        }
    }

    /**
     * Recursively finds the max independent set in G(V,E).
     * @param intersections V
     * @return a(G), where a is the independent number, otherwise known
     * as the size of the maximum independent set.
     */
    private int maxIndependentSet(List<Node> intersections) {
        int vertexDegree = intersections.get(0).getDegree();
        switch (vertexDegree) {
            case 0: handleZerothDegree(intersections);
            case 1: return handleFirstDegree(intersections);
            case 2: return handleSecondDegree(intersections);
            case 3: return handleThirdDegree(intersections);
            case 4: handleFourthDegree(intersections);
            default:
                System.out.println("Found the edge case.");
        }
        return maxIndependentSet(intersections);
    }

    private void handleZerothDegree(List<Node> intersections) {
        intersections.remove(0);
        intersectionCount--;
    }

    private int handleFirstDegree(List<Node> intersections) {
        Node neighbor = intersections.get(0).neighbors[0];
        intersections.remove(neighbor);
        intersections.remove(0);
        intersectionCount -= 2;
        return 1 + maxIndependentSet(intersections);
    }

    private int handleSecondDegree(List<Node> intersections) {
        // TODO UNCLEAR
        Node neighbor1 = intersections.get(0).neighbors[0];
        Node neighbor2 = intersections.get(0).neighbors[1];

        if (remainingVisCycle(intersections)) {
            return intersectionCount / 2;
        } else if (neighbor1.isNeighborOf(neighbor2)) {
            intersections.remove(neighbor1);
            intersections.remove(neighbor2);
            intersections.remove(0);
            intersectionCount -= 3;
            return 1 + maxIndependentSet(intersections);
        } else {
            return Math.max(1 + maxIndependentSet())
        }
    }

    // TODO still unclear
    private boolean remainingVisCycle(List<Node> intersections) {
        for (int i = 1; i < intersections.size(); i++) {
            if (intersections.get(i).getDegree() != 2) return false;
        }
        return true;
    }

    private int handleThirdDegree(List<Node> intersections) {
        // a_1..a_n are adjacent vertices to vertex v.
        Node v = intersections.get(0);
        Node a1 = v.neighbors[0];
        Node a2 = v.neighbors[1];
        Node a3 = v.neighbors[2];

        if (a1.isNeighborOf(a2) && a1.isNeighborOf(a3) && a2.isNeighborOf(a3)) {
            intersections.remove(a1);
            intersections.remove(a2);
            intersections.remove(a3);
            intersections.remove(v);
            return 1 + maxIndependentSet(intersections);
        }
        // TODO 2.1
    }

    private void handleFourthDegree(List<Node> intersections) {

    }
}
