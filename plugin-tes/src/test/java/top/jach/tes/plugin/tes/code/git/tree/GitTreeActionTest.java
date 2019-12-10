package top.jach.tes.plugin.tes.code.git.tree;

import org.junit.Test;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;

public class GitTreeActionTest {

    @Test
    public void execute() {
        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes"));
        InfoTool.saveInputInfos(reposInfo);
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(GitTreeAction.LOCAL_REPO_DIR, new File("../"))
                .createSaveValueInfos(GitTreeAction.BRANCH, "master")
                .createSaveValueInfos(GitTreeAction.REPOS_ID, reposInfo.getId())
                .createSaveValueInfos(GitTreeAction.REPO_NAME, reposInfo.getRepos().get(0).getName())
                ;
        Action action = new GitTreeAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}