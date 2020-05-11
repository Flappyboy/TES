package top.jach.tes.app.web.controller;

import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.jach.tes.app.web.AppApplication;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.app.web.entity.TaskEntity;
import top.jach.tes.app.web.repository.TaskEntityRepository;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;

@RestController()
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskEntityRepository taskEntityRepository;

    @Autowired
    InfoRepositoryFactory infoRepositoryFactory;

    @Autowired
    ILoggerFactory iLoggerFactory;

    @Autowired
    ContextFactory contextFactory;


    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResponseEntity execute(TaskEntity taskEntity) {
        Task task = taskEntity.toTask();
        task.initBuild();
        task = taskRepository.save(task);
//        task.setAction(AppApplication.actionMap.get(taskEntity.getAction()));
        task.execute(contextFactory.createContext(task.getProject()));
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity query(Long projectId, Integer pageNum, Integer pageSize) {
//        Project p = project.toProject();
        TaskEntity taskEntity = new TaskEntity();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        taskEntity.setProjectEntity(projectEntity);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<TaskEntity> result = taskEntityRepository.findAll(Example.of(taskEntity),pageable);
        PageQueryDto pageQueryDto = PageQueryDto.create(pageNum, pageSize).addResult(TaskEntity.entitiesToTasks(result.getContent()), result.getTotalElements());
        return ResponseEntity.ok(pageQueryDto);
    }
}
