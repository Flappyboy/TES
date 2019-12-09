package top.jach.tes.core.impl.repository;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.InfoRepository;

import java.util.*;

public class SimpleInfoRepository implements InfoRepository<Info, Object> {
    private Map<Long, Info> infoIdMap = new HashMap<>();
    private Map<Long, Set<Long>> projectIdInfoIdsMap = new HashMap<>();
    private Map<Long, Long> infoIdProjectIdMap = new HashMap<>();

    @Override
    public Info saveProfile(Info info, Long projectId) {
        infoIdMap.put(info.getId(), info);
        Set<Long> infoIds = projectIdInfoIdsMap.get(projectId);
        if(infoIds == null){
            infoIds = new HashSet<>();
            projectIdInfoIdsMap.put(projectId, infoIds);
        }
        infoIds.add(info.getId());
        infoIdProjectIdMap.put(info.getId(), projectId);
        return null;
    }

    @Override
    public Info saveDetail(Info info) {
        infoIdMap.put(info.getId(), info);
        return info;
    }

    @Override
    public Info updateProfileByInfoId(Info info) {
        infoIdMap.put(info.getId(), info);
        return null;
    }

    @Override
    public Info deleteByInfoId(Long infoId) {
        infoIdMap.remove(infoId);
        Long projectId = infoIdProjectIdMap.get(infoId);
        infoIdProjectIdMap.remove(infoId);
        Set<Long> ids = projectIdInfoIdsMap.get(projectId);
        if(ids!=null){
            ids.remove(infoId);
        }
        return null;
    }

    @Override
    public PageQueryDto queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        throw new RuntimeException("SimpleInfoRepository do not support criteria query!");
    }

    @Override
    public PageQueryDto queryProfileByCustom(Object o, PageQueryDto pageQueryDto) {
        throw new RuntimeException("SimpleInfoRepository do not support custom query!");
    }

    @Override
    public List queryDetailsByInfoIds(List<Long> infoIds) {
        List<Info> infos = new ArrayList<>();
        for (Long id:
                infoIds) {
            Info info = infoIdMap.get(id);
            if(infos != null) {
                infos.add(info);
            }
        }
        return infos;
    }
}
