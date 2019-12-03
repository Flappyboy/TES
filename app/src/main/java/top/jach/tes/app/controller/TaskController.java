package top.jach.tes.app.controller;

import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.jach.tes.app.entity.TaskEntity;
import top.jach.tes.core.domain.context.BaseContext;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.factory.ContextFactory;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;
import top.jach.tes.core.repository.ProjectRepository;
import top.jach.tes.core.repository.TaskRepository;

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
