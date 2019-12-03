package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.LongInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

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
