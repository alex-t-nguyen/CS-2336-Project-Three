# Project-Three
Simple Integral Calculator

**Objective:** Use object-oriented programming to implement and utilize a binary tree

**Problem:** Doc Brown has gotten himself in a jam. The software he uses to evaluate integrals has become corrupt
and he is in need of assistance. Without this software, his quantum physics calculations will be incorrect, possibly
creating time paradox of epic proportions! Doc Brown is enlisting your assistance in the matter. Your future may
depend on it.

**Pseudocode:** Your pseudocode should describe the following items

- Identify the functions you plan to create for each class
    - You do not have to include pseudocode for basic items like constructors, accessors, mutators
- For each function, identify the following
    - Determine the parameters
    - Determine the return type
    - Detail the step-by-step logic that the function will perform

**Details:**

- Start the program by prompting the user for the input filename
    o This would normally be hardcoded in an application, but zyLabs requires a filename for multiple
       test files
- This program will read basic integrals from a file and evaluate them.
- If the integral is definite, the program will create the anti-derivative and evaluate for the given interval.
- If the integral is indefinite, then just the anti-derivative should be created.

**Classes**

- Use good programming practice for classes – proper variable access, mutators and accessors, proper
    constructors, etc.


- Remember that classes exist to be used by other people. Just because you don’t use it in the program
    doesn’t mean it shouldn’t be coded and available for others to use in theirs.
- As with previous projects, you are open to design the classes as you see fit with the minimum requirements
    listed below
- All classes must be of your own design and implementation.
    - **Do not use the pre-defined Java classes (-20 points if pre-defined classes used)**
- **Requirements**
    - Binary Tree class
       - Named **BinTree.java**
       - Root pointer
       - Will contain functions for basic functionality of a binary tree
          - Insert
          - Search
          - Delete
       - May contain other functions
       - All traversals of the tree will be done recursively (-10 points if not)
          - This includes functions that add, delete, or search the tree
       - The order of the nodes matters
          - A binary tree Is better than a linked list
    - Node class
       - Named **Node.java**
       - Generic class (-10 points if not)
       - Left and right pointers
       - Payload object to hold data
    - Payload class
       - Named **Payload.java**
       - Coefficient
       - Exponent

**Expressions**

- Consist of simple polynomial terms - the highest degree will be 10
    - If multiple terms include the same exponent, combine those terms
- Exponents will be represented by the ^ character.
- Do not assume that the expression will be in order from highest to lowest exponent.
- All coefficients will be integers.
- The | (pipe) character will be used to represent the integral symbol
- If an integral is definite, there will be a number before and after the | character

```
 ∫ x dx = a|b x dx
 The endpoints of the interval for the definite integral will be integer values
```
- The variable will always be ‘x’ and the integral will always end with dx
- There will always be a space before the first term of the integral and before dx
    o Do not assume there will be spaces anywhere else in the expression
- If a term has no coefficient, it is assumed to be 1


- **Example Input:**
    - | 3x^2 + 2x + 1 dx
    - 1|4 x^-2 +3x+4 dx
    - – 2|2 x^3 – 4x dx

**Input:** All expressions will be read from a file. There is no limit to the number of expressions that can be in the file.
Each expression will be on a separate line (see examples above).

**Output:**

- All output will be written to the console
- Each anti-derivative will be displayed to a separate line
    - Definite integrals will also include the interval and value (see examples below)
       ▪ Values will be to 3 decimal places
- Use the ^ character to represent exponents
- Order the terms from greatest to least exponent
- Fractions will be simplified
    - All fractions will be enclosed in parentheses
- A space will proceed and follow each operator (+ or -)
- Indefinite anti-derivative format
    - expression_space_plus_space_capital C_newline
- Definite anti-derivative format
    - expression_comma_space_upper bound_pipe_lower
       bound__space__equal__space_value_newline
- **Example Output (based on input above):**
    - x^3 + x^2 + x + C
    - (3/2)x^2 + 4x -x^-1, 1|4 = 35. 250
    - (1/4)x^4 – 2x^2, -2|2 = 0

**EXTRA CREDIT:** Add to your program to allow evaluation of indefinite integrals containing trigonometric functions
(potential 15 extra points)

- **The trig terms must be stored in the tree with all other nodes.**
- Simple expressions inside the trig functions
    - The term inside the trig function will not have an exponent
- The trig anti-derivatives will be listed at the end of the expression in the order encountered
- There will be a space between the trig function and the inner coefficient of the trig function
    - This is true for both input and output
- **Example Input:**
    - | sin x + cos x dx
    - | 1 – cos 4x dx
    - | 3x^4 – 6x^2 + 2 sin 10x dx
- **Example Output:**
    - – cos x + sin x + C
    - x – (1/4)sin 4x + C
    - (3/5)x^5 – 2x^3 – (1/5)cos 10x + C
