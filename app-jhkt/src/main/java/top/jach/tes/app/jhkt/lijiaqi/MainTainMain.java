package top.jach.tes.app.jhkt.lijiaqi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.dts.DtssInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.maintain.MainTain;
import top.jach.tes.plugin.jhkt.maintain.MainTainsInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTainMain extends DevApp {
    public static void main(String[] args) throws ParseException {
        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        microservices = MicroservicesInfo.createInfoByExcludeMicroservice(microservices,
                "x_2b", "x_1b", "x_23", "x_1d/x_6eed",
                "x_39",
                "x_1f",
                "x_27/x_25",
                "c_demo/c_demoa",
                "c_demo/c_demob");
        microservices.setName(InfoNameConstant.MicroservicesForReposExcludeSomeHistory);
        InfoTool.saveInputInfos(microservices);
        DtssInfo dtssInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.BugDts, DtssInfo.class);
        PairRelationsInfo bugMicroserviceRelations = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.RelationBugAndMicroservice, PairRelationsInfo.class);
        Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap = new HashMap<>();
        for (Microservice microservice :
                microservices.getMicroservices()) {
            GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = new GitCommitsForMicroserviceInfo();
            gitCommitsForMicroserviceInfo
                    .setReposId(microservices.getReposId())
                    .setMicroserviceName(microservice.getElementName())
                    .setStatisticDiffFiles(null)
                    .setGitCommits(null);
            List<Info> infos = Environment.infoRepositoryFactory.getRepository(GitCommitsForMicroserviceInfo.class)
                    .queryDetailsByInfoAndProjectId(gitCommitsForMicroserviceInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0) {
                gitCommitsForMicroserviceInfoMap.put(microservice.getElementName(), (GitCommitsForMicroserviceInfo)infos.get(0));
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        int[] ds = {1,2,3,6};
        for (int di = 0; di < ds.length; di++) {
            int d = ds[di];
            for (int i = 0; i+d < 7; i++) {
                Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                start.set(2019, 5+i, 1);
                Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                end.set(2019, 5+i+d, 1);
                mainTainResult(microservices, dtssInfo, bugMicroserviceRelations, gitCommitsForMicroserviceInfoMap, start.getTimeInMillis(), end.getTimeInMillis());
            }
        }
    }

    private static void mainTainResult(MicroservicesInfo microservices, DtssInfo dtssInfo, PairRelationsInfo bugMicroserviceRelations, Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap, long start, long end) {
        MainTainsInfo info = MainTainsInfo.createInfo(DataAction.DefaultReposId,
                microservices,
                gitCommitsForMicroserviceInfoMap,
                dtssInfo,
                bugMicroserviceRelations,
                start,
                end
                );
        StringBuilder sb = new StringBuilder();
        sb.append("Name,")
                .append("bugCount,")
                .append("commitCount,")
                .append("commitAddLineCount,")
                .append("commitDeleteLineCount,")
                .append("Commit Overlap Ratio (COR),")
                .append("Commit Fileset Overlap Ratio (CFOR),")
                .append("Pairwise Committer Overlap (PCO),")
                .append('\n')
        ;
        for (MainTain mainTain :
                info.getMainTainList()) {
            sb.append(mainTain.getElementName())
                    .append(',')
                    .append(nullToEmpty(mainTain.getBugCount()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getCommitCount()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getCommitAddLineCount()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getCommitDeleteLineCount()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getCommitOverlapRatio()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getCommitFilesetOverlapRatio()))
                    .append(',')
                    .append(nullToEmpty(mainTain.getPairwiseCommitterOverlap()))
                    .append(',')
                    .append('\n')
            ;
        }SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            System.out.println(sb);
            FileUtils.write(new File(String.format("F://data/tes/maintain/bug_commit_add_sub_COR_CFOR_PCO/%s_%s.csv",format.format(new Date(start)), format.format(new Date(end)))),sb, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String nullToEmpty(Object obj){
        return (obj == null) ? "" : obj.toString();
    }
}
