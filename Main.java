// Alex Nguyen
// atn170001

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

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
                else if(equation.charAt(i) == '-' && !Character.isDigit(equation.charAt(i - 1)))
                {
                    n += equation.substring(i, i + 1);
                    if(equation.charAt(i + 1) == ' ')
                        i++;
                }
                else if(!(n.equals("") && !(n.equals("-"))))
                {
                    nodes[arrayIndex] = n; // Put equation node into array
                    n = ""; // Reset string
                    arrayIndex++; // Increment array index of equation nodes
                    if(equation.charAt(i) == '-')
                        n += equation.substring(i, i + 1);
                }
            }

            int coeff, exponent;
            for(int i = 0; nodes[i] != null; i++) // Go up to nodes.length because dx is not actually added to array
            { 
                coeff = 0;
                exponent = 0;
                int xIndex = nodes[i].indexOf('x');
                int carrotIndex = nodes[i].indexOf('^');
                if(xIndex != -1) // If equation substring has x value
                {
                    if(xIndex == 0) // If no coefficient
                        coeff = 1;  // coefficient equals 1
                    else
                        coeff = Integer.parseInt(nodes[i].substring(0, xIndex));    // Get coefficient of equation substring
                    if(carrotIndex == -1) // If x^1 == x
                        exponent = 1;
                    else
                        exponent = Integer.parseInt(nodes[i].substring(carrotIndex + 1));   // Get exponent of equation substring
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
                    }
                    else    // If no multiple terms in equation
                    {
                        data = new Payload(); // Make new payload to create new node with
                    }
                    data.setCoefficient(data.getCoefficient() + coeff); // Add coefficients of same terms together
                    data.setExponent(data.getExponent() + exponent);    // Add exponents of same terms together
                }
                // If not multipe terms
                if(!multipleTerms)
                {
                    // Insert node to binary tree in correct location
                    binaryTree.insert(data);
                }
            }   // END for loop to read through equation
            reverseInOrder(binaryTree, binaryTree.getRoot());
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
                    lowerBoundSum = reverseInOrderBounded(binaryTree.getRoot(), lowerBound, lowerBoundSum);
                    upperBoundSum = reverseInOrderBounded(binaryTree.getRoot(), upperBound, upperBoundSum);
                    if(upperBoundSum - lowerBoundSum == Math.floor(upperBoundSum - lowerBoundSum))
                        System.out.printf(", " + lowerBound + "|" + upperBound + " = " + ((int) (upperBoundSum - lowerBoundSum)));
                    else
                        System.out.printf(", " + lowerBound + "|" + upperBound + " = " + "%.3f", upperBoundSum - lowerBoundSum);
                }
            }
            else // If indefinite integral (no boundaries)
            {
                System.out.print(" + C");
            }
            System.out.println();
            binaryTree.deleteTree();
            /*
                Create another for loop to go through the binary tree (right most node to left most node -> highest to lowest exponent)
                Acecss each node data while iterating through tree
                Integrate the data and display integrated equation
                If there was a boundary (pipeline character)
                    Display the format for pipeline (integrated equation and definite integral answer)
                Else
                    Display the format for no pipeline (integrated equation with + C)
            */
            /* ---------------------NOTES------------------------ */
            /* If coefficient is negative display absolute value of coefficient and display minus sign in equation
                Make specific answer node displays if exponent is special (ie. -1 --> ln())   
            */
        } // END while loop to read through whole file

        input.close();
        fileReader.close();
    }

    // This method calls reverseInorderRec() 
    public static void reverseInOrder(BinTree<Payload> b, Node<Payload> root)  { 
        reverseInOrderRec(b, root); 
    } 
    
    // This method calls reverseInorderRecBounded() 
    public static double reverseInOrderBounded(Node<Payload> root, int boundary, double sum)  { 
        return reverseInOrderIteratedBounded(root, boundary, sum); 
    } 

    // A utility function to do revsrse in order traversal of BST and integrate without bounds
    public static void reverseInOrderRec(BinTree<Payload> b, Node<Payload> root) { 
         if (root != null) { 
            reverseInOrderRec(b, root.getRight()); 
            integrate(b, root);
            reverseInOrderRec(b, root.getLeft()); 
         } 
    }

    // A utility function to do reverse in order traversal of BST and integrate with bounds
    public static double reverseInOrderIteratedBounded(Node<Payload> root, int boundary, double sum) { 
        
        if (root == null)
        { 
           return 0;
        }
        Stack<Node<Payload>> stack = new Stack<Node<Payload>>(); 
  
        // traverse the tree 
        while (root != null || stack.size() > 0) 
        { 
  
            /* Reach the right most Node of the 
            root Node */
            while (root !=  null) 
            { 
                /* place pointer to a tree node on 
                   the stack before traversing 
                  the node's right subtree */
                stack.push(root);   // Puts right subtree onto stack initially, then adds left subtree when it is not null
                root = root.getRight(); 
            } 
  
            /* Root must be NULL at this point */
            root = stack.pop(); 
  
            sum += definiteIntegrate(root, boundary);
  
            /*  After visiting the node and it's right subtree,
                next visit the left subtree */
            root = root.getLeft(); 
        }
        return sum; 
    }
     
    public static void integrate(BinTree<Payload> tree, Node<Payload> root)
    {
        /*  Recur all the way to right most node
            Check if param root is equal to that node (exponent and coeff)
            If equal
                Set firstNode = true;
            else
                Set firstNode = false;
        */
        boolean firstNode = false;
        Node<Payload> rightMost = getRightMost(tree.getRoot());
        //Node<Payload> leftMost = getLeftMost(tree.getRoot());
        //System.out.println("B: " + rightMost.getData().getExponent());
        if(root.getData().getCoefficient() == rightMost.getData().getCoefficient())
        {
            if(root.getData().getExponent() == rightMost.getData().getExponent())
                firstNode = true;
        }

        int coefficient = root.getData().getCoefficient();
        int divisor = root.getData().getExponent();

        boolean divisible = false;
        root.getData().setExponent(++divisor);
        divisor = root.getData().getExponent();

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
                System.out.print("lnx");
            }
            else if(divisor == 1)  // Coefficient and x (no ^)
            {
                if(coefficient != 1)
                    System.out.print(coefficient);
                System.out.print("x");
            }
            else    // Coefficient, x, ^, and exponent
            {
                if(divisible)
                {
                    if(coefficient != 1)
                        System.out.print(coefficient);
                }
                else
                    System.out.print("(" + coefficient + "/" + divisor + ")");
                System.out.print("x^" + divisor); // Divisor is same number as integrated exponent
            }
        }
        else
        {
            if(coefficient % divisor == 0)
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
                if(coefficient != 1)
                    System.out.print(coefficient);
                System.out.print("x");
            }
            else    // Coefficient, x, ^, and exponent
            {
                if(divisible)
                {
                    if(coefficient != 1)
                        System.out.print(coefficient);
                }
                else
                    System.out.print("(" + coefficient + "/" + divisor + ")");
                System.out.print("x^" + divisor); // Divisor is same number as integrated exponent
            }
        }
        /*if(root.getData().getCoefficient() == leftMost.getData().getCoefficient())
        {
            if(root.getData().getExponent() == leftMost.getData().getExponent())
                System.out.print(" + C");
        }*/
        
    }

    public static double definiteIntegrate(Node<Payload> root, int boundary)
    {
        // MATH isn't working, could be that coefficient isn't negative when it should be from after integrating equation
        double sum = 0;
        sum = Math.pow(boundary, root.getData().getExponent());
        //System.out.println("sum: " + sum);
        sum *= ((double)root.getData().getCoefficient() / root.getData().getExponent());
        //sum *= sum2;
        //System.out.print("sum: " + sum);
        //System.out.println("sum2: " + (double)root.getData().getCoefficient() / root.getData().getExponent());
        return sum;
    }

    public static Node<Payload> getRightMost(Node<Payload> root)
    {
        if(root == null)
            return null;
        if(root.getRight() == null)
            return root;
        return getRightMost(root.getRight());
    }

    public static Node<Payload> getLeftMost(Node<Payload> root)
    {
        if(root == null)
            return null;
        if(root.getLeft() == null)
            return root;
        return getLeftMost(root.getLeft());
    }
}
