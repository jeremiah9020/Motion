package me.jabberjerry.motion.util;

public class Quadruple<A,B,C,D>  {

    public final A first;
    public final B second;
    public final C third;
    public final D fourth;

    public Quadruple(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quadruple)) {
            return false;
        }
        Quadruple<?, ?, ?, ?> p = (Quadruple<?, ?, ?, ?>) o;
        return first.equals(p.first) && second.equals(p.second) && third.equals(p.third) && fourth.equals(p.fourth);
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode()) ^ (third == null ? 0 : third.hashCode()) ^ (fourth == null ? 0 : fourth.hashCode());
    }

    public static <A,B,C,D> Quadruple <A, B, C, D> create(A a, B b, C c, D d) {
        return new Quadruple<>(a,b,c,d);
    }
}