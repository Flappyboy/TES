package top.jach.tes.core.api.domain;

import lombok.Getter;

/**
 * 代表一个项目工程
 * 目前没有什么业务，仅用于对Info和Task做一个隔离
 */
@Getter
public class Project extends Entity{
    private String name;

    private String desc;

    public static Project createNewProject(String name, String desc){
        Project project = new Project();
        project.setName(name).setDesc(desc);
        project.initBuild();
        return project;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public Project setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
