package top.jach.tes.plugin.tes.code.git.version;

import org.eclipse.jgit.api.errors.GitAPIException;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VersionsInfo extends Info {
    private Long reposId;
    private List<Version> versions = new ArrayList<>();

    public static VersionsInfo createInfoFromTags(ReposInfo reposInfo, Repo.RepoToGit repoToGit, List<String> tags) throws GitAPIException, IOException {
        VersionsInfo info = new VersionsInfo();
        info.initBuild();
        for (String tag:
                tags) {
            Version version = Version.VersionFromTag(reposInfo, repoToGit, tag);
            info.addVersion(version);
        }
        return info;
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
