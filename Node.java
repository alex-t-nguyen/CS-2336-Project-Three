// Alex Nguyen
// atn170001

public class Node<T> {

    // DATA MEMBERS

    private Payload data;
    private Node<T> left;
    private Node<T> right;

    // CONSTRUCTORS
    
    /**
     * Default constructor
     */
    public Node()
    {
        data = null;
        left = null;
        right = null;
    }

    /**
     * Overloaded Constructor for leaves
     * @param p payload
     * @param l left node
     * @param r right node
     */
    public Node(Payload p)
    {
        data = p;
        left = null;
        right = null;
    }

    // ACCESSORS
    
    /**
     * Returns left node
     * @return left node
     */
    public Node<T> getLeft()
    {
        return left;
    }

    /**
     * Returns right node
     * @return right node
     */
    public Node<T> getRight()
    {
        return right;
    }

    /**
     * Returns data
     * @return data
     */
    public Payload getData()
    {
        return data;
    }

    // MUTATORS

    /**
     * Sets left node
     * @param l left node
     */
    public void setLeft(Node<T> l)
    {
        left = l;
    }

    /**
     * Sets right node
     * @param p right node
     */
    public void setRight(Node<T> r)
    {
        right = r;
    }

    /**
     * Sets data
     * @param p data
     */
    public void setData(Payload p)
    {
        data = p;
    }

}