package top.jach.tes.plugin.tes.code.git.tree;

import org.eclipse.jgit.lib.FileMode;
import top.jach.tes.core.domain.action.*;
import top.jach.tes.core.domain.context.Context;
import top.jach.tes.core.domain.info.value.FileInfo;
import top.jach.tes.core.domain.info.value.LongInfo;
import top.jach.tes.core.domain.info.value.StringInfo;
import top.jach.tes.core.domain.info.value.ValueInfo;
import top.jach.tes.core.domain.meta.InfoField;
import top.jach.tes.core.domain.meta.LongField;
import top.jach.tes.core.domain.meta.Meta;
import top.jach.tes.core.domain.meta.StringField;
import top.jach.tes.plugin.tes.code.repo.RepoInfo;
import top.jach.tes.plugin.tes.utils.JGitUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitTreeAction implements Action {
    public static final String REPOS_ID = "repos_id";
    public static final String REPO_NAME = "repo_name";
    public static final String LOCAL_REPO_DIR = "local_repo_dir";
    public static final String COMMIT_SHA = "commit_sha";
    public static final String BRANCH = "branch";
    public static final String Tag = "tag";

    @Override
    public String getName() {
        return "Git Tree";
    }

    @Override
    public String getDesc() {
        return "通过git获取对应commit的目录文件结构";
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(LOCAL_REPO_DIR).setInfoClass(FileInfo.class),
                StringField.createField(COMMIT_SHA),
                StringField.createField(BRANCH),
                StringField.createField(Tag),
                StringField.createField(REPO_NAME),
                LongField.createField(REPOS_ID));
    }

    @Override
    public OutputInfos execute(InputInfos inputInfo, Context context) {
        File repoDir = ValueInfo.getValueFromInputInfos(inputInfo, LOCAL_REPO_DIR, FileInfo.class);
        String sha = ValueInfo.getValueFromInputInfos(inputInfo, COMMIT_SHA, StringInfo.class);
        String branch = ValueInfo.getValueFromInputInfos(inputInfo, BRANCH, StringInfo.class);
        Long reposId = ValueInfo.getValueFromInputInfos(inputInfo, REPOS_ID, LongInfo.class);
        String repoName = ValueInfo.getValueFromInputInfos(inputInfo, REPO_NAME, StringInfo.class);

        String shaOrBranch = sha != null ? sha : branch;
        try {
            List<Tree> treeList = new ArrayList<>();
            if (sha == null){
                sha = JGitUtil.doGetCommit(repoDir, branch).getName();
            }
            JGitUtil.dolsTree(repoDir,
                    shaOrBranch,
                    "",
                    true,
                    (repository, treeWalk) -> {
                        /*context.Logger().info(
                                "name string: {}, file mode: {}, path string: {}, tree count: {}, operation: {}",
                                treeWalk.getNameString(),
                                FileModeToString(treeWalk.getFileMode()),
                                treeWalk.getPathString(),
                                treeWalk.getTreeCount(),
                                treeWalk.getOperationType().name()
                        );*/
                        treeList.add(new Tree().setRelativePath(treeWalk.getPathString())
                                .setFileMode(FileModeToString(treeWalk.getFileMode())));
                    });
            TreesInfo treesInfo = TreesInfo.createInfo().setTrees(treeList).setSha(sha).setBranchName(branch);
            treesInfo.setName("GitTreeInfo");
            if(reposId != null) {
                treesInfo.setReposId(reposId);
                treesInfo.setRepoName(repoName);
            }
            return DefaultOutputInfos.WithSaveFlag(treesInfo);
        } catch (Exception e) {
            context.Logger().error("GitTreeAction", e);
        }
        return null;
    }

    public static String FileModeToString(FileMode fileMode) {
        switch (fileMode.getBits() & FileMode.TYPE_MASK) {
            case FileMode.TYPE_MISSING:
                if (fileMode.getBits() == 0)
                    return "MISSING";
                break;
            case FileMode.TYPE_TREE:
                return "TREE";
            case FileMode.TYPE_FILE:
                if ((fileMode.getBits() & 0111) != 0)
                    return "EXECUTABLE_FILE";
                return "REGULAR_FILE";
            case FileMode.TYPE_SYMLINK:
                return "SYMLINK";
            case FileMode.TYPE_GITLINK:
                return "GITLINK";
        }
        return fileMode.toString();
    }
}
