package top.jach.tes.core.impl.matching;

import java.util.*;

/**
 *
 * @param <N>
 * @param <M>
 */
public class OneToOneMatching<N,M> implements OneToOneMatchingStrategy<N,M> {

    Set<OneToOneMatchingStrategy<N,M>> strategys = new LinkedHashSet<>();
    public void register(OneToOneMatchingStrategy<N,M>... strategy){
        strategys.addAll(Arrays.asList(strategy));
    }

    @Override
    public M NToM(N n) {
        M result = null;
        for (OneToOneMatchingStrategy<N,M> strategy: strategys) {
            M m = strategy.NToM(n);
            if(m != null){
                result = m;
            }
        }
        return result;
    }

    @Override
    public N MToN(M m) {
        N result = null;
        for (OneToOneMatchingStrategy<N,M> strategy: strategys) {
            N n = strategy.MToN(m);
            if(n != null){
                result = n;
            }
        }
        return result;
    }

}
