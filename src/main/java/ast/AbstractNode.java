package ast;

import java.util.List;
import cms.util.maybe.Maybe;

public abstract class AbstractNode implements Node {

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Node nodeAt(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Node> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     *
     * @return the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     *
     * This method does not need to be implemented and may be removed from the interface.
     */
    public Maybe<Node> getParent() {
        // TODO Auto-generated method stub
        return Maybe.none();
    }

    /**
     * You can remove this method if you don't like it.
     *
     * Sets the parent of this {@code Node}.
     *
     * @param p the node to set as this {@code Node}'s parent.
     */
    public void setParent(Node p) {
        // TODO Auto-generated method stub
    }

    /**
     * @return the String representation of the tree rooted at this {@code Node}.
     */
    public abstract String toString();

}
