package top.jach.tes.plugin.jach.git;

import top.jach.tes.core.domain.info.Info;

import java.util.List;

public class GitInfo extends Info {
    public GitInfo(List<Commit> commits) {
        this.setName("GIT_COMMIT");
        this.commits = commits;
    }

    List<Commit> commits;

    public List<Commit> getCommits() {
        return commits;
    }

    public GitInfo setCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }
}
