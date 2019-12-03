package top.jach.tes.plugin.jach.code.repo;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

@Data
public class RepoInfo extends Info {
    Repo repo;

    public RepoInfo() {
    }

    public static RepoInfo createInfo(Repo repo){
        RepoInfo repoInfo = new RepoInfo();
        repoInfo.setRepo(repo);
        return repoInfo;
    }
}
