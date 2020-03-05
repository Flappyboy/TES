package top.jach.tes.plugin.jhkt.maintain;

import lombok.Getter;
import org.eclipse.jgit.diff.DiffEntry;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.dts.Dts;
import top.jach.tes.plugin.jhkt.dts.DtssInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.DiffFile;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.commit.StatisticDiffFiles;

import java.util.*;

@Getter
public class MainTainsInfo extends Info {
    private Long reposId;
    private Long startTime;
    private Long endTime;
    private String version;
    private InfoProfile elementsInfo;
    private List<MainTain> mainTainList = new ArrayList<>();
//根据时间段获取可维护性指标
    public static MainTainsInfo createInfo(Long reposId,
                                           MicroservicesInfo microservicesInfo,
                                           Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap,
                                           DtssInfo dtssInfo,
                                           PairRelationsInfo bugAndMicroserviceRelations,
                                           Long startTime, Long endTime){
        MainTainsInfo info = new MainTainsInfo();
        info.initBuild();
        info.setReposId(reposId)
                .setStartTime(startTime)
                .setEndTime(endTime);
        List<Dts> dtss = dtssInfo.getBugs();

        // 建立问题单的Name和问题单的映射，方便用Name查找问题单
        Map<String, Dts> nameDtsMap = new HashMap<>();
        for (Dts dts:dtss) {
            nameDtsMap.put(dts.getName(), dts);
        }
        // 遍历微服务，求每个微服务下的维护相关的指标
        for (Microservice microservice:
              microservicesInfo) {
            MainTain mainTain = new MainTain();
            mainTain.setElementName(microservice.getElementName());
            // 每个微服务下的bug数
            long dtsCount = 0;
            for (PairRelation relation :
                    bugAndMicroserviceRelations.getRelations()) {
                Dts dts = nameDtsMap.get(relation.getSourceName());
                if(dts==null || !relation.getTargetName().equals(microservice.getElementName()) || dts.getFindTime()<startTime || dts.getFindTime()>=endTime){
                    continue;
                }
                dtsCount++;
            }
            mainTain.setBugCount(dtsCount);

            GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = gitCommitsForMicroserviceInfoMap.get(microservice.getElementName());
            if (gitCommitsForMicroserviceInfo != null){
                List<GitCommit> gitCommits = gitCommitsForMicroserviceInfo.getGitCommits();
                StatisticDiffFiles statisticDiffFiles = new StatisticDiffFiles();
                Long commitCount = 0l;
                Set<String> changeFileSet = new HashSet<>();
                Map<String, Set<String>> commiterChangeFileMap = new HashMap<>();
                Long allChangeFileCount = 0l;
                for (GitCommit gitCommit :
                        gitCommits) {
                    // 根据时间段过滤
                    if(gitCommit.getCommitTime()<startTime/1000 || gitCommit.getCommitTime()>=endTime/1000){
                        continue;
                    }
                    // commit总数
                    commitCount++;
                    // diff统计
                    statisticDiffFiles.addDiffFiles(gitCommit.getDiffFiles());

                    for (DiffFile diffFile:
                            gitCommit.getDiffFiles()) {

                        // 暂时忽略修改之外的情况，如增加删除文件
                        if(!diffFile.getChangeType().equals(DiffEntry.ChangeType.MODIFY.name())){
                            continue;
                        }

                        // 所有文件
                        changeFileSet.add(diffFile.getOldPath());

                        // 每次修改的文件数的和
                        allChangeFileCount++;

                        // 每个提交者分别的修改的文件的数量
                        String authorEmail = gitCommit.getAuthorEmail();
                        Set<String> fileset = commiterChangeFileMap.get(authorEmail);
                        if(fileset == null){
                            fileset = new HashSet<>();
                            commiterChangeFileMap.put(authorEmail, fileset);
                        }
                        fileset.add(diffFile.getOldPath());
                    }
                }
                mainTain.setCommitCount(commitCount);
                mainTain.setCommitAddLineCount(statisticDiffFiles.getAddSize());
                mainTain.setCommitDeleteLineCount(statisticDiffFiles.getSubSize());
                if(changeFileSet.size()!=0) {
                    mainTain.setCommitOverlapRatio(allChangeFileCount / (double) changeFileSet.size());
                }
                Long allCommiterFileSet = 0l;
                for (Set<String> fileset :
                        commiterChangeFileMap.values()) {
                    allCommiterFileSet += fileset.size();
                }
                if(changeFileSet.size()!=0) {
                    mainTain.setCommitFilesetOverlapRatio(allCommiterFileSet / (double) changeFileSet.size());
                }
                Double allCommitterOverlap = 0.0;
                for (Map.Entry<String, Set<String>> entry :
                        commiterChangeFileMap.entrySet()) {
                    String author = entry.getKey();
                    Set<String> fileset = entry.getValue();
                    Double committerOverlap = 0.0;
                    for (Map.Entry<String, Set<String>> entry2 :
                            commiterChangeFileMap.entrySet()) {
                        String author2 = entry2.getKey();
                        if(author.equals(author2)){
                            continue;
                        }
                        Set<String> fileset2 = entry2.getValue();
                        Set<String> intersection = new HashSet<>();
                        intersection.addAll(fileset);
                        intersection.retainAll(fileset2);
                        Set<String> union = new HashSet<>();
                        union.addAll(fileset);
                        union.addAll(fileset2);
                        if(union.size()>0) {
                            committerOverlap += (double) intersection.size() / union.size();
                        }
                    }
                    allCommitterOverlap += committerOverlap;
                }
                if(commiterChangeFileMap.size()>0) {
                    mainTain.setPairwiseCommitterOverlap(allCommitterOverlap / commiterChangeFileMap.size());
                }
            }
            info.addMainTain(mainTain);
        }
        return info;
    }

    //根据版本获取可维护性数据
    public static MainTainsInfo newCreateInfo(Long reposId,
                                           MicroservicesInfo microservicesInfo,
                                           Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap,
                                           DtssInfo dtssInfo,
                                           PairRelationsInfo bugAndMicroserviceRelations,String version){
        MainTainsInfo info = new MainTainsInfo();
        info.initBuild();
        info.setReposId(reposId)
                .setVersion(version);
        List<Dts> dtss = dtssInfo.getBugs();

        // 建立问题单的Name和问题单的映射，方便用Name查找问题单
        Map<String, Dts> nameDtsMap = new HashMap<>();
        for (Dts dts:dtss) {
            nameDtsMap.put(dts.getName(), dts);
        }
        // 遍历微服务，求每个微服务下的维护相关的指标
        for (Microservice microservice:
                microservicesInfo) {
            MainTain mainTain = new MainTain();
            mainTain.setElementName(microservice.getElementName());
            GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = gitCommitsForMicroserviceInfoMap.get(microservice.getElementName());
            // 每个微服务下的bug数
            long dtsCount = 0;
            //由于dts与version的关联路径有点曲折，后面再写，BugCount暂时都导出为0/////////////////////////////////
            for (PairRelation relation :
                    bugAndMicroserviceRelations.getRelations()) {//传入的这个参数没有用到
                Dts dts = nameDtsMap.get(relation.getSourceName());
                if(dts==null){
                    continue;
                }
                Map<String, Set<String>> repoShasMap=dts.getRepoShasMap();//NullPointerException

                List<String> shass=new ArrayList<>();//存储该dts问题单上所有的sha，每个sha对应一个gitCommit
                for(Map.Entry<String,Set<String>> a:repoShasMap.entrySet()){
                    for(String str:a.getValue()){
                        shass.add(str);
                    }
                }
                List<String> versha=new ArrayList<>();//该版本下所有gitCommits的sha集合
                //获得该微服务下所有的gitCommits
                if (gitCommitsForMicroserviceInfo != null) {
                    List<GitCommit> gcs = gitCommitsForMicroserviceInfo.getGitCommits();
                    for(GitCommit gt:gcs){
                        versha.add(gt.getSha());
                    }
                }
                List<String> tmpsha=new ArrayList<>(shass);
                tmpsha.retainAll(versha);//若二者完全没有交集，说明这个dts肯定不属于这个版本
                if(dts==null || !relation.getTargetName().equals(microservice.getElementName())||tmpsha.size()==0){
                    continue;
                }
                dtsCount++;
            }
            mainTain.setBugCount(dtsCount);


            if (gitCommitsForMicroserviceInfo != null){
                List<GitCommit> gitCommits = gitCommitsForMicroserviceInfo.getGitCommits();
                StatisticDiffFiles statisticDiffFiles = new StatisticDiffFiles();
                Long commitCount = 0l;
                Set<String> changeFileSet = new HashSet<>();
                Map<String, Set<String>> commiterChangeFileMap = new HashMap<>();
                Long allChangeFileCount = 0l;
                for (GitCommit gitCommit :
                        gitCommits) {
                    // commit总数
                    commitCount++;
                    // diff统计
                    statisticDiffFiles.addDiffFiles(gitCommit.getDiffFiles());
                    for (DiffFile diffFile:
                            gitCommit.getDiffFiles()) {

                        // 暂时忽略修改之外的情况，如增加删除文件
                        if(!diffFile.getChangeType().equals(DiffEntry.ChangeType.MODIFY.name())){
                            continue;
                        }

                        // 所有文件
                        changeFileSet.add(diffFile.getOldPath());

                        // 每次修改的文件数的和
                        allChangeFileCount++;

                        // 每个提交者分别的修改的文件的数量
                        String authorEmail = gitCommit.getAuthorEmail();
                        Set<String> fileset = commiterChangeFileMap.get(authorEmail);
                        if(fileset == null){
                            fileset = new HashSet<>();
                            commiterChangeFileMap.put(authorEmail, fileset);
                        }
                        fileset.add(diffFile.getOldPath());
                    }
                }
                mainTain.setCommitCount(commitCount);
                mainTain.setCommitAddLineCount(statisticDiffFiles.getAddSize());
                mainTain.setCommitDeleteLineCount(statisticDiffFiles.getSubSize());
                if(changeFileSet.size()!=0) {
                    mainTain.setCommitOverlapRatio(allChangeFileCount / (double) changeFileSet.size());
                }
                Long allCommiterFileSet = 0l;
                for (Set<String> fileset :
                        commiterChangeFileMap.values()) {
                    allCommiterFileSet += fileset.size();
                }
                if(changeFileSet.size()!=0) {
                    mainTain.setCommitFilesetOverlapRatio(allCommiterFileSet / (double) changeFileSet.size());
                }
                Double allCommitterOverlap = 0.0;
                for (Map.Entry<String, Set<String>> entry :
                        commiterChangeFileMap.entrySet()) {
                    String author = entry.getKey();
                    Set<String> fileset = entry.getValue();
                    Double committerOverlap = 0.0;
                    for (Map.Entry<String, Set<String>> entry2 :
                            commiterChangeFileMap.entrySet()) {
                        String author2 = entry2.getKey();
                        if(author.equals(author2)){
                            continue;
                        }
                        Set<String> fileset2 = entry2.getValue();
                        Set<String> intersection = new HashSet<>();
                        intersection.addAll(fileset);
                        intersection.retainAll(fileset2);
                        Set<String> union = new HashSet<>();
                        union.addAll(fileset);
                        union.addAll(fileset2);
                        if(union.size()>0) {
                            committerOverlap += (double) intersection.size() / union.size();
                        }
                    }
                    allCommitterOverlap += committerOverlap;
                }
                if(commiterChangeFileMap.size()>0) {
                    mainTain.setPairwiseCommitterOverlap(allCommitterOverlap / commiterChangeFileMap.size());
                }
            }
            info.addMainTain(mainTain);
        }
        return info;
    }


    public MainTainsInfo addMainTain(MainTain mainTain){
        this.mainTainList.add(mainTain);
        return this;
    }

    public Map<String, MainTain> nameMainTainMap(){
        Map<String, MainTain> map = new HashMap<>();
        for (MainTain mainTain :
                getMainTainList()) {
            if(mainTain != null) {
                map.put(mainTain.getElementName(), mainTain);
            }
        }
        return map;
    }

    public MainTainsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public MainTainsInfo setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public MainTainsInfo setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public MainTainsInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public MainTainsInfo setElementsInfo(InfoProfile elementsInfo) {
        this.elementsInfo = elementsInfo;
        return this;
    }

    public MainTainsInfo setMainTainList(List<MainTain> mainTainList) {
        this.mainTainList = mainTainList;
        return this;
    }
}
