package top.jach.tes.core.domain.meta;

import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.util.Arrays;
import java.util.List;

public abstract class InfoField implements Field<Info>, Field.FieldCriteria {
    @Override
    public Class<Info> getInputClass() {
        return Info.class;
    }

    @Override
    public Info getInfo(Info input, InfoRepositoryFactory infoRepositoryFactory) {
        List<Info> infoList = infoRepositoryFactory.getRepository(input.getInfoClass()).queryDetailsByInfoIds(Arrays.asList(input));
        return infoList.get(0);
    }

    protected abstract String infoName();

    protected abstract Class<? extends Info> infoClass();

    @Override
    public String criteria() {
        if(StringUtils.isNotBlank(infoName())){
            return "{name: \""+infoName()+"\"}";
        }
        if(infoClass() != null){
            return "{class: \""+infoClass().getName()+"\"}";
        }
        return null;
    }
}
