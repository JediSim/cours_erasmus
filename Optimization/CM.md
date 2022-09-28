---
Optimization Techniques
---

# Optimization Techniques

---

- Decision variables x = [x(1),x(2)...x(n)]
- Objective function: f(x)
- Decision space: D
- Constraints: g(x) and h(x)
- (Global/local) optimum: x\*

**Important**
minimize f(x) = maximize -f(x)

## Basic Optimization algo

- **Analytical approach**: the function has an explicit analytical expression, derivative over all the variables
- **Exact methods**: the funct respects some specific hypotheses it's linear or quadratic problem. The method converges to the exact solution after a finite amount of steps of an iterative procedure

- **Approximate methods**: the function respects some hyptoheses and can be solved by applying an iterative procedure with an infinite number of steps. The application of the procedure for a finite amount of steps still leads to an approximation of the optimum.
  For instance, soe kind of gradient descent

### Global optimization: find the global optimum

- Unimodal vs multimodal: one vs many optima
- Approaches:
  - **Deterministic**: brute force (evaluate all points)
  - **Stochastic**: random search
  - More advanced: DIRECT, basin hopping, etc

#### Grid search

pb :

- can be very expensive

  - more parameters, the larger the grid
  - if every point are expensive to evaluate

- continuous parameters
  - we may miss the optimum
  - we need to discretize the parameter

#### Random search

- you are unlikely to keep completely missing the optimum
- a grid search may spend lots of time in a "bad area"
- A sampling methodology is needed

### Local optimization: find the local optimum

- **Classic (gradient-based) methods**: use gradient or higher level derivates of the objective funct.
  gradient descent, Newton's method, quasi-Newton methods, etc

- **Gradient-free methods**: do not use derivatives of the objective funct, but heuristic method:
  - Nelder-Mead, simulated annealing, etc

#### Nelder-Mead simplex

- **Downhill simplex method**
  - **Simplex**: geometrical figure in N dimension with N+1 points
  - **The algorithm**: main equation $x = x_c + \alpha(x_c-x^n)$
- **Practical considerations**:
  - Relatively robust to initial solutions
  - Not very robust to noise
  - Extensively used:
    - can deal with non-differentiable funstions, and even with some discontinuities
    - cannot directly handle constaints
  - Often considerd as a baseline methods

#### Powell's method

- **The algorithm**: cf [Wikipedia](https://en.wikipedia.org/wiki/Powell%27s_method) and poly

## Deterministic/stochastic global optimization

### DIVIDING RECTANGLES

- good for explore. But slow at convergence

partitioning mechanism

Better spilt horizontally.

### BASIN HOPPING

- work well with a function with many local optima (many spikes)

1. Hopping
2. Local search
3. Acceptance/rejection of the new solution

## Derivative-based optimization

### GRADIENT DESCENT

1. first technique
   $\alpha$ is the step size (ex: page 8)
   with this technique, when you find a local optimum of convex function, you are sure to find the global optimum

page 11 the convex fonction can be flat

### 2ND ORDER METHODS - CURVATURE INFORMATION

#### NEWTON METHOD

best BFGS
