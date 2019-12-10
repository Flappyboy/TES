package top.jach.tes.core.api.domain;

import lombok.Getter;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.InfoProfile;

import java.util.Map;

/**
 * 表示执行的一个任务实体，包括输入，执行的acion,执行的状态等。
 */
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
                    inputInfos.putInfoFromProfile(entry.getKey(), entry.getValue(), context.InfoRepositoryFactory());
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
