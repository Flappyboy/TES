package top.jach.tes.plugin.jhkt.maintain;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.diff.DiffEntry;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationWithElements;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.core.impl.domain.relation.Relation;
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
    private String revision;
    private InfoProfile elementsInfo;
    private List<MainTain> mainTainList = new ArrayList<>();

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
        Map<String, Dts> nameDtsMap = new HashMap<>();
        for (Dts dts:dtss) {
            nameDtsMap.put(dts.getName(), dts);
        }
        for (Microservice microservice:
              microservicesInfo) {
            MainTain mainTain = new MainTain();
            mainTain.setElementName(microservice.getElementName());
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

    public MainTainsInfo addMainTain(MainTain mainTain){
        this.mainTainList.add(mainTain);
        return this;
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

    public MainTainsInfo setRevision(String revision) {
        this.revision = revision;
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
