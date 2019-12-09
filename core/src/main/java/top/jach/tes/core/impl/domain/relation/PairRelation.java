package top.jach.tes.core.impl.domain.relation;

import lombok.Data;

@Data
public class PairRelation extends Relation{
    String sourceName;
    String targetName;

    public PairRelation() {
    }

    public PairRelation(String sourceName, String targetName) {
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public PairRelation setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public PairRelation setTargetName(String targetName) {
        this.targetName = targetName;
        return this;
    }

    @Override
    public Relation clone() {
        return new PairRelation(getSourceName(), getTargetName()).setValue(getValue());
    }
}
