# Ex 1: DIRECT

---

In this first exercise we will use DIRECT as a search algorithm

## Questions

- How does the eps parameter influence the search process?
  The eps parameter regulates the trade-off between local and global search. The smaller the eps, the more local the search is. The larger the eps, the more global the search is.
  $f(c_i)-Kd_i$
  it's K in this formula.
- How does the number of maximum iterations influence the search process?
  If the number of maximum iterations is too small, the search process will not be able to find the global minimum. If the number of maximum iterations is too large, the search process will take too long to find the global minimum.

# Ex 2: Basin-hopping

---

In this first exercise we will use Basin-hopping as a search algorithm

## Questions

- What is the influence of the following parameters on the search process?
  - T : The temperature. The higher the temperature, the more likely the search process is to accept a worse solution. The lower, the algorithm will be more greedy
  - stepsize : The maximum step size. The algorithm can make bigger steps if the stepsize is higher. If the stepsize is to high you can go out of the bounds of f()
  - stepsize_interval : interval for how often to update the stepsize. With a higher interval, the stepsize will be updated less often and you don't have big jumps
  - step_update :
- How does the number of maximum iterations influence the search process?
  If the number of maximum iterations is too small, the search process will not be able to find the global minimum. If the number of maximum iterations is too large, the search process will take too long to find the global minimum.
- How does the starting point influence the search process?
  If you are closed to the global minimu you can make little steps and find the global minimum.

## General questions

- How do the approaches seen in today's lab compare to the one seen in the previous lab?
