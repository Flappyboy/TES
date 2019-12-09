package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.domain.meta.Field;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

import java.util.Arrays;
import java.util.List;

public class InfoField extends FieldWithName<InfoProfile> implements Field.FieldCriteria {
    String infoName;
    Class<? extends Info> infoClass;

    public static InfoField createField(String name, String displayName){
        InfoField infoField = new InfoField();
        infoField.name = name;
        infoField.displayName = displayName;
        return infoField;
    }
    public static InfoField createField(String name){
        return createField(name, name);
    }


    public InfoField setInfoName(String infoName) {
        this.infoName = infoName;
        return this;
    }

    public InfoField setInfoClass(Class<? extends Info> infoClass) {
        this.infoClass = infoClass;
        return this;
    }

    @Override
    public Class<InfoProfile> getInputClass() {
        return InfoProfile.class;
    }

    @Override
    public Info getInfo(InfoProfile input, InfoRepositoryFactory infoRepositoryFactory) {
        List<Info> infoList = infoRepositoryFactory.getRepository(input.getInfoClass()).queryDetailsByInfoIds(Arrays.asList(input.getId()));
        return infoList.get(0);
    }

    protected String infoName(){
        return infoName;
    }

    protected Class<? extends Info> infoClass(){
        return infoClass;
    }

    @Override
    public String criteria() {

        return null;
    }
}
