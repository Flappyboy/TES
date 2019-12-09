package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.domain.info.value.DoubleInfo;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

public class DoubleField extends ValueField<Double> {

    public static DoubleField createField(String name, String displayName){
        DoubleField field = new DoubleField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static DoubleField createField(String name){
        return createField(name, name);
    }

    @Override
    public Class<Double> getInputClass() {
        return Double.class;
    }

    @Override
    public Info getInfo(Double input, InfoRepositoryFactory infoRepositoryFactory) {
        return DoubleInfo.createInfo(input);
    }
}
