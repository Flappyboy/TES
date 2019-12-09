package top.jach.tes.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.domain.action.StatefulAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Table(name = "task")
@Entity
public class TaskEntity {
    @Id
    private Long id;

    private Long createdTime;

    private Long updatedTime;

    // 任务的输入
    @ElementCollection
    private Map<String, InfoProfileEntity> inputInfos;

    // 执行的步骤
    private String action;

    // Task的状态
    private String status;

    // 所属Task
    @ManyToOne
    private ProjectEntity projectEntity;

    public Task toTask(){
        Task task = new Task(projectEntity.toProject());
        task.setInputInfos(InfoProfileEntity.entitiesToInfoProfiles(inputInfos))
                .setStatus(status).setAction(StatefulAction.deserializeActionFromJson(action))
                .setId(id).setUpdatedTime(updatedTime).setCreatedTime(createdTime);
        return task;
    }

    public static TaskEntity createFromTask(Task task){
        TaskEntity entity = new TaskEntity();
        BeanUtils.copyProperties(task, entity);
        entity.setAction(StatefulAction.serializeActionToJson(task.getAction()));
        entity.setInputInfos(InfoProfileEntity.infoProfilesToEntities(task.getInputInfos()));
        entity.setProjectEntity(ProjectEntity.createFromProject(task.getProject()));
        return entity;
    }

    public static List<TaskEntity> createFromTasks(List<Task> tasks){
        List<TaskEntity> TaskEntities = new ArrayList<>();
        for (Task task :
                tasks) {
            TaskEntities.add(createFromTask(task));
        }
        return TaskEntities;
    }
    public static List<Task> entitiesToTasks(List<TaskEntity> entities){
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity entity :
                entities) {
            tasks.add(entity.toTask());
        }
        return tasks;
    }
}
