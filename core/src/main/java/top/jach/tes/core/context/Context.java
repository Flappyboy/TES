package top.jach.tes.core.context;

import org.slf4j.Logger;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;


public interface Context {
    TempSpace TempSpace();
    Logger Logger();
    Task currentTask();
    InfoRepositoryFactory InfoRepositoryFactory();
}
