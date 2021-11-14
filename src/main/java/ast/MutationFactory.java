package ast;

/** A factory that produces the public static Mutation objects corresponding to each mutation */
public class MutationFactory {
    public static Mutation getRemove() {
        return new Mutation1();
    }

    public static Mutation getSwap() {
        return new Mutation2();
    }

    public static Mutation getReplace() {
        return new Mutation3();
    }

    public static Mutation getTransform() {
        return new Mutation4();
    }

    public static Mutation getInsert() {
        return new Mutation5();
    }

    public static Mutation getDuplicate() {
        return new Mutation6();
    }
}
