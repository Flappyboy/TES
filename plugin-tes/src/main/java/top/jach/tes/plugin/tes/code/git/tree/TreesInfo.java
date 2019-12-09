package top.jach.tes.plugin.tes.code.git.tree;

import lombok.Getter;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.WithRepo;

import java.util.List;

@Getter
public class TreesInfo extends Info implements WithRepo {
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

    public TreesInfo setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public TreesInfo setBranchName(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public TreesInfo setTagName(String tagName) {
        TagName = tagName;
        return this;
    }
}
