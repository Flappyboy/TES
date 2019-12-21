package top.jach.tes.plugin.tes.code.git.tree;

import org.junit.Test;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;

public class GitTreeActionTest {

    @Test
    public void execute() throws ActionExecuteFailedException {
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
//        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);

        OutputInfos outputInfos = action.execute(infoProfileMap.toInputInfos(Environment.infoRepositoryFactory),
                Environment.contextFactory.createContext(Environment.defaultProject));
        for (Info info :
                outputInfos.getInfoList()) {
            System.out.println(info);
        }
    }
}