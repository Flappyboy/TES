package top.jach.tes.core.impl.matching;

import java.util.HashMap;
import java.util.Map;

public class DefaultOneToOneMatchingStrategy<N,M> implements OneToOneMatchingStrategy<N,M>, LinkNM<N, M> {

    Map<N, M> N2MMap = new HashMap<>();
    Map<M, N> M2NMap = new HashMap<>();

    @Override
    public M NToM(N n) {
        return N2MMap.get(n);
    }

    @Override
    public N MToN(M m) {
        return M2NMap.get(m);
    }


    public DefaultOneToOneMatchingStrategy link(N n, M m){
        N2MMap.put(n,m);
        M2NMap.put(m,n);
        return this;
    }
}
