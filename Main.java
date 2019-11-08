// Alex Nguyen
// atn170001

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static final int HIGHEST_EXPONENT = 10;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner fileReader = null;
        Scanner input = new Scanner(System.in);
        String inputFilename = input.nextLine();

        File inFile = new File(inputFilename);
        try {
            fileReader = new Scanner(inFile);
        }
        // Catch exception if file not
        catch (FileNotFoundException e)
        {
            input.close();
            throw e;
        }

        BinTree<Payload> binaryTree = new BinTree<>();

        while(fileReader.hasNextLine())
        {
            Payload data = null;
            boolean multipleTerms = false;
            String equation = fileReader.nextLine();
            String n = "";
            String[] nodes = new String[equation.length()];

            /* Integral boundary variables */
            int upperBound = 0, lowerBound = 0;
            boolean boundaries = false;
            int pipeChar = equation.indexOf("|");
            /* If there are boundaries on the integral */
            if(pipeChar != 0)
            {
                lowerBound = Integer.parseInt(equation.substring(0, pipeChar)); // Set lower boundary
                upperBound = Integer.parseInt(equation.substring(pipeChar + 1, equation.indexOf(" ")));   // Set upper boundary
                boundaries = true;
            }

            /* Length of string containing boundaries */
            int beginning1 = (equation.substring(0, pipeChar)).length();
            int beginning2 = (equation.substring(pipeChar, equation.indexOf(" "))).length();
            int firstIndex = beginning1 + beginning2 + 1; // First index of integral (not including boundaries)

            /* Loop through entire equation to get each node */
            for(int i = firstIndex, arrayIndex = 0; i < equation.length(); i++)
            {
                // If character is not an operator or space add to string to create node
                if(equation.charAt(i) != '+' && equation.charAt(i) != ' ' && equation.charAt(i) != '-' && equation.charAt(i) != 'd')
                {
                    n += equation.substring(i, i + 1); // Combine each character of equation node into a single string
                }
                else if(equation.charAt(i) == '-' && !Character.isDigit(equation.charAt(i - 1)) && equation.charAt(i-1) != 'x')
                {
                    n += equation.substring(i, i + 1);
                    if(equation.charAt(i + 1) == ' ')
                        i++;
                }
                else if(!(n.equals("")) && !(n.equals("-")))
                {
                    nodes[arrayIndex] = n; // Put equation node into array
                    n = ""; // Reset string
                    arrayIndex++; // Increment array index of equation nodes
                    if(equation.charAt(i) == '-')
                        n += equation.substring(i, i + 1);
                }
                
            }

            int coeff = 0, exponent = 0;
            for(int i = 0; nodes[i] != null; i++) // Go up to nodes.length because dx is not actually added to array
            { 
                multipleTerms = false;
                coeff = 0;
                exponent = 0;
                int xIndex = nodes[i].indexOf('x');
                int carrotIndex = nodes[i].indexOf('^');
                if(xIndex != -1) // If equation substring has x value
                {
                    if(xIndex == 0) // If no coefficient
                        coeff = 1;  // coefficient equals 1    
                    else
                    {
                        if(nodes[i].charAt(0) == '-' && nodes[i].charAt(1) == 'x')
                            coeff = -1;
                        else
                            coeff = Integer.parseInt(nodes[i].substring(0, xIndex)); 
                    }
                    if(carrotIndex == -1) // If x^1 == x
                        exponent = 1;
                    else
                    {                       
                        exponent = Integer.parseInt(nodes[i].substring(carrotIndex + 1));   // Get exponent of equation substring
                    }
                }
                else    // Equation substring is only a number
                    coeff = Integer.parseInt(nodes[i].substring(0));

                if(binaryTree.getRoot() == null)
                {
                    data = new Payload();
                    data.setCoefficient(coeff);
                    data.setExponent(exponent);
                }
                else
                {
                    /** Search through binary tree for same exponent
                    If same exponent
                        set payload as the node's payload and set coeff and exponent to payload
                    Else
                        create new payload object and set coeef and exponent to new payload objec t
                    */
                    Node<Payload> EQnode = binaryTree.searchNode(binaryTree.getRoot(), exponent);
                    if(EQnode != null)   // If multiple terms in equation, combine into 1 term
                    {
                        data = EQnode.getData();
                        multipleTerms = true;
                        data.setCoefficient(data.getCoefficient() + coeff); // Add coefficients of same terms together 
                    }
                    else    // If no multiple terms in equation
                    {
                        data = new Payload(); // Make new payload to create new node with
                        data.setCoefficient(coeff); // Add coefficients of same terms together
                        data.setExponent(exponent);    // Add exponents of same terms together
                    }
                }
                // If not multipe terms
                if(!multipleTerms)
                {
                    // Insert node to binary tree in correct location
                    binaryTree.insert(data);
                }
            }   // END for loop to read through equation
            //binaryTree.deleteNode(binaryTree.getRoot(), 10);
            reverseInOrder(binaryTree, binaryTree.getRoot());
            //binaryTree.deleteNode(binaryTree.getRoot(), 3);
            if(boundaries) // If definite integral (there are boundaries)
            {
                /* 
                    FIRST CHECK
                    If upper and lower boundaries are the same (SUM = 0)
                        Print out boundaries and sum
                            **<,> <upper_bound>|<lower_bound> <=> 0**
                    Else
                    Make second recursive function right-most to left-most node
                    Initialize double sum = 0
                    Recusrively iterate through binary tree upper-boundary
                    Do the math for each node
                        **Integrated exponent is already part of binary tree due to first recursion to print integrated equation**
                        1) Math.pow(boundary, exponent)
                        2) Multiple result by (coefficient / exponent)
                            **Coefficient not changed from earlier when printing equation**
                        3) Sum += result
                    Recursively iterate through binary tree for lower-boundary
                    Do the math for each node
                        **Integrated exponent is already part of binary tree due to first recursion to print integrated equation**
                        1) Math.pow(boundary, exponent)
                        2) Multiple result by (coefficient / exponent)
                            **Coefficient not changed from earlier when printing equation**
                        3) Sum += result
                    Print out boundaries and sum
                        **Sum formatted to 3 decimal places**
                        **<,> <upper_bound>|<lower_bound> <=> sum**
                */
                if(upperBound == lowerBound)
                    System.out.print(", " + lowerBound + "|" + upperBound + " = " + 0);
                else
                {
                    double upperBoundSum = 0, lowerBoundSum = 0;
                    lowerBoundSum = reverseInOrderBounded(binaryTree.getRoot(), lowerBound);
                    upperBoundSum = reverseInOrderBounded(binaryTree.getRoot(), upperBound);
                    System.out.printf(", " + lowerBound + "|" + upperBound + " = " + "%.3f", upperBoundSum - lowerBoundSum);
                }
            }
            else // If indefinite integral (no boundaries)
            {
                if(coeff == 0)
                    System.out.print(0);
                System.out.print(" + C");
            }
            System.out.println();
            binaryTree.deleteTree();
        } // END while loop to read through whole file
        //binaryTree.deleteNode(binaryTree.getRoot(), 10);
        input.close();
        fileReader.close();
    }

    /**
     * This method calls reverseInorderRec() 
     * @param b binary tree to traverse
     * @param root root node of tree
    */
    public static void reverseInOrder(BinTree<Payload> b, Node<Payload> root)  { 
        reverseInOrderRec(b, root); 
    } 
    
    /**
     * This method calls reverseInorderRecBounded() 
     * @param root root node of tree
     * @param boundary boundary to calculate definite integral with
     * @return definite integral answer
     */
    public static double reverseInOrderBounded(Node<Payload> root, int boundary)  { 
        return reverseInOrderRecBounded(root, boundary); 
        
    } 

    /**
     * Traverses tree in reverse in order and prints unbounded integrated equation
     * @param b binary tree to traverse
     * @param root root of binary tree
     */
    public static void reverseInOrderRec(BinTree<Payload> b, Node<Payload> root) { 
         if (root != null) { 
            reverseInOrderRec(b, root.getRight());
            integrate(b, root);
            reverseInOrderRec(b, root.getLeft()); 
         } 
    }

    /**
     * Traverses tree in reverse in order and prints result to bounded integrated equation
     * @param root node to calculate
     * @param boundary boundary to calculate definite integral with
     * @return result of definite inegral
     */
    public static double reverseInOrderRecBounded(Node<Payload> root, int boundary) { 
        
        if (root == null)
           return 0;
        return (definiteIntegrate(root, boundary) + reverseInOrderRecBounded(root.getLeft(), boundary)
                + reverseInOrderRecBounded(root.getRight(), boundary));
    }
    
    /**
     * Prints integrated form of equation unbounded
     * @param tree tree to traverse
     * @param root node to integrate
     */
    public static void integrate(BinTree<Payload> tree, Node<Payload> root)
    {
        boolean firstNode = false;
        Node<Payload> rightMost = getRightMost(tree.getRoot());

        // Get right-most node to check if integrating first node in integral
        if(root.getData().getCoefficient() == rightMost.getData().getCoefficient())
        {
            if(root.getData().getExponent() == rightMost.getData().getExponent())
                firstNode = true;
        }

        int coefficient = root.getData().getCoefficient();  // Store coefficient
        int divisor = root.getData().getExponent(); // Store exponent to divide

        boolean divisible = false;
        root.getData().setExponent(++divisor);  // Increment exponent in integration
        divisor = root.getData().getExponent(); // Store number to divide coefficient by in integration

        if(coefficient == 0)    // If terms cancel or there are no terms
            System.out.print(0);
        else
        {
            if(!firstNode)
            {
                if(divisor > 0 && coefficient > 0) // Coefficient and exponent are positive
                    System.out.print(" + ");
                else if(divisor < 0 && coefficient < 0) // Coefficient and exponent are negative
                {    
                    System.out.print(" + ");
                    coefficient = Math.abs(coefficient);
                    
                }
                else if(divisor < 0 && coefficient > 0) // Coefficient is positive, exponent is negative
                {    
                    System.out.print(" - ");
                    coefficient = Math.abs(coefficient);
                    
                }
                else if(divisor > 0 && coefficient < 0) // Coefficient is negative, exponent is positive
                {    
                    System.out.print(" - ");
                    coefficient = Math.abs(coefficient);
                }
                else if(divisor == 0) // Exponent is 0 ---> ln(x)
                {
                    if(coefficient < 0) // Coefficient is positive
                        System.out.print(" - ");
                    else                // Coefficient is negative
                        System.out.print(" + ");
                }
                if(coefficient % divisor == 0) // If integrated coefficient is NOT a fraction
                {
                    coefficient = coefficient / divisor;
                    if(divisor < 0)   // If divisor is negative, change coefficient to positive to prevent printing double negative
                        coefficient = Math.abs(coefficient);
                    divisible = true;
                }
                if(divisor == 0)   // If ln(x)
                {
                    System.out.print("ln x");
                }
                else if(divisor == 1)  // Coefficient and x (no ^)
                {
                    if(coefficient != 1)    // Print out coefficient if it is not 1 (sign checking above accounts for -1 coefficient)
                        System.out.print(coefficient);
                    System.out.print("x");
                }
                else    // Coefficient, x, ^, and exponent
                {
                    if(divisible)
                    {
                        if(coefficient != 1)    // Print out coefficient if it is not 1 (sign checking above accounts for -1 coefficient)
                            System.out.print(coefficient);
                    }
                    else
                    {
                        // Pass in absolute value of divisor when simplifying to prevent double negative
                        System.out.print("(" + simplifyFraction(coefficient, Math.abs(divisor)) + ")");
                    }
                    System.out.print("x^" + divisor); // Divisor is same number as integrated exponent
                }
            }
            else
            {
                if(coefficient % divisor == 0)  // Simplify coefficient if it is reducable to integer from fraction
                {
                    coefficient = coefficient / divisor;
                    divisible = true;
                } 
                if(divisor == 0)   // If ln(x)
                {
                    System.out.print("lnx");
                }
                else if(divisor == 1)  // Coefficient and x (no ^)
                {
                    if(coefficient != 1)    // If coefficient greater than 1, need to display coefficient
                    {
                        if(coefficient == -1)   // Print minus sign if coefficient is -1 instead of -1 coefficient
                            System.out.print("-");
                        else
                            System.out.print(coefficient);
                    }
                    System.out.print("x");
                }
                else    // Coefficient, x, ^, and exponent
                {
                    if(divisible)
                    {
                        if(coefficient != 1)    // If coefficient greater than 1, need to display coefficient
                        {
                            if(coefficient == -1)   // Print minus sign if coefficient is -1 instead of -1 coefficient
                                System.out.print("-");
                            else
                                System.out.print(coefficient);
                        }   // Otherwise don't display a coefficient because it is 1
                    }
                    else
                        System.out.print("(" + simplifyFraction(coefficient, Math.abs(divisor)) + ")");
                    System.out.print("x^" + divisor); // Divisor is same number as integrated exponent
                }
            }
        }
    }

    /**
     * Calculates result of single node with boundary after integration
     * @param root node to calculate
     * @param boundary boundary to use with calculation
     * @return result of single node integrated with boundary
     */
    public static double definiteIntegrate(Node<Payload> root, int boundary)
    {
        double sum = 0;
        sum = Math.pow(boundary, root.getData().getExponent());
        sum *= ((double)root.getData().getCoefficient() / root.getData().getExponent());
        return sum;
    }

    /**
     * Gets the right-most node of tree
     * @param root root of tree
     * @return right-most node of tree
     */
    public static Node<Payload> getRightMost(Node<Payload> root)
    {
        if(root == null)
            return null;
        if(root.getRight() == null)
            return root;
        return getRightMost(root.getRight());
    }

    /**
     * Returns greatest common denominator (same as greatest common multiple)
     * @param a numerator
     * @param b denominator
     * @return greatest common denominator/multiple
     */
    public static int gcm(int a, int b)
    {
        return b == 0 ? a: gcm(b, a % b);
    }

    /**
     * Returns string of simplified fraction
     * @param a numerator
     * @param b denominator
     * @return simplified fraction
     */
    public static String simplifyFraction(int a, int b)
    {
        int gcm = gcm(a,b);
        //If denominator is negative, display absolute value of denominator and display minus sign in equation
        if(gcm < 0)
            gcm = Math.abs(gcm);
        return (a / gcm) + "/" + (b / gcm);
    }
}
