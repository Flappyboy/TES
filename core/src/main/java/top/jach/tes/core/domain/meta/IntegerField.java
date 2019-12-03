package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.IntegerInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

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
