package top.jach.tes.core.api.service;

import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.info.Info;

public interface InfoService {
    void saveInfos(Project project, Iterable<Info> infos);
}
