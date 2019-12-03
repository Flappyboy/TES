package top.jach.tes.core.factory.info;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.repository.InfoRepository;

public interface InfoRepositoryFactory {
    InfoRepository getRepository(Class<? extends Info> infoClass);

    default <IR extends InfoRepository> IR getRepository(Class<? extends Info> infoClass, Class<IR> infoRepositoryClass){
        return (IR) getRepository(infoClass);
    }
}
