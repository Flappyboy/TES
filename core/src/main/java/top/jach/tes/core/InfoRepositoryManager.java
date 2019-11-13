package top.jach.tes.core;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.dto.PageQueryDto;
import top.jach.tes.core.repository.AllInfoRepository;
import top.jach.tes.core.repository.InfoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoRepositoryManager {

//    static Map<String, InfoRepository> nameInfoDaoMap = new HashMap<>();
    static Map<Class, InfoRepository> clazzInfoDaoMap = new HashMap<>();

    static List<InfoRepository> infoRepositoryList = new ArrayList<>();

    public static List<InfoRepository> allInfoRepositorys(){
        return infoRepositoryList;
    }

    private static void register(InfoRepository infoRepository){
        for (InfoRepository oldInfoRepository :
                infoRepositoryList) {
            if(infoRepository.getClass().equals(oldInfoRepository.getClass())){
                return;
            }
        }
        infoRepositoryList.add(infoRepository);
    }

   /* public static void register(String infoName, InfoRepository infoDao){
        if(StringUtils.isBlank(infoName)){
            throw new RuntimeException("infoName is blank or null");
        }
        nameInfoDaoMap.put(infoName, infoDao);
        register(infoDao);
    }*/

    public static void register(Class clazz, InfoRepository infoRepository){
        if(clazz == null){
            throw new RuntimeException("clazz is null");
        }
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz is not subclass for Info");
        }
        clazzInfoDaoMap.put(clazz, infoRepository);
        register(infoRepository);
    }

    public static InfoRepository getInfoDao(Class clazz){
        if(!Info.class.isAssignableFrom(clazz)){
            throw new RuntimeException("clazz "+ clazz.getName()+" is not subclass of Info");
        }
        return clazzInfoDaoMap.get(clazz);
    }

    public static InfoRepository getInfoDao(String name){
        return clazzInfoDaoMap.get(InfoNameManager.getClassByName(name));
    }

    public static InfoRepository getInfoDao(Info info){
        if(info == null) {
            throw new RuntimeException("info is null");
        }
        InfoRepository infoRepository = null;
//        infoRepository = nameInfoDaoMap.get(info.getName());
        if(infoRepository == null){
            infoRepository = getInfoDao(info.getClass());
        }
        return infoRepository;
    }

    Map<Long, InfoRepository> InfoId2Repository = new HashMap<>();

    public static InfoRepository getInfoDaoByInfoId(Long infoid){
        if(infoid == null) {
            throw new RuntimeException("info is null");
        }
        for (InfoRepository infoRepository:
            allInfoRepositorys()) {
            if(infoRepository instanceof AllInfoRepository){
                continue;
            }
            Info info = new Info();
            info.setId(infoid);
            PageQueryDto result = infoRepository.queryProfileByInfoAndProjectId(info, null, PageQueryDto.create(0,0));
            if(result.getTotal()>0){
                return infoRepository;
            }
        }
        return null;
    }
}
