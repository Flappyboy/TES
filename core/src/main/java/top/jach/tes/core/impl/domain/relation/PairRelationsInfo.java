package top.jach.tes.core.impl.domain.relation;

import top.jach.tes.core.api.domain.info.InfoProfile;

public class PairRelationsInfo extends RelationsInfo<PairRelation> {
    private InfoProfile sourceElementsInfo;

    private InfoProfile targetElementsInfo;

    public static PairRelationsInfo createInfo(){
        PairRelationsInfo pairRelations = new PairRelationsInfo();
        pairRelations.initBuild();
        return pairRelations;
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
