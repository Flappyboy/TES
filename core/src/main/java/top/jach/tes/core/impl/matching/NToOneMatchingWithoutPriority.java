package top.jach.tes.core.impl.matching;

import java.util.*;

/**
 * 无优先级代表会遍历所有策略，把所有匹配都算上
 * @param <N>
 * @param <M>
 */
public class NToOneMatchingWithoutPriority<N,M> implements NToOneMatchingStrategy<N,M> {
    Set<NToOneMatchingStrategy<N,M>> strategys = new LinkedHashSet<>();
    public void register(NToOneMatchingStrategy<N,M>... strategy){
        strategys.addAll(Arrays.asList(strategy));
    }

    @Override
    public M NToM(N n) {
        M result = null;
        for (NToOneMatchingStrategy<N,M> strategy: strategys) {
            M m = strategy.NToM(n);
            if(m != null){
                result = m;
            }
        }
        return result;
    }

    @Override
    public Set<N> MToN(M m) {
        Set<N> allNs = new LinkedHashSet<>();
        for (NToOneMatchingStrategy<N,M> strategy: strategys) {
            Set<N> ns = strategy.MToN(m);
            allNs.addAll(ns);
        }
        return allNs;
    }
}
