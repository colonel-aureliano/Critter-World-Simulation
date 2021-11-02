package model;

import ast.Node;
import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
import cms.util.maybe.Maybe;

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
        //((ProgramImpl) p).provideCritter(this);
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
            if(evaluate(r)){

            }
        }
    }

    private boolean evaluate(Rule r){
        // evaluates a rule
        return false; //TODO
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
