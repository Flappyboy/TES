package top.jach.tes.plugin.tes.code.git.commit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;
import java.io.IOException;

public class GitCommitForSpecificCommitsActionTest {

    @Test
    public void execute() throws IOException, GitAPIException {
        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes"));
        GitCommitsInfo gitCommitsInfo = GitCommitsInfo.createInfoForAllRefs(reposInfo.getId(), "tes",
                Git.open(new File("../")));

        InfoTool.saveInputInfos(reposInfo, gitCommitsInfo);
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.LOCAL_REPO_DIR, new File("../"))
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.REPOS_ID, reposInfo.getId())
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.REPO_NAME, reposInfo.getRepos().get(0).getName())
                ;
        infoProfileMap.addInfoProfile(GitCommitForSpecificCommitsAction.SPECIFIC_COMMITS, gitCommitsInfo);
        Action action = new GitCommitForSpecificCommitsAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
