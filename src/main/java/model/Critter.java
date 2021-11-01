package model;

import cms.util.maybe.Maybe;

import java.util.Arrays;

public class Critter implements ReadOnlyCritter {

    String name;
    int[] mem; // length >= 7, each entry >= 0

    public Critter(String n, int[] arr){
        name=n;
        mem=arr;
        if(!classInv()){
            System.out.println("WARNING: critter created with invalid values.");
            //setDefault();
        }
    }

    /*private void setDefault(){
        int[] t;
        if(mem.length<7){
            t=mem;

        }
    }*/

    private boolean classInv(){
        return mem.length>=7 && Arrays.stream(mem).allMatch(x -> x>=0);
    }

    @Override
    public String getSpecies() {
        return null;
    }

    @Override
    public int[] getMemory() {
        return mem.clone();
    }

    @Override
    public String getProgramString() {
        return null;
    }

    @Override
    public Maybe<String> getLastRuleString() {
        return null;
    }
}
