package top.jach.tes.plugin.tes.code.git.version;

import top.jach.tes.core.api.domain.info.Info;

import java.util.ArrayList;
import java.util.List;
//可以删除了，用不上
public class VersionInfo extends Info {
    public static final String INFO_NAME = "VersionInfo";
    private Long reposId;
    private Version version;

    public static VersionInfo createInfo(Version version){
        VersionInfo info = new VersionInfo();
        info.initBuild();
        info.setVersion(version);
        return info;
    }

    public Long getReposId() {
        return reposId;
    }

    public VersionInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public Version getVersion() {
        return version;
    }

    public VersionInfo setVersion(Version version) {
        this.version = version;
        return this;
    }
}
