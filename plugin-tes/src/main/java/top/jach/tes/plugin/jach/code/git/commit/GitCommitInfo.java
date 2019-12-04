package top.jach.tes.plugin.jach.code.git.commit;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.List;

@Data
public class GitCommitInfo extends Info {

    public GitCommitInfo() {
    }

    public GitCommitInfo(List<Commit> commits) {
        this.setName("GIT_COMMIT");
        this.commits = commits;
    }

    public static GitCommitInfo createInfo(){
        GitCommitInfo info = new GitCommitInfo();
        info.initBuild();
        return info;
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
