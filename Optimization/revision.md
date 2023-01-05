# revision

## methods

- [Grid search](#grid-search)
- [Random search](#random-search)
- [Nelder-Mead method](#nelder-mead-method)
- [Powell's method](#powells-method)
- [DIvining RECTangles DIRECT](#dividing-rectangles-direct)
- [Basin Hopping](#basin-hopping)
- [Gradient descent](#gradient-descent)
- [Variable neighborhood search](#variable-neighborhood-search)
- [Iterated local search](#iterated-local-search)
- [Simulated annealing](#simulated-annealing)
- [Bayesian optimization](#bayesian-optimization)
- [Genetic algorithm](#genetic-algorithm)
- [Computational swarm optimization](#computational-swarm-optimization)
- [Multi-objective optimization](#multi-objective-optimization)
- [Design of experiment](#design-of-experiment)
- [Linear programming](#linear-programming)
- [Quadratic programming](#quadratic-programming)
- [Iteger (linear) programming](#integer-linear-programming)

## Grid search

just separate the space into a grid, and evaluate the function at each point of the grid.
Expensive and can take some time for nothing.

## Random search

You need luke to find the optimum.

## Nelder-Mead method

It's the method with triangles.

- Reflexion
- Expansion
- outside contraction
  - shrink
- inside contraction
  - shrink

## Powell's method

It's the method with lines.

- You take a starting point
- You take a direction
- You make a 1D optimization in that direction
- You take the new point and a new direction and repeat

## DIviding RECTangles DIRECT

It's the method with rectangles.

**Pros :**

- Don't need to know the gradient
- Relatively simple

**Cons :**

- less effective in high dimension
- can be slow to find the optimum with accuracy

**basic idea :**

- select rectangles
- divide it
- select the best rectangles and repeat

You need to had a stopping criterion.

- number of iteration
- minimum size of the rectangle (accuracy)
- objective function convergence tolerance

**Problems :**

- if you have to much local minima, you can spend a lot of time in a bad area
- if you have to much rectangles selected, exemple of the function who converge in the corner

## Basin Hopping

- make a jump
- make a local search
- if the local search is better, keep it
- repeat

## Gradient descent

- choose a step size
- compute the gradient
- go in the opposite direction with jump of size step size

if you have a convex function when you find local minima you are at the global minima.

**different methods :**

- Steepest descent (1st order derivative)
- Conjugate gradient (1st order derivative)
- Quasi-Newton (2nd order derivative)
- Newton's method (2nd order derivative)

Newton are really fast

## Variable neighborhood search

**basic idea :**

you have a partial solution fixed and you want to find the best solution with this partial solution.
Sometime you change some varaible in the partial solution to improve it. You find witch variable to change with a local search in the neighborhood.

- start with a random solution
- take a random partial solution
- make a local search to find a solution with the partial random solution
- if the local search is better, keep it
- repeat until you have reach the maximum number of neighborhood

**Varaible neighborhood descent VND :**

```
Procedure VND
select {Nk}, k = 1, ..., kmax
find an initial solution x
k ← 1
repeat until k > kmax
    x' ← FindBestNeighbour(Nk (x))
    if f(x') < f(x) then
        x ← x'
        k ← 1
    else
        k ← k + 1
End
```

**Reduced VNS RVNS :**

Same as VND but don't search a best neighbour in the neighborhood, just search a random neighbor.

**VARIABLE NEIGHBORHOOD DECOMPOSITION SEARCH VNDS :**

```
Procedure VNDS
select {Nk}, k = 1, ..., kmax
find an initial solution x
choose a stopping condition
k ← 1
repeat until k = kmax
    x' ← RandomSolution(Nk (x))
    t' ← DetermineFreeComponents(x')
    t'' ← LocalSearch(t')
    x'' ← InjectComponents(x',t'')
    if f(x'') < f(x) then
        x ← x''
        k ← 1
    else
        k ← k + 1
End
```

## Iterated local search

**basic idea :**

- you take a first solution
- you make a local search
- you add a random perturbation on it and you keep in memory the previsious solution
- you make a new local search
- You keep it if the acceptation criterion is true
- repeat

you need to find the good perturbation : too much will make the perturabation a random thing, too little and the local search will undo the perturbation.

acceptation criterion :

- control balance between intensification and diversification
  - if you keep only the beter solution you will have an extrem intensification
  - with random acceptation you will have an extrem diversification

## Simulated annealing

**basic idea :**

we walk on the function (it's like exploring the neigborhood) if the next step is better we go there, if not we go there with a probability calculated with $e^{f(x_n)-f(x_{n-1})/T}$.

The size of T influe the probability of escape from a local minima.

## Bayesian optimization

Take a function f(x) and EI(x) a function that give the expected position of the optimum.
on the boundaries cut it in 2 and make an opservation of f(x) on this position.
make an observation on eacth maximum of EI(x) and repeat.

good for function where computation is expensive.

## Genetic algorithm

**basic idea :**
Create a population of random solution and make a selection, reproduction, crossover and mutation on it.

## Computational swarm optimization

**basic idea :**

Ants move on the graph and leave a pheromone trail. The ants follow the trail with a probability proportional to the pheromone trail.

## Multi-objective optimization

**Scalarization :** different objectives are combined into one (non-)linear function.

**Lexicographic ordering :** order each objective by order of importance. solve the first objective, solve the second with the result of the first, solve the third with the result of the first and second, etc.

**$\epsilon$-constraint method :** solve each problem separately, then take the solution that is in each solution set.

**Pareto front :** set of solutions that are not dominated by any other solution.

## Design of experiment

**Sample :**

- **Halton sequence :** generate a sequence of points in a normalized space. Constructed determinisrically, based on coprime numbers. Quasi-random sequence (low discrepancy sequence). Efficient for low dimensions.

- **Sobol sequence :**

- **Latin hypercube sampling :** create 1 grid per variable (grid of size n, where each symbol can occur only once). Then concatenate the grids. L1(a1A, b2B, c3C) L2(c3B, a1C, b2A) L3(b2C, c3A, a1B)

## Linear programming

$$
maximize 2x_1 + x_2 \\
subject\ to\ 3x_1 + x_2 \leq 9 \\
x_1 + 2x_2 \leq 6 \\
x_1, x_2 \geq 0
$$

## Quadratic programming

$$
minize f(x) = \frac{1}{2}x^TQx + c^Tx \\
subject\ to\ Ax \leq b \\
x \geq 0
$$

## Integer (linear) programming

$$
minize c^Tx \\
subject\ to\ Gx \leq h \\
x \in \mathbb{Z}^n\ (integer\ variables)
$$
