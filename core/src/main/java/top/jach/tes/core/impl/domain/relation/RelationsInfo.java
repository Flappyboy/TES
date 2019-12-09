package top.jach.tes.core.impl.domain.relation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.jach.tes.core.api.domain.info.Info;

import java.util.*;

@Getter
@Setter
@ToString
public abstract class RelationsInfo<R extends Relation> extends Info implements Iterable<R> {

    private HashMap<R, R> relations = new HashMap<>();

    public Collection<R> getRelations(){
        return relations.values();
    }

    public RelationsInfo setRelations(Collection<R> relations){
        for (R r :
                relations) {
            addRelation(r);
        };
        return this;
    }

    public RelationsInfo addRelation(R relation) {
        relations.put(relation, relation);
        return this;
    }
    public RelationsInfo addRelationByAddValue(R relation){
        if(relations.containsKey(relation)){
            relations.get(relation).addValue(relation.getValue());
        }else{
            relations.put(relation, relation);
        }
        return this;
    }

    @Override
    public Iterator<R> iterator() {
        return relations.values().iterator();
    }

    public int size() {
        return relations.size();
    }
}
