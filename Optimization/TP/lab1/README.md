---
Readme of the lab 1
---

# TP 1

# Ex 1: Grid Search

In this first exercise we will use grid search as a search algorithm

## Questions

- How does the step size influence the quality of the best point obtained?
  If the step size is too small, the search will be very long. If the step size is too big, the search will not be precise enough.
- How does the step size influence the search cost?
  The search cost is the number of points evaluated.

---

# Ex 2: Random Search

In this exercise we will use Random Search to search for the optimum.

## Questions

- How does the number of samples drawn affect the search?
  The number of samples drawn affects the search by increasing the number of points evaluated. More points evaluated means a better search.
- How does this method compare to Grid Search? What are the advantages and disadvantages?
  This method is better than Grid Search because it is faster. The disadvantage is that it is not as precise as Grid Search (ex: if you don't have luck all the points can be on the wrong value, but if you are lucky they can be on the optimum).

---

# Ex. 3: Powell Optimization

In this exercise we will focus on the Powel optimization algorithm.

parameters:

- fun: function to optimize
- $x_0$: initial point
- maxiter: maximum number of iteration

## Questions

- What happens when varying the parameters of the algorithm?
  The parameters of the algorithm are the initial point and the maximum number of iterations. The initial point is the point where the algorithm starts. The maximum number of iterations is the maximum number of points evaluated. If the maximum number of iterations is too small, the algorithm will not be able to find the optimum. If the maximum number of iterations is too big, the algorithm will take too much time to find the optimum. If the initial point is to far it's take more time to find the optimum.
- How they influence the optimization process?
  The initial point influences the optimization process by starting the algorithm at a certain point. The maximum number of iterations influences the optimization process by limiting the number of points evaluated.
- The effects of these parameters is the same across different functions?
  The initial point have the same effect on different functions.
- How does this algorithm compare to the previous?

---

# Ex. 4: Nelder Mead Optimization

In this exercise we will focus on the Nelder Mead optimization algorithm.
Similar to the previous exercise, answer the following questions:

## Questions

- What happens when varying the parameters of the algorithm?
  If you put the initial point far away from the optimum, it will take more time to find the optimum. If you put the maximum number of iterations too small, it will not be able to find the optimum. If you put the maximum number of iterations too big, it will take too much time to find the optimum (but it's more accurate).
- How they influence the optimization process?
  If you start at the good place it will take less time to find the optimum, but if is it to far you need more iterations to find the optimum.
  If you don't have enough iterations you can't find the optimum.
- The effects of these parameters is the same across different functions?
  You search the minimum and you start to close of a minimum local you can't find the reel minimum (exemple with Michalewicz funct).
- How does this algorithm compare to the previous?
  With this algorithm you can miss the optimum if you don't have enough iterations or if you are to close of a local minimum.

parameters:

- fun: function to optimize
- 𝑥0 : initial point
- maxiter: maximum number of iteration

---

# Instructions and questions

Concisely note down your observations from the previous exercises (follow the bullet points) and
think about the following questions.

- How the benchmark functions influence the optimization algorithms? There is an algorithm which is always better than the other?
  The benchmark functions influence the optimization algorithms by giving the algorithm a function to optimize. The algorithm which is always better than the other is the one which is the most adapted to the function to optimize (exemple: with local minimum you are sure to find the minimum with powell but it's more tricky with Nelder Mead).
- The choice of the parameters is influenced by the function to optimize? And how the algorithm are influenced by the parameters?
  You need to choose a good point to start and good number of iterations. More close you are of the optimum, less iterations you need.
  With many iterations the algorithm takes more time but it's more precise.
  with Nelder Mead a bad start can make you miss the optimum.
