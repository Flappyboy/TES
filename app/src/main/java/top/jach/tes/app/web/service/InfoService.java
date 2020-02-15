package top.jach.tes.app.web.service;

import top.jach.tes.app.web.dto.InfoType;
import top.jach.tes.core.api.domain.info.InfoProfile;

import java.util.List;

public interface InfoService {
    List<InfoType> findAllInfoTypes(Long projectId);
}
