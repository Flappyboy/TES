package top.jach.tes.core.service.infoservice;

import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.info.Info;

public interface InfoService {
    void saveInfos(Project project, Iterable<Info> infos);
}
