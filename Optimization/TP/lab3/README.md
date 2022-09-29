# Ex. 1: Gradient Descent

---

In this first exercise, we will focus on the Gradient Descent algorithm.

- How the Learning Rate influences optimization?
  The learning rate is the $\alpha$ in this equation $x - \alpha \nabla_x f(x)$ with to high value the algorithm will diverge and to low value the algorithm will take too much time to converge.
- How does tolerance influences the search?
  the tolerance is the $\epsilon$ use to say when the algorithm find the optimum. If the tolerance is too high the algorithm will find not a very accurate optimum
- The effects of these parameters are the same across different functions?
  Yes, the effects are the same because the algorithm is the same, the only thing that change is the function to optimize.

# Ex. 2: Newton Method

---

In this exercise, we will see the Newton method.
Similar to the previous exercise, answer the following questions:

- How does tolerance influence the search?
  we just have the start point, the number of maximum iteration and the tolerance. The tolerance is the $\epsilon$ use to say when the algorithm find the optimum
- Is it faster to converge with respect to GD?
  it is faster with quadratique function but gradian descent is faster with other function.
- The result is similar across different functions?
  The result is similar but with different speed. It's faster with quadratique function but slower with other function.

# Ex. 3: BFGS Optimization

---

In this exercise, we will focus on the BFGS optimization algorithm.
Similar to the previous exercise, answer the following questions:

- Varying these parameters what happens?
  eps is the size of the step and maxiter is the maximum number of iteration
- How do they influence the evolution process?
- What is the difference with L-BFGS? Hint: you have to change the BFGSfunction, calling the right method to minimize. See [here](https://docs.scipy.org/doc/scipy/reference/optimize.minimize-lbfgsb.html#optimize-minimize-lbfgsb) the parameters available.
  It's usefull when the number of design variable is very high. It's faster and lighter for the memory than BFGS but it's less accurate.
  (cf diapo 27)

# Instructions and questions

---

Concisely note down your observations from the previous exercises (follow the bullet points) and
think about the following questions.

- What is the difference in search cost (the number of function/derivative evaluations) between these methods?
  the newton method is the fastest, it will take less evaluation to find the optimum.
- Comparing these methods to the ones of the first laboratory, are they faster? Or find the optimal more efficiently?
  these methods are faster and find the optimal more efficiently. With the gradiant the function are more "smart" and know a lot of information about the function to optimize, it's not like the first lab where the function are more random and don't know anything about the function to optimize.
