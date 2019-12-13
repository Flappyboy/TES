package top.jach.tes.plugin.tes.code.git.commit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.info.value.LongInfo;
import top.jach.tes.core.impl.domain.info.value.StringInfo;
import top.jach.tes.core.impl.domain.info.value.ValueInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.meta.LongField;
import top.jach.tes.core.impl.domain.meta.StringField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GitCommitForSpecificCommitsAction implements Action {
    public static final String REPOS_ID = "repos_id";
    public static final String REPO_NAME = "repo_name";
    public static final String SPECIFIC_COMMITS = "specific_commits";
    public static final String LOCAL_REPO_DIR = "local_repo_dir";
    @Override
    public String getName() {
        return "GitCommit";
    }

    @Override
    public String getDesc() {
        return "git commit";
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(LOCAL_REPO_DIR).setInfoClass(FileInfo.class),
                InfoField.createField(SPECIFIC_COMMITS).setInfoClass(GitCommitsInfo.class),
                StringField.createField(REPO_NAME),
                LongField.createField(REPOS_ID));
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) {
        File repoDir = ValueInfo.getValueFromInputInfos(inputInfos, LOCAL_REPO_DIR, FileInfo.class);
        Long reposId = ValueInfo.getValueFromInputInfos(inputInfos, REPOS_ID, LongInfo.class);
        String repoName = ValueInfo.getValueFromInputInfos(inputInfos, REPO_NAME, StringInfo.class);
        GitCommitsInfo gitCommitsInfo = inputInfos.getInfo(SPECIFIC_COMMITS, GitCommitsInfo.class);

        try {
            GitCommitsInfo result = GitCommitsInfo.createInfo(reposId, repoName);
            Git git = Git.open(repoDir);
            LogCommand log = git.log();

            for (GitCommit gitCommits :
                    gitCommitsInfo.getGitCommits()) {
                String sha = gitCommits.getSha();
                log.add(ObjectId.fromString(sha));
            }

            RevWalk revWalk = new RevWalk(git.getRepository());
            Iterable<RevCommit> commits = log.call();

            DiffFormatter df = Utils.diffFormatter(git.getRepository());
            for (RevCommit commit :
                commits) {
                GitCommit gitCommit = GitCommit.createByRevCommit(commit, git, revWalk, df);
                if(gitCommit !=null ){
                    result.addGitCommits(gitCommit);
                }
            }
            revWalk.dispose();
            return DefaultOutputInfos.WithSaveFlag(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }
}
