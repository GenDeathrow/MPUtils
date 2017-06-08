package stanhebben.zenscript.parser;

/**
 * This interface represents an iterator over integer values.
 *
 * @author Stan Hebben
 */
public interface IteratorI {

    /**
     * Returns true if the iteration has more elements.
     *
     * @return true if the iteration has more elements
     */
    boolean hasNext();

    /**
     * Returns the next element in this iteration.
     *
     * @return the next element in this iteration
     */
    int next();
}
