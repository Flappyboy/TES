package top.jach.tes.core.impl.domain.relation;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;

import java.util.ArrayList;
import java.util.List;

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
}
