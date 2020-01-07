package top.jach.tes.plugin.tes.code.repo;

import top.jach.tes.core.api.domain.info.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<Repo> reposFromNames(Set<String> names) {
        List<Repo> repos = new ArrayList<>();
        for (Repo repo :
                this.getRepos()) {
            if (names.contains(repo.getName())) {
                repos.add(repo);
            }
        }
        return repos;
    }


    public List<Repo> getRepos() {
        return repos;
    }

    public ReposInfo setRepos(List<Repo> repos) {
        this.repos = repos;
        return this;
    }
}
