package top.jach.tes.core.impl.domain.relation;

import lombok.Getter;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class PairRelationWithElementsList <S extends Element, T extends Element> extends ArrayList<PairRelationWithElements<S, T>> {
    private ElementsInfo<S> sourceInfo;
    private ElementsInfo<T> targetInfo;

    private PairRelationWithElementsList() {
    }

    public static <S extends Element, T extends Element>  PairRelationWithElementsList<S, T> create(ElementsInfo<S> sourceInfo, ElementsInfo<T> targetInfo, Collection<PairRelation> relations){
        PairRelationWithElementsList<S, T> pairRelationWithElements = create(sourceInfo, targetInfo);
        for (PairRelation relation:
                relations) {
            S se = sourceInfo.getElementByName(relation.getSourceName());
            T te = targetInfo.getElementByName(relation.getTargetName());
            pairRelationWithElements.add(PairRelationWithElements.create(se, te));
        }
        return pairRelationWithElements;
    }

    public static <S extends Element, T extends Element>  PairRelationWithElementsList<S, T> create(ElementsInfo<S> sourceInfo, ElementsInfo<T> targetInfo){
        PairRelationWithElementsList<S, T> pairRelationWithElements = new PairRelationWithElementsList<S, T>();
        pairRelationWithElements.sourceInfo = sourceInfo;
        pairRelationWithElements.targetInfo = targetInfo;
        return pairRelationWithElements;
    }

}
