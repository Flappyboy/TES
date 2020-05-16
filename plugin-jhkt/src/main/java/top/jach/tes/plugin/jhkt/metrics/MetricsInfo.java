package top.jach.tes.plugin.jhkt.metrics;

import lombok.Data;
import lombok.extern.java.Log;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.go.GoFile;
import top.jach.tes.plugin.tes.code.go.GoPackage;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;

import java.util.*;

/**
 * @author: qiming
 * @date: 2020/1/11 16:11
 * @description: 不同版本的不同代码仓有一个MetricsInfo
 */
@Log

@Data
public class MetricsInfo extends Info {
    private Long reposId;
    private Long startTime;
    private Long endTime;
    private InfoProfile elementsInfo;
    private String version;

    private List<Metrics> metricsList = new ArrayList<>();
    private MicroservicesInfo microservicesInfo;

    /**
     * 大项目的名称
     */
//    private static String projName = "x_3.x_5.x_7/x_9/x_b/x_d";
    private static String projName = "x_3.x_5.x_7/x_9/x_b/x_d/";
    /**
     * 微服务的绝对路径名称集合，elementName
     */
    private List<String> microservicesNames = new ArrayList<>();
    /**
     * (K,V)  (仓库版本名称，对应版本的仓库包含的packages)
     */
    private  Map<String, GoPackagesInfo> reopRevisionMap = new HashMap<>();
    /**
     * (K,V)  (仓库路径，goPackagesInfo)
     */
//    private static Map<String,GoPackagesInfo> goPacksInReopMap = new HashMap<>();

    /**
     * 单个微服务与多个package对应
     */
    private Map<String, List<GoPackage>> packagesInMicroMap = new HashMap<>();

    /**
     * 一个包依赖哪些包
     */
    private Map<String, List<String>> packagesInPackageMap = new HashMap<>();
    /**
     * 一个文件依赖哪些包
     */
    private Map<String, List<GoPackage>> packagesInFileMap = new HashMap<>();

    /**
     * 一个包中有哪些文件
     */
    private Map<String, List<GoFile>> filesInPackageMap = new HashMap<>();

    private Map<String,Set<String>> subTopicsMap=new HashMap<>();
    private Map<String,Set<String>> pubTopicsMap=new HashMap<>();

//    private static List<MetricsInfo> metricsInfoList = new ArrayList<>();

    public static MetricsInfo createInfo(String version, Map<String, GoPackagesInfo> reopRevisionMap, List<String> microsserviceNames, MicroservicesInfo microservicesInfo) {
        //一个版本对应一个metricsInfo
        MetricsInfo metricsInfo = new MetricsInfo();
        //设置仓库版本号名称、仓库-包对应信息、包含的微服务信息
        metricsInfo.setVersion(version);
        metricsInfo.setReopRevisionMap(reopRevisionMap);
        metricsInfo.setMicroservicesNames(microsserviceNames);
        metricsInfo.setMicroservicesInfo(microservicesInfo);

        for(Map.Entry<String, GoPackagesInfo> reopPackage : reopRevisionMap.entrySet()){
            //获取单个版本的reop下的所有package
            List<GoPackage> goPackagesList = reopPackage.getValue().getGoPackages();

            for (int i = 0; i < goPackagesList.size(); i++) {
                //去除包路径中的大项目前缀
                String refinedPackageName=excludeProjName(goPackagesList.get(i).getPath());
                String microserviceName = metricsInfo.MatchedMicroServiceName(metricsInfo,refinedPackageName);
                //如果判断属于微服务项目，那么添加到对应的微服务-package集合中
                if (!microserviceName.equals("-1")) {
                    metricsInfo.packagesInMicroMap.get(microserviceName).add(goPackagesList.get(i));
                    //获取这个package中的文件集合
                    List<GoFile> fileList = goPackagesList.get(i).getGoFiles();
                    //添加一个包中的文件。确定版本代码仓中的包的路径唯一，因此不会出现else
                    if (!metricsInfo.filesInPackageMap.containsKey(goPackagesList.get(i).getPath())) {
                        metricsInfo.filesInPackageMap.put(goPackagesList.get(i).getPath(), fileList);
                    } else {
                        log.info("data error: 同一版本的仓库中出现同名包路径:版本号为"+version+";仓库名为："+reopPackage.getKey()+
                                "；重复的路径名为："+goPackagesList.get(i).getPath());
                    }

                    //存储该包中的所有文件依赖的包路径(去重)
                    List<String> packagesPath = new ArrayList<>();
                    for (int j = 0; j < fileList.size(); j++) {
                        //获取这个文件中的包
                        List<GoPackage> packagesInFile = fileList.get(j).getGoPackages();
                        for(GoPackage goPackage: packagesInFile){
                            if(!packagesPath.contains(goPackage.getPath())){
                                packagesPath.add(goPackage.getPath());
                            }
                        }
                    }
                    //一个包依赖的所有包
                    if (!metricsInfo.packagesInPackageMap.containsKey(goPackagesList.get(i).getPath())) {
                        metricsInfo.packagesInPackageMap.put(goPackagesList.get(i).getPath(), packagesPath);
                    } else {
                        log.info("data error: 同一版本的仓库中出现同名包路径2:版本号为"+version+";仓库名为："+reopPackage.getKey() +
                                "；重复的路径名为："+goPackagesList.get(i).getPath());
                    }
                }
            }
        }

        List<Microservice> microservicesList = microservicesInfo.getMicroservices();

        for(Microservice microservice: microservicesList){
            if(!metricsInfo.getPubTopicsMap().containsKey(microservice.getElementName())){
                metricsInfo.getPubTopicsMap().put(microservice.getElementName(),microservice.getPubTopics());
            }
            if(!metricsInfo.getSubTopicsMap().containsKey(microservice.getElementName())){
                metricsInfo.getSubTopicsMap().put(microservice.getElementName(),microservice.getSubTopics());
            }
        }



        for (Map.Entry<String, List<GoPackage>> microEntry : metricsInfo.packagesInMicroMap.entrySet()) {
            String microserviceName = microEntry.getKey();
//            if(!microserviceName.equals("x_13/x_46f")){continue;}
            Metrics metrics = new Metrics();
            metrics.setElementName(microserviceName);
            metrics.setPackagesInMicroList(metricsInfo.getTypicalPackages(microserviceName, metricsInfo.packagesInMicroMap));
            metrics.setPackagesInPackageMap(metricsInfo.getTypicalPackToPack(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.packagesInPackageMap));
            metrics.setPackagesDependenceInSameMicro(metricsInfo.getPackDependencyInSameMicro(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.packagesInPackageMap));
            metrics.setPackagesInFileMap(metricsInfo.getTypicalPacksInFile(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.filesInPackageMap));
            metrics.setFilesInPackageMap(metricsInfo.getTypicalFilesInPack(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.filesInPackageMap));
            metrics.setDependPacksMap(metricsInfo.getPackDependencyMap(metrics));
            //以上六个参数必须在前计算确定
            metrics.setInitMatrix(metricsInfo.createPackDependencyMatrix(metrics));
            metrics.setPropagationCost(metricsInfo.calPropagationCost(metrics));
            metrics.setPackageCout(metricsInfo.calpackagecout(metrics));
            metrics.setPackageIndependenceLevel(metricsInfo.calPackIndependenceLevel(metrics));

            metrics.setFileCount(metricsInfo.getTotalFileNumInMicro(metrics));
//                tempTotalFilesInAll+=getTotalFileNumInMicro(metrics);
            metrics.setFileIndependenceLevel(metricsInfo.calFileIndependenceLevel(metrics));
            metricsInfo.metricsList.add(metrics);


            for(Microservice microservice: microservicesList){
                if(microservice.getElementName().equals(microserviceName)){
                    metrics.setSubTopicsCount(microservice.getSubTopics().size());
                    metrics.setPubTopicsCount(microservice.getPubTopics().size());
                    metrics.setSubTopicsOneOf(microservice.getSubTopicOneOf());
                    break;
                }
            }

            Set<String> mySubtopicsSet=metricsInfo.getSubTopicsMap().get(microserviceName);
            Set<String> myPubtopicsSet=metricsInfo.getPubTopicsMap().get(microserviceName);

            int fanInCoupling=0,fanOutCoupling=0;
            for(String subtopicName: mySubtopicsSet) {
                //查看本微服务中订阅的每个接口 来自其他哪个微服务发布的接口
                for (Map.Entry<String, Set<String>> pubMic : metricsInfo.getPubTopicsMap().entrySet()) {
                    if (!microserviceName.equals(pubMic.getKey())) {
//                        continue;
                        Set<String> pubtopicsList = pubMic.getValue();
                        for (String pubtopicName : pubtopicsList) {
                            if (subtopicName.equals(pubtopicName)) {
                                Map<String, List<String>> subscribeMicroMap = metrics.getSubscribeMicroMap();
                                if (!subscribeMicroMap.containsKey(pubMic.getKey())) {
                                    subscribeMicroMap.put(pubMic.getKey(), new ArrayList<>());
                                }
                                subscribeMicroMap.get(pubMic.getKey()).add(pubtopicName);
                                fanInCoupling++;
                            }
                        }
                    }

                }
            }

            for(String pubtopicName: myPubtopicsSet) {
                //查看本微服务中发布的每个接口 会被其他哪个微服务订阅
                for (Map.Entry<String, Set<String>> subMic : metricsInfo.getSubTopicsMap().entrySet()) {
                    Set<String> subtopicsList = subMic.getValue();
                    if (!microserviceName.equals(subMic.getKey())) {
                        for (String subtopicName : subtopicsList) {
                            if (subtopicName.equals(pubtopicName)) {
                                Map<String, List<String>> publishMicroMap = metrics.getPublishMicroMap();
                                if (!publishMicroMap.containsKey(subMic.getKey())) {
                                    publishMicroMap.put(subMic.getKey(), new ArrayList<>());
                                }
                                publishMicroMap.get(subMic.getKey()).add(subtopicName);
                                fanOutCoupling++;
                            }
                        }
                    }
                }
            }
            //计算微服务间的扇入、扇出耦合
            metrics.setFanInCoupling(fanInCoupling);
            metrics.setFanOutCoupling(fanOutCoupling);
            metrics.setFanInAndOutCoupling(fanInCoupling+fanOutCoupling);
            metrics.setFanInMultiOutCoupling(fanInCoupling*fanOutCoupling);
            //微服务间相互依赖耦合
            int interdependenceCoupling=0;
            Map<String,List<String>> publishMicroMap=metrics.getPublishMicroMap();
            for(Map.Entry<String,List<String>> entry:publishMicroMap.entrySet()){
                if(metrics.getSubscribeMicroMap().containsKey(entry.getKey())){
                    interdependenceCoupling++;
                }
            }
            metrics.setInterdependenceCoupling(interdependenceCoupling);
            //计算微服务内的扇入/扇出耦合乘积之和
            long fanInMultiOutCouplingInSameMicro=0;
            Map<String,List<String>> dependPacksMapInSameMicro=metrics.getPackagesDependenceInSameMicro();
            for(Map.Entry<String,List<String>> dependPackInSameMicro: dependPacksMapInSameMicro.entrySet()){
                int inCoupling=dependPackInSameMicro.getValue().size();
                int outCoupling=0;
                if(metrics.getDependPacksMap().get(dependPackInSameMicro.getKey())!=null){
                    outCoupling=metrics.getDependPacksMap().get(dependPackInSameMicro.getKey()).size();
                }

                fanInMultiOutCouplingInSameMicro+=((inCoupling*outCoupling)==0?inCoupling+outCoupling:inCoupling*outCoupling);
            }
            metrics.setFanInMultiOutCouplingInSameMicro(fanInMultiOutCouplingInSameMicro);


            //计算IUCohesion 和 pubtopicsOneOf
            int pubtopicsNum=metrics.getPubTopicsCount();
            Map<String,List<String>> pubtopicsMap=metrics.getPublishMicroMap();
            int totalClientSubtopics=0;

            for(Map.Entry<String,List<String>> entry:pubtopicsMap.entrySet()){
                totalClientSubtopics+=entry.getValue().size();
                List<String> pubTopics=entry.getValue();
                for(Microservice microservice:microservicesInfo){
                    if(microservice.getElementName().equals(entry.getKey())){
                        Map<String,Integer> subtopicOneOf=microservice.getSubTopicOneOf();
                        for(Map.Entry<String,Integer> subtopic: subtopicOneOf.entrySet()){
                            if(pubTopics.contains(subtopic.getKey())){
                                if(!metrics.getPubTopicsOneOf().containsKey(subtopic.getKey())){
                                    metrics.getPubTopicsOneOf().put(subtopic.getKey(),subtopic.getValue());
                                }else{
                                    Integer oldPubtopicOneOfNum=metrics.getPubTopicsOneOf().get(subtopic.getKey());
                                    metrics.getPubTopicsOneOf().put(subtopic.getKey(),Math.max(oldPubtopicOneOfNum,subtopic.getValue()));
                                }
                            }
                        }
                    }
                }
            }
            metrics.setIUCohesion(1.0*totalClientSubtopics/(pubtopicsNum*pubtopicsMap.size()));



            Map<String,Integer> subTopicsOneOf=metrics.getSubTopicsOneOf();
            int dataStructureCount=0;
            for(Map.Entry<String,Integer> topicOneOf:subTopicsOneOf.entrySet()){
                dataStructureCount+=topicOneOf.getValue();
                if(topicOneOf.getValue()==0){
                    dataStructureCount+=1;
                }
            }
            metrics.setSubDataStructureCount(dataStructureCount+metrics.getSubTopicsCount()-subTopicsOneOf.size());

            Map<String,Integer> pubTopicsOne=metrics.getPubTopicsOneOf();
            int pubDataStructureCount=0;
            for(Map.Entry<String,Integer> topicOneOf: pubTopicsOne.entrySet()){
                pubDataStructureCount+=topicOneOf.getValue();
                if(topicOneOf.getValue()==0){
                    pubDataStructureCount+=1;
                }
            }
            metrics.setPubDataStructureCount(pubDataStructureCount+metrics.getPubTopicsCount()-pubTopicsOne.size());

            metrics.setAllDataStructureCount(metrics.getSubDataStructureCount()+metrics.getPubDataStructureCount());

            //计算接口数据类型相似度
            int subTopicSimilarityUp=0;
            for(Map.Entry<String,Integer> subTopicOO : subTopicsOneOf.entrySet()){
                int subTopicValue=subTopicOO.getValue();
                if(subTopicValue==0){
                    subTopicSimilarityUp+=1;
                }else{
                    subTopicSimilarityUp=subTopicSimilarityUp+(1+subTopicValue)*subTopicValue/2;
                }
            }
            subTopicSimilarityUp=subTopicSimilarityUp+metrics.getSubTopicsCount()-subTopicsOneOf.size();
            int subTopicSimilarityDown=metrics.getSubDataStructureCount();
            metrics.setSubTopicSimilarity(1.0*subTopicSimilarityUp/((1+subTopicSimilarityDown)*subTopicSimilarityDown/2));

            int pubTopicSimilarityUp=0;
            for(Map.Entry<String,Integer> pubTopicOO : pubTopicsOne.entrySet()){
                int pubTopicValue=pubTopicOO.getValue();
                if(pubTopicValue==0){
                    pubTopicSimilarityUp+=1;
                }else{
                    pubTopicSimilarityUp=pubTopicSimilarityUp+(1+pubTopicValue)*pubTopicValue/2;
                }
            }
            pubTopicSimilarityUp=pubTopicSimilarityUp+metrics.getPubTopicsCount()-pubTopicsOne.size();
            int pubTopicSimilarityDown=metrics.getPubDataStructureCount();
            metrics.setPubTopicSimilarity(1.0*pubTopicSimilarityUp/((1+pubTopicSimilarityDown)*pubTopicSimilarityDown/2));


            //计算连通块，连通路径等
//            if(metrics.getElementName().equals("x_13/x_46f")){
                metrics.setConnectingBlocks(metrics.calConnectingBlocks());
                metrics.calPathNum();
//            }
//
            //计算包、文件的独立等级
//            if(metrics.getElementName().equals("x_13/x_46f")){
//                metrics.calPackageDependenceLevel();
//            }



        }

        return metricsInfo;
    }

//    public static void main(String[] args) throws ActionExecuteFailedException {
//
////        ReopDividedByRevision reopDivided = new ReopDividedByRevision();
//////        QueryGoPackages queryGoPackages = new QueryGoPackages();
////        //按照不同版本获取不同的代码仓
////         reopRevisionMap  = reopDivided.getReopRevisionMap();
////        //获取所有微服务名称
////        microservicesNames = reopDivided.getMicroservicesNames();
//
//        QueryGoPackages queryGoPackages = new QueryGoPackages();
//        reopRevisionMap = queryGoPackages.getGoPackageMap();
//        microservicesNames = queryGoPackages.getMicroservicesNames();
//
//        int tempTotalFilesInAll = 0;
//
//        //按照不同的代码仓版本来遍历每个reop
//        for ( Map.Entry<String, Map<String, GoPackagesInfo>> entry : reopRevisionMap.entrySet()) {
//                //一个版本对应一个metricsInfo
//                MetricsInfo metricsInfo = new MetricsInfo();
//                //设置仓库版本号名称
//                metricsInfo.setRevision(entry.getKey());
//                //按照版本对所有代码仓进行分类
//                Map<String, GoPackagesInfo> reopPackages = entry.getValue();
//                System.out.println("版本号："+entry.getKey());
//                int indexNum=0;
//
//                for(Map.Entry<String, GoPackagesInfo> reopPackage : reopPackages.entrySet()){
//                    //获取单个版本的reop下的所有package
//                    List<GoPackage> goPackagesList = reopPackage.getValue().getGoPackages();
//
//                    for (int i = 0; i < goPackagesList.size(); i++) {
//                        //去除包路径中的大项目前缀
////                        String refinedPackageName = excludeProjName(goPackagesList.get(i).getPath());
//                        String refinedPackageName=excludeProjName(goPackagesList.get(i).getPath());
//                        String microserviceName = metricsInfo.MatchedMicroServiceName(entry.getKey(),metricsInfo,refinedPackageName);
//                        //如果判断属于微服务项目，那么添加到对应的微服务-package集合中
//                        if (!microserviceName.equals("-1")) {
//                            metricsInfo.packagesInMicroMap.get(microserviceName).add(goPackagesList.get(i));
//                            //获取这个package中的文件集合
//                            List<GoFile> fileList = goPackagesList.get(i).getGoFiles();
//                            //添加一个包中的文件。确定版本代码仓中的包的路径唯一，因此不会出现else
//                            if (!metricsInfo.filesInPackageMap.containsKey(goPackagesList.get(i).getPath())) {
//                                metricsInfo.filesInPackageMap.put(goPackagesList.get(i).getPath(), fileList);
//                            } else {
//                                log.info("data error: 同一版本的仓库中出现同名包路径:版本号为"+entry.getKey()+";仓库名为："+reopPackage.getKey()+
//                                        "；重复的路径名为："+goPackagesList.get(i).getPath());
//                            }
//
//                            //存储该包中的所有文件依赖的包路径(去重)
//                            List<String> packagesPath = new ArrayList<>();
//                            for (int j = 0; j < fileList.size(); j++) {
//                                //获取这个文件中的包
//                                List<GoPackage> packagesInFile = fileList.get(j).getGoPackages();
//                                for(GoPackage goPackage: packagesInFile){
//                                    if(!packagesPath.contains(goPackage.getPath())){
//                                        packagesPath.add(goPackage.getPath());
//                                    }
//                                }
////                            //一个文件中的所有包
////                            if (!packagesInFileMap.containsKey(goPackagesList.get(i).getPath() + "/" + fileList.get(j))) {
////                                packagesInFileMap.put(goPackagesList.get(i).getPath() + "/" + fileList.get(j).getName(), packagesInFile);
////                            } else {
////                                packagesInFileMap.get(goPackagesList.get(i).getPath() + "/" + fileList.get(j)).addAll(packagesInFile);
////                            }
//
//                            }
//                            //一个包依赖的所有包
//                            if (!metricsInfo.packagesInPackageMap.containsKey(goPackagesList.get(i).getPath())) {
//                                metricsInfo.packagesInPackageMap.put(goPackagesList.get(i).getPath(), packagesPath);
//                            } else {
//                                log.info("data error: 同一版本的仓库中出现同名包路径2:版本号为"+entry.getKey()+";仓库名为："+reopPackage.getKey() +
//                                            "；重复的路径名为："+goPackagesList.get(i).getPath());
//                            }
//                        }
//                    }
//                }
////                System.out.println("版本"+entry.getKey()+"中合计"+indexNum+"个goPackage");
//
//            for (Map.Entry<String, List<GoPackage>> microEntry : metricsInfo.packagesInMicroMap.entrySet()) {
//                String microserviceName = microEntry.getKey();
//
//                Metrics metrics = new Metrics();
//                metrics.setElementName(microserviceName);
//                metrics.setPackagesInMicroList(metricsInfo.getTypicalPackages(microserviceName, metricsInfo.packagesInMicroMap));
//                metrics.setPackagesInPackageMap(metricsInfo.getTypicalPackToPack(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.packagesInPackageMap));
//                metrics.setPackagesDependenceInSameMicro(metricsInfo.getPackDependencyInSameMicro(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.packagesInPackageMap));
//                metrics.setPackagesInFileMap(metricsInfo.getTypicalPacksInFile(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.filesInPackageMap));
//                metrics.setFilesInPackageMap(metricsInfo.getTypicalFilesInPack(microserviceName, metricsInfo.packagesInMicroMap, metricsInfo.filesInPackageMap));
//                metrics.setDependPacksSet(metricsInfo.getPackDependencySet(metrics));
//                //以上六个参数必须在前计算确定
//                metrics.setInitMatrix(metricsInfo.createPackDependencyMatrix(metrics));
//                metrics.setPropagationCost(metricsInfo.calPropagationCost(metrics));
//
//                metrics.setPackageIndependenceLevel(metricsInfo.calPackIndependenceLevel(metrics));
//                metrics.setFileCount(metricsInfo.getTotalFileNumInMicro(metrics));
////                tempTotalFilesInAll+=getTotalFileNumInMicro(metrics);
//                metrics.setFileIndependenceLevel(metricsInfo.calFileIndependenceLevel(metrics));
//                metricsInfo.metricsList.add(metrics);
//
////                if(entry.getKey().equals("x_1635-x_95d.x_893.x_935_x_1ff_x_e0a9_x_2922b") && microserviceName.equals("x_1f")){
////                    List<GoFile> fileList = metrics.getFilesInPackageMap().get(microserviceName);
////                    for(GoFile file : fileList){
////                        System.out.println("x_1f中的文件名分别为："+file.getName());
////                    }
////                }
//
//            }
//            if(entry.getKey().equals("xx_1635-x_95d.x_4af.x_893.x_ec8b_x_1ff_x_2922d")){
//                List<String> serviceNames=microservicesNames.get(entry.getKey());
//                for(String s:serviceNames){
//                    System.out.println("版本x_2922d中的微服务名称为："+s);
//                }
//            }
//
//            for (Metrics metrics : metricsInfo.metricsList) {
//                //"x_f/x_59 20个包"
////            if (metrics.getElementName().equals("x_33/x_744d")) {
//                System.out.println(metrics);
////            }
////            System.out.println(metrics.getElementName());
//            }
////        System.out.println("总共有"+tempTotalFilesInAll+"个文件");
//
//            log.info("版本"+entry.getKey()+"中共多少个微服务中"+metricsInfo.packagesInMicroMap.size());
//
//        }
////        //打印版本x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1中的所有微服务名称
////        System.out.println("打印版本x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1中的所有微服务名称");
////        List<String> servicesNamesInVersion=microservicesNames.get("x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1");
////        for(String s:servicesNamesInVersion){
////            System.out.println(s);
////        }
////        for (Map.Entry<String, Set<GoPackage>> entry : packagesInPackageMap.entrySet()) {
////            if ("x_3.x_5.x_7/x_9/x_b/x_d/x_33/x_744d/x_5b".equals(entry.getKey())) {
////                System.out.println("它依赖" + entry.getValue().size() + "个包");
////            }
////        }
//
////        int totalPackageInMicro=0;
////        for(Map.Entry<String,List<GoPackage>> entry : packagesToMicroMap.entrySet()){
////            System.out.println("在微服务"+entry.getKey()+"中有"+entry.getValue().size());
////            totalPackageInMicro+=entry.getValue().size();
////        }
////        System.out.println("在23个微服务中共有"+totalPackageInMicro+"个包");
////        int index=0;
////        for(Map.Entry<String,Set<GoPackage>> entry: packagesInPackageMap.entrySet()){
////            index++;
////            System.out.println("package"+index+entry.getKey()+"依赖"+entry.getValue().size());
////        }
////        index=0;
////        for(Map.Entry<String,List<GoPackage>> entry: packagesInFileMap.entrySet()){
////            index++;
////            System.out.println("文件"+index+entry.getKey()+"中有"+entry.getValue().size());
////        }
////        index=0;
////        for(Map.Entry<String,List<GoFile>> entry: filesInPackageMap.entrySet()){
////            index++;
////            System.out.println("包"+index+entry.getKey()+"共有"+entry.getValue().size()+"个文件");
////        }
//
////        int index=0;
////        for(int i=0;i<metricsList.size();i++){
////            System.out.println("微服务数量"+metricsList.get(i));
////            Map<String,Set<GoPackage>> map = metricsList.get(i).getPackagesInPackageMap();
////            for(Map.Entry<String,Set<GoPackage>> entry: map.entrySet()){
////                index++;
////                System.out.println("包"+index+entry.getKey()+"依赖"+entry.getValue().size()+"个包");
////            }
////        }
////        System.out.println("共有"+metricsInfoList.size()+"个版本的代码仓");
//
//    }

    /**
     * 获取指定微服务名下的所有包
     *
     * @param microserviceName
     * @param packInMicro      微服务与包 对应的map
     * @return
     */
    public List<GoPackage> getTypicalPackages(String microserviceName, Map<String, List<GoPackage>> packInMicro) {
        List<GoPackage> typicalPackages = new ArrayList<>();
        for (Map.Entry<String, List<GoPackage>> entry : packInMicro.entrySet()) {
            if (microserviceName.equals(entry.getKey())) {
                //得到特定微服务下的所有包
                typicalPackages = entry.getValue();
            }
        }
        if (typicalPackages == null) {
            throw new RuntimeException("未找到特定微服务中的包，请检查微服务名称");
        }
        return typicalPackages;
    }

    /**
     * 获取特定微服务的包与包的依赖关系，所依赖的包可以来自其他的微服务
     *
     * @param microserviceName
     * @param packInMicro      微服务与包 对应的map
     * @param packToPackMap    包与包 对应的map
     * @return (包名 ， 包路径集合) value中的包可以来自不同的微服务
     */
    public  Map<String, List<String>> getTypicalPackToPack(String microserviceName, Map<String, List<GoPackage>> packInMicro, Map<String, List<String>> packToPackMap) {
        //返回的Map
        Map<String, List<String>> typicalMap = new HashMap<>();
        List<GoPackage> typicalPackages = getTypicalPackages(microserviceName, packInMicro);

        for (int i = 0; i < typicalPackages.size(); i++) {
            //如果在packToPackMap中找到了指定的包，则在范围的map中添加相应的依赖关系
            if (packToPackMap.containsKey(typicalPackages.get(i).getPath())) {
                typicalMap.put(typicalPackages.get(i).getPath(), packToPackMap.get(typicalPackages.get(i).getPath()));
            }
        }
        return typicalMap;
    }

    /**
     * 获取特定微服务下的包与包的依赖关系，所依赖的包只能是来自该特定微服务下的包
     *
     * @param microserviceName
     * @param packInMicro      微服务与包 对应的map
     * @param packToPackMap    包与包 对应的map
     * @return （包名，包路径集合） value中的包必须来自同一个微服务
     */
    public  Map<String, List<String>> getPackDependencyInSameMicro(String microserviceName, Map<String, List<GoPackage>> packInMicro, Map<String, List<String>> packToPackMap) {
        Map<String, List<String>> typicalMap = new HashMap<>();
        List<GoPackage> typicalPackages = getTypicalPackages(microserviceName, packInMicro);

        for (int i = 0; i < typicalPackages.size(); i++) {
            if (packToPackMap.containsKey(typicalPackages.get(i).getPath())) {
                //获取某特定微服务下的package依赖的所有package
                List<String> packsPath = packToPackMap.get(typicalPackages.get(i).getPath());
                //需要重新开辟空间，如果直接在set上操作，会导致packToPackMap中的Set发生变化
                List<String> packToPackInSameMicro = new ArrayList<>();
                packToPackInSameMicro.addAll(packsPath);

                //遍历这个packToPackInSameMicro，去除非本微服务中的package
                Iterator<String> iterator = packToPackInSameMicro.iterator();
                while (iterator.hasNext()) {
                    String goPackagePath = iterator.next();
                    boolean flag = true;
                    //如果该package所依赖的包不在本微服务中，即包的路径名不相同
                    for (GoPackage goPackage : typicalPackages) {
                        if (goPackage.getPath().equals(goPackagePath)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        iterator.remove();
                    }

                }
                typicalMap.put(typicalPackages.get(i).getPath(), packToPackInSameMicro);
            }
        }
        return typicalMap;
    }

    /**
     * 获取指定微服务名称下的所有package包含的文件
     *
     * @param microserviceName
     * @param packInMicro      微服务与包 对应的map
     * @param filesInPackMap   包与文件 对应的map
     * @return （包名，包中文件）
     */
    public  Map<String, List<GoFile>> getTypicalFilesInPack(String microserviceName, Map<String, List<GoPackage>> packInMicro, Map<String, List<GoFile>> filesInPackMap) {
        Map<String, List<GoFile>> typicalMap = new HashMap<>();
        List<GoPackage> typicalPackages = getTypicalPackages(microserviceName, packInMicro);
        for (int i = 0; i < typicalPackages.size(); i++) {
            if (filesInPackMap.containsKey(typicalPackages.get(i).getPath())) {
                typicalMap.put(typicalPackages.get(i).getPath(), filesInPackMap.get(typicalPackages.get(i).getPath()));
            }
        }
        return typicalMap;
    }

    /**
     * 获取指定微服务名称下的所有文件中依赖的包
     *
     * @param microserviceName
     * @param packInMicro      微服务与包 对应的map
     * @param filesInPackMap   包与文件 对应的map
     * @return (文件名 ， 文件依赖的包)
     */
    public  Map<String, List<GoPackage>> getTypicalPacksInFile(String microserviceName, Map<String, List<GoPackage>> packInMicro, Map<String, List<GoFile>> filesInPackMap) {
        //获取不同微服务下，不同的包所包含的文件 (包名，文件列表)
        Map<String, List<GoFile>> typicalFilesInPack = getTypicalFilesInPack(microserviceName, packInMicro, filesInPackMap);
        Map<String, List<GoPackage>> typicalMap = new HashMap<>();
        for (Map.Entry<String, List<GoFile>> entry : typicalFilesInPack.entrySet()) {
            List<GoFile> fileList = entry.getValue();
            for (GoFile file : fileList) {
                typicalMap.put(entry.getKey() + "/" + file.getName(), file.getGoPackages());
            }
        }
        return typicalMap;
    }

    /**
     * 获取被本微服务中的包所依赖的 本微服务中的其他包路径
     * 它的前置条件为在metrics中设置了“单个微服务与多个package对应packagesInMicroList”、
     *                              "单个微服务中的一个包依赖自身微服务中的包packagesDependenceInSameMicro"这两个参数
     * @param metrics
     * @return
     */
    public Map<String,Set<String>> getPackDependencyMap(Metrics metrics){
        //获取该微服务中包内之间的依赖关系，依赖的包只能来自本微服务中
        Map<String, List<String>> packagesDependenceInSameMicro = metrics.getPackagesDependenceInSameMicro();
        //一个包 被那些包依赖
        Map<String,Set<String>> packDependencyMap = new HashMap<>();
//        Set<String> packDependencySet = new HashSet<>();
        //获取本微服务被依赖的所有包

        int corrletions=0;
        for(Map.Entry<String,List<String>> entry: packagesDependenceInSameMicro.entrySet()){
            List<String> packagesNames=entry.getValue();
            for(String s:packagesNames){
                if(!packDependencyMap.containsKey(s)){
                    packDependencyMap.put(s,new HashSet<String>());
                }
                packDependencyMap.get(s).add(entry.getKey());
                corrletions++;

            }
//            packDependencySet.addAll(entry.getValue());
        }
        metrics.setFanInOrOutCouplingInSameMicro(corrletions);

//        metrics.setDependPacksMap(packDependencyMap);
        return packDependencyMap;
//        return packDependencySet;
    }
    /**
     * 计算包的独立等级
     * 它的前置条件为在metrics中设置了“单个微服务与多个package对应packagesInMicroList”、
     *                              "单个微服务中的一个包依赖自身微服务中的包packagesDependenceInSameMicro"这两个参数
     * @param metrics
     * @return
     */
    public  Double calPackIndependenceLevel(Metrics metrics){
        //获取该微服务中的所有包
        List<GoPackage> packagesInMicroList = metrics.getPackagesInMicroList();
        //获取本微服务被依赖的所有包的数量
//        Set<String> packDependencySet = getPackDependencySet(metrics);
        int packDependencyNum=metrics.getDependPacksMap().size();
        return 1 - 1.0*packDependencyNum/packagesInMicroList.size();
    }

    public  int calpackagecout(Metrics metrics){
        //获取该微服务中的所有包
        List<GoPackage> packagesInMicroList = metrics.getPackagesInMicroList();

        return packagesInMicroList.size();
    }
    /**
     * 获取该微服务内所有的文件数量
     * @param metrics
     * @return
     */
    public  Long getTotalFileNumInMicro(Metrics metrics){
        //获取该微服务中的一个包中有哪些文件
        Map<String, List<GoFile>> filesInPackageMap = metrics.getFilesInPackageMap();
        Long totalFileNum=0L;
        for(Map.Entry<String,List<GoFile>> entry:filesInPackageMap.entrySet()){
            totalFileNum+=entry.getValue().size();
        }
        return totalFileNum;
    }

    /**
     * 获取被本微服务中的其他包依赖 的包所有的文件个数
     * @param metrics
     * @return
     */
    public  Long getDependedFileNum(Metrics metrics){
        //获取本微服务被依赖的所有包
//        Set<String> packDependencySet = getPackDependencySet(metrics);
        Map<String,Set<String>> packDependencyMap=metrics.getDependPacksMap();
        //获取该微服务中的一个包中有哪些文件
        Map<String, List<GoFile>> filesInPackageMap = metrics.getFilesInPackageMap();
        //计算该微服务中的总文件个数，被本微服务中其他包 所依赖的包中的文件个数
        Long  dependedFileNum=0L;
        for(Map.Entry<String,List<GoFile>> entry:filesInPackageMap.entrySet()){
//            for(String goPackagePath: packDependencySet){
//                //如果该包被本微服务中的其他包依赖，则需要统计它包含的文件个数
//                if(entry.getKey().equals(goPackagePath)){
//                    dependedFileNum+=entry.getValue().size();
//                }
//            }
            if(packDependencyMap.containsKey(entry.getKey())){
                dependedFileNum+=entry.getValue().size();
            }
        }
        return dependedFileNum;
    }
    /**
     * 计算文件的独立等级
     * @param metrics
     * @return
     */
    public  Double calFileIndependenceLevel(Metrics metrics){
        Long totalFileNum =getTotalFileNumInMicro(metrics), dependedFileNum=getDependedFileNum(metrics);
        return 1- 1.0*dependedFileNum/totalFileNum;
    }

    /**
     * 创建微服务内 包之间的依赖关系初始状态矩阵
     * @param metrics
     * @return
     */
    public  int[][] createPackDependencyMatrix(Metrics metrics){
        //获取微服务中的所有包
        List<GoPackage> packagesInMicroList = metrics.getPackagesInMicroList();
        //给微服务中的每个包确定顺序，便于数组的初始状态输入
        Map<String,Integer> packNum = new HashMap<>();
        int rows=0;
        for(GoPackage goPackage: packagesInMicroList){
            packNum.put(goPackage.getPath(),rows++);
        }
        //获取微服务内包的依赖关系map（同一微服务中）
        Map<String,List<String>> packagesDependenceInSameMicro = metrics.getPackagesDependenceInSameMicro();

        //二维矩阵行数和列数相等
        int[][] initMatrix = new int[rows][rows];

        //初始化数组，默认所有包都没有依赖，即每个点之间都不连通
        for(int i=0;i<rows;i++){
            for(int j=0;j<rows;j++){
                if(i==j){
                    initMatrix[i][j]=1;
                }else{
                    initMatrix[i][j]=Integer.MAX_VALUE;
                }
            }
        }

        //初始状态输入，根据 微服务内包的依赖关系(同一微服务) 确定矩阵初始连通情况
        for(Map.Entry<String,List<String>> entry: packagesDependenceInSameMicro.entrySet()){
            //获取行号
            int rowNum=packNum.get(entry.getKey());
            for( String goPackagePathInSet : entry.getValue()){
                int colNum= packNum.get(goPackagePathInSet);
                //1 表示包与包有依赖关系
                initMatrix[rowNum][colNum]=1;
                //由于设定为 非有向图，因此需要镜像
                //initMatrix[colNum][rowNum]=1;
            }
        }
        return initMatrix;
    }
    public  Double calPropagationCost(Metrics metrics){
        //得到初始状态的包依赖矩阵
        int[][] initMatrix = metrics.getInitMatrix();

//        for(int i=0;i<initMatrix.length;i++){
//            for(int j=0;j<initMatrix[0].length;j++){
//                System.out.print(initMatrix[i][j]+"  ");
//            }
//            System.out.println();
//        }
        ShortestPathFloyd floydAlgorithm = new ShortestPathFloyd(initMatrix);
        floydAlgorithm.floyd();
        //计算传播成本
        int connectedNodes=0;
        for(int i=0;i<initMatrix.length;i++){
            for(int j=0;j<initMatrix[0].length;j++){
                if(floydAlgorithm.isConnected(i,j)){
                    connectedNodes+=1;
                }
            }
        }
        //d打印传播矩阵

        return 1-1.0*connectedNodes/(initMatrix.length*initMatrix.length);
    }


    /**
     * 去除packageName前缀的大项目名称
     * @param packageName
     * @return
     */
    public static String excludeProjName(String packageName) {
//        System.out.println("packageName为："+packageName);
        String[] packageNames=packageName.split(projName);
        if(packageNames.length>1){
            return  packageNames[1];
        }else{
            return packageName;
        }
//        return packageNames[1];
    }

    /**
     * 判断package路径名称是否在微服务名称集合中
     * @param refinedPackageName 去除主项目的package路径名
     * @return 如果在微服务集合中，则返回微服务的绝对路径，否则返回-1
     */
    public  String MatchedMicroServiceName(MetricsInfo metricsInfo, String refinedPackageName) {
        List<String> serviceNames=metricsInfo.getMicroservicesNames();
        //打印版本x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1中的所有微服务名称
//        System.out.println("打印版本x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1中的所有微服务名称");


        for (int i = 0; i < serviceNames.size(); i++) {
            if (refinedPackageName.startsWith(serviceNames.get(i))) {
                //匹配到微服务路径，添加到对应集合中
                if (metricsInfo.packagesInMicroMap.get(serviceNames.get(i)) == null) {
                    metricsInfo.packagesInMicroMap.put(serviceNames.get(i), new ArrayList<>());
                }
//                if(metricsInfo.revision.equals("x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1")){
//
//                }
                return serviceNames.get(i);
            }
        }
        //x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1
//        if(versionName.equals("x_1635-x_95d.x_4af.x_893.x_ec8b_x_1ff_x_2922d")){
//            System.out.println("打印版本x_3c9_x_95d.x_893.x_893.x_e09d_x_43_x_8b_x_e09f_x_e0a1中的所有微服务名称");
//            for(String s:serviceNames){
//                System.out.println(s);
//            }

//            System.out.println("前来匹配的包名为：x_3.x_5.x_7/x_9/x_b/x_d/"+refinedPackageName);
//        }
        return "-1";
    }


}
