package top.jach.tes.core.domain;

import lombok.Getter;
import top.jach.tes.core.context.Context;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.action.OutputInfos;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class Task extends Entity {

    // 任务的输入
    private Map<String, Info> inputInfos;

    // 执行的步骤
    private Action action;

    // Task的状态
    private String status;

    // 所属project
    private Project project;

    private InfoRepositoryFactory infoRepositoryFactory;

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
        InputInfos inputInfos = new DefaultInputInfos();
        if(this.inputInfos != null) {
            for (Map.Entry<String, Info> entry :
                    this.inputInfos.entrySet()) {
                List<Info> result = context.InfoRepositoryFactory().getRepository(entry.getValue().getInfoClass()).queryDetailsByInfoIds(Arrays.asList(entry.getValue()));
                if (result.size() > 0) {
                    inputInfos.put(entry.getKey(), result.get(0));
                }
            }
        }
        OutputInfos outputInfos = action.execute(inputInfos, context);
        setStatus(Status.Complete.name());
    }

    public Task setInputInfos(Map<String, Info> inputInfos) {
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

    public Task setInfoRepositoryFactory(InfoRepositoryFactory infoRepositoryFactory) {
        this.infoRepositoryFactory = infoRepositoryFactory;
        return this;
    }
}
