package top.jach.tes.core.domain;

import lombok.Getter;
import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.action.OutputInfos;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class Task extends Entity {

    // 任务的输入
    private Map<String, InfoProfile> inputInfos;

    // 执行的步骤
    private Action action;

    // Task的状态
    private String status;

    // 所属project
    private Project project;

    private Context context;

    public Task(Project project) {
        this.project = project;
    }

    public static Task createNewTask(Project project){
        Task task = new Task(project);
        task.initBuild();
        return task;
    }

    public enum Status{
        Draft(),
        Doing(),
        Warn(),
        Complete(),
        Error(),
        Terminate()
    }

    public void execute(Context context){
        this.context = context;
        setStatus(Status.Doing.name());
        try {
            InputInfos inputInfos = new DefaultInputInfos();
            if (this.inputInfos != null) {
                for (Map.Entry<String, InfoProfile> entry :
                        this.inputInfos.entrySet()) {
                    List<Info> result = context.InfoRepositoryFactory().getRepository(entry.getValue().getInfoClass()).
                            queryDetailsByInfoIds(Arrays.asList(entry.getValue().getId()));
                    if (result.size() > 0) {
                        inputInfos.put(entry.getKey(), result.get(0));
                    }
                }
            }
            OutputInfos outputInfos = action.execute(inputInfos, context);

            setStatus(Status.Complete.name());
        } catch (Throwable e){
            context.Logger().error("error: \n", e);
            setStatus(Status.Error.name());
        }
    }

    public Task setInputInfos(Map<String, InfoProfile> inputInfos) {
        this.inputInfos = inputInfos;
        return this;
    }

    public Task setAction(Action action) {
        this.action = action;
        return this;
    }

    public Task setStatus(String status) {
        this.status = status;
        return this;
    }
}
