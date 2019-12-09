package top.jach.tes.core.easy;

import org.slf4j.ILoggerFactory;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.InfoRepository;
import top.jach.tes.core.api.service.InfoService;
import top.jach.tes.core.impl.domain.context.BaseContextFactory;
import top.jach.tes.core.impl.domain.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.impl.factory.DefaultInfoRepositoryFactory;
import top.jach.tes.core.impl.matching.DefaultNToOneMatchingStrategy;
import top.jach.tes.core.impl.matching.NToOneMatchingStrategy;
import top.jach.tes.core.impl.repository.SimpleInfoRepository;
import top.jach.tes.core.impl.service.DefaultInfoService;

import java.util.Set;

public class Environment {
    public static SimpleInfoRepository simpleInfoRepository = new SimpleInfoRepository();
    public static ILoggerFactory iLoggerFactory = new SimpleLoggerFactory();
    public static InfoRepositoryFactory infoRepositoryFactory;
    public static ContextFactory contextFactory;
    public static InfoService infoService;
    public static Project defaultProject = Project.createNewProject("Easy", "project for easy");

    static {
        infoRepositoryFactory = infoRepositoryFactory();
        contextFactory = new BaseContextFactory(iLoggerFactory, infoRepositoryFactory);
        infoService = new DefaultInfoService(contextFactory);
    }

    private static InfoRepositoryFactory infoRepositoryFactory(){
        DefaultInfoRepositoryFactory factory = new DefaultInfoRepositoryFactory();
        DefaultNToOneMatchingStrategy<Class<? extends Info>, InfoRepository> strategy = new DefaultNToOneMatchingStrategy<>();
        factory.register(new NToOneMatchingStrategy<Class<? extends Info>, InfoRepository>() {
            @Override
            public InfoRepository NToM(Class<? extends Info> aClass) {
                return simpleInfoRepository;
            }

            @Override
            public Set<Class<? extends Info>> MToN(InfoRepository infoRepository) {
                return null;
            }
        });
        return factory;
    }
}
