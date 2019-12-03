package top.jach.tes.plugin.jach.code.git.tree;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.List;

@Data
public class TreeInfo extends Info {
    Long repoId;
    List<Tree> trees;

    public TreeInfo() {
    }

    public TreeInfo setRepoId(Long repoId) {
        this.repoId = repoId;
        return this;
    }

    public TreeInfo setTrees(List<Tree> trees) {
        this.trees = trees;
        return this;
    }
}
