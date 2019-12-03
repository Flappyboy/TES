package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.LongInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public class LongField extends ValueField<Long> {

    public static LongField createField(String name, String displayName){
        LongField field = new LongField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static LongField createField(String name){
        return createField(name, name);
    }


    @Override
    public Class<Long> getInputClass() {
        return Long.class;
    }

    @Override
    public Info getInfo(Long input, InfoRepositoryFactory infoRepositoryFactory) {
        return LongInfo.createInfo(input);
    }
}
