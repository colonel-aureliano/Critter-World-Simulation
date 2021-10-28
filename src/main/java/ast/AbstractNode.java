package ast;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import javax.management.relation.RelationNotFoundException;

public abstract class AbstractNode implements Node {

    protected static Node root;
    protected List<Node> children;
    protected boolean hasChild = true; // indicates whether the Node has child

    public static Node getRoot(){
        return root;
    }

    protected void setRoot(Node n){
        root = n;
    }

    public AbstractNode(){
        hasChild = false;
    }

    public AbstractNode(Node s){
        children = new ArrayList<>(Arrays.asList(s));
    }

    public AbstractNode(Node l, Node r){
        children = new ArrayList<>(Arrays.asList(l,r));
    }

    public AbstractNode(List<Node> c) {
        children = c;
    }

    @Override
    public final int size() {
        int t = 0;
        if (hasChild) {
            for(Node n: children){
                t+=n.size();
            }
        }
        return t+1;
    }

    @Override
    public Node nodeAt(int index) {
        if (index == 0) {
            return this;
        }
        int t = 1;
        for(Node n: children){
            try{
                return (n.nodeAt(index-t));
            }
            catch (Exception e){
                t += n.size();
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        sb.append(this);
        return sb;
    }

    @Override
    public abstract Node clone();

    @Override
    public List<Node> getChildren() {
        if(hasChild) {
            return children;
        }
        throw new IllegalArgumentException("getChildren() called on a leaf node.");
    }

    /**
     * Returns the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     *
     * @return the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     * Requires: this must exist in the AST of root.
     *
     * This method does not need to be implemented and may be removed from the interface.
     */
    public Maybe<Node> getParent() {
        if(this == root){
            return Maybe.none();
        }
        else{
            Node n;
            for (int i = 0; i < root.size(); i++){
                n = root.nodeAt(i);
                try{
                    if(n.getChildren().contains(this)){
                        return Maybe.some(n);
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            throw new IllegalArgumentException("getParent() error.");
        }
    }

    /**
     * @return the String representation of the tree rooted at this {@code Node}.
     */
    public abstract String toString();

}
