package top.jach.tes.plugin.tes.code.repo;

import org.eclipse.jgit.api.Git;

public class Repo {
    String name;

    String remoteUrl;

    public String getName() {
        return name;
    }

    public Repo setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public Repo setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        return this;
    }

    public interface RepoToGit{
        Git repoToGit(Repo repo);
    }
}
