package top.jach.tes.core.matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultNToOneMatchingStrategy<N,M> implements NToOneMatchingStrategy<N,M> {

    Map<N, M> N2MMap = new HashMap<>();
    Map<M, Set<N>> M2NMap = new HashMap<>();

    @Override
    public M NToM(N n) {
        return N2MMap.get(n);
    }

    @Override
    public Set<N> MToN(M m) {
        Set<N> ns = M2NMap.get(m);
        if(ns == null){
            ns = new HashSet<>();
            M2NMap.put(m, ns);
        }
        return ns;
    }

    void link(N n, M m){
        N2MMap.put(n,m);
        MToN(m).add(n);
    }
}
