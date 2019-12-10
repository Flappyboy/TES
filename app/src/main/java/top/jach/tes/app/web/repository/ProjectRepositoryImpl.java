package top.jach.tes.app.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.ProjectRepository;

@Component
public class ProjectRepositoryImpl implements ProjectRepository {
    @Autowired
    ProjectEntityRepository projectEntityRepository;

    @Override
    public Project save(Project project) {
        return projectEntityRepository.save(ProjectEntity.createFromProject(project)).toProject();
    }

    @Override
    public Project updateByProjectId(Project project) {
        return projectEntityRepository.save(ProjectEntity.createFromProject(project)).toProject();
    }

    @Override
    public PageQueryDto<Project> pageQueryByProject(Project project, PageQueryDto<Project> pageQueryDto) {
        Pageable pageable = PageRequest.of(pageQueryDto.getPageNum(), pageQueryDto.getPageSize(), Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<ProjectEntity> result = projectEntityRepository.findAll(Example.of(ProjectEntity.createFromProject(project)),pageable);
        pageQueryDto.addResult(ProjectEntity.entitiesToProjects(result.getContent()), result.getTotalElements());
        return pageQueryDto;
    }

    @Override
    public void delete(Long projectId) {
        projectEntityRepository.deleteById(projectId);
    }
}
