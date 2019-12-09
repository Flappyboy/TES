package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.impl.domain.info.value.StringInfo;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

public class StringField extends ValueField<String> {

    public static StringField createField(String name, String displayName){
        StringField field = new StringField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static StringField createField(String name){
        return createField(name, name);
    }


    @Override
    public Class<String> getInputClass() {
        return String.class;
    }

    @Override
    public Info getInfo(String input, InfoRepositoryFactory infoRepositoryFactory) {
        return StringInfo.createInfo(input);
    }
}
