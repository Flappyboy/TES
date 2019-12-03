package top.jach.tes.plugin.jach.code.repo;

import lombok.Data;

@Data
public class Repo {

    String name;

    String remoteUrl;

    public Repo setName(String name) {
        this.name = name;
        return this;
    }

    public Repo setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        return this;
    }
}
