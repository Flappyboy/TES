package top.jach.tes.plugin.jhkt.metrics;

import lombok.extern.java.Log;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qiming
 * @date: 2020/1/12 15:13
 * @description: 对李佳奇传来的原始数据按照仓库版本进行分类
 */
@Log
public class ReopDividedByRevision {
    /**
     * (K,V)  (仓库版本号，对应版本的仓库包含的packages)
     */
    private Map<Long,Map<String, GoPackagesInfo>> reopRevisionMap=new HashMap<>();
    /**
     * 微服务的绝对路径名称集合，elementName
     */
    private List<String> microservicesNames = new ArrayList<>();

    /**
     * 存储李佳奇传过来的原始数据
     */
    private Map<String, GoPackagesInfo> goPackageMap=new HashMap<>();

    public Map<Long,Map<String, GoPackagesInfo>> getReopRevisionMap(){
        try {
           // QueryGoPackages goPackages = new QueryGoPackages();
//            goPackageMap=goPackages.getGoPackageMap();
            log.info("总计共有"+goPackageMap.size()+"个代码仓");
//            microservicesNames=goPackages.getMicroservicesNames();
            //遍历每个仓库
            for (Map.Entry<String, GoPackagesInfo> entry : goPackageMap.entrySet()) {
                //获取代码仓的版本
                Long reopId = entry.getValue().getReposId();
                if(!reopRevisionMap.containsKey(reopId)){
                    reopRevisionMap.put(reopId,new HashMap<>());
                }
                reopRevisionMap.get(reopId).put(entry.getKey(),entry.getValue());
            }
        } catch (Exception e) {
            log.info("data error: 获取李佳奇传来的原始数据失败!");
            e.printStackTrace();
        }
        return reopRevisionMap;
    }

    public List<String> getMicroservicesNames(){
        return microservicesNames;
    }

    public static void main(String[] atgs){
        ReopDividedByRevision reopDividedByRevision = new ReopDividedByRevision();
        reopDividedByRevision.getReopRevisionMap();
        List<String> list = reopDividedByRevision.getMicroservicesNames();
        for(String str:list){
            System.out.println(str);
        }
    }

}
