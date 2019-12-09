package top.jach.tes.jhkt.lijiaqi;

import top.jach.tes.core.easy.InfoTool;
import top.jach.tes.core.easy.InputInfoProfiles;
import top.jach.tes.core.easy.TaskTool;
import top.jach.tes.dev.app.DevApp;
import top.jach.tes.plugin.tes.code.git.tree.GitTreeAction;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;

public class GitTree extends DevApp {
    public static void main(String[] args) {
        // 创建不存在于数据库中的Info，一般是一些参数
        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes"));
        InfoTool.saveInputInfos(reposInfo);
/*
        Info repoName = StringInfo.createInfo(reposInfo.getRepos().get(0).getName());
        Info reposId = LongInfo.createInfo(reposInfo.getId());
        Info repoDir = FileInfo.createInfo(new File("./"));
        Info sha = StringInfo.createInfo("master");
*/

        // 然后将这些info存入数据库
//        InfoTool.saveInputInfos(repoDir, sha, reposId, repoName);

        // 创建一些已存在的InfoProfile
//        InfoProfile infoProfile = new InfoProfile(123l, ValueInfo.class);
/*
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(GitTreeAction.LOCAL_REPO_DIR, repoDir)
                .addInfoProfile(GitTreeAction.COMMIT_SHA, sha)
                .addInfoProfile(GitTreeAction.REPOS_ID, reposId)
                .addInfoProfile(GitTreeAction.REPO_NAME, repoName)
                ;
*/
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(GitTreeAction.LOCAL_REPO_DIR, new File("./"))
                .createSaveValueInfos(GitTreeAction.BRANCH, "master")
                .createSaveValueInfos(GitTreeAction.REPOS_ID, reposInfo.getId())
                .createSaveValueInfos(GitTreeAction.REPO_NAME, reposInfo.getRepos().get(0).getName())
                ;

        Action action = new GitTreeAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
