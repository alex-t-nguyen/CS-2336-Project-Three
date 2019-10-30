// Alex Nguyen
// atn170001

public class Payload {

    // DATA MEMBERS

    private int coefficient;
    private int exponent;

    // CONSTRUCTORS
    
    /**
     * Default constructor
     */
    public Payload()
    {
        coefficient = 0;
        exponent = 0;
    }

    /**
     * Overloaded Constructor
     * @param coeff coefficient
     * @param exp exponent
     */
    public Payload(int coeff, int exp)
    {
        coefficient = coeff;
        exponent = exp;
    }

    // ACCESSORS

    /**
     * Returns coefficient
     * @return coefficient
     */
    public int getCoefficient()
    {
        return coefficient;
    }

    /**
     * Returns exponent
     * @return exponent
     */
    public int getExponent()
    {
        return exponent;
    }

    // MUTATORS

    /**
     * Sets coefficient
     * @param coeff coefficient
     */
    public void setCoefficient(int coeff)
    {
        coefficient = coeff;
    }

    /**
     * Sets exponent
     * @param exp exponent
     */
    public void setExponent(int exp)
    {
        exponent = exp;
    }
}