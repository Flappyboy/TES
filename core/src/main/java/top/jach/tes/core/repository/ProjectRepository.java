package top.jach.tes.core.repository;

import top.jach.tes.core.domain.Project;
import top.jach.tes.core.dto.PageQueryDto;

public interface ProjectRepository extends Repository{

    Project save(Project project);

    Project updateByProjectId(Project project);

    PageQueryDto<Project> pageQueryByProject(Project project, PageQueryDto<Project> pageQueryDto);

    void delete(Long projectId);
}
