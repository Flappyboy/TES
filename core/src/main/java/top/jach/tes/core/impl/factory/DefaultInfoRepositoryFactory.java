package top.jach.tes.core.impl.factory;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.impl.matching.NToOneMatchingWithoutPriority;
import top.jach.tes.core.api.repository.InfoRepository;

public class DefaultInfoRepositoryFactory extends NToOneMatchingWithoutPriority<Class<? extends Info>, InfoRepository> implements InfoRepositoryFactory {

    @Override
    public InfoRepository getRepository(Class<? extends Info> clazz) {
        return NToM(clazz);
    }
}
