package top.jach.tes.core.impl.domain.relation;

import org.apache.commons.lang3.tuple.Pair;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PairRelationsInfo extends RelationsInfo<PairRelation> {
    private InfoProfile sourceElementsInfo;

    private InfoProfile targetElementsInfo;

    public static PairRelationsInfo createInfo(){
        PairRelationsInfo pairRelations = new PairRelationsInfo();
        pairRelations.initBuild();
        return pairRelations;
    }
    private <S extends Element, T extends Element> PairRelationWithElementsList<S, T> toPairRelationWithElementsList(InfoRepositoryFactory infoRepositoryFactory){
        ElementsInfo<S> sourceInfo = (ElementsInfo) sourceElementsInfo.toInfoWithDetail(infoRepositoryFactory);
        ElementsInfo<T> targetInfo = (ElementsInfo<T>) sourceInfo;
        if (!sourceElementsInfo.getId().equals(targetElementsInfo.getId())){
            targetInfo = (ElementsInfo) targetElementsInfo.toInfoWithDetail(infoRepositoryFactory);
        }
        return PairRelationWithElementsList.create(sourceInfo, targetInfo, getRelations());
    }

    public PairRelationsInfo setSourceElementsInfo(InfoProfile sourceElementsInfo) {
        this.sourceElementsInfo = sourceElementsInfo;
        return this;
    }

    public PairRelationsInfo setTargetElementsInfo(InfoProfile targetElementsInfo) {
        this.targetElementsInfo = targetElementsInfo;
        return this;
    }

    public InfoProfile getSourceElementsInfo() {
        if(sourceElementsInfo == null){
            return targetElementsInfo;
        }
        return sourceElementsInfo;
    }

    public InfoProfile getTargetElementsInfo() {
        if(targetElementsInfo == null){
            return sourceElementsInfo;
        }
        return targetElementsInfo;
    }

    // 去重
    public PairRelationsInfo deWeight(){
        PairRelationsInfo pairRelationsInfo = createInfo();
        pairRelationsInfo.setSourceElementsInfo(this.getSourceElementsInfo());
        pairRelationsInfo.setTargetElementsInfo(this.getTargetElementsInfo());
        Set<Pair<String, String>> pairs = new HashSet<>();
        for (PairRelation pr :
                this) {
            Pair<String, String> pair = Pair.of(pr.getSourceName(),pr.getTargetName());
            boolean flag = true;
            for (Pair<String, String> p :
                    pairs) {
                if(p.getRight().equals(pr.getSourceName())&&p.getLeft().equals(pr.getTargetName())){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                pairs.add(pair);
            }
        }
        for (Pair<String, String> p :
                pairs) {
            pairRelationsInfo.addRelation((PairRelation) new PairRelation(p.getLeft(), p.getRight()).setValue(1d));
        }
        return pairRelationsInfo;
    }
}
