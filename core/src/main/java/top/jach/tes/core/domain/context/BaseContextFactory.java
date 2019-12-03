package top.jach.tes.core.domain.context;

import org.slf4j.ILoggerFactory;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.factory.ContextFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public class BaseContextFactory implements ContextFactory {
    private ILoggerFactory loggerFactory;

    private InfoRepositoryFactory infoRepositoryFactory;

    public BaseContextFactory(ILoggerFactory loggerFactory, InfoRepositoryFactory infoRepositoryFactory) {
        this.loggerFactory = loggerFactory;
        this.infoRepositoryFactory = infoRepositoryFactory;
    }

    @Override
    public Context createContext(Project project) {
        BaseContext baseContext = new BaseContext(project, loggerFactory, infoRepositoryFactory);
        baseContext.initBuild();
        return baseContext;
    }
}
