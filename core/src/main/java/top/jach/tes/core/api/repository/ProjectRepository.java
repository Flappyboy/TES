package top.jach.tes.core.api.repository;

import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.dto.PageQueryDto;

public interface ProjectRepository extends Repository{

    Project save(Project project);

    Project updateByProjectId(Project project);

    PageQueryDto<Project> pageQueryByProject(Project project, PageQueryDto<Project> pageQueryDto);

    void delete(Long projectId);
}
