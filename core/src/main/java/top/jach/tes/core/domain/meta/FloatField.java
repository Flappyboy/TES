package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.FloatInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public class FloatField extends ValueField<Float> {

    public static FloatField createField(String name, String displayName){
        FloatField field = new FloatField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static FloatField createField(String name){
        return createField(name, name);
    }

    @Override
    public Class<Float> getInputClass() {
        return Float.class;
    }

    @Override
    public Info getInfo(Float input, InfoRepositoryFactory infoRepositoryFactory) {
        return FloatInfo.createInfo(input);
    }
}
