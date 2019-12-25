package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.WithRepo;
import top.jach.tes.plugin.tes.utils.JGitUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

// 包含一个代码仓下的所有commit
@Data
public class GitCommitsInfo extends Info implements WithRepo {
    private Long reposId;
    private String repoName;

    private Long startTime = null;

    private Long endTime = null;

    private String revision = null;

    private String revisionSha = null;

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
            GitCommit gitCommit = GitCommit.createByRef(reposId, repoName, ref, git);
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
    public static GitCommitsInfo createInfoByLogFromCommit(File repoDir, Long reposId, String repoName, String sourceSha) throws IOException, GitAPIException {
        Set<String> shas = new HashSet<>();
        shas.add(sourceSha);
        return createInfoByLogFromCommit(repoDir, reposId, repoName, shas, null);
    }

    public static GitCommitsInfo createInfoByLogFromCommit(File repoDir, Long reposId, String repoName, Set<String> sourceShas) throws IOException, GitAPIException {
        return createInfoByLogFromCommit(repoDir, reposId, repoName, sourceShas, null);
    }
    public static GitCommitsInfo createInfoByLogFromCommit(File repoDir, Long reposId, String repoName, Set<String> sourceShas, Set<String> excludeShas) throws IOException, GitAPIException {
        GitCommitsInfo result = GitCommitsInfo.createInfo(reposId, repoName);
        Git git = Git.open(repoDir);
        LogCommand log = git.log();

        for (String sha :
                sourceShas) {
            log.add(ObjectId.fromString(sha));
        }

        RevWalk revWalk = new RevWalk(git.getRepository());
        Iterable<RevCommit> commits = log.call();

        Set<String> shas = new HashSet<>();
        DiffFormatter df = Utils.diffFormatter(git.getRepository());
        for (RevCommit commit :
                commits) {
            if(shas.contains(commit.getName())){
                System.out.println("createInfoByLogFromCommit sha 重复"+commit.getName());
                continue;
            }
            shas.add(commit.getName());
            if(excludeShas != null && excludeShas.contains(commit.getName())){
                continue;
            }
            GitCommit gitCommit = GitCommit.createByRevCommit(reposId, repoName, commit, git, revWalk, df);
            if(gitCommit !=null ){
                result.addGitCommits(gitCommit);
            }
        }
        revWalk.dispose();
        return result;
    }

    /**
     * 通过parentSha进行回溯，从一个gitCommitsInfo中创建一个新的gitCommitsInfo，其中的commit都是指定sha的祖先
     * @param gitCommitsInfo
     * @param sha 指定开始revision的sha
     * @param excludeShas 需要忽略的shas
     * @return
     */
    public static GitCommitsInfo createInfoFromCommit(GitCommitsInfo gitCommitsInfo, String sha, Set<String> excludeShas){
        Map<String, GitCommit> allShasGitCommitMap = gitCommitsInfo.allShasGitCommitMap();
        Set<String> allShas = new HashSet<>();
        Queue<String> queueShas = new LinkedList<>();
        GitCommitsInfo result = GitCommitsInfo.createInfo(gitCommitsInfo.getReposId(), gitCommitsInfo.getRepoName());
        queueShas.add(sha);
        allShas.add(sha);
        while (queueShas.size()>0){
            String pSha = queueShas.poll();
            GitCommit pGitCommit = allShasGitCommitMap.get(pSha);
            result.addGitCommits(pGitCommit);
            Set<String> parentShas = pGitCommit.getParentShas();
            if(parentShas != null){
                for (String parentSha :
                        parentShas) {
                    // 排除已添加的，和需要忽略的sha
                    if (allShas.contains(parentSha) || excludeShas.contains(parentSha)){
                        continue;
                    }
                    queueShas.add(parentSha);
                    allShas.add(parentSha);
                }
            }
        }
        return result;
    }

    /**
     * 指定不同版本，获取gitCommits
     * @param gitCommitsInfo
     * @param sourceShas
     * @return
     */
    public static List<GitCommitsInfo> createInfosForVersions(GitCommitsInfo gitCommitsInfo, List<String> sourceShas){
        Set<String> allShas = new HashSet<>();
        List<GitCommitsInfo> result = new ArrayList<>();
        for (String sourceSha:
                sourceShas) {
            System.out.println("start"+sourceSha);
            GitCommitsInfo info = createInfoFromCommit(gitCommitsInfo, sourceSha, allShas);
            info.setRevision(sourceSha);
            info.setRevisionSha(sourceSha);
            result.add(info);
            allShas.addAll(info.allShas());
        }
        return result;
    }

    public Set<String> allShas(){
        Set<String> shas = new HashSet<>();
        for (GitCommit gitCommit :
                getGitCommits()) {
            shas.add(gitCommit.getSha());
        }
        return shas;
    }

    public Map<String, GitCommit> allShasGitCommitMap(){
        Map<String, GitCommit> allShasGitCommitMap = new HashMap<>();
        for (GitCommit gitCommit :
                getGitCommits()) {
            allShasGitCommitMap.put(gitCommit.getSha(), gitCommit);
        }
        return allShasGitCommitMap;
    }

    public GitCommitsInfo addGitCommits(GitCommit... gitCommits){
        this.gitCommits.addAll(Arrays.asList(gitCommits));
        for (GitCommit gitCommit :
                gitCommits) {
            Integer time = gitCommit.getCommitTime();
            if(this.getStartTime() != null) {
                if (time < this.getStartTime()) {
                    this.setStartTime(Long.valueOf(time));
                }
            }else{
                this.setStartTime(Long.valueOf(time));
            }
            if(this.getEndTime() != null) {
                if (time > this.getEndTime()) {
                    this.setEndTime(Long.valueOf(time));
                }
            }else{
                this.setEndTime(Long.valueOf(time));
            }
        }
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
