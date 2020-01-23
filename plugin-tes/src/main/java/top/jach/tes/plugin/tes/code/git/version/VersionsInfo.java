package top.jach.tes.plugin.tes.code.git.version;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VersionsInfo extends Info {
    public static final String INFO_NAME = "VersionsInfo";
    private Long reposId;
    private List<Version> versions = new ArrayList<>();

    public static VersionsInfo createInfo(){
        VersionsInfo info = new VersionsInfo();
        info.initBuild();
        return info;
    }

    public static VersionsInfo createInfoByVersions(List<Version>... versions){
        VersionsInfo info = createInfo();
        for (List<Version> vs :
                versions) {
            for (Version v :
                    vs) {
                info.addVersion(v);
            }
        }
        return info;
    }

    public static VersionsInfo createInfoByTime(ReposInfo reposInfo, Map<String, List<GitCommit>> repoGitCommitMap,
                                                Long startTime, Long endTime){
        VersionsInfo info = createInfo();
        for (List<GitCommit> gcs :
                repoGitCommitMap.values()) {
            Collections.sort(gcs, Comparator.comparingInt(GitCommit::getCommitTime));
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(new Date(startTime));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        while (true){
            Long targetTime = calendar.getTimeInMillis();
            if(targetTime > endTime){
                break;
            }
            calendar.add(Calendar.MONTH, 1);
            Version version = new Version();
            version.setReposId(reposInfo.getId());
            version.setVersionName(format.format(new Date(targetTime)));
            Map<String, String> repoShaMap = new HashMap<>();
            version.setRepoShaMap(repoShaMap);
            for (Repo repo :
                    reposInfo.getRepos()) {
                String name = repo.getName();
                List<GitCommit> gitCommits = repoGitCommitMap.get(name);
                if (gitCommits == null){
                    continue;
                }
                GitCommit lastGitCommit = null;
                GitCommit result = gitCommits.get(gitCommits.size()-1);
                for (int i = 0; i < gitCommits.size(); i++) {
                    GitCommit gitCommit = gitCommits.get(i);
                    if (gitCommit.getCommitTime()*1000l > targetTime){
                            result = lastGitCommit;
                            break;
                    }
                    lastGitCommit = gitCommit;
                }
                if (result==null || targetTime-(result.getCommitTime()*1000l)>60*60*24*40*1000l){
                    continue;
                }
                repoShaMap.put(repo.getName(), result.getSha());
            }
            if(version.getRepoShaMap().size()>0){
                info.addVersion(version);
            }
        }

        return info;
    }

    public static VersionsInfo createInfoFromTags(ReposInfo reposInfo, Repo.RepoToGit repoToGit, List<String> tags) throws GitAPIException, IOException {
        VersionsInfo info = createInfo();
        for (String tag:
                tags) {
            Version version = Version.VersionFromTag(reposInfo, repoToGit, tag);
            version.setVersionName(tag);
            info.addVersion(version);
        }
        return info;
    }

    public List<String> shasForRepo(String repoName){
        List<String> shas = new ArrayList<>();
        for (Version version :
                versions) {
            String sha = version.getRepoShaMap().get(repoName);
            if(StringUtils.isNoneBlank(sha)){
                shas.add(sha);
            }
        }
        return shas;
    }

    public VersionsInfo addVersion(Version version){
        versions.add(version);
        return this;
    }

    public Long getReposId() {
        return reposId;
    }

    public VersionsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public VersionsInfo setVersions(List<Version> versions) {
        this.versions = versions;
        return this;
    }
}
