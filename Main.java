package com.learning;

import com.google.common.graph.*;

import java.security.SecureRandom;
import java.security.Timestamp;
import java.util.*;
import javafx.util.Pair;

import static com.learning.InputUtils.*;

public class Main {
    public static void buildNeighborhood(MutableNetwork<String, EdgeType> graph, ArrayList<String> adjacencyList) {
        String node = adjacencyList.get(0);
        graph.addNode(node);

        for (int i = 1; i < adjacencyList.size(); i++)  {
            String currentNeighbor = adjacencyList.get(i);
            graph.addNode(currentNeighbor);

            if (!graph.hasEdgeConnecting(node, currentNeighbor)) {
                graph.addEdge(node, currentNeighbor, new EdgeType(node, currentNeighbor));
            }
        }
    }

    public static MutableNetwork<String, EdgeType> buildGraph() {
        MutableNetwork<String, EdgeType> graph = NetworkBuilder
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

    public static String chooseNodeAtRandom(MutableNetwork<String, EdgeType> graph) {
        int i = 0;
        String node = "";
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        int numberOfNodes = graph.nodes().size();
        int randomNodeIndex = (random.nextInt(numberOfNodes));

        for (Iterator<String> it = graph.nodes().iterator();
             it.hasNext() && i <= randomNodeIndex;
             i++, node = it.next());

        return node;
    }

    public static String chooseNeighborAtRandom(MutableNetwork<String, EdgeType> graph, String node)
    {
        int i = 0;
        int randomNeighborIndex = 0;
        String chosenNeighborNode = "";
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        Set<String> neighbors = graph.adjacentNodes(node);
        Iterator<String> listOfNeighbors = neighbors.iterator();

        randomNeighborIndex = random.nextInt(neighbors.size());

        for (;
            listOfNeighbors.hasNext() && i <= randomNeighborIndex;
            i++, chosenNeighborNode = listOfNeighbors.next());

        return chosenNeighborNode;
    }

    public static EdgeType chooseEdgeRandomly(MutableNetwork<String, EdgeType> graph) {
        String nodeU = chooseNodeAtRandom(graph);
        String nodeV = chooseNeighborAtRandom(graph, nodeU);
        Set<EdgeType> edgesUV = graph.edgesConnecting(nodeU, nodeV);
        return (new ArrayList<EdgeType>(edgesUV)).get(0);
    }

    public static void replaceIncidentEdges(MutableNetwork<String, EdgeType> graph, String superNode, String nodeV) {
        Set<String> adjacentNodesToNodeV = new HashSet(graph.adjacentNodes(nodeV));
        for (String neighborOfNodeV : adjacentNodesToNodeV) {
            Set<EdgeType> parallelEdges = new HashSet<EdgeType>(graph.edgesConnecting(neighborOfNodeV, nodeV));
            for (EdgeType edge : parallelEdges) {
                graph.addEdge(superNode, neighborOfNodeV, new EdgeType(superNode, neighborOfNodeV));
            }
        }
    }

    public static void contractNodes(MutableNetwork<String, EdgeType> graph, String nodeU, String nodeV)
    {
        /* delete all edges between u and v */
        Set<EdgeType> edgesUV = graph.edgesConnecting(nodeU, nodeV);
        Set<EdgeType> edgesUVCopy = new HashSet<>(edgesUV);

        for (EdgeType edge : edgesUVCopy) {
            graph.removeEdge(edge);
        }

        /* replace u and v with a new supernode uv */
        String newSuperNodeUV = nodeU + ", " + nodeV;
        graph.addNode(newSuperNodeUV);

        /* replace *all* edges incident to u or v with edges incident to supernode uv */
        replaceIncidentEdges(graph, newSuperNodeUV, nodeV);
        replaceIncidentEdges(graph, newSuperNodeUV, nodeU);

        /* replace u and v with a new supernode uv */
        graph.removeNode(nodeU);
        graph.removeNode(nodeV);
    }

    public static int findMinimumCut(MutableNetwork<String, EdgeType> graph) {
        int minCut = 0;
        int numberOfNodes = graph.nodes().size();
        while (numberOfNodes > 2) {
            /* choose random edge (u, v) */
            EdgeType edge = chooseEdgeRandomly(graph);

            /* contract the nodes (u, v) */
            contractNodes(graph, edge.getNodeU(), edge.getNodeV());

            /* compute current size |V| */
            numberOfNodes = graph.nodes().size();
        }
        minCut = graph.edges().size();
        return minCut;
    }

    public static void main(String[] args) {
        int i = 0;
        int smallestCutFound = Integer.MAX_VALUE;

        /* must run a sufficient number of times, considering that it is a randomized algorithm */
        while (i < 50000) {
            int minCut = 0;
            initInputReader();
            MutableNetwork<String, EdgeType> graph = buildGraph();
            minCut = findMinimumCut(graph);
            if (minCut < smallestCutFound)
                smallestCutFound = minCut;

            System.out.println(i + " >>>" + minCut);
            i++;
        }

        System.out.println(smallestCutFound);
    }
}
