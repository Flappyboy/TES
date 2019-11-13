package top.jach.tes.core.matching;

import java.util.Set;

public interface NToOneMatchingStrategy<N,M> {

    M NToM(N n);

    Set<N> MToN(M m);
}
