package top.jach.tes.core.impl.matching;

import java.util.Set;

public interface NToOneMatchingStrategy<N,M> {

    M NToM(N n);

    Set<N> MToN(M m);
}
