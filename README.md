# Karger's Algorithm - General Description
The Karger's Algorithm is a Monte Carlo method to handle the problem of global minimum cut in a undirected connected graph G = (V, E). It is a randomized algorithm, which means its behavior is defined not only by the input, but also by randomly generated numbers. It was created by PhD David Karger in 1993, at Stanford University.

# The Problem
Consider the given undirected graph below:

![Input Example](./input_graph.jpg?raw=true)

The given graph ```G = (V, E)``` is such that ```V``` is the set of all nodes in the graph, ```V = {0, 1, 2, 3}``` ,  and ```E``` is the set of all edges connecting the nodes, ```E = {0-1, 0-2, 0-3, 1-3, 2-3}```. A **cut** in the given graph is defined by partitioning the set of nodes ```V``` into two subsets ```S``` and ```V - S```, implying that removing the edges connecting ```S``` and ```V - S```results in some disconnected graph. The **size** of the cut is the cardinality of the set of edges connecting ```S``` and ```V - S```.

One possible way of partioning the input graph is grouping the nodes ```V = {0, 1, 2, 3}``` using the following partition: ```S = {0, 2}``` and ```V - S = {1, 3}```, resulting in the following **multigraph**:

![Possible Cut](./possible_cut.jpg?raw=true)

The size of the cut above is 3, since there are three edges connecting ```S = {0, 2}``` and ```V - S = {1, 3}```. The Karger's Algorithm try to determine the **minimum cut** possible for some input graph. For the input graph above, the minimum cut has size of 2:

![Possible Cut](./output_graph.jpg?raw=true)

