package top.jach.tes.core.impl.domain.context;

import org.slf4j.ILoggerFactory;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

public class BaseContextFactory implements ContextFactory {
    private ILoggerFactory loggerFactory;

    private InfoRepositoryFactory infoRepositoryFactory;

    public BaseContextFactory(ILoggerFactory loggerFactory, InfoRepositoryFactory infoRepositoryFactory) {
        this.loggerFactory = loggerFactory;
        this.infoRepositoryFactory = infoRepositoryFactory;
    }

    @Override
    public Context createContext(Project project) {
        SimpleContext simpleContext = new SimpleContext(project, loggerFactory, infoRepositoryFactory);
        simpleContext.initBuild();
        return simpleContext;
    }
}
