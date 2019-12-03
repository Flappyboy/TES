package top.jach.tes.plugin.jach.code.repo;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

@Data
public class RepoInfo extends Info {
    String name;

    String remoteUrl;

    public static RepoInfo createInfo(String name ,String remoteUrl){
        RepoInfo repoInfo = new RepoInfo();
        repoInfo.setName(name);
        repoInfo.setRemoteUrl(remoteUrl);
        repoInfo.initBuild();
        return repoInfo;
    }

    public RepoInfo setName(String name) {
        this.name = name;
        return this;
    }

    public RepoInfo setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        return this;
    }
}
