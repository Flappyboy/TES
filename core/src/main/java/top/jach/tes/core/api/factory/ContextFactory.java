package top.jach.tes.core.api.factory;

import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.Project;

public interface ContextFactory {
    Context createContext(Project project);
}
