package top.jach.tes.core.api.domain.context;

import org.slf4j.Logger;
import top.jach.tes.core.api.domain.Entity;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

/**
 * 代表action执行过程中的上下文，提供临时的文件资源，日志记录，当前的Project等
 */
public abstract class Context extends Entity {
    public abstract TempSpace TempSpace();
    public abstract Logger Logger();
    public abstract Project currentProject();
    public abstract InfoRepositoryFactory InfoRepositoryFactory();

    /**
     * 当一个Task，执行完后，需要销毁。
     */
    public abstract void dispose();
}
