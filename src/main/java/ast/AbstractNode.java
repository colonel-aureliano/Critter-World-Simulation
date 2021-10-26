package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cms.util.maybe.Maybe;

public abstract class AbstractNode implements Node {

    protected List<Node> children;
    protected Node left;
    protected Node right;
    protected Node single;
    private int which;
    // which = 0: leaf node, no children
    // 1: one children
    // 2: two children
    // 3: more than two children

    public AbstractNode(){
        which = 0;
    }

    public AbstractNode(Node s){
        single=s;
        which = 1;
    }

    public AbstractNode(Node l, Node r){
        left=l;
        right=r;
        which = 2;
    }

    public AbstractNode(List<Node> c) {
        children = c;
        which = 3;
    }

    @Override
    public final int size() {
        int t = 0;
        switch(which){
            case 0:
                return 1;
            case 1:
                t+=single.size();
                break;
            case 2:
                t+=left.size();
                t+=right.size();
                break;
            case 3:
                for(Node n: children){
                    t+=n.size();
                }
                break;
        }
        return t+1;
    }

    @Override
    public Node nodeAt(int index) {
        if(index==0){
            return this;
        }
        switch(which){
            case 0:
                break;
            case 1:
                return single.nodeAt(index-1);
            case 2:
                try{
                    return left.nodeAt(index-1);
                }
                catch (Exception e){
                    return right.nodeAt(index-1-left.size());
                }
            case 3:
                int t = 1;
                for(Node n: children){
                    try{
                        return (n.nodeAt(index-t));
                    }
                    catch (Exception e){
                        t+=n.size();
                    }
                }
                break;
        }
        throw new IllegalArgumentException("nodeAt() called with illegal index.");
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(this);
        return sb;
    }

    @Override
    public Node clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Node> getChildren() {
        switch(which){
            case 1:
                return new ArrayList<>(Arrays.asList(single));
            case 2:
                return new ArrayList<>(Arrays.asList(left,right));
            case 3:
                return children;
        }
        throw new IllegalArgumentException("getChildren() called on a leaf node.");
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
