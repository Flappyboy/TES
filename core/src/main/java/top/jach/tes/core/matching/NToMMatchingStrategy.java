package top.jach.tes.core.matching;

import java.util.Set;

public interface NToMMatchingStrategy<N,M> {

    Set<M> NToM(N n);

    Set<N> MToN(M m);
}
