package top.jach.tes.plugin.jach.code.git.commit;

import top.jach.tes.core.domain.info.Info;

import java.util.List;

public class GitCommitInfo extends Info {
    public GitCommitInfo(List<Commit> commits) {
        this.setName("GIT_COMMIT");
        this.commits = commits;
    }

    List<Commit> commits;

    public List<Commit> getCommits() {
        return commits;
    }

    public GitCommitInfo setCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }
}
