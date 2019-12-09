package top.jach.tes.app.controller;

import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.jach.tes.app.entity.TaskEntity;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;

@RestController("/api/task")
public class TaskController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

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
        task.execute(contextFactory.createContext(task.getProject()));
        return ResponseEntity.ok().build();
    }
}
