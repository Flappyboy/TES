package top.jach.tes.plugin.tes.code.repo;

import top.jach.tes.core.domain.info.Info;

public class RepoInfo extends Info {

    private Repo repo = new Repo();

    public static RepoInfo createInfo(String name ,String remoteUrl){
        RepoInfo repoInfo = new RepoInfo();
        repoInfo.setName(name);
        repoInfo.setRemoteUrl(remoteUrl);
        repoInfo.initBuild();
        return repoInfo;
    }

    public RepoInfo setName(String name) {
        this.repo.setName(name);
        return this;
    }

    public RepoInfo setRemoteUrl(String remoteUrl) {
        this.repo.setRemoteUrl(remoteUrl);
        return this;
    }

    public String getName(){
        return this.repo.getName();
    }

    public String getRemoteUrl(){
        return this.repo.getRemoteUrl();
    }
}
