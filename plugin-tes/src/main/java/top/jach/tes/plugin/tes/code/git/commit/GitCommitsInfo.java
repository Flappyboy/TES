package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.MissingObjectException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 包含一个代码仓下的所有commit
public class GitCommitsInfo extends Info implements WithRepo {
    private Long reposId;
    private String repoName;

    private Long startTime = null;

    private Long endTime = null;

    private String revision = null;

    private String revisionSha = null;

    private List<GitCommit> gitCommits = new ArrayList<>();

    private Map<String, GitCommit> shaGitCommitMap = new HashMap<>();

    public static GitCommitsInfo createInfo(Long reposId, String repoName){
        GitCommitsInfo info = new GitCommitsInfo();
        info.setRepoName(repoName).setReposId(reposId);
        info.initBuild();
        return info;
    }

    public static GitCommitsInfo createInfoByInfos(List<GitCommitsInfo> gitCommitsInfos){
        if(gitCommitsInfos == null || gitCommitsInfos.size() == 0){
            throw new RuntimeException("GitCommitsInfo createInfoByInfos 输入参数 不可为空或者长度为0");
        }
        GitCommitsInfo gitCommitsInfo = gitCommitsInfos.get(0);
        GitCommitsInfo info = createInfo(gitCommitsInfo.getReposId(), gitCommitsInfo.getRepoName());
        for (GitCommitsInfo gc :
                gitCommitsInfos) {
            info.addGitCommits(gc.getGitCommits());
        }
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
            try {
                log.add(ObjectId.fromString(sha));
            }catch (Exception e){
                System.out.println("createInfoByLogFromCommit: "+ repoName + " 缺失： "+ sha);
            }
        }

        RevWalk revWalk = new RevWalk(git.getRepository());
        Iterable<RevCommit> commits = log.call();

        ExecutorService executor = new ThreadPoolExecutor(3, 15, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

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
            executor.execute(() -> {
                GitCommit gitCommit = null;
                try {
                    gitCommit = GitCommit.createByRevCommit(reposId, repoName, commit, git, revWalk, df);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
                if(gitCommit !=null ){
                    synchronized (result) {
                        result.addGitCommits(gitCommit);
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        revWalk.dispose();
        return result;
    }

    public static GitCommitsInfo extendInfoByRecursionNewCommit(File repoDir, Long reposId, String repoName, GitCommitsInfo gitCommitsInfo, ShasFromGitCommit shasFromGitCommit) throws IOException, GitAPIException {
        Set<String> allShas = new HashSet<>();
        GitCommitsInfo extendGitCommitsInfo = gitCommitsInfo;
        List<GitCommitsInfo> extendsInfo = new ArrayList<>();

        while (true) {
            extendsInfo.add(extendGitCommitsInfo);
            allShas.addAll(extendGitCommitsInfo.allShas());

            Set<String> shas = new HashSet<>();
            for (GitCommit gc :
                    extendGitCommitsInfo.getGitCommits()) {
                Set<String> otherShas = shasFromGitCommit.shasFromGitCommit(gc);
                for (String sha :
                        otherShas) {
                    if (!allShas.contains(sha)) {
                        shas.add(sha);
                    }
                }
            }
            if(shas.isEmpty()){
                break;
            }
            extendGitCommitsInfo = createInfoByLogFromCommit(repoDir, reposId, repoName, shas, allShas);
        }
        return createInfoByInfos(extendsInfo);
    }
    public interface ShasFromGitCommit{
        Set<String> shasFromGitCommit(GitCommit gitCommit);
    }

    public static GitCommitsInfo createInfoFromCommit(GitCommitsInfo gitCommitsInfo, String sha, Set<String> excludeShas) throws Exception {
        return createInfoFromCommit(gitCommitsInfo,sha, excludeShas, null);
    }

    /**
     * 通过parentSha进行回溯，从一个gitCommitsInfo中创建一个新的gitCommitsInfo，其中的commit都是指定sha的祖先
     * @param gitCommitsInfo
     * @param sha 指定开始revision的sha
     * @param excludeShas 需要忽略的shas
     * @return
     */
    public static GitCommitsInfo createInfoFromCommit(GitCommitsInfo gitCommitsInfo, String sha, Set<String> excludeShas, ShasFromGitCommit shasFromGitCommit) throws Exception {
        Map<String, GitCommit> allShasGitCommitMap = gitCommitsInfo.allShasGitCommitMap();
        GitCommit initCommit = allShasGitCommitMap.get(sha);
        if(initCommit == null){
            throw new Exception("createInfoFromCommit 初始commit丢失 " +sha);
        }
        Set<String> allShas = new HashSet<>();
        Queue<String> queueShas = new LinkedList<>();
        GitCommitsInfo result = GitCommitsInfo.createInfo(gitCommitsInfo.getReposId(), gitCommitsInfo.getRepoName());
        queueShas.add(sha);
        allShas.add(sha);
        while (queueShas.size()>0){
            String pSha = queueShas.poll();
            GitCommit pGitCommit = allShasGitCommitMap.get(pSha);
            if(pGitCommit == null){
                System.out.println("缺失： "+pSha);
                continue;
            }
            result.addGitCommits(pGitCommit);
            Set<String> parentShas = pGitCommit.getParentShas();
            if(shasFromGitCommit != null) {
                Set<String> otherShas = shasFromGitCommit.shasFromGitCommit(pGitCommit);
                if(parentShas == null){
                    parentShas = new HashSet<>();
                }
                if(otherShas != null) {
                    parentShas.addAll(otherShas);
                }
            }
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

    public static List<GitCommitsInfo> createInfosForVersions(GitCommitsInfo gitCommitsInfo, List<String> sourceShas) throws Exception {
        return createInfosForVersions(gitCommitsInfo, sourceShas, null);
    }

    /**
     * 指定不同版本，获取gitCommits
     * @param gitCommitsInfo
     * @param sourceShas
     * @return
     */
    public static List<GitCommitsInfo> createInfosForVersions(GitCommitsInfo gitCommitsInfo, List<String> sourceShas, ShasFromGitCommit shasFromGitCommit) throws Exception {
        Set<String> allShas = new HashSet<>();
        List<GitCommitsInfo> result = new ArrayList<>();
        for (String sourceSha:
                sourceShas) {
            System.out.println("start"+sourceSha);
            GitCommitsInfo info = createInfoFromCommit(gitCommitsInfo, sourceSha, allShas, shasFromGitCommit);
            info.setRevision(sourceSha);
            info.setRevisionSha(sourceSha);
            result.add(info);
            allShas.addAll(info.allShas());
        }
        return result;
    }

    public Set<String> allShas(){
        /*Set<String> shas = new HashSet<>();
        for (GitCommit gitCommit :
                getGitCommits()) {
            shas.add(gitCommit.getSha());
        }*/
        return shaGitCommitMap.keySet();
    }

    public Map<String, GitCommit> allShasGitCommitMap(){
        /*Map<String, GitCommit> allShasGitCommitMap = new HashMap<>();
        for (GitCommit gitCommit :
                getGitCommits()) {
            allShasGitCommitMap.put(gitCommit.getSha(), gitCommit);
        }*/
        return shaGitCommitMap;
    }

    public GitCommitsInfo addGitCommits(List<GitCommit> gitCommits){
        for (GitCommit gitCommit :
                gitCommits) {
            if(StringUtils.isBlank(gitCommit.getSha()) || this.shaGitCommitMap.containsKey(gitCommit.getSha())){
                continue;
            }
            this.shaGitCommitMap.put(gitCommit.getSha(), gitCommit);
            this.gitCommits.add(gitCommit);
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

    public GitCommitsInfo addGitCommits(GitCommit... gitCommits){
        return addGitCommits(Arrays.asList(gitCommits));
    }

    @Override
    public Long getReposId() {
        return reposId;
    }

    @Override
    public String getRepoName() {
        return repoName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getRevision() {
        return revision;
    }

    public String getRevisionSha() {
        return revisionSha;
    }

    /**
     * gitCommits的一个拷贝
     * @return
     */
    public List<GitCommit> getGitCommits() {
        return new ArrayList<>(gitCommits);
    }

    public GitCommitsInfo setGitCommits(List<GitCommit> gitCommits) {
        this.gitCommits.clear();
        this.shaGitCommitMap.clear();
        this.addGitCommits(gitCommits);
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

    public GitCommitsInfo setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public GitCommitsInfo setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public GitCommitsInfo setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public GitCommitsInfo setRevisionSha(String revisionSha) {
        this.revisionSha = revisionSha;
        return this;
    }
}
