package top.jach.tes.core.matching;

import java.util.Map;
import java.util.Set;

/**
 * 无优先级代表会遍历所有策略，把所有匹配都算上
 * @param <N>
 * @param <M>
 */
public class NToOneMatchingWithPriority<N,M> extends NToOneMatchingWithoutPriority<N, M> {

    @Override
    public Set<N> MToN(M m) {
        Set<N> result = null;
        for (NToOneMatchingStrategy<N,M> strategy: strategys) {
            Set<N> ns = strategy.MToN(m);
            if(ns!=null){
                result = ns;
            }
        }
        return result;
    }
}
