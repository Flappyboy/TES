package top.jach.tes.plugin.tes.code.git.commit;

import org.apache.commons.collections.SetUtils;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GitCommitsInfoTest {

    @Test
    public void createInfosForVersions() throws Exception {
        ReposInfo reposInfo = ReposInfo.createInfo();
        reposInfo.addRepo(new Repo().setName("tes"));
        GitCommitsInfo gitCommitsInfo = GitCommitsInfo.createInfoForAllRefs(reposInfo.getId(), "tes",
                Git.open(new File("../")));

        InfoTool.saveInputInfos(reposInfo, gitCommitsInfo);

        GitCommitsInfo allGitCommits = GitCommitsInfo.createInfoByLogFromCommit(new File("../"),
                reposInfo.getId(),
                reposInfo.getRepos().get(0).getName(),
                gitCommitsInfo.allShas());

        List<String> versionShas = Arrays.asList(
                "4fcf437f121ab2bb198ff2a99b771823a6c96a09",
                "039e6bbabc1d05a8aeaab80a8654337e393694d3"
        );
        System.out.println("go");
        List<GitCommitsInfo> result = GitCommitsInfo.createInfosForVersions(allGitCommits, versionShas);
        System.out.println("end");
        Set<String> allShas = new HashSet<>();
        for (GitCommitsInfo gc :
                result) {
            Set<String> shas = gc.allShas();
            Set<String> checkShas = GitCommitsInfo.createInfoByLogFromCommit(new File("../"), gc.getReposId(), gc.getRepoName(), gc.getRevisionSha()).allShas();
            Set<String> diff = new HashSet<>();
            diff.addAll(shas);
            diff.addAll(allShas);
            diff.removeAll(checkShas);
            System.out.println(gc.getRevision());
            System.out.println("多的"+diff);
            diff.clear();
            diff.addAll(checkShas);
            diff.removeAll(shas);
            diff.removeAll(allShas);
            System.out.println("少的"+diff);
            allShas.addAll(shas);
        }
    }
}