package top.jach.tes.app;

import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;

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

    @Autowired
    ContextFactory contextFactory;

/*    @Test
    void contextLoads() {
    }

    @Test
    void process(){
        *//*Project project = Project.createNewProject("tes", "just test");
        projectRepository.save(project);

        Task task = Task.createNewTask(project);
        task.setInputInfos(new HashMap<>());
        DefaultAction action = new DefaultAction();
        action.setSaveAction(new SaveInfoAction());
        action.setAction(new GitCommitAction());
        task.setAction(action);
        taskRepository.save(task);
        task.execute(contextFactory.createContext(project));*//*
    }*/

}
