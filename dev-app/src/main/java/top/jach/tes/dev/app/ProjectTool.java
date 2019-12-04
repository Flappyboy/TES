package top.jach.tes.dev.app;

import top.jach.tes.core.domain.Project;

public class ProjectTool {
    public static Project DevAppProject(){
        return Project.createNewProject("DEV_APP", "dev app project");
    }
}
