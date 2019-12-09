package top.jach.tes.core.easy;

import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.impl.domain.action.DefaultAction;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.api.domain.info.InfoProfile;

import java.util.Map;

public class TaskTool {
    public static Task excuteAction(Action action, Map<String, InfoProfile> inputInfos){
        Task task = Task.createNewTask(Environment.defaultProject);
        task.setAction(action);
        task.setInputInfos(inputInfos);
        task.setStatus(Task.Status.Draft.name());
        Context context = Environment.contextFactory.createContext(task.getProject());
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
