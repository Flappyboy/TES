package top.jach.tes.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.jach.tes.app.entity.ProjectEntity;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.ProjectRepository;

@RestController("/api/project")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(ProjectEntity project) {
        Project p = Project.createNewProject(project.getName(), project.getDesc());
        p = projectRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity update(ProjectEntity project) {
        Project p = project.toProject();
        p = projectRepository.updateByProjectId(p);
        return ResponseEntity.ok(p);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity query(ProjectEntity project, Integer pageNum, Integer pageSize) {
        Project p = project.toProject();
        PageQueryDto<Project> result = projectRepository.pageQueryByProject(p, PageQueryDto.create(pageNum, pageSize));
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(Long projectId) {
        projectRepository.delete(projectId);
        return ResponseEntity.ok().build();
    }
}
