package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoOfInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.util.Arrays;
import java.util.List;

public abstract class MultiInfosField<I extends Info> implements Field<List<Info>>{

    @Override
    public Class<List<Info>> getInputClass() {
        return null;
    }

    @Override
    public Info getInfo(List<Info> input, InfoRepositoryFactory infoRepositoryFactory) {
        InfoOfInfo<I> infoOfInfo =  InfoOfInfo.createInfoOfInfo();
        for (Info info :
                input) {
            List<Info> infoList = infoRepositoryFactory.getRepository(info.getInfoClass()).queryDetailsByInfoIds(Arrays.asList(input));
            Info i = infoList.get(0);
            infoOfInfo.addInfos((I) i);
        }
        return infoOfInfo;
    }
}
