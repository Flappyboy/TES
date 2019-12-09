package top.jach.tes.core.impl.repository;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.InfoRepository;

import java.util.List;

public class SimpleInfoRepository implements InfoRepository {
    @Override
    public Info saveProfile(Info info, Long projectId) {
        return null;
    }

    @Override
    public Info saveDetail(Info info) {
        return null;
    }

    @Override
    public Info updateProfileByInfoId(Info info) {
        return null;
    }

    @Override
    public Info deleteByInfoId(Long infoId) {
        return null;
    }

    @Override
    public PageQueryDto queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        return null;
    }

    @Override
    public PageQueryDto queryProfileByCustom(Object o, PageQueryDto pageQueryDto) {
        throw new RuntimeException("SimpleInfoRepository do not have custom query!");
    }

    @Override
    public List queryDetailsByInfoIds(List infoIds) {
        return null;
    }
}
