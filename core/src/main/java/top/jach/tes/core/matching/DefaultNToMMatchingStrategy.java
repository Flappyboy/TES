package top.jach.tes.core.matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultNToMMatchingStrategy<N,M> implements NToMMatchingStrategy<N,M> {

    Map<N, Set<M>> N2MMap = new HashMap<>();
    Map<M, Set<N>> M2NMap = new HashMap<>();

    @Override
    public Set<M> NToM(N n) {
        Set<M> ms = N2MMap.get(n);
        if(ms == null){
            ms = new HashSet<>();
            N2MMap.put(n, ms);
        }
        return ms;
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
        NToM(n).add(m);
        MToN(m).add(n);
    }
}
