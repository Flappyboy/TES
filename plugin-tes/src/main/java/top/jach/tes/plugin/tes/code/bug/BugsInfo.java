package top.jach.tes.plugin.tes.code.bug;

import top.jach.tes.core.api.domain.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BugsInfo<B extends Bug> extends Info {
    Long reposId;

    private List<B> bugs = new ArrayList<>();

    public static BugsInfo createInfo(Long reposId){
        BugsInfo info = new BugsInfo();
        info.initBuild();
        info.setReposId(reposId);
        return info;
    }

    public BugsInfo<B> addBugs(B... bugs){
        this.bugs.addAll(Arrays.asList(bugs));
        return this;
    }

    public Long getReposId() {
        return reposId;
    }

    public BugsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public List<B> getBugs() {
        return bugs;
    }

    public BugsInfo<B> setBugs(List<B> bugs) {
        this.bugs = bugs;
        return this;
    }
}
