package top.jach.tes.core.impl.domain.relation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Relation {
    private Double value;



    public Relation setValue(Double value) {
        this.value = value;
        return this;
    }

    public void addValue(Double v){
        value += v;
    }

    abstract public Relation clone();
}
