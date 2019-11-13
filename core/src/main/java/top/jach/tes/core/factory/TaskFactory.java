package top.jach.tes.core.factory;

import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.domain.info.InputInfo;

public class TaskFactory {
    public static Task createTask(Project project, Action action, InputInfo inputInfo){
            Task task = new Task();
            task.initBuild();
            return task;
    }
}
