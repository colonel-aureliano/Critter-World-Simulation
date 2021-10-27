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
     * You can remove this method if you don't like it.
     *
     * Sets the parent of this {@code Node}.
     *
     * @param p the node to set as this {@code Node}'s parent.
     */
    public boolean setParent(Node p) {
        if(!((AbstractNode) p).hasChild){
            return false; // e.g. 3 cannot be the parent of anything.
        }

        try {
            String oldParent = this.getParent().get().getClass().getSimpleName();

            AbstractNode n = (AbstractNode) this.getParent().get();
            int i = n.children.indexOf(this);
            n.children.remove(i);
            n.children.add(i,p);

            int count = ((AbstractNode) p).children.size();
            ((AbstractNode) p).children.clear();
            ((AbstractNode) p).children.add(this);
            count--;
            if(count==0) {return true;}

            Random rand = new Random();
            int s = root.size();

            int t = -1;
            // t represents what nodes are needed to fill up children nodes in inserted node
            // 0: Expr
            // 1: Condition
            // 2: Command
            // 3: Rule

            String newParent = p.getClass().getSimpleName();

            switch(oldParent){
                case "Mem":
                case "Sensor":
                case "Relation":
                    t = 0;
                    break;
                case "BinaryCondition":
                    t = 1;
                    break;
                case "Command:":
                    t = 2;
                    break;
                case "Rule":
                    if(oldParent.equals(newParent)){
                        if (this.getClass().getSimpleName()=="Command"){
                            t=1;
                        }
                        else{
                            t=2;
                        }
                    }
                    else{
                        t=1;
                    }
                    break;
                case "ProgramImpl":
                    t=3;
            }

            while (count > 0) {
                try{
                    int index = rand.nextInt(s);

                    switch(t) {
                        case 0:
                            if (!(root.nodeAt(index) instanceof Expr)) { continue;}
                            break;
                        case 1:
                            if (!(root.nodeAt(index) instanceof Condition)) { continue;}
                            break;
                        case 2:
                            if (!(root.nodeAt(index) instanceof Command)) { continue;}
                            break;
                        case 3:
                            if (!(root.nodeAt(index) instanceof Rule)) { continue;}
                            break;
                    }
                    if(root.nodeAt(index)==p){
                        continue; // the child of a node cannot be itself
                    }
                    ((AbstractNode) p).children.add(root.nodeAt(index));
                    count--;
                } catch (Exception e){
                    continue;
                }
            }
            return true;
        } catch (NoMaybeValue noMaybeValue) {
            return false;
        }
    }


    /**
     * @return the String representation of the tree rooted at this {@code Node}.
     */
    public abstract String toString();

}
