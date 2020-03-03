package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.impl.domain.info.value.FileInfo;

import java.io.File;

public class FileField extends ValueField<File> {

    public static FileField createField(String name, String displayName){
        FileField field = new FileField();
        field.name = name;
        field.displayName = name;
        return field;
    }

    public static FileField createField(String name){
        return createField(name, name);
    }

    @Override
    public Class<File> getInputClass() {
        return File.class;
    }

    @Override
    public Info getInfo(File input, InfoRepositoryFactory infoRepositoryFactory) {
        return FileInfo.createInfo(input);
    }
}
