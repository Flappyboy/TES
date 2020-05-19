package top.jach.tes.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.ProjectRepository;

@RestController()
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @PostMapping
    public ResponseEntity create(ProjectEntity project) {
        Project p = Project.createNewProject(project.getName(), project.getDesc());
        p = projectRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @PutMapping
    public ResponseEntity update(ProjectEntity project) {
        Project p = project.toProject();
        p = projectRepository.updateByProjectId(p);
        return ResponseEntity.ok(p);
    }

    @GetMapping()
    public ResponseEntity query(Integer pageNum, Integer pageSize) {
//        Project p = project.toProject();
        Project p = new Project();
        PageQueryDto<Project> result = projectRepository.pageQueryByProject(p, PageQueryDto.create(pageNum, pageSize));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity delete(Long projectId) {
        try {
            projectRepository.delete(projectId);
        }catch (Exception e){

        }
        return ResponseEntity.ok().build();
    }
}
