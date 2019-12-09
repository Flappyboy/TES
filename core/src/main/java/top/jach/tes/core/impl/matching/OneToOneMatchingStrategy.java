package top.jach.tes.core.impl.matching;

public interface OneToOneMatchingStrategy<N,M> {

    M NToM(N n);

    N MToN(M m);
}
