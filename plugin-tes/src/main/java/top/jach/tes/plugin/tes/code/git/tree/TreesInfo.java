package top.jach.tes.plugin.tes.code.git.tree;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.List;

@Data
public class TreesInfo extends Info {
    Long reposId;
    String repoName;
    String sha;
    String branchName;
    String TagName;
    List<Tree> trees;

    public static TreesInfo createInfo(){
        TreesInfo info = new TreesInfo();
        info.initBuild();
        return info;
    }

    public TreesInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public TreesInfo setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public TreesInfo setTrees(List<Tree> trees) {
        this.trees = trees;
        return this;
    }
}
