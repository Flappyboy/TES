package top.jach.tes.core.domain.context;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import top.jach.tes.core.domain.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.factory.info.DefaultInfoRepositoryFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.io.File;

public class BaseContext extends Context {
    private ILoggerFactory loggerFactory;

    private Project project;

    private InfoRepositoryFactory infoRepositoryFactory;

    protected BaseContext(Project project, ILoggerFactory loggerFactory, InfoRepositoryFactory infoRepositoryFactory) {
        this.loggerFactory = loggerFactory;
        this.project = project;
        this.infoRepositoryFactory = infoRepositoryFactory;
    }

    protected BaseContext(Project project) {
        this(project, new SimpleLoggerFactory(), new DefaultInfoRepositoryFactory());
    }

    @Override
    public TempSpace TempSpace() {
        return new DefaultTempSpace(new File("./tmp/"+getId()));
    }

    @Override
    public Logger Logger() {
        return loggerFactory.getLogger(String.valueOf(getId()));
    }

    @Override
    public Project currentProject() {
        return project;
    }

    @Override
    public InfoRepositoryFactory InfoRepositoryFactory() {
        return infoRepositoryFactory;
    }

    @Override
    public void dispose() {

    }
}
