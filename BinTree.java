// Alex Nguyen
// atn170001

public class BinTree<T> {

    // DATA MEMBERS

    private Node<T> root;

    // CONSTRUCTORS

    /**
     * Default constructor
     */
    public BinTree()
    {
        root = null;
    }

    /** 
     * Overloaded constructor
     * @param r root node of binary tree
     */
    public BinTree(Node<T> r)
    {
        root = r;
    }
    
    // ACCESSORS
    
    public Node<T> getRoot()
    {
        return root;
    }
    
    // FUNCTIONS

    /**
     * Calls insertNode
     * @param data payload to insert into binary tree
     */
    public void insert(Payload data)
    {
        root = insertNode(root, data);
    }

    /**
     * Recursively insert node into binary tree
     * @param iter iterating node
     * @param newNode node to insert
     * @return inserted node
     */
    public Node<T> insertNode(Node<T> iter, Payload newNode)
    {
        /* If the tree is empty assign root to new node */
        if(iter == null)
        {
            iter = new Node<>(newNode);
            return iter;
        }
        
        /* Otherwise ,recur down the tree */
        if(newNode.getExponent() < iter.getData().getExponent())
        {
            iter.setLeft(insertNode(iter.getLeft(), newNode));
        }
        else if(newNode.getExponent() > iter.getData().getExponent())
        {
            iter.setRight(insertNode(iter.getRight(), newNode));
        }

        /* Return unchanged node pointer */
        return iter;
    }

    /**
     * Searches for node in binary tree
     * @param root iterating node to compare to
     * @param exponent node with exponent to search for
     * @return node searched for
     */
    public Node<T> searchNode(Node<T> root, int exponent)
    {
        // Base cases: if root is null or exponent is present at root
        if(root == null || root.getData().getExponent() == exponent)
            return root;

        // Exponent is less than root's epxonent
        if(root.getData().getExponent() > exponent)
            return searchNode(root.getLeft(), exponent);

        // Exponent is greater than root's exponent
        return searchNode(root.getRight(), exponent);
    }

    /** 
     * Deletes node in binary tree
     * @param n node to delete
     */
    public Node<T> deleteNode(Node<T> n, int exponent)
    {

        // Base case if tree is empty
        // Also if node to delete is not in tree
        if(n == null)
            return n;
        
        // Recur down the tree
        // Goes until node to delete is found or null (if node is not in tree)
        if(exponent < n.getData().getExponent())
            n.setLeft(deleteNode(n.getLeft(), exponent));
        else if(exponent > n.getData().getExponent())
            n.setRight(deleteNode(n.getRight(), exponent));
        else
        {
            // Node with only 1 child or no child
            // Returns node to store child data in parent node --> child becomes its own parent
            if(n.getLeft() == null)
                return n.getRight();
            else if(n.getRight() == null)
                return n.getLeft();
            
            // Node with 2 children
            // Get smallest node in node to delete's right subtree
            n.getData().setExponent(getMinValue(n.getRight()));

            // Delete the smallest node in node to delete's right subtree
            n.setRight(deleteNode(n.getRight(), n.getData().getExponent()));
        }
        return n;
    }

    /**
     * Returns the smallest value
     * @param root node to check value of
     * @return lowest exponent in branch
     */
    public int getMinValue(Node<T> root)
    {
        if(root == null)
            return 0;
        if(root.getLeft() == null)
            return root.getData().getExponent();
        return getMinValue(root.getLeft());
    }

    /**
     * Deletes entire binary tree
     */
    public void deleteTree()
    {
        root = null;
    }
}
