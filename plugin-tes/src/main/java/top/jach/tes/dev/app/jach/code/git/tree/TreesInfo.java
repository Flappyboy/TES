package top.jach.tes.dev.app.jach.code.git.tree;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.List;

@Data
public class TreesInfo extends Info {
    Long repoId;
    List<Tree> trees;

    public static TreesInfo createInfo(){
        TreesInfo info = new TreesInfo();
        info.initBuild();
        return info;
    }

    public TreesInfo setRepoId(Long repoId) {
        this.repoId = repoId;
        return this;
    }

    public TreesInfo setTrees(List<Tree> trees) {
        this.trees = trees;
        return this;
    }
}
