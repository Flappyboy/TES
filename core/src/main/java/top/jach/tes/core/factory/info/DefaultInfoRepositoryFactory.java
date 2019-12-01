package top.jach.tes.core.factory.info;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.matching.NToOneMatchingWithoutPriority;
import top.jach.tes.core.repository.InfoRepository;

public class DefaultInfoRepositoryFactory extends NToOneMatchingWithoutPriority<Class<? extends Info>, InfoRepository> implements InfoRepositoryFactory {

    @Override
    public InfoRepository getRepository(Class<? extends Info> clazz) {
        return NToM(clazz);
    }
}
