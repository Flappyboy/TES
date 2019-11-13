package top.jach.tes.core.repository;

import top.jach.tes.core.InfoRepositoryManager;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.dto.PageQueryDto;

import java.util.ArrayList;
import java.util.List;

public class AllInfoRepository implements InfoRepository<Info> {

    @Override
    public Info saveProfile(Info info, Long projectId) {
        return InfoRepositoryManager.getInfoDao(info).saveProfile(info, projectId);
    }

    @Override
    public Info saveDetail(Info info) {
        return InfoRepositoryManager.getInfoDao(info).saveDetail(info);
    }

    @Override
    public Info updateProfileByInfoId(Info info) {
        return InfoRepositoryManager.getInfoDao(info).updateProfileByInfoId(info);
    }

    @Override
    public Info deleteByInfoId(Long infoId) {
        return InfoRepositoryManager.getInfoDaoByInfoId(infoId).deleteByInfoId(infoId);
    }

    @Override
    public PageQueryDto<Info> queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        InfoRepository infoRepository = null;
        if(info!=null && info.getName()!=null){
            
            infoRepository = InfoRepositoryManager.getInfoDao(info.getName());
        }
        if(infoRepository==null){
            infoRepository = InfoRepositoryManager.getInfoDaoByInfoId(info.getId());
        }
        return infoRepository.queryProfileByInfoAndProjectId(info, projectId, pageQueryDto);
    }

    @Override
    public List<Info> queryDetailsByInfoIds(List<Long> infoIds) {
        if(infoIds==null || infoIds.size()<=0){
            return new ArrayList<>();
        }
        return InfoRepositoryManager.getInfoDaoByInfoId(infoIds.get(0)).queryDetailsByInfoIds(infoIds);
    }


}
