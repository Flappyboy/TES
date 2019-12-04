package top.jach.tes.plugin.tes.code.repo;

import top.jach.tes.core.domain.info.Info;

import java.util.ArrayList;
import java.util.List;

public class ReposInfo extends Info {
    private List<Repo> repos = new ArrayList<>();

    public static ReposInfo createInfo(){
        ReposInfo repoInfo = new ReposInfo();
        repoInfo.initBuild();
        return repoInfo;
    }

    public ReposInfo addRepo(Repo repo){
        repos.add(repo);
        return this;
    }

    public List<Repo> getRepos() {
        return repos;
    }

    public ReposInfo setRepos(List<Repo> repos) {
        this.repos = repos;
        return this;
    }
}
