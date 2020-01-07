package top.jach.tes.plugin.tes.code.git.version;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VersionInfo extends Info {
    private String versionName;
    private String tag;
    private String branch;
    private Long reposId;
    private String repoName;
    private String sha;
    private Map<String, String> repoShaMap = new HashMap<>();

    public static VersionInfo createInfo(ReposInfo reposInfo, Repo.RepoToGit repoToGit, String tag) throws GitAPIException {
        VersionInfo info = new VersionInfo();
        info.initBuild();
        info.setTag(tag);
        for (Repo repo: reposInfo.getRepos()) {
            Git git = repoToGit.repoToGit(repo);
            List<Ref> refs = git.tagList().call();
            for (Ref ref :
                    refs) {
                if(ref.getName().equals("refs/tags/"+tag)){
                    info.getRepoShaMap().put(repo.getName(), ref.getObjectId().getName());
                    break;
                }
            }
        }
        return info;
    }

    public Set<String> repos(){
        return repoShaMap.keySet();
    }

    public String getSha() {
        return sha;
    }

    public VersionInfo setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public Long getReposId() {
        return reposId;
    }

    public VersionInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public String getRepoName() {
        return repoName;
    }

    public VersionInfo setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public Map<String, String> getRepoShaMap() {
        return repoShaMap;
    }

    public VersionInfo setRepoShaMap(Map<String, String> repoShaMap) {
        this.repoShaMap = repoShaMap;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public VersionInfo setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public VersionInfo setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public VersionInfo setBranch(String branch) {
        this.branch = branch;
        return this;
    }
}
