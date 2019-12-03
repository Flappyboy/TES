package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.DoubleInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

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
