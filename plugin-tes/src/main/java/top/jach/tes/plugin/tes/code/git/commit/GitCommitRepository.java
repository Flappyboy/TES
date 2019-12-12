package top.jach.tes.plugin.tes.code.git.commit;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface GitCommitRepository {
    void saveGitCommits(List<GitCommit> gitCommits, Long reposId, String repoName);
    Set<String> findShasByRepo(Long reposId, String repoName);
    Iterable<GitCommit> findByRepoAndShas(Long reposId, String repoName, List<String> shas);
    void deleteOldVersionData(Long reposId, String repoName);
}
