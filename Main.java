package com.learning;

import com.google.common.graph.*;
import java.util.*;
import javafx.util.Pair;

import static com.learning.InputUtils.*;

public class Main {
    public static void buildNeighborhood(MutableNetwork<String, String> graph, ArrayList<String> adjacencyList) {
        String node = adjacencyList.get(0);
        graph.addNode(node);

        for (int i = 1; i < adjacencyList.size(); i++)  {
            String currentNeighbor = adjacencyList.get(i);
            graph.addNode(currentNeighbor);

            graph.addEdge(node, currentNeighbor, node + "<-->" + currentNeighbor);
//            if (!graph.hasEdgeConnecting(node, currentNeighbor)) {
//            }
        }
    }

    public static MutableNetwork<String, String> buildGraph() {
        MutableNetwork<String, String> graph = NetworkBuilder
                                                    .undirected()
                                                    .allowsParallelEdges(true)
                                                    .allowsSelfLoops(false)
                                                    .build();
        String currentLine = readNextLine();
        while (!currentLine.isBlank()) {
            ArrayList<String> adjacencyList = parseLineIntoStringList(currentLine);
            buildNeighborhood(graph, adjacencyList);
            currentLine = readNextLine();
        }

        return graph;
    }

    public static String chooseNodeAtRandom(MutableNetwork<String, String> graph) {
        int i = 0;
        String node = "";
        Random random =  new Random();
        int numberOfNodes = graph.nodes().size();
        int randomNodeIndex = (random.nextInt(numberOfNodes));

        for (Iterator<String> it = graph.nodes().iterator();
             it.hasNext() && i <= randomNodeIndex;
             i++, node = it.next());

        return node;
    }

    public static String chooseNeighborAtRandom(MutableNetwork<String, String> graph, String node)
    {
        int i = 0;
        int randomNeighborIndex = 0;
        String chosenNeighborNode = "";
        Random random = new Random();
        Set<String> neighbors = graph.adjacentNodes(node);
        Iterator<String> listOfNeighbors = neighbors.iterator();

        randomNeighborIndex = random.nextInt(neighbors.size());

        for (;
            listOfNeighbors.hasNext() && i <= randomNeighborIndex;
            i++, chosenNeighborNode = listOfNeighbors.next());

        return chosenNeighborNode;
    }

    public static Pair<String, String> chooseEdgeRandomly(MutableNetwork<String, String> graph) {
        String nodeU = chooseNodeAtRandom(graph);
        String nodeV = chooseNeighborAtRandom(graph, nodeU);
        Pair<String, String> edge = new Pair<String, String>(nodeU, nodeV);
        return edge;
    }

    public static void contractNodes(MutableNetwork<String, String> graph, String node1, String node2)
    {
        String edgeToBeRemoved = "";
        String newSuperNode = node1 + ", " + node2;
        for (String tmp : graph.edgesConnecting(node1, node2)) {
            edgeToBeRemoved = tmp;
            break;
        }

        graph.addNode(newSuperNode);
        Set<String> neighborsOfNode1 = graph.adjacentNodes(node1);
        Set<String> neighborsOfNode2 = graph.adjacentNodes(node2);
        Set<String> allNeighbors = new HashSet<>(neighborsOfNode1);
        allNeighbors.addAll(neighborsOfNode2);

        for (String node : allNeighbors) {
            graph.addEdge(newSuperNode, node, newSuperNode + "<-->" + node);
        }

        graph.removeEdge(edgeToBeRemoved);
        graph.removeNode(node1);
        graph.removeNode(node2);
    }

    public static int findMinimumCut(MutableNetwork<String, String> graph) {
        int minCut = 0;
        int numberOfNodes = graph.nodes().size();
        while (numberOfNodes > 2) {
            /* choose random edge (u, v) */
            Pair<String, String> edge = chooseEdgeRandomly(graph);

            /* contract the nodes (u, v) */
            contractNodes(graph, edge.getKey(), edge.getValue());

            /* compute current size |V| */
            numberOfNodes = graph.nodes().size();
        }

        /* count crossing edges between the left two supernodes */
        for (String node : graph.nodes()) {
            System.out.println(node);
        }

        return graph.edges().size();
    }

    public static void main(String[] args) {
        int i = 0;
        int minCut = 0;
        initInputReader();
        MutableNetwork<String, String> graph = buildGraph();
        minCut = findMinimumCut(graph);
        System.out.println(minCut);
    }
}
