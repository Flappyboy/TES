package top.jach.tes.app.jhkt.chenjiali;

import org.apache.commons.collections.ArrayStack;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.Threshold.ThresholdResult;
import top.jach.tes.plugin.jhkt.arcsmell.ui.UiAction;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.jhkt.smellCallable.SloppyCallable;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author:AdminChen
 * @date:2020/5/23
 * @description:
 */
//这边只负责供给各种不同的源数据，用线程池来并发计算AS，并拿到各个AS计算得到的结果
public class ThresholdTest extends DevApp {
    public static void main(String[] args){
        System.out.println(testUiThreshold().size());
        List<ThresholdResult> data=isExpected(testUiThreshold());//符合预期的源数据+运行结果集合

    }

    public static List<ThresholdResult> testUiThreshold(){
        //存放各个线程运行结果
        List<ThresholdResult> resuts=new ArrayList<>();
        //新建线程池
        ExecutorService executor=new ThreadPoolExecutor(5, 15, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        VersionsInfo versionsInfoForRelease = DataAction.queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

        for (int i = 0; i < versionsInfoForRelease.getVersions().size()-1; i++) {
            Version version = versionsInfoForRelease.getVersions().get(i);
            // 查询version版本下的所有微服务
            MicroservicesInfo microservices = DataAction.queryLastMicroservices(context, reposInfo.getId(), null, version);

            //存储单个版中所有微服务名称
            List<String> microserviceNames = microservices.microserviceNames();
            //获取pairrelationsInfo
            PairRelationsInfo pairRelationsInfoWithoutWeight = microservices.callRelationsInfoByTopic(false).deWeight();
            pairRelationsInfoWithoutWeight.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
            InfoTool.saveInputInfos(pairRelationsInfoWithoutWeight);

            //查询version版本下所有微服务的commit信息
            Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap = new HashMap<>();
            List<GitCommit> gct=new ArrayList<>();
            for(Microservice microservice: microservices){//由于MicroserviceInfo类实现了Iterator方法，因此可以这样遍历
                GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = DataAction.queryLastGitCommitsForMicroserviceInfo(context, reposInfo.getId(), microservice.getElementName(), version);
                gitCommitsForMicroserviceInfoMap.put(microservice.getElementName(),gitCommitsForMicroserviceInfo);
                if(gitCommitsForMicroserviceInfo==null){
                    //System.out.println("GitCommitsForMicroserviceInfo  "+microservice.getElementName()+"  "+version.getVersionName());
                    continue;
                }
                gct.addAll(gitCommitsForMicroserviceInfo.getGitCommits());
            }
            //给gitCommits去重
            List<GitCommit> gitCommits=gct.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getReposId() + "#" + o.getRepoName() + "#" + o.getSha()))),ArrayList::new));;

            //获得当前数据源下所有阈值的组合，每一个内嵌的list都是一组阈值
            List<List<Double>> thrs=new ArrayList<>();
            for(int ie=3;ie<7;ie++){
                for(int j=5;j<13;j++){
                    for(int k=3;k<7;k++){
                        for(int h=5;h<11;h++){
                            List<Double> list=new ArrayList<>();
                            list.add((double)ie);
                            list.add((double)j);
                            list.add((double)k);
                            list.add((double)h);
                            thrs.add(list);
                        }
                    }
                }
            }
            //根据多种阈值组合计算AS
            for(int l=0;l<thrs.size();l++){
                synchronized (thrs){
                    int len=(thrs.get(l).get(0)).intValue();
                    double impact=thrs.get(l).get(1);
                    double cochange=thrs.get(l).get(2);
                    double change=thrs.get(l).get(3);
                    int finalL = l;
                    executor.execute(() -> {
                        ElementsValue elementsValue=null;
                        elementsValue = UiAction.calculateUi(gitCommits,len,microservices,pairRelationsInfoWithoutWeight,impact,cochange,change);
                        if(elementsValue!=null){
                                synchronized (resuts){
                                    resuts.add(new ThresholdResult(microservices,pairRelationsInfoWithoutWeight,thrs.get(finalL),elementsValue));
                                }
                        }

                    });
                }
            }
            executor.shutdown();


        }
        return resuts;
    }

   public static List<ThresholdResult> isExpected(List<ThresholdResult> data){
        List<ThresholdResult> result=new ArrayList<>();
       //新建线程池
       ExecutorService executor=new ThreadPoolExecutor(5, 15, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        //这里也要用到并发判断，新写一个方法专门判断一个list<double>集合中的数据分布是否符合规律，然后在excutor中调用该方法
        for(ThresholdResult thrsult:data){
            List<Double> evalues=new ArrayList<>(thrsult.getElementsValue().getValueMap().values());
            executor.execute(() -> {
                if(judge(evalues)){
                    synchronized (result){
                        result.add(thrsult);
                    }
                }
            });

        }
        executor.shutdown();
        return result;

    }
//什么样的数据是我们想要的？
    public static boolean judge(List<Double> values){

        return false;

    }



}
