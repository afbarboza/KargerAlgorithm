# Karger's Algorithm - General Description
The Karger's Algorithm is a Monte Carlo method to handle the problem of global minimum cut in a undirected connected graph G = (V, E). It is a randomized algorithm, which means its behavior is defined not only by the input, but also by randomly generated numbers. It was created by PhD David Karger in 1993, at Stanford University.

### The Problem
Consider the given undirected graph below:

![Input Example](./input_graph.jpg?raw=true)

The given graph ```G = (V, E)``` is such that ```V``` is the set of all nodes in the graph, ```V = {0, 1, 2, 3}``` ,  and ```E``` is the set of all edges connecting the nodes, ```E = {0-1, 0-2, 0-3, 1-3, 2-3}```. A **cut** in the given graph is defined by partitioning the set of nodes ```V``` into two subsets ```S``` and ```V - S```, implying that removing the edges connecting ```S``` and ```V - S```results in some disconnected graph. The **size** of the cut is the cardinality of the set of edges connecting ```S``` and ```V - S```.

For example, one possible way of partioning the input graph is grouping the nodes ```V = {0, 1, 2, 3}``` using the following partition: ```S = {0, 2}``` and ```V - S = {1, 3}```, resulting in the following **multigraph**:

![Possible Cut](./possible_cut.jpg?raw=true)

The size of the cut above is 3, since there are three edges connecting ```S = {0, 2}``` and ```V - S = {1, 3}```. The Karger's Algorithm try to determine the **global minimum cut** possible for some input graph. For the input graph above, the minimum cut has size of 2:

![Possible Cut](./output_graph.jpg?raw=true)

### The Algorithm

This implementation reads the `input.txt` file containing the nodes and edges. The first number in the i-th line represent the i-th node of the graph, and the following numbers in the i-th line represent all the nodes which node i is connected to. After reading the input and then building the graph data structure, the Karger's Algorithm works in the following way:

```
while (number_of_nodes > 2) {
    /* choose uniformly random edge (u, v) */
    edge = choose_random_edge(graph)
    
    /* contract the nodes connected by the edge chosen in the previous step */
    contract_nodes(graph, edge)
    
    /* compute current size of set V */
    number_of_nodes = number_of_nodes(graph)
}
```
### Implementation Details

Since we are not worried about [how to implement graphs](https://www.geeksforgeeks.org/graph-and-its-representations/) but instead only interested in some operations over graphs, this implementation use [Mutable Networks](https://github.com/google/guava/wiki/GraphsExplained#network) from [Guava](https://github.com/google/guava/wiki) to work with multigraphs. Also, notice that Karger Algorithm is nested inside a main loop which looks like this:

```
while (i < 50000) {
    currentCut = findMinimumCut(graph)
    if (currentCut < smallestCut) {
        smallestCut = currentCut
    }
}
```

While `50000` could look like some magical number, it is indeed necessary to increase the [probability of success](https://en.wikipedia.org/wiki/Karger%27s_algorithm#Success_probability_of_the_contraction_algorithm) of this randomized algorithm. For our input with n = 200 nodes, the number of executions to minimize the probability of not finding the minimum cut should be:

![Equation](./nbr_executions.gif?raw=true)

While `119400` is the precise number in order to maximize the odds of finding the global minimum cut, another variable should be considered: the overall time of execution for the Karger's Algorithm. Empirically, `50000` proved to be satsifactory enough.

### Bibliographic References
Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, and Clifford Stein. 2009. Introduction to Algorithms, Third Edition (3rd. ed.). The MIT Press.

Sanjoy Dasgupta, Christos H. Papadimitriou, and Umesh Vazirani. 2006. Algorithms (1st. ed.). McGraw-Hill, Inc., USA.

Tim Roughgarden. 2018. Algorithms Illuminated (Part 1): The Basics (Volume 1).
