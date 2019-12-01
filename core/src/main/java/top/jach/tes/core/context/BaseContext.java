package top.jach.tes.core.context;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import top.jach.tes.core.context.log.simple.SimpleLoggerFactory;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.factory.info.DefaultInfoRepositoryFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public class BaseContext implements Context {

    private ILoggerFactory loggerFactory;

    private Task task;

    private InfoRepositoryFactory infoRepositoryFactory;

    public BaseContext(ILoggerFactory loggerFactory, Task task, InfoRepositoryFactory infoRepositoryFactory) {
        this.loggerFactory = loggerFactory;
        this.task = task;
        this.infoRepositoryFactory = infoRepositoryFactory;
    }

    public BaseContext(Task task) {
        this(new SimpleLoggerFactory(), task, new DefaultInfoRepositoryFactory());
    }

    @Override
    public TempSpace TempSpace() {
        return new DefaultTempSpace();
    }

    @Override
    public Logger Logger() {
        return loggerFactory.getLogger(String.valueOf(task.getId()));
    }

    @Override
    public Task currentTask() {
        return task;
    }

    @Override
    public InfoRepositoryFactory InfoRepositoryFactory() {
        return infoRepositoryFactory;
    }
}
