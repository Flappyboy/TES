package top.jach.tes.core.impl.matching;

import java.util.Set;

public interface NToMMatchingStrategy<N,M> {

    Set<M> NToM(N n);

    Set<N> MToN(M m);
}
