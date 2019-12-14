package top.jach.tes.app.jhkt.lijiaqi;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitForSpecificCommitsAction;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfoMongoRepository;
import top.jach.tes.plugin.tes.code.git.tree.GitTreeAction;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;
import java.io.IOException;

public class GitCommits extends DevApp {
    public static void main(String[] args) throws IOException, GitAPIException {
        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes")).setId(20002l);
        GitCommitsInfo gitCommitsInfo = GitCommitsInfo.createInfoForAllRefs(reposInfo.getId(), "tes",
                Git.open(new File("./")));

        InfoTool.saveInputInfos(reposInfo, gitCommitsInfo);
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(GitTreeAction.LOCAL_REPO_DIR, new File("./"))
                .createSaveValueInfos(GitTreeAction.REPOS_ID, reposInfo.getId())
                .createSaveValueInfos(GitTreeAction.REPO_NAME, reposInfo.getRepos().get(0).getName())
                ;
        infoProfileMap.addInfoProfile(GitCommitForSpecificCommitsAction.SPECIFIC_COMMITS, gitCommitsInfo);
        Action action = new GitCommitForSpecificCommitsAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
