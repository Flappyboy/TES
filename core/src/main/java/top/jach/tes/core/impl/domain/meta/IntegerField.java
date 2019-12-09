package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.domain.info.value.IntegerInfo;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

public class IntegerField extends ValueField<Integer> {

    public static IntegerField createField(String name, String displayName){
        IntegerField field = new IntegerField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static IntegerField createField(String name){
        return createField(name, name);
    }

    @Override
    public Class<Integer> getInputClass() {
        return Integer.class;
    }

    @Override
    public Info getInfo(Integer input, InfoRepositoryFactory infoRepositoryFactory) {
        return IntegerInfo.createInfo(input);
    }
}
