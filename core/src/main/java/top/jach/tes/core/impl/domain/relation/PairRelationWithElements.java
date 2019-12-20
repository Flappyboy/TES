package top.jach.tes.core.impl.domain.relation;

import top.jach.tes.core.impl.domain.element.Element;

public class PairRelationWithElements<S extends Element, T extends Element> extends PairRelation {
    S sourceElement;
    T targetElement;

    public static <S extends Element, T extends Element> PairRelationWithElements<S, T> create(S s, T t){
        PairRelationWithElements pairRelationWithElements = new PairRelationWithElements();
        pairRelationWithElements.sourceElement = s;
        pairRelationWithElements.targetElement = t;
        return pairRelationWithElements;
    }

    public S getSourceElement() {
        return sourceElement;
    }

    public T getTargetElement() {
        return targetElement;
    }
}
