package top.jach.tes.core.api.factory;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.repository.InfoRepository;

/**
 * 通过InfoClass获取对应的InfoRepository
 */
public interface InfoRepositoryFactory {
    InfoRepository getRepository(Class<? extends Info> infoClass);

    default <IR extends InfoRepository> IR getRepository(Class<? extends Info> infoClass, Class<IR> infoRepositoryClass){
        return (IR) getRepository(infoClass);
    }
}
