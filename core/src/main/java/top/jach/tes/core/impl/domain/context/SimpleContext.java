package top.jach.tes.core.impl.domain.context;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.context.TempSpace;
import top.jach.tes.core.impl.domain.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.impl.factory.DefaultInfoRepositoryFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

import java.io.File;

public class SimpleContext extends Context {
    private ILoggerFactory loggerFactory;

    private Project project;

    private InfoRepositoryFactory infoRepositoryFactory;

    private File baseDir;

    protected SimpleContext(Project project, ILoggerFactory loggerFactory, InfoRepositoryFactory infoRepositoryFactory) {
        this(project, loggerFactory, infoRepositoryFactory,
                new File(System.getProperties().getProperty("user.home")+"/.tes-app/context/tmp"));
    }

    public SimpleContext(Project project, ILoggerFactory loggerFactory, InfoRepositoryFactory infoRepositoryFactory, File baseDir) {
        this.loggerFactory = loggerFactory;
        this.project = project;
        this.infoRepositoryFactory = infoRepositoryFactory;
        this.baseDir = baseDir;
        if(!baseDir.exists()){
            baseDir.mkdirs();
        }
    }

    protected SimpleContext(Project project) {
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
