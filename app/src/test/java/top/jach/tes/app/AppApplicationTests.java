package top.jach.tes.app;

import org.junit.jupiter.api.Test;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.jach.tes.core.context.BaseContext;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.Task;
import top.jach.tes.core.domain.action.DefaultAction;
import top.jach.tes.core.domain.action.SaveInfoAction;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;
import top.jach.tes.core.repository.ProjectRepository;
import top.jach.tes.core.repository.TaskRepository;
import top.jach.tes.plugin.jach.git.GitCommitAction;

import java.lang.reflect.AccessibleObject;
import java.util.HashMap;

@SpringBootTest
class AppApplicationTests {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    InfoRepositoryFactory infoRepositoryFactory;

    @Autowired
    ILoggerFactory iLoggerFactory;

    @Test
    void contextLoads() {
    }

    @Test
    void process(){
        Project project = Project.createNewProject("tes", "just test");
        projectRepository.save(project);

        Task task = Task.createNewTask(project);
        task.setInputInfos(new HashMap<>());
        DefaultAction action = new DefaultAction();
        action.setSaveAction(new SaveInfoAction());
        action.setAction(new GitCommitAction());
        task.setAction(action);
        taskRepository.save(task);
        task.execute(new BaseContext(iLoggerFactory, task, infoRepositoryFactory));
    }

}
