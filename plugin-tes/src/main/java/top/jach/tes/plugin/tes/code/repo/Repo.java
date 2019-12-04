package top.jach.tes.plugin.tes.code.repo;

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
}
