package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.ValueInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public abstract class ValueField<V> implements Field<V> {

    @Override
    public Info getInfo(V input, InfoRepositoryFactory infoRepositoryFactory) {
        return ValueInfo.createValueInfo(input, null, getInputClass());
    }
}
