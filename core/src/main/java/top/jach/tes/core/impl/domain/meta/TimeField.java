package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.domain.info.value.LongInfo;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

public class TimeField extends ValueField<Long> {
    public static TimeField createField(String name, String displayName){
        TimeField field = new TimeField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static TimeField createField(String name){
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
