package top.jach.tes.app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.jach.tes.core.domain.Project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "project")
@Entity
public class ProjectEntity {

    @Id
    private Long id;

    private Long createdTime;

    private Long updatedTime;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`desc`")
    private String desc;

    public Project toProject(){
        Project project = new Project();
        project.setDesc(desc).setName(name).setId(id).setCreatedTime(createdTime).setUpdatedTime(updatedTime);
        return project;
    }

    public static ProjectEntity createFromProject(Project project){
        ProjectEntity projectEntity = new ProjectEntity();
        BeanUtils.copyProperties(project, projectEntity);
        return projectEntity;
    }

    public static List<ProjectEntity> createFromProjects(List<Project> projects){
        List<ProjectEntity> projectEntities = new ArrayList<>();
        for (Project project :
            projects) {
            projectEntities.add(createFromProject(project));
        }
        return projectEntities;
    }
    public static List<Project> entitiesToProjects(List<ProjectEntity> entities){
        List<Project> projects = new ArrayList<>();
        for (ProjectEntity entity :
                entities) {
            projects.add(entity.toProject());
        }
        return projects;
    }
}
