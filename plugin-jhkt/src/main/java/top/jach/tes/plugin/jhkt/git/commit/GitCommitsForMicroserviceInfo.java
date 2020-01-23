package top.jach.tes.plugin.jhkt.git.commit;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.DiffFile;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.git.commit.StatisticDiffFiles;
import top.jach.tes.plugin.tes.code.repo.WithRepo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GitCommitsForMicroserviceInfo extends GitCommitsInfo implements WithRepo {
    private Long microservicesId;

    private String microserviceName;

    private String version;

    private Long commitsCount;

    private StatisticDiffFiles statisticDiffFiles;

    public static List<GitCommitsForMicroserviceInfo> createInfoFromMicroservicesInfo(MicroservicesInfo microservicesInfo, GitCommitsInfo gitCommitsInfo) {
        microservicesInfo.checkRepoEquals(gitCommitsInfo);
        List<GitCommitsForMicroserviceInfo> list = new ArrayList<>();
        for (Microservice microservice:
                microservicesInfo.getMicroservices()) {
            microservice.getElementName();
            GitCommitsForMicroserviceInfo info = createInfoFromGitCommitsInfo(microservicesInfo, microservice.getElementName(), gitCommitsInfo);
            if(info != null){
                info.setName("CommitsInMicroservice");
                list.add(info);
            }
        }
        return list;
    }

    public static GitCommitsForMicroserviceInfo createInfoFromGitCommitsInfo(MicroservicesInfo microservicesInfo, String microserviceName, GitCommitsInfo gitCommitsInfo) {
//        microservicesInfo.checkRepoEquals(gitCommitsInfo);

        GitCommitsForMicroserviceInfo info = new GitCommitsForMicroserviceInfo();
        info.initBuild();
        info.setMicroservicesId(microservicesInfo.getId());
        info.setMicroserviceName(microserviceName);
        info.setReposId(gitCommitsInfo.getReposId());
        info.setRepoName(gitCommitsInfo.getRepoName());
        info.setVersion(microservicesInfo.getVersion());

        Microservice microservice = microservicesInfo.getElementByName(microserviceName);
        if(microservice == null){
            throw new RuntimeException("microserviceName不存在");
        }

        String path = StringUtils.strip(microservice.getPath(), "/\\")+"/";

        StatisticDiffFiles statisticDiffFiles = StatisticDiffFiles.create(null);
        info.setStatisticDiffFiles(statisticDiffFiles);
        for (GitCommit commit:
                gitCommitsInfo.getGitCommits()) {

            // 目前排除了merge
            if (commit.getParentCount()>1){
                continue;
            }
            boolean tag = false;
            for (DiffFile diffFile :
                    commit.getDiffFiles()) {
                String newPath = diffFile.getNewPath();
                String oldPath = diffFile.getOldPath();

                if (StringUtils.isBlank(path) || "/".equals(path) || (newPath !=null && newPath.startsWith(path)) || (oldPath != null && oldPath.startsWith(path))){
                    if (!tag) {
                        info.addCommit(commit);
                        tag = true;
                    }
                    statisticDiffFiles.addDiffFiles(diffFile);
                }
            }
        }

        return info;
    }

    public GitCommitsForMicroserviceInfo addCommit(GitCommit commit){
        super.addGitCommits(commit);
        return this;
    }

    public GitCommitsForMicroserviceInfo setMicroservicesId(Long microservicesId) {
        this.microservicesId = microservicesId;
        return this;
    }

    public GitCommitsForMicroserviceInfo setMicroserviceName(String microserviceName) {
        this.microserviceName = microserviceName;
        return this;
    }

    public GitCommitsForMicroserviceInfo setReposId(Long reposId) {
        super.setReposId(reposId);
        return this;
    }

    public GitCommitsForMicroserviceInfo setRepoName(String repoName) {
        super.setRepoName(repoName);
        return this;
    }

    public GitCommitsForMicroserviceInfo setCommitsCount(Long commitsCount) {
        this.commitsCount = commitsCount;
        return this;
    }

    public GitCommitsForMicroserviceInfo setStatisticDiffFiles(StatisticDiffFiles statisticDiffFiles) {
        this.statisticDiffFiles = statisticDiffFiles;
        return this;
    }
}
