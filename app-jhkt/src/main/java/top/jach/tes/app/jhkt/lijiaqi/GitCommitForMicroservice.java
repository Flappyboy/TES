package top.jach.tes.app.jhkt.lijiaqi;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitForMicroserviceAction;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitForSpecificCommitsAction;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;
import java.io.IOException;

public class GitCommitForMicroservice extends DevApp {
    public static void main(String[] args) throws ActionExecuteFailedException, IOException, GitAPIException {
        File file = new File("./");

        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes"));
        GitCommitsInfo gitCommitsInfo = GitCommitsInfo.createInfoForAllRefs(reposInfo.getId(), "tes",
                Git.open(file));

        InfoTool.saveInputInfos(reposInfo, gitCommitsInfo);
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.LOCAL_REPO_DIR, file)
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.REPOS_ID, reposInfo.getId())
                .createSaveValueInfos(GitCommitForSpecificCommitsAction.REPO_NAME, "tes")
                ;
        infoProfileMap.addInfoProfile(GitCommitForSpecificCommitsAction.SPECIFIC_COMMITS, gitCommitsInfo);
        Action action = new GitCommitForSpecificCommitsAction();
        OutputInfos outputInfos = action.execute(infoProfileMap.toInputInfos(Environment.infoRepositoryFactory), Environment.contextFactory.createContext(Environment.defaultProject));
        Info info = outputInfos.getInfoList().get(0);

        MicroservicesInfo microservices = MicroservicesInfo.createInfo().setReposId(reposInfo.getId()).setRepoName("tes");
        microservices.addMicroservice(new Microservice().setElementName("app").setPath("app/"))
                .addMicroservice(new Microservice().setElementName("core").setPath("core/"))
                .addMicroservice(new Microservice().setElementName("app-dev").setPath("app-dev/"))
                .addMicroservice(new Microservice().setElementName("app-jhkt").setPath("app-jhkt/"))
                .addMicroservice(new Microservice().setElementName("plugin-jhkt").setPath("plugin-jhkt/"))
                .addMicroservice(new Microservice().setElementName("plugin-tes").setPath("plugin-tes/"))
        ;
        InfoTool.saveInputInfos(microservices, info);

        infoProfileMap = InputInfoProfiles.InputInfoProfiles().addInfoProfile(GitCommitForMicroserviceAction.MICROSERVICES_INFO, microservices)
                .addInfoProfile(GitCommitForMicroserviceAction.GIT_COMMITS_INFO, info);

        action = new GitCommitForMicroserviceAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
