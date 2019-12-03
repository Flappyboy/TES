package top.jach.tes.core.dev.app;

import top.jach.tes.core.domain.context.BaseContext;
import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.DefaultAction;
import top.jach.tes.core.domain.action.SaveInfoAction;
import top.jach.tes.core.domain.info.InfoProfile;

import java.util.Map;

public class TaskTool {
    public static Task excuteAction(Action action, Map<String, InfoProfile> inputInfos){
        Task task = Task.createNewTask(ProjectTool.DevAppProject());
        task.setAction(action);
        task.setInputInfos(inputInfos);
        task.setStatus(Task.Status.Draft.name());
        Context context = Environment.contextFactory().createContext(task.getProject());
        task.execute(context);
        return task;
    }

    public static Task excuteActionAndSaveInfo(Action action, Map<String, InfoProfile> inputInfos){
        DefaultAction defaultAction = new DefaultAction();
        defaultAction.setSaveAction(new SaveInfoAction());
        defaultAction.setAction(action);
        return excuteAction(defaultAction, inputInfos);
    }
}
