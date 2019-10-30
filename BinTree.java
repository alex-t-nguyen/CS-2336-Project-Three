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
    public void deleteNode(Node<T> n)
    {

    }

    /**
     * Deletes entire binary tree
     */
    public void deleteTree()
    {
        root = null;
    }
    
    /*
    // This method mainly calls InorderRec() 
    public void inorder() 
    { 
        inorderRec(this.root); 
    } 
   
     // A utility function to do inorder traversal of BST 
    public void inorderRec(Node<T> root) 
    { 
         if (root != null) { 
             inorderRec(root.getLeft()); 
             System.out.println(root.getData().getExponent()); 
             inorderRec(root.getRight()); 
         } 
    } 
    */
}
