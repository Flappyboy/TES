package top.jach.tes.core.domain.context;

import org.slf4j.Logger;
import top.jach.tes.core.domain.Entity;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public abstract class Context extends Entity {
    public abstract TempSpace TempSpace();
    public abstract Logger Logger();
    public abstract Project currentProject();
    public abstract InfoRepositoryFactory InfoRepositoryFactory();
    public abstract void dispose();
}
