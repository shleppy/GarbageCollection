package nl.ru.s1047178;

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

    private int[][] streets;

    /**
     * @param n number of streets
     * @param m number of intersections
     * @param k number of bins
     */
    GarbageCollection(int n, int m, int k,int[][] streets) {
        this.streetCount = n;
        this.intersectionCount = m;
        this.binCount = k;
        this.streets = streets;

        intersections = new ArrayList<>();
        for (int i = 0; i < intersectionCount; i++) {
            intersections.add(new Node(i));
        }
    }

    private static class Node implements Comparable<Node> {
        // intersection index
        int index;

        // neighbouring intersections
        List<Node> neighbors;

        Node(int i) {
            this.index = i;
            this.neighbors = new ArrayList<>();
        }

        /**
         * @param n Node to check if it is a neighbor
         * @return true if Node n is an adjacent node to this node, that is there exists
         * an edge between this node v and node n = (v, n) in E.
         */
        boolean isNeighborOf(Node n) {
            for (int i = 0; i < getDegree(); i++) {
                if (neighbors.get(i) == n) return true;
            }
            return false;
        }

        /**
         * @return number of edges
         */
        int getDegree() {
            return neighbors.size();
        }

        void printNode() {
            System.out.printf("Intersection [%d] neighbors: ", index + 1);
            for (Node neighbor : neighbors) {
                System.out.printf("%d ", neighbor.index + 1);
            }
            System.out.println();
        }

        @Override
        public int compareTo(Node node) {
            // perhaps more efficient to use ArrayList and retrieve size than this.
            return this.getDegree() - node.getDegree();
        }

    }

    /**
     * Start of algorithm
     * The idea of the algorithm is to find the largest subset S of graph G(V,E) where no two vertices
     * in the set S represent an edge in G and then check if the size of S
     * is larger than k. Thus, S is maximal if there exists no subset of S
     * in G that abides the aforementioned rule.
     */
    boolean isPossible() {
        createGraph(streets);

        // Sort by order of minimum degree
        Collections.sort(intersections);

//        for (Node intersection : intersections) {
//            intersection.printNode();
//        }

        int independenceNumber = maxIndependentSet(intersections);
        System.out.printf("Independence number: %d\n", independenceNumber);

        return independenceNumber >= binCount;
    }

    /**
     * Creates the graph by creating a bidirectional link between all adjacent nodes.
     */
    private void createGraph(int[][] streets) {
        for (int i = 0; i < streetCount; ++i) {
            int[] street = streets[i];
            Node is1 = intersections.get(street[0] - 1);
            Node is2 = intersections.get(street[1] - 1);
            is1.neighbors.add(is2);
            is2.neighbors.add(is1);
        }
    }

    /**
     * Main algorithm
     * Recursively finds the max independent set in G(V,E).
     *
     * @param intersections remaining vertices
     * @return a(G), where a is the independent number, otherwise known
     * as the size of the maximum independent set.
     */
    private int maxIndependentSet(List<Node> intersections) {
        if (intersections.size() == 0) return 0;
        int vertexDegree = intersections.get(0).getDegree();
        switch (vertexDegree) {
            case 0: return handleZerothDegree(intersections);
            case 1: return handleFirstDegree(intersections);
            case 2: return handleSecondDegree(intersections);
            case 3: return handleThirdDegree(intersections);
            case 4: return handleFourthDegree(intersections);
            default:
                System.out.println("Found the edge case.");
        }
//        return maxIndependentSet(intersections);
        return -1;
    }

    // Case: all remaining vertices have a degree >= 0
    private int handleZerothDegree(List<Node> intersections) {
        intersections.remove(0);

        Collections.sort(intersections);
        return 1 + maxIndependentSet(intersections);
    }

    // Case: all remaining vertices have a degree >= 1
    private int handleFirstDegree(List<Node> intersections) {
        Node neighbor = intersections.get(0).neighbors.get(0);
        removeFromV(intersections, neighbor);
        removeFromV(intersections, intersections.get(0));

        Collections.sort(intersections);
        return 1 + maxIndependentSet(intersections);
    }

    // Case: all remaining vertices have a degree >= 2
    private int handleSecondDegree(List<Node> intersections) {
        Node neighbor1 = intersections.get(0).neighbors.get(0);
        Node neighbor2 = intersections.get(0).neighbors.get(1);

        if (remainingVisCycle(intersections)) {
            return intersections.size() / 2;
        } else if (neighbor1.isNeighborOf(neighbor2)) {
            removeFromV(intersections, neighbor1);
            removeFromV(intersections, neighbor2);
            removeFromV(intersections, intersections.get(0));

            Collections.sort(intersections);
            return 1 + maxIndependentSet(intersections);
        } else {
            List<Node> isCopyOne = secondDegreeEdgeSet(intersections);
            List<Node> isCopyTwo = secondDegreeNoEdgeSet(intersections);

            int maxCandidateOne = 1 + maxIndependentSet(isCopyOne);
            int maxCandidateTwo = 2 + maxIndependentSet(isCopyTwo);
            return Math.max(maxCandidateOne, maxCandidateTwo);
        }
    }

    // Case: all remaining vertices have a degree >= 3
    private int handleThirdDegree(List<Node> intersections) {
        // a_1..a_n are adjacent vertices to vertex v.
        Node v = intersections.get(0);
        Node a1 = v.neighbors.get(0);
        Node a2 = v.neighbors.get(1);
        Node a3 = v.neighbors.get(2);

        // booleans representing neighbours e.g a1 Neighbors a2 etc.
        boolean a1Na2 = a1.isNeighborOf(a2);
        boolean a1Na3 = a1.isNeighborOf(a3);
        boolean a2Na3 = a2.isNeighborOf(a2);

        // case 0: all neighbours are adjacent
        boolean allNeighborsAdjacent =
                a1Na2 && a1Na3 && a2Na3;

        if (allNeighborsAdjacent) {
           return thirdDegreeAllEdgesSet(intersections, a1, a2, a3);
        }

        // case 1: two edges exist between neighbors
        if (a1Na2 && a1Na3) {
            return thirdDegreeTwoEdgesSet(intersections, a1, a2, a3);
        }

        if (a1Na2 && a2Na3) {
            return thirdDegreeTwoEdgesSet(intersections, a2, a1, a3);
        }

        if (a1Na3 && a2Na3) {
            return thirdDegreeTwoEdgesSet(intersections, a3, a1, a2);
        }

        // case 2: one edge exists between neighbors
        if (a1Na2) {
            return thirdDegreeOneEdgeSet(intersections, a1, a2, a3);
        }
        if (a1Na3) {
            return thirdDegreeOneEdgeSet(intersections, a1, a3, a2);
        }
        if (a2Na3) {
            return thirdDegreeOneEdgeSet(intersections, a2, a3, a1);
        }

        // case 3: no edges exist between neighbors
        else {
            return thirdDegreeNoEdgeSet(intersections, a1, a2, a3);
        }
    }

    // Case: all remaining vertices have a degree = 4
    private int handleFourthDegree(List<Node> intersections) {
        if (intersections.size() == 5) return 1;
        if (intersections.size() > 5) {
            List<Node> isCopyOne = new ArrayList<>(intersections);
            Node v = isCopyOne.get(0);
            for (Node node : v.neighbors) {
                removeFromV(isCopyOne, node);
            }

            removeFromV(isCopyOne, isCopyOne.get(0));
            Collections.sort(isCopyOne);
            int maxCandidateOne = 1 + maxIndependentSet(isCopyOne);

            List<Node> isCopyTwo = new ArrayList<>(intersections);
            removeFromV(isCopyTwo, v);
            Collections.sort(isCopyTwo);
            int maxCandidateTwo = maxIndependentSet(isCopyTwo);

            return Math.max(maxCandidateOne, maxCandidateTwo);
        }
        return -1; // impossible.. hopefully
    }

    private void removeFromV(List<Node> intersections, Node v) {
        Iterator<Node> iterator = intersections.iterator();
        while (iterator.hasNext()) {
            Iterator<Node> neighbors = iterator.next().neighbors.iterator();
            while(neighbors.hasNext()) {
                if (neighbors.next() == v) {
                    neighbors.remove();
                    break;
                }
            }
        }
        intersections.remove(v);

        for (Node intersection : intersections) {
            for (Node neighbor : intersection.neighbors) {
                if (neighbor == v) {
                    intersection.neighbors.remove(neighbor);
                    break;
                }
            }
        }
        intersections.remove(v);
    }

    /**
     * Helper method
     * If every remaining adjacent node of vertex v (at index 0 in intersections) has a
     * degree of 2, then the remaining vertices of V form a Cycle.
     *
     * @param intersections The remaining list of vertices.
     * @return True if every adjacent node has a degree of exactly 2.
     */
    private boolean remainingVisCycle(List<Node> intersections) {
        for (int i = 1; i < intersections.size(); i++) {
            if (intersections.get(i).getDegree() != 2) return false;
        }
        return true;
    }

    /**
     * Helper method
     * Case: There exists an edge between node v's adjacent nodes.
     * (a_1, a_2) in E, where {a_1, a_2} = A(v).
     *
     * @param intersections the remaining vertices
     * @return A deep copy of intersections, where vertex v and its neighbors are removed.
     */
    private List<Node> secondDegreeEdgeSet(List<Node> intersections) {
        // Create a deep copy of intersections to make them independently mutable
        List<Node> isCopyOne = new ArrayList<>(intersections);

        // remove v and its two neighbors
        removeFromV(isCopyOne, isCopyOne.get(0).neighbors.get(1));
        removeFromV(isCopyOne, isCopyOne.get(0).neighbors.get(0));
        removeFromV(isCopyOne, isCopyOne.get(0));

        Collections.sort(isCopyOne);
        return isCopyOne;
    }

    /**
     * Helper method
     * Case: There exists no edge between node v's adjacent nodes.
     * (a_1, a_2) not in E, where {a_1, a_2} = A(v).
     *
     * @param intersections the remaining vertices
     * @return A deep copy of intersections, where all neighbors of neighbors of v
     * have been removed.
     */
    private List<Node> secondDegreeNoEdgeSet(List<Node> intersections) {
        // Create a deep copy of intersections to make them independently mutable
        List<Node> isCopyTwo = new ArrayList<>(intersections);

        // Find all neighbors of adjacent nodes to v
        List<Node> neighborsOfNeighbors = new ArrayList<>();
        for (int i = 0; i < isCopyTwo.get(0).neighbors.size(); i++) {
            for (Node nOfn : isCopyTwo.get(0).neighbors.get(i).neighbors) {
                if (nOfn != null) neighborsOfNeighbors.add(nOfn);
            }
        }

        // Remove all neighbors of neighbors
        for (Node neighborsOfNeighbor : neighborsOfNeighbors) {
            removeFromV(isCopyTwo, neighborsOfNeighbor);
        }

        Collections.sort(isCopyTwo);
        return isCopyTwo;
    }

    private int thirdDegreeAllEdgesSet(List<Node> intersections, Node a1, Node a2, Node a3) {
        removeFromV(intersections, a1);
        removeFromV(intersections, a2);
        removeFromV(intersections, a3);
        removeFromV(intersections, intersections.get(0));
        Collections.sort(intersections);
        return 1 + maxIndependentSet(intersections);
    }

    /**
     * @param intersections remaining vertices
     * @param a1 first node of which all neighbors should be removed
     * @param a2 second node of which all neighbors should be removed
     * @param a3 remaining adjacent node
     * @return the independent number of the remaining set
     */
    private int thirdDegreeTwoEdgesSet(List<Node> intersections, Node a1, Node a2, Node a3) {
        List<Node> isCopyOne = new ArrayList<>(intersections);
        removeFromV(isCopyOne, a1);
        removeFromV(isCopyOne, a2);
        removeFromV(isCopyOne, a3);
        removeFromV(isCopyOne, intersections.get(0));
        Collections.sort(isCopyOne);
        int maxCandidateOne = 1 + maxIndependentSet(isCopyOne);

        List<Node> isCopyTwo = new ArrayList<>(intersections);
        for (Node neighbor : a2.neighbors) {
            removeFromV(isCopyTwo, neighbor);
        }
        for (Node neighbor : a3.neighbors) {
            removeFromV(isCopyTwo, neighbor);
        }
        Collections.sort(isCopyTwo);
        int maxCandidateTwo = 2 + maxIndependentSet(isCopyTwo);

        return Math.max(maxCandidateOne, maxCandidateTwo);
    }

    private int thirdDegreeOneEdgeSet(List<Node> intersections, Node a1, Node a2, Node a3) {
        //complements of each neighbor
        List<Node> complementA1 = new ArrayList<>(intersections);
        List<Node> complementA2 = new ArrayList<>(intersections);
        List<Node> complementA3 = new ArrayList<>(intersections);
        //building the compliment a1
        for (Node neighbor :a1.neighbors) {
            removeFromV(complementA1, neighbor);
    }
    removeFromV(complementA1, a3);
    removeFromV(complementA1, a1);

    //compliment a2
        for (Node neighbor :a2.neighbors) {
        removeFromV(complementA2, neighbor);
    }
    removeFromV(complementA2, a3);
    removeFromV(complementA2, a2);

    //compliment a3
        for (Node neighbor :a3.neighbors) {
        removeFromV(complementA3, neighbor);
    }
    removeFromV(complementA3, a1);
    removeFromV(complementA3, a2);
    removeFromV(complementA3, a3);

    List <Node> candidateCopyOne = new ArrayList<>(intersections);
    removeFromV(candidateCopyOne, a1);
    removeFromV(candidateCopyOne, a2);
    removeFromV(candidateCopyOne, a3);
    removeFromV(candidateCopyOne, candidateCopyOne.get(0));
        Collections.sort(candidateCopyOne);
    int maxCandidateOne = maxIndependentSet(candidateCopyOne);

        //checking if the intersections of the complements from a1 a3 <=  a2 a3
        if(intersectingNodes(complementA1,complementA3).size() <= intersectingNodes(complementA2,complementA3).size()){
            Collections.sort(complementA3);
            int maxCandidateTwo = maxIndependentSet(complementA3);
            return Math.max(1+maxCandidateOne,2+maxCandidateTwo);
        }
        else {
            int maxCandidateTwo = maxIndependentSet(intersectingNodes(complementA1,complementA3));
            int maxCandidateThree = maxIndependentSet(intersectingNodes(complementA2,complementA3));
            return Math.max(1+maxCandidateOne,Math.max(2+maxCandidateTwo,2+maxCandidateThree));
        }

    }

    private int thirdDegreeNoEdgeSet(List<Node> intersections, Node a1, Node a2, Node a3) {
        //complements of each neighbor
        List<Node> complementA1 = new ArrayList<>(intersections);
        List<Node> complementA2 = new ArrayList<>(intersections);
        List<Node> complementA3 = new ArrayList<>(intersections);
        //building the compliment a1
        for (Node neighbor :a1.neighbors) {
            removeFromV(complementA1, neighbor);
        }
        removeFromV(complementA1, a3);
        removeFromV(complementA1, a2);
        removeFromV(complementA1, a1);

        //compliment a2
        for (Node neighbor :a2.neighbors) {
            removeFromV(complementA2, neighbor);
        }
        removeFromV(complementA2, a3);
        removeFromV(complementA2, a1);
        removeFromV(complementA2, a2);

        //compliment a3
        for (Node neighbor :a3.neighbors) {
            removeFromV(complementA3, neighbor);
        }
        removeFromV(complementA3, a1);
        removeFromV(complementA3, a2);
        removeFromV(complementA3, a3);

        List <Node> candidateCopyOne = new ArrayList<>(intersections);
        removeFromV(candidateCopyOne, a1);
        removeFromV(candidateCopyOne, a2);
        removeFromV(candidateCopyOne, a3);
        removeFromV(candidateCopyOne, candidateCopyOne.get(0));
        Collections.sort(candidateCopyOne);
        int maxCandidateOne = maxIndependentSet(candidateCopyOne);

        List<Node> allIntersect = intersectingNodes(intersectingNodes(complementA1,complementA2),complementA2);
        Collections.sort(allIntersect);
        int allIntersectSize = allIntersect.size();
        int intersectionsSize = intersections.size();
        //comp intersects >= V - 7
        if(allIntersectSize >= intersectionsSize-7){
            return Math.max(1+maxCandidateOne, maxIndependentSet(allIntersect));
        }
        //comp intersects either v-8 or v-9
        if(allIntersectSize == intersectionsSize-8 || allIntersectSize == intersectionsSize -9){
            //intersection of the complement of a1 and a2, a1 and a3 and a2 and a3
            List<Node> ca1Ica2 = intersectingNodes(complementA1,complementA2);
            List<Node> ca1Ica3 = intersectingNodes(complementA1,complementA3);
            List<Node> ca2Ica3 = intersectingNodes(complementA2,complementA3);

            if (ca1Ica2.size() <= allIntersectSize+1 && ca1Ica3.size() <= allIntersectSize+1 && ca2Ica3.size() <= allIntersectSize+1){
                return Math.max(1+maxCandidateOne, 3+ maxIndependentSet(allIntersect));
            }

            if (ca1Ica2.size() >= allIntersectSize +2){
                return Math.max(Math.max(maxCandidateOne+1,maxIndependentSet(ca1Ica2)+2),maxIndependentSet(allIntersect)+3);
            }

            if (ca1Ica3.size() >= allIntersectSize +2){
                return Math.max(Math.max(maxCandidateOne+1,maxIndependentSet(ca1Ica3)+2),maxIndependentSet(allIntersect)+3);
            }

            if (ca2Ica3.size() >= allIntersectSize +2){
                return Math.max(Math.max(maxCandidateOne+1,maxIndependentSet(ca2Ica3)+2),maxIndependentSet(allIntersect)+3);
            }
            //this means the && in the first if should be or's
            else {
                return -10;
            }
        }

        //comp for everything else
        else{
            return (1 + maxCandidateOne);
        }
    }

    private List<Node> intersectingNodes(List<Node> i1, List<Node> i2){
        List<Node> interNodes = new ArrayList<>();
        for (Node n : i1){
            if(i2.contains(n)) {
                interNodes.add(n);
            }
        }
        return interNodes;
    }
}
