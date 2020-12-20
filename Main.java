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

    public static String chooseNeighborNodeAtRandom(MutableNetwork<String, String> graph, String node) {
        int i = 0;
        String chosenNode = "";
        int degreeNode = graph.degree(node);
        Set<String> neighbors = graph.adjacentNodes(node);
        int chosenNodeIndex = (new Random()).nextInt(degreeNode);

        Iterator<String> it = neighbors.iterator();
        do {
            i++;
            chosenNode = it.next();
        } while (it.hasNext() && i < chosenNodeIndex);

        return chosenNode;
    }

    public static Pair<String, String> chooseEdgeRandomly(MutableNetwork<String, String> graph) {
        int i = 0;
        Random seed = new Random();
        Pair<String, String> edge = null;
        String node1 = "", node2 = "";
        int numberOfNodes = graph.nodes().size();
        int node1Index = seed.nextInt(numberOfNodes - (1 + 1)) + 1;

        for (Iterator<String> it = graph.nodes().iterator(); it.hasNext() && i < node1Index ; i++) {
            node1 = it.next();
        }
        node2 = chooseNeighborNodeAtRandom(graph, node1);

        edge = new Pair<String, String>(node1, node2);
        return edge;
    }


    public static void contractNodes(MutableNetwork<String, String> graph, String node1, String node2)
    {
        if (!graph.hasEdgeConnecting(node1, node2)) {
            System.out.println("No edge to contract between nodes (" + node1 + ", " + node2 + ")");
            System.exit(255);
        }

        String newSuperNode = node1 + ", " + node2;
        String edgeToBeRemoved = "";
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
