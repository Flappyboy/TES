package top.jach.tes.core.matching;
import java.util.*;

/**
 * 无优先级代表会遍历所有策略，把所有匹配都算上
 * @param <N>
 * @param <M>
 */
public class NToMMatchingWithoutPriority<N,M> implements NToMMatchingStrategy<N,M> {
    Set<NToMMatchingStrategy<N,M>> strategys = new LinkedHashSet<>();
    public void register(NToMMatchingStrategy<N,M>... strategy){
        strategys.addAll(Arrays.asList(strategy));
    }

    @Override
    public Set<M> NToM(N n) {
        Set<M> allMs = new LinkedHashSet<>();
        for (NToMMatchingStrategy<N,M> strategy: strategys) {
            Set<M> ms = strategy.NToM(n);
            allMs.addAll(ms);
        }
        return allMs;
    }

    @Override
    public Set<N> MToN(M m) {
        Set<N> allNs = new LinkedHashSet<>();
        for (NToMMatchingStrategy<N,M> strategy: strategys) {
            Set<N> ns = strategy.MToN(m);
            allNs.addAll(ns);
        }
        return allNs;
    }
}
