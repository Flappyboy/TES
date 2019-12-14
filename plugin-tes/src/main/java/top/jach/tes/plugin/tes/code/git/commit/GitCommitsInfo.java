package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.WithRepo;
import top.jach.tes.plugin.tes.utils.JGitUtil;

import java.io.IOException;
import java.util.*;

@Data
public class GitCommitsInfo extends Info implements WithRepo {
    private Long reposId;
    private String repoName;

    private List<GitCommit> gitCommits = new ArrayList<>();

    public static GitCommitsInfo createInfo(Long reposId, String repoName){
        GitCommitsInfo info = new GitCommitsInfo();
        info.setRepoName(repoName).setReposId(reposId);
        info.initBuild();
        return info;
    }

    public static GitCommitsInfo createInfoForAllRefs(Long reposId, String repoName, Git git) throws IOException, GitAPIException {
        List<Ref> refs = git.getRepository().getRefDatabase().getRefs();
        GitCommitsInfo info = createInfo(reposId, repoName);
        RevWalk revWalk = new RevWalk(git.getRepository());
        Map<String, GitCommit> map = new HashMap<>();
        for (Ref ref :
                refs) {
            GitCommit gitCommit = GitCommit.createByRef(ref, git);
            if(gitCommit==null){
                continue;
            }
            map.put(gitCommit.getSha(), gitCommit);
        }
        for (GitCommit gitCommit :
                map.values()) {
            info.addGitCommits(gitCommit);
        }
        return info;
    }

    public Set<String> allShas(){
        Set<String> shas = new HashSet<>();
        for (GitCommit gitCommit :
                getGitCommits()) {
            shas.add(gitCommit.getSha());
        }
        return shas;
    }

    public GitCommitsInfo addGitCommits(GitCommit... gitCommits){
        this.gitCommits.addAll(Arrays.asList(gitCommits));
        return this;
    }

    public List<GitCommit> getGitCommits() {
        return gitCommits;
    }

    public GitCommitsInfo setGitCommits(List<GitCommit> gitCommits) {
        this.gitCommits = gitCommits;
        return this;
    }

    public GitCommitsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public GitCommitsInfo setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }
}
