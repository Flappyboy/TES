package top.jach.tes.core.factory;

import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.Project;

public interface ContextFactory {
    Context createContext(Project project);
}
