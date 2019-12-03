package top.jach.tes.plugin.jach;

import top.jach.tes.plugin.InfoTool;
import top.jach.tes.plugin.InputInfoProfiles;
import top.jach.tes.plugin.TaskTool;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.value.FileInfo;
import top.jach.tes.core.domain.info.value.StringInfo;
import top.jach.tes.plugin.jach.code.git.tree.GitTreeAction;
import top.jach.tes.plugin.jach.code.repo.Repo;
import top.jach.tes.plugin.jach.code.repo.RepoInfo;

import java.io.File;

public class GitTree {
    public static void main(String[] args) {
        // 创建不存在于数据库中的Info，一般是一些参数
        Info repoInfo = RepoInfo.createInfo(new Repo().setName("dddsample"));
        Info repoDir = FileInfo.createInfo(new File("E:\\workspace\\otherproject\\dddsample-core"));
        Info sha = StringInfo.createInfo("master");

        // 然后将这些info存入数据库
        InfoTool.saveInputInfos(repoDir, sha, repoInfo);

        // 创建一些已存在的InfoProfile
//        InfoProfile infoProfile = new InfoProfile(123l, ValueInfo.class);

        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(GitTreeAction.LOCAL_REPO_DIR, repoDir)
                .addInfoProfile(GitTreeAction.COMMIT_SHA, sha)
                .addInfoProfile(GitTreeAction.REPO_INFO, repoInfo)
                ;

        Action action = new GitTreeAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
