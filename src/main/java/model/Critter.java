package model;

import ast.*;
import cms.util.maybe.Maybe;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Critter implements ReadOnlyCritter {

    String name;
    int[] mem; // length >= 7, each entry >= 0
    Program program;

    /**
     * Constructs a Critter object.
     * @param n species name
     * @param arr mem values
     * @param p program rules
     */
    public Critter(String n, int[] arr, Program p){
        name=n;
        mem=arr;
        program=p;
        if(!classInv()){
            System.out.println("WARNING: critter created with invalid values, will reset invalid values to default.");
            setDefault();
        }
        ((ProgramImpl) p).critterToObserve(this);
    }

    private void setDefault(){
        for(int i = 0; i < mem.length; i++){
            if(i==0 && mem[i]<7){
                System.out.println("Resetting mem[0] to 7.");
            }
            else if(i==6 && (mem[i]<0 || mem[i]>99)){
                Random r = new Random();
                mem[i]=r.nextInt(100);
                System.out.println("Resetting mem[6] to a random value: "+mem[i]);
            }
            else if(i!=6 &&mem[i]<1){
                mem[i]=1;
                System.out.println("Resetting mem["+i+"] to 1.");
            }

        }
    }

    private boolean classInv(){
        if(mem.length>=7 && mem[0]>=7 && mem[1]>=1 && mem[2]>=1
                && mem[3]>=1 && mem[4]>=1 && mem[5]>=1 && (mem[6]>=0 && mem[6]<=99) ){
            if(name!=null && program!=null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advances the critter by one time step.
     */
    public void step(){
        List<Node> l = program.getChildren();
        for(Node i: l){
            Rule r = (Rule) i;
            if(r.value()){
                // Condition for rule is true. Should perform update/action.
                Node c = r.getChildren().get(1); // Command node
                for(Node n: c.getChildren()){
                    if(n.getCategory()== NodeCategory.ACTION){
                        //TODO perform action
                        return;
                    }
                    // n is an instance of Update, with children l,r, represents mem[l] := r
                    try{
                        mem[((Expr)n.getChildren().get(0)).value()]=((Expr)n.getChildren().get(1)).value();
                    } catch (IndexOutOfBoundsException e){
                        // do not perform update
                    }
                }
            }
        } //TODO perform the process for up to 999 times if no ACTION is found
    }

    @Override
    public String getSpecies() {
        return name;
    }

    @Override
    public int[] getMemory() {
        return mem.clone();
    }

    @Override
    public String getProgramString() {
        return program.toString();
    }

    @Override
    public Maybe<String> getLastRuleString() {
        return null;
    }
}
