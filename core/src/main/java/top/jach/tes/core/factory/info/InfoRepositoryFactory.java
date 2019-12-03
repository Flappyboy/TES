package top.jach.tes.core.factory.info;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.repository.InfoRepository;

public interface InfoRepositoryFactory {
    InfoRepository<Info> getRepository(Class<? extends Info> clazz);
}
