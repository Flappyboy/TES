package top.jach.tes.core.impl.matching;

import java.util.Set;

/**
 * 优先级代表会返回所遍历的最后一个匹配的策略
 * 注意，只要不为null,及时size为0也会算作是匹配成功
 * @param <N>
 * @param <M>
 */
public class NToMMatchingWithPriority<N,M> extends NToMMatchingWithoutPriority<N, M> {

    @Override
    public Set<M> NToM(N n) {
        Set<M> result = null;
        for (NToMMatchingStrategy<N,M> strategy: strategys) {
            Set<M> ms = strategy.NToM(n);
            if(ms!=null){
                result = ms;
            }
        }
        return result;
    }

    @Override
    public Set<N> MToN(M m) {
        Set<N> result = null;
        for (NToMMatchingStrategy<N,M> strategy: strategys) {
            Set<N> ns = strategy.MToN(m);
            if(ns!=null){
                result = ns;
            }
        }
        return result;
    }
}
